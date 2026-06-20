package com.cws.std.memory

actual fun fetchMemoryInfo(): MemoryInfo {
    val navigator = js("navigator")
    val performance = js("performance")

    val deviceMemory = (navigator.deviceMemory ?: 0) as Int
    val totalPhysicalSize = deviceMemory * 1024L * 1024L * 1024L

    val usedPhysicalSize = try {
        val memory = performance.memory
        (memory.usedJSHeapSize as Double).toLong()
    } catch (e: dynamic) {
        0L
    }

    return MemoryInfo(
        totalHeapSize = totalPhysicalSize,
        freeHeapSize = totalPhysicalSize - usedPhysicalSize,
        totalPhysicalSize = totalPhysicalSize,
        freePhysicalSize = totalPhysicalSize - usedPhysicalSize
    )
}