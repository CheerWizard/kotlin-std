package com.cws.std.memory

import java.io.File

actual fun fetchMemoryInfo(): MemoryInfo {
    val procInfoFile = File("/proc/meminfo").readLines()
    var totalPhysicalSize: Long = 0
    var freePhysicalSize: Long = 0

    procInfoFile.forEach { line ->
        when {
            line.startsWith("MemTotal:") -> {
                totalPhysicalSize = line.split(Regex("\\s+"))[1].toLong() * 1024
            }
            line.startsWith("MemFree:") -> {
                freePhysicalSize = line.split(Regex("\\s+"))[1].toLong() * 1024
            }
        }
    }

    val runtime = Runtime.getRuntime()

    return MemoryInfo(
        freeHeapSize = runtime.freeMemory(),
        totalHeapSize = runtime.totalMemory(),
        freePhysicalSize = freePhysicalSize,
        totalPhysicalSize = totalPhysicalSize
    )
}