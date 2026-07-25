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
@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
package com.cws.std.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.value
import platform.Foundation.NSProcessInfo
import platform.darwin.KERN_SUCCESS
import platform.darwin.TASK_VM_INFO
import platform.darwin.integer_tVar
import platform.darwin.mach_msg_type_number_tVar
import platform.darwin.mach_task_self_
import platform.darwin.task_info
import platform.darwin.task_vm_info_data_t
import platform.posix.getpid
import platform.posix.pthread_getname_np
import platform.posix.pthread_self
import platform.posix.pthread_threadid_np
import kotlin.experimental.ExperimentalNativeApi

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    return memScoped {
        val totalPhysicalSize = NSProcessInfo.processInfo.physicalMemory.toLong()
        val info = alloc<task_vm_info_data_t>()
        val count = alloc<mach_msg_type_number_tVar>()
        count.value = (sizeOf<task_vm_info_data_t>() / sizeOf<integer_tVar>()).toUInt()

        val result =
            task_info(
                mach_task_self_,
                TASK_VM_INFO.toUInt(),
                info.ptr.reinterpret(),
                count.ptr,
            )

        val usedPhysicalSize = if (result == KERN_SUCCESS) info.phys_footprint.toLong() else 0

        return@memScoped MemoryInfo(
            totalPhysicalSize = totalPhysicalSize,
            freePhysicalSize = totalPhysicalSize - usedPhysicalSize,
            // on iOS there is no VM heap memory
            totalHeapSize = totalPhysicalSize,
            freeHeapSize = totalPhysicalSize - usedPhysicalSize,
        )
    }
}

actual fun PlatformInfo.fetchCurrentProcessId(): Int = getpid()

actual fun PlatformInfo.fetchCurrentThreadId(): Int =
    memScoped {
        val tid = alloc<ULongVar>()
        pthread_threadid_np(pthread_self(), tid.ptr)
        tid.value.toInt()
    }

actual fun PlatformInfo.fetchCurrentThreadName(): String {
    val buffer = ByteArray(256)
    pthread_getname_np(pthread_self(), buffer.refTo(0), buffer.size.toULong())
    return buffer.decodeToString().trimEnd('\u0000')
}

actual fun PlatformInfo.fetchMaxThreadCount(): Int = maxOf(1, Platform.getAvailableProcessors() - 1)
