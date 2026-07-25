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
@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.platform

import com.cws.std.async.JsNavigator
import com.cws.std.async.JsPerformance
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsException
import kotlin.js.js
import kotlin.math.max

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    val navigator = JsNavigator()
    val performance = JsPerformance()

    val deviceMemory = navigator.deviceMemory
    val totalPhysicalSize = deviceMemory * 1024L * 1024L * 1024L

    val usedPhysicalSize =
        try {
            performance.memory.usedJSHeapSize.toLong()
        } catch (_: JsException) {
            0L
        }

    return MemoryInfo(
        totalHeapSize = totalPhysicalSize,
        freeHeapSize = totalPhysicalSize - usedPhysicalSize,
        totalPhysicalSize = totalPhysicalSize,
        freePhysicalSize = totalPhysicalSize - usedPhysicalSize,
    )
}

actual fun PlatformInfo.fetchCurrentThreadId(): Int = 1

actual fun PlatformInfo.fetchCurrentProcessId(): Int = 1

actual fun PlatformInfo.fetchCurrentThreadName(): String = "main"

actual fun PlatformInfo.fetchMaxThreadCount(): Int = max(6, JsNavigator().hardwareConcurrency - 1)
