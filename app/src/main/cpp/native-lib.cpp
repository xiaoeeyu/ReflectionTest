#include <jni.h>
#include <string>
#include <android/log.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_xiaoeryu_reflectiontest_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    // 调用publicStaticField
    jclass TestJclass = env->FindClass("com/xiaoeryu/reflectiontest/Test");     // 注意寻找类的时候使用“/”分隔
    // jfieldID GetFieldID(jclass clazz, const char* name, const char* sig)
    jfieldID publicStaticField_jfieldID = env->GetStaticFieldID(TestJclass, "publicStaticField", "Ljava/lang/String;");
    // jobject GetStaticObjectField(jclass clazz, jfieldID fieldID)
    jstring publicStaticField_content =(jstring) env->GetStaticObjectField(TestJclass, publicStaticField_jfieldID);
    const char* content_ptr = env->GetStringUTFChars(publicStaticField_content, nullptr);
    // int __android_log_print(int prio, const char* tag, const char* fmt, ...)
    __android_log_print(4, "xiaoeryu->jni", "jni->%s", content_ptr);

    return env->NewStringUTF(hello.c_str());
}