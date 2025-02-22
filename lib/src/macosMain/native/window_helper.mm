#include <jni.h>
#include <dispatch/dispatch.h>
#include <Cocoa/Cocoa.h>

static JavaVM* jvm = nullptr;

static NSString* jStringToNSString(JNIEnv *env, jstring text) {
    const jchar* textChars = env->GetStringChars(text, NULL);
    const int textLength = env->GetStringLength(text);
    NSString* textString =  [[NSString alloc] initWithCharacters:textChars length:textLength];
    
    env->ReleaseStringChars(text, textChars);
    return textString;
}

static NSWindow* findWindowForTitle(NSString* title) {
    NSArray<NSWindow*>* windows = [[NSApplication sharedApplication] orderedWindows];
    for (NSWindow* win in windows) {
        if([[win title] isEqualToString:title]) {
            return win;
        }
    }
    
    return nil;
}

static NSWindow* findLastWindow() {
    NSArray<NSWindow*>* windows = [[NSApplication sharedApplication] orderedWindows];
    
    return windows.lastObject;
}

extern "C" {
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT jlong JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_findLastWindow(JNIEnv *env, jobject obj) {
    __block NSWindow* window = nil;
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        window = findLastWindow();
    });
    
    return reinterpret_cast<jlong>(window);
}

JNIEXPORT jlong JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_findWindowForName(JNIEnv *env, jobject obj, jstring windowName) {
    __block NSString* strTitle = jStringToNSString(env, windowName);

    __block NSWindow* window = nil;
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        window = findWindowForTitle(strTitle);
    });
    
    [strTitle release];
    return reinterpret_cast<jlong>(window);
}

JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowLevel(JNIEnv *env, jobject obj, jlong windowHandle, jint windowLevel) {
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        NSWindowLevel level = static_cast<NSWindowLevel>(windowLevel);
        NSWindow* window = (NSWindow*) windowHandle;
        
        [window setLevel: level];
    });
}

JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowCollectionBehavior(JNIEnv *env, jobject obj, jlong windowHandle, jint windowCollectionBehavior) {
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        NSWindowCollectionBehavior collectionBehavior = static_cast<NSWindowLevel>(windowCollectionBehavior);
        NSWindow* window = (NSWindow*) windowHandle;
        
        [window setCollectionBehavior: collectionBehavior];
    });
}

JNIEXPORT jint JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_getCGWindowLevelForKey(JNIEnv *env, jobject obj, jint cGWindowLevelKey) {
    return static_cast<jint>(CGWindowLevelForKey(static_cast<CGWindowLevelKey>(cGWindowLevelKey)));
}
}
