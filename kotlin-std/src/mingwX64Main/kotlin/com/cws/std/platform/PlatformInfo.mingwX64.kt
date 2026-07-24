@file:OptIn(ExperimentalForeignApi::class)

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

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    return memScoped {
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
}

actual fun PlatformInfo.fetchCurrentProcessId(): Int = GetCurrentProcessId().toInt()

actual fun PlatformInfo.fetchCurrentThreadId(): Int = GetCurrentThreadId().toInt()

actual fun PlatformInfo.fetchCurrentThreadName(): String = memScoped {
    val ptr = alloc<CPointerVar<UShortVar>>()
//    GetThreadDescription(GetCurrentThread(), ptr.ptr)
    val name = ptr.value?.toKString() ?: ""
    LocalFree(ptr.value)
    name
}

actual fun PlatformInfo.fetchMaxThreadCount(): Int {
    return memScoped {
        val sysInfo = alloc<SYSTEM_INFO>()
        GetSystemInfo(sysInfo.ptr)
        // usually one CPU core has 2 physical threads
        sysInfo.dwNumberOfProcessors.toInt() * 2
    }
}