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
package com.cws.std.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pin
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.darwin.REMOVEFILE_RECURSIVE
import platform.darwin.removefile
import platform.posix.__sFILE
import platform.posix.errno
import platform.posix.fclose
import platform.posix.feof
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fwrite
import platform.posix.strerror

private fun FileMode.toNativeFileMode(): String =
    when (this) {
        FileMode.OPEN_EXISTING -> "r+"
        FileMode.CLEAR_WHEN_OPEN -> "w+"
        FileMode.CREATE_IF_NOT_EXIST -> "a+"
    }

@OptIn(ExperimentalForeignApi::class)
actual class File actual constructor(
    private val filepath: String,
    private val mode: FileMode,
) : AutoCloseable {
    actual val size: Int get() {
        return if (file == null) 0 else feof(file)
    }

    actual val isOpened: Boolean
        get() = file != null

    private var file: CPointer<__sFILE>? = null

    init {
        open()
    }

    actual override fun close() {
        file?.let { file ->
            fflush(file)
            fclose(file)
        }
        file = null
    }

    actual fun open() {
        if (isOpened) return
        file = fopen(filepath, mode.toNativeFileMode())
        if (file == null) {
            val errorString = strerror(errno)?.toKString()
            println("Failed to open file - $filepath with mode ${mode.toNativeFileMode()} with error $errorString")
        }
    }

    actual fun write(
        bytes: ByteArray,
        offset: Int,
        size: Int,
    ): Int {
        file?.let { file ->
            bytes.usePinned { pinned ->
                return fwrite(
                    pinned.addressOf(offset),
                    1u,
                    size.toULong(),
                    file,
                ).toInt()
            }
        }
        return 0
    }

    actual fun read(
        bytes: ByteArray,
        offset: Int,
        size: Int,
    ): Int {
        file?.let { file ->
            bytes.usePinned { pinned ->
                return fread(
                    pinned.addressOf(offset),
                    1u,
                    size.toULong(),
                    file,
                ).toInt()
            }
        }
        return 0
    }

    actual fun flush() {
        file?.let { file ->
            fflush(file)
        }
    }

    actual fun delete() {
        file?.let { file ->
            fclose(file)
            removefile(filepath, null, REMOVEFILE_RECURSIVE)
        }
        file = null
    }
}
