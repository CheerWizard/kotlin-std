package com.cws.std.platform

import kotlin.math.roundToInt

data class MemoryInfo(
    val totalHeapSize: Long,
    val freeHeapSize: Long,
    val totalPhysicalSize: Long,
    val freePhysicalSize: Long
)

object PlatformInfo {

    val memoryInfo = fetchMemoryInfo()

    val maxThreadCount: Int = fetchMaxThreadCount()

    fun getMemorySize(percentage: Float): Int {
        return (memoryInfo.totalPhysicalSize * (percentage / 100f))
            .roundToInt()
            .coerceIn(1024 * 1024, Int.MAX_VALUE)
    }

}

expect fun PlatformInfo.fetchMemoryInfo(): MemoryInfo

expect fun PlatformInfo.fetchMaxThreadCount(): Int

expect fun PlatformInfo.fetchCurrentThreadName(): String

expect fun PlatformInfo.fetchCurrentThreadId(): Int

expect fun PlatformInfo.fetchCurrentProcessId(): Int
