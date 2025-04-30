#include <jni.h>
#include <windows.h>
#include <psapi.h>
#include <iostream>

static JavaVM* jvm = nullptr;

static bool setWindowStickyOptions(HWND window) {
    return SetWindowLong(window, GWL_EXSTYLE, (GetWindowLong(window, GWL_EXSTYLE) | WS_EX_TOOLWINDOW)) != 0 &&
        SetWindowPos(window, NULL, 0,0,0,0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_FRAMECHANGED) != 0;
}

static bool setWindowTopMostOptions(HWND window) {
    return SetWindowLong(window, GWL_EXSTYLE, GetWindowLong(window, GWL_EXSTYLE) | WS_EX_TOPMOST) != 0 &&
           SetWindowPos(window, HWND_TOPMOST, 0,0,0,0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOACTIVATE | SWP_FRAMECHANGED)!= 0;
}

static HWND findComponentWindow(JNIEnv *env, jobject component) {
    jclass componentClass = env->GetObjectClass(component);
    jfieldID peerFieldID = env->GetFieldID(componentClass, "peer", "Ljava/awt/peer/ComponentPeer;");
    if (peerFieldID == nullptr) {
        std::cerr << "Failed to get ComponentPeer field" << std::endl;
        return nullptr;
    }

    jobject peer = env->GetObjectField(component, peerFieldID);
    if (peer == nullptr) {
        std::cerr << "Failed to get ComponentPeer" << std::endl;
        return nullptr;
    }

    jclass peerClass = env->GetObjectClass(peer);
    // WWindowPeer -> WPanelPeer -> WCanvasPeer -> WComponentPeer (hwnd)
    jclass componentPeerClass = env->GetSuperclass(env->GetSuperclass(env->GetSuperclass(peerClass)));

    jfieldID hwndFieldID = env->GetFieldID(componentPeerClass, "hwnd", "J");
    if (hwndFieldID == nullptr) {
        std::cerr << "Failed to get HWND field" << std::endl;
        return nullptr;
    }

    jlong pointer = env->GetLongField(peer, hwndFieldID);
    if (pointer <= 0L) {
        std::cerr << "Failed to get HWND" << std::endl;
    }

    return reinterpret_cast<HWND>(pointer);
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
        jvm = vm;
        return JNI_VERSION_1_6;
    }

    JNIEXPORT jlong JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_findWindowForComponent(JNIEnv *env, jobject obj, jobject component) {
        HWND windowHandle = findComponentWindow(env, component);

        return reinterpret_cast<jlong>(windowHandle);
    }

    JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowSticky(JNIEnv *env, jobject obj, jlong windowHandle) {
        HWND window = reinterpret_cast<HWND>(windowHandle);

        if (!setWindowStickyOptions(window)) {
            std::cerr << "Failed to set window as sticky" << std::endl;
        }
    }

    JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowTopMost(JNIEnv *env, jobject obj, jlong windowHandle) {
        HWND window = reinterpret_cast<HWND>(windowHandle);

        if(!setWindowTopMostOptions(window)) {
            std::cerr << "Failed to set window as topmost" << std::endl;
        }
    }
}
