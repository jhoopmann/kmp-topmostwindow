#include <jni.h>
#include <stdlib.h>
#include <unistd.h>
#include <iostream>
#include <vector>
#include <string.h>
#include <thread>
#include <X11/Xlib.h>
#include <X11/Xatom.h>

static JavaVM* jvm = nullptr;

static int errorHandler(Display* display, XErrorEvent* error) {
    char errorText[256];
    XGetErrorText(display, error->error_code, errorText, sizeof(errorText));

    std::cerr << "WindowHelper X11 Error: " << errorText << std::endl;

    return 0;
}

template <typename T>
static T* getWindowProperty(Display* display, Window window, Atom req_type, const char* name, unsigned long* nitemsout) {
    Atom prop = XInternAtom(display, name, true);
    Atom actual_type;
    int actual_format;
    unsigned long bytes_after;
    unsigned char* data = nullptr;

    int status = 0;
    if ((status = XGetWindowProperty(display, window, prop, 0, ~0, false, req_type, &actual_type, &actual_format, nitemsout, &bytes_after, &data)) != Success) {
        std::cerr << "Status: " << status << ": Failed to get property " << name << " for window " << window << std::endl;

        *nitemsout = 255;
    }

    return reinterpret_cast<T*>(data);
}

template<typename T>
static bool setWindowProperty(Display* display, Window window, Atom req_type, int mode, const char* name, T* data, unsigned long length = 1) {
    Atom prop = XInternAtom(display, name, true);

    int status = 0;
    if((status = XChangeProperty(display, window, prop, req_type, 32, mode, reinterpret_cast<const unsigned char*>(data), length)) != 1) {
        std::cerr << "Status: " << status << ": Failed to set property " << name << " for window " << window << std::endl;

        return false;
    }

    for (unsigned int i = 0; i < length; i++) {
        XEvent ev;
        memset(&ev, 0, sizeof(ev));
        ev.xclient.type = ClientMessage;
        ev.xclient.message_type = prop;
        ev.xclient.display = display;
        ev.xclient.window = window;
        ev.xclient.format = 32;
        ev.xclient.data.l[0] = 1;// _NET_WM_STATE_ADD
        ev.xclient.data.l[1] = ((Atom*)data)[i];
        ev.xclient.data.l[2] = 0;
        XSendEvent(display, DefaultRootWindow(display), False, SubstructureNotifyMask | SubstructureRedirectMask, &ev);
    }

    XFlush(display);
    return true;
}

template<typename T>
static T* addWindowProperties(Display* display, Window window, Atom req_type, const char* name, T* data, unsigned long length) {
    unsigned long nexistingitems = 0;
    T* existingData = getWindowProperty<T>(display, window, req_type, name, &nexistingitems);
    if (nexistingitems == 255) {
        std::cerr << "Failed to retrieve existing properties " << name << " for window " << window << std::endl;

        return nullptr;
    } else if(nexistingitems == 0 || existingData == nullptr) {
        std::cerr << "No properties existing " << name << " for window " << window << std::endl;
    }

    unsigned int insertDataLength = 0;
    T* insertData = new T[length];

    for(unsigned int i = 0; i < length; i++) {
        bool found = false;
        for(unsigned int e = 0; e < nexistingitems; e++) {
            if (data[i] == existingData[e]) {
                found = true;
                break;
            }
        }

        if (!found) {
            insertData[insertDataLength] = data[i];
            insertDataLength++;
        }
    }

    XFree(existingData);

    if (!setWindowProperty(display, window, req_type, PropModeAppend, name, insertData, insertDataLength)) {
        std::cerr << "Failed to set property to merge " << name << " for window " << window << std::endl;

        delete[] insertData;
        insertData = nullptr;
    }

    return insertData;
}

static Window findWindowForName(Display* display, Window root, const char* name) {
    Window matchingWindow = 0L;

    Window* children;
    unsigned int number_of_children;
    Window parent_return, root_return;

    if (XQueryTree(display, root, &root_return, &parent_return, &children, &number_of_children) && number_of_children > 0) {
        for (unsigned int i = 0; i < number_of_children; i++) {
            char* window_name = nullptr;
            if (XFetchName(display, children[i], &window_name) > 0 && window_name) {
                if (strcmp(window_name, name) == 0) {
                    matchingWindow = children[i];
                }

                XFree(window_name);
            }

            if (matchingWindow == 0L) {
                matchingWindow = findWindowForName(display, children[i], name);
            }

            if (matchingWindow > 0) {
                break;
            }
        }

        XFree(children);
    }

    return matchingWindow;
}

static Display* openDisplay() {
    Display* display = XOpenDisplay(nullptr);
    if (display == nullptr) {
        std::cerr << "Failed to open X display" << std::endl;

        return nullptr;
    }

    XSetErrorHandler(errorHandler);

    return display;
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
        jvm = vm;
        return JNI_VERSION_1_6;
    }

    JNIEXPORT jlong JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_findWindowForName(JNIEnv *env, jobject obj, jstring windowName) {
        const char* windowName_c = env->GetStringUTFChars(windowName, nullptr);
        Window window = 0L;

        Display* display = openDisplay();
        if (display != nullptr) {
            Window root = DefaultRootWindow(display);
            window = findWindowForName(display, root, windowName_c);

            if (window == 0L) {
                std::cerr << "Failed to find window by name: " << windowName_c << std::endl;
            }

            XCloseDisplay(display);
        }

        env->ReleaseStringUTFChars(windowName, windowName_c);
        return static_cast<jlong>(window);
    }

    JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_addWindowMode(JNIEnv *env, jobject obj, jlong windowHandle, jobjectArray windowModes) {
        Display* display = openDisplay();
        if (display == nullptr) {
            return;
        }

        unsigned int modesLength = env->GetArrayLength(windowModes);
        Atom states[modesLength];

        for (unsigned int i = 0; i < modesLength; i++) {
            jstring modeString = static_cast<jstring>(env->GetObjectArrayElement(windowModes, i));
            const char* mode_c = env->GetStringUTFChars(modeString, nullptr);

            states[i] = XInternAtom(display, mode_c, true);
            env->ReleaseStringUTFChars(modeString, mode_c);
        }

        Window window = (Window) windowHandle;
        Atom* inserted = addWindowProperties<Atom>(display, window, XA_ATOM, (const char*) "_NET_WM_STATE", (Atom*) &states, modesLength);
        if (inserted != nullptr) {
            delete[] inserted;
        }

        XCloseDisplay(display);
    }
}
