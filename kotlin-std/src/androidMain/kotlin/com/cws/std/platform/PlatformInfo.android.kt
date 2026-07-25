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
package com.cws.std.platform

import java.io.File

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
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
        totalPhysicalSize = totalPhysicalSize,
    )
}
