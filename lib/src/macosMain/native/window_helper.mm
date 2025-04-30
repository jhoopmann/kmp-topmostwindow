#include <jni.h>
#include <jawt.h>
#include <jawt_md.h>
#include <dispatch/dispatch.h>
#include <Cocoa/Cocoa.h>

static JavaVM* jvm = nullptr;

static NSWindow* getComponentWindow(JNIEnv* env, jobject component) {
    jclass componentClass = env->GetObjectClass(component);
    jfieldID peerFieldID = env->GetFieldID(componentClass, "peer", "Ljava/awt/peer/ComponentPeer;");
    if (peerFieldID == nullptr) {
        NSLog(@"Failed to get ComponentPeer field of component class");
        return 0L;
    }
    jobject peer = env->GetObjectField(component, peerFieldID); // LWWindowPeer.java
    if (peer == nullptr) {
        NSLog(@"Failed to get ComponentPeer of component");
        return 0L;
    }

    jclass peerClass = env->GetObjectClass(peer);
    jfieldID platformWindowFieldID = env->GetFieldID(peerClass, "platformWindow", "Lsun/lwawt/PlatformWindow;");
    if (platformWindowFieldID == nullptr) {
        NSLog(@"Failed to get PlatformWindow field of ComponentPeer class");
        return 0L;
    }
    jobject platformWindow = env->GetObjectField(peer, platformWindowFieldID); // LWWindowPeer.java
    if (platformWindow == nullptr) {
        NSLog(@"Failed to get PlatformWindow of ComponentPeer");
        return 0L;
    }

    jclass platformWindowClass = env->GetObjectClass(platformWindow);
    jclass retainedResourceClass = env->GetSuperclass(platformWindowClass);

    jfieldID pointerFieldID = env->GetFieldID(retainedResourceClass, "ptr", "J");
    if (pointerFieldID == nullptr) {
        NSLog(@"Failed to get Pointer field of PlatformWindow class");
        return 0L;
    }
    jlong pointer = env->GetLongField(platformWindow, pointerFieldID); // LWWindowPeer.java
    if (pointer <= 0L) {
        NSLog(@"Failed to get Pointer of PlatformWindow");
        return 0L;
    }

    return reinterpret_cast<NSWindow*>(pointer);
}

extern "C" {
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT jlong JNICALL Java_de_jhoopmann_stickywindow_awt_native_WindowHelper_findWindowForComponent(JNIEnv *env, jobject obj, jobject component) {
    NSWindow* window = getComponentWindow(env, component);

    return reinterpret_cast<jlong>(window);
}

JNIEXPORT void JNICALL Java_de_jhoopmann_stickywindow_awt_native_WindowHelper_setWindowLevel(JNIEnv *env, jobject obj, jlong windowHandle, jint windowLevel) {
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        NSWindowLevel level = static_cast<NSWindowLevel>(windowLevel);
        NSWindow* window = (NSWindow*) windowHandle;

        [window setLevel: level];
    });
}

JNIEXPORT void JNICALL Java_de_jhoopmann_stickywindow_awt_native_WindowHelper_setWindowCollectionBehavior(JNIEnv *env, jobject obj, jlong windowHandle, jint windowCollectionBehavior) {
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        NSWindowCollectionBehavior collectionBehavior = static_cast<NSWindowLevel>(windowCollectionBehavior);
        NSWindow* window = (NSWindow*) windowHandle;

        [window setCollectionBehavior: collectionBehavior];
    });
}

JNIEXPORT jint JNICALL Java_de_jhoopmann_stickywindow_awt_native_WindowHelper_getCGWindowLevelForKey(JNIEnv *env, jobject obj, jint cGWindowLevelKey) {
    return static_cast<jint>(CGWindowLevelForKey(static_cast<CGWindowLevelKey>(cGWindowLevelKey)));
}
}
