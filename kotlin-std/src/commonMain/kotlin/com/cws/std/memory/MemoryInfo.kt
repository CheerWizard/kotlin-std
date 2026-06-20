package com.cws.std.memory

import kotlin.math.roundToInt

data class MemoryInfo(
    val totalHeapSize: Long,
    val freeHeapSize: Long,
    val totalPhysicalSize: Long,
    val freePhysicalSize: Long
)

private lateinit var memoryInfo: MemoryInfo

fun getMemoryInfo(): MemoryInfo {
    if (!::memoryInfo.isInitialized) {
        memoryInfo = fetchMemoryInfo()
    }
    return memoryInfo
}

fun getMemorySize(percentage: Float): Int {
    return (getMemoryInfo().totalPhysicalSize * (percentage / 100f))
        .roundToInt()
        .coerceIn(1024 * 1024, Int.MAX_VALUE)
}

internal expect fun fetchMemoryInfo(): MemoryInfo