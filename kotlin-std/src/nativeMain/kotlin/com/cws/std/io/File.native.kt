@file:OptIn(UnsafeNumber::class)

package com.cws.std.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.pin
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.FILE
import platform.posix.SEEK_END
import platform.posix.SEEK_SET
import platform.posix.errno
import platform.posix.fclose
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fread
import platform.posix.fseek
import platform.posix.ftell
import platform.posix.fwrite
import platform.posix.remove
import platform.posix.strerror

private fun FileMode.toNativeFileMode(): String = when (this) {
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

    private var file: CPointer<FILE>? = null

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

    actual fun write(bytes: ByteArray, offset: Int, size: Int): Int {
        file?.let { file ->
            bytes.usePinned { pinned ->
                return fwrite(
                    pinned.addressOf(offset),
                    1u,
                    size.toULong(),
                    file
                ).toInt()
            }
        }
        return 0
    }

    actual fun read(bytes: ByteArray, offset: Int, size: Int): Int {
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
            remove(filepath)
        }
        file = null
    }

}