#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_gachat_main_MainActivity_stringFromJNI(JNIEnv *env, jobject jobj) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
