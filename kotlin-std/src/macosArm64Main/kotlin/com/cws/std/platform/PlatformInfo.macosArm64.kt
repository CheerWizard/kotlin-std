@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)

package com.cws.std.platform

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.sizeOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.toLong
import kotlinx.cinterop.value
import platform.darwin.KERN_SUCCESS
import platform.darwin.mach_task_self_
import platform.darwin.task_threads
import platform.darwin.vm_deallocate
import platform.libkern.UIntVar
import platform.posix.fgets
import platform.posix.getpid
import platform.posix.pclose
import platform.posix.popen
import platform.posix.pthread_getname_np
import platform.posix.pthread_self
import platform.posix.pthread_threadid_np
import kotlin.experimental.ExperimentalNativeApi

private fun runCommand(cmd: String): String {
    val command = popen(cmd, "r") ?: return ""
    val result = StringBuilder()
    memScoped {
        val buf = allocArray<ByteVar>(128)
        while (fgets(buf, 128, command) != null) {
            result.append(buf.toKString())
        }
    }
    pclose(command)
    return result.toString().trim()
}

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    val total = runCommand("sysctl -n hw.memsize").toLongOrNull() ?: -1L
    // vm_stat reports page counts; multiply by page size (usually 4096 or 16384 on Apple Silicon)
    val pageSize = runCommand("sysctl -n hw.pagesize").toLongOrNull() ?: 4096L
    val vmStat = runCommand("vm_stat")
    val active = Regex("Pages active:\\s+(\\d+)").find(vmStat)?.groupValues?.get(1)?.toLongOrNull() ?: 0L
    val wired = Regex("Pages wired down:\\s+(\\d+)").find(vmStat)?.groupValues?.get(1)?.toLongOrNull() ?: 0L
    val used = (active + wired) * pageSize
    val free = total - used
    return MemoryInfo(
        totalPhysicalSize = total,
        freePhysicalSize = free,
        totalHeapSize = total,
        freeHeapSize = free,
    )
}

actual fun PlatformInfo.fetchCurrentProcessId(): Int = getpid()

actual fun PlatformInfo.fetchCurrentThreadId(): Int {
    return memScoped {
        val id = alloc<ULongVar>()
        pthread_threadid_np(pthread_self(), id.ptr)
        id.value.toInt()
    }
}

actual fun PlatformInfo.fetchCurrentThreadName(): String {
    return memScoped {
        val name = allocArray<ByteVar>(64)
        pthread_getname_np(pthread_self(), name, 64u)
        name.toKString().trim()
    }
}

actual fun PlatformInfo.fetchMaxThreadCount(): Int = maxOf(1, Platform.getAvailableProcessors() - 1)