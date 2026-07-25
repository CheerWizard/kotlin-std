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

import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UShortVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import platform.windows.GetCurrentProcess
import platform.windows.GetCurrentProcessId
import platform.windows.GetCurrentThemeName
import platform.windows.GetCurrentThread
import platform.windows.GetCurrentThreadId
import platform.windows.GetSystemInfo
import platform.windows.GlobalMemoryStatusEx
import platform.windows.LocalFree
import platform.windows.MEMORYSTATUSEX
import platform.windows.SYSTEM_INFO
import kotlin.experimental.ExperimentalNativeApi

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo =
    memScoped {
        val status = alloc<MEMORYSTATUSEX>()
        status.dwLength = sizeOf<MEMORYSTATUSEX>().toUInt()
        GlobalMemoryStatusEx(status.ptr)
        MemoryInfo(
            totalPhysicalSize = status.ullTotalPhys.toLong(),
            freePhysicalSize = status.ullAvailPhys.toLong(),
            totalHeapSize = status.ullTotalPhys.toLong(),
            freeHeapSize = status.ullAvailPhys.toLong(),
        )
    }

actual fun PlatformInfo.fetchCurrentProcessId(): Int = GetCurrentProcessId().toInt()

actual fun PlatformInfo.fetchCurrentThreadId(): Int = GetCurrentThreadId().toInt()

actual fun PlatformInfo.fetchCurrentThreadName(): String =
    memScoped {
        val ptr = alloc<CPointerVar<UShortVar>>()
//    GetThreadDescription(GetCurrentThread(), ptr.ptr)
        val name = ptr.value?.toKString() ?: ""
        LocalFree(ptr.value)
        name
    }

actual fun PlatformInfo.fetchMaxThreadCount(): Int =
    memScoped {
        val sysInfo = alloc<SYSTEM_INFO>()
        GetSystemInfo(sysInfo.ptr)
        // usually one CPU core has 2 physical threads
        sysInfo.dwNumberOfProcessors.toInt() * 2
    }
