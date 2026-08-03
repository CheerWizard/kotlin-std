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

import com.cws.print.Print
import com.cws.std.memory.NativeBuffer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.plus
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.darwin.ByteVar
import platform.darwin.REMOVEFILE_RECURSIVE
import platform.darwin.removefile
import platform.posix.MAP_SHARED
import platform.posix.PROT_READ
import platform.posix.PROT_WRITE
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.__sFILE
import platform.posix.errno
import platform.posix.fclose
import platform.posix.feof
import platform.posix.fflush
import platform.posix.fileno
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.fwrite
import platform.posix.mmap
import platform.posix.munmap
import platform.posix.strerror

actual enum class FileMode(val mode: String) {
    CREATE_IF_NOT_EXIST("a+"),
    CLEAR_WHEN_OPEN("w+"),
    OPEN_EXISTING("r+"),
}

actual enum class FileAccess(val flags: Int) {
    READ_ONLY(PROT_READ),
    WRITE_ONLY(PROT_WRITE),
    READ_WRITE(PROT_READ or PROT_WRITE),
}

@OptIn(ExperimentalForeignApi::class)
actual class File actual constructor(
    private val filepath: String,
    private val mode: FileMode,
    access: FileAccess,
) {

    companion object {
        private const val TAG = "File"
    }

    actual val size: Int get() {
        val f = file ?: return 0
        val current = ftell(f)
        fseek(f, 0, SEEK_END)
        val end = ftell(f)
        // seek back to where file cursor was to prevent incorrect read/write
        fseek(f, current, SEEK_SET)
        return end.toInt()
    }

    actual val isOpened: Boolean
        get() = file != null

    actual var mapped: NativeBuffer? = null
        private set

    actual val access: FileAccess = access

    private var file: CPointer<__sFILE>? = null
    private var fileDescriptor: Int = -1

    internal actual suspend fun closeImpl() {
        file?.let { file ->
            fflush(file)
            fclose(file)
        }
        file = null
    }

    internal actual suspend fun openImpl() {
        file = fopen(filepath, mode.mode)
        if (file == null) {
            val errorString = strerror(errno)?.toKString()
            Print.e(TAG) { "Failed to open file - $filepath with mode ${mode.mode} with error $errorString" }
            return
        }
        fileDescriptor = fileno(file)
    }

    internal actual suspend fun writeImpl(
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

    internal actual suspend fun writeImpl(buffer: NativeBuffer, offset: Int, size: Int): Int {
        file?.let { file ->
            buffer.buffer?.let { buffer ->
                return fwrite(
                    buffer + offset,
                    1u,
                    size.toULong(),
                    file,
                ).toInt()
            }
        }
        return 0
    }

    actual suspend fun readImpl(
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

    internal actual suspend fun readImpl(buffer: NativeBuffer, offset: Int, size: Int): Int {
        file?.let { file ->
            buffer.buffer?.let { buffer ->
                return fread(
                    buffer + offset,
                    1u,
                    size.toULong(),
                    file,
                ).toInt()
            }
        }
        return 0
    }

    actual suspend fun flushImpl() {
        file?.let { file ->
            fflush(file)
        }
    }

    actual suspend fun delete() {
        file?.let { file ->
            fclose(file)
            removefile(filepath, null, REMOVEFILE_RECURSIVE)
        }
        file = null
    }

    internal actual suspend fun mapImpl(offset: Int, size: Int): NativeBuffer? {
        if (fileDescriptor == -1) return null
        val ptr = mmap(
            null,
            size.toULong(),
            access.flags,
            MAP_SHARED,
            fileDescriptor,
            0
        ) ?: return null
        mapped = NativeBuffer(ptr.reinterpret(), size)
        return mapped
    }

    internal actual suspend fun unmapImpl() {
        val mapped = mapped ?: return
        munmap(mapped.buffer, mapped.limit.toULong())
    }
}
