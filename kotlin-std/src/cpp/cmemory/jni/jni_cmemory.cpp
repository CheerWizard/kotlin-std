/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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