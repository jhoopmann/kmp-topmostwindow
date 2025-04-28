#include <jni.h>
#include <Cocoa/Cocoa.h>
#include <dispatch/dispatch.h>

static JavaVM* jvm = nullptr;

extern "C" {
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT bool JNICALL Java_de_jhoopmann_topmostwindow_awt_native_ApplicationHelper_setActivationPolicy(JNIEnv* env, jobject obj, jint applicationActivationPolicy) {
    __block bool result = true;
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        NSApplicationActivationPolicy activationPolicy = static_cast<NSApplicationActivationPolicy>(applicationActivationPolicy);
        if (activationPolicy != [[NSApplication sharedApplication] activationPolicy]) {
            result = [[NSApplication sharedApplication] setActivationPolicy: activationPolicy];
        }
    });
    return result;
}

JNIEXPORT jint JNICALL Java_de_jhoopmann_topmostwindow_awt_native_ApplicationHelper_getActivationPolicy(JNIEnv* env, jobject obj) {
    __block int result = NSApplicationActivationPolicyRegular;
    dispatch_async_and_wait(dispatch_get_main_queue(), ^{
        result = [[NSApplication sharedApplication] activationPolicy];
    });
    return static_cast<jint>(result);
}
}
