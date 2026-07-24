package com.cws.std.platform

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    val runtime = Runtime.getRuntime()
    val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
    return MemoryInfo(
        totalHeapSize = runtime.totalMemory(),
        freeHeapSize = runtime.freeMemory(),
        totalPhysicalSize = osBean.totalMemorySize,
        freePhysicalSize = osBean.freeMemorySize
    )
}