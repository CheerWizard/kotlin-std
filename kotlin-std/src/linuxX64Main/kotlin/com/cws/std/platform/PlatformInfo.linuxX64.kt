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
@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
package com.cws.std.platform

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.closedir
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.getpid
import platform.posix.opendir
import platform.posix.pthread_self
import platform.posix.readdir
import kotlin.experimental.ExperimentalNativeApi

actual fun PlatformInfo.fetchMemoryInfo(): MemoryInfo {
    val file =
        fopen("/proc/meminfo", "r")
            ?: return MemoryInfo(
                totalHeapSize = 0L,
                freeHeapSize = 0L,
                totalPhysicalSize = 0L,
                freePhysicalSize = 0L,
            )

    var totalKb = 0L
    var freeKb = 0L

    try {
        memScoped {
            val line = allocArray<ByteVar>(256)
            while (fgets(line, 256, file) != null) {
                val text = line.toKString()
                when {
                    text.startsWith("MemTotal:") -> totalKb = text.filter { it.isDigit() }.toLong()
                    text.startsWith("MemAvailable:") -> freeKb = text.filter { it.isDigit() }.toLong()
                }
            }
        }
    } finally {
        fclose(file)
    }

    return MemoryInfo(
        totalPhysicalSize = totalKb * 1000,
        freePhysicalSize = freeKb * 1000,
        totalHeapSize = totalKb * 1000,
        freeHeapSize = freeKb * 1000,
    )
}

actual fun PlatformInfo.fetchCurrentProcessId(): Int = getpid()

actual fun PlatformInfo.fetchCurrentThreadId(): Int = pthread_self().toInt()

actual fun PlatformInfo.fetchCurrentThreadName(): String {
    val file = fopen("/proc/thread-self/comm", "r") ?: return ""
    return try {
        memScoped {
            val name = allocArray<ByteVar>(64)
            fgets(name, 64, file)
            name.toKString().trim()
        }
    } catch (_: Throwable) {
        ""
    } finally {
        fclose(file)
    }
}

actual fun PlatformInfo.fetchMaxThreadCount(): Int = maxOf(1, Platform.getAvailableProcessors() - 1)
