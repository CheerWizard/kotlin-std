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

import kotlin.math.roundToInt

data class MemoryInfo(
    val totalHeapSize: Long,
    val freeHeapSize: Long,
    val totalPhysicalSize: Long,
    val freePhysicalSize: Long,
)

object PlatformInfo {
    val memoryInfo = fetchMemoryInfo()

    val maxThreadCount: Int = fetchMaxThreadCount()

    fun getMemorySize(percentage: Float): Int =
        (memoryInfo.totalPhysicalSize * (percentage / 100f))
            .roundToInt()
            .coerceIn(1024 * 1024, Int.MAX_VALUE)
}

expect fun PlatformInfo.fetchMemoryInfo(): MemoryInfo

expect fun PlatformInfo.fetchMaxThreadCount(): Int

expect fun PlatformInfo.fetchCurrentThreadName(): String

expect fun PlatformInfo.fetchCurrentThreadId(): Int

expect fun PlatformInfo.fetchCurrentProcessId(): Int
