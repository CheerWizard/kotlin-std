#include "../cmemory.hpp"
#include <jni.h>
#include <cstring>

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_std_memory_CMemory_malloc(JNIEnv* env, jobject thiz, jint size) {
    void* ptr = cmemory::malloc(size);
    if (!ptr) return nullptr;
    return env->NewDirectByteBuffer(ptr, size);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_cws_std_memory_CMemory_free(JNIEnv* env, jobject thiz, jobject buffer) {
    void* ptr = env->GetDirectBufferAddress(buffer);
    cmemory::free(ptr);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_std_memory_CMemory_realloc(JNIEnv* env, jobject thiz, jobject buffer, jint size) {
    void* oldPtr = env->GetDirectBufferAddress(buffer);
    void* ptr = nullptr;
    jobject newBuffer = nullptr;

    if (oldPtr) {
        ptr = cmemory::realloc(oldPtr, size);
    } else {
        ptr = cmemory::malloc(size);
    }

    if (ptr) {
        newBuffer = env->NewDirectByteBuffer(ptr, size);
    }

    return newBuffer;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_cws_std_memory_CMemory_addressOf(JNIEnv * env, jobject thiz, jobject buffer) {
    return reinterpret_cast<jlong>(env->GetDirectBufferAddress(buffer));
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_std_memory_CMemory_toByteBuffer(JNIEnv * env, jobject thiz, jlong ptr, jint capacity) {
    return env->NewDirectByteBuffer((void*) ptr, capacity);
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_cws_std_memory_CMemory_toByteBufferString(JNIEnv * env, jobject thiz, jlong ptr) {
    return env->NewDirectByteBuffer((void*) ptr, strlen((const char*) ptr));
}