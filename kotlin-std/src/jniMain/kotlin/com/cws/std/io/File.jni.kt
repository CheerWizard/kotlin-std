package com.cws.std.io

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File

actual class File actual constructor(
    private val filepath: String,
    private val mode: FileMode,
) : AutoCloseable {

    actual val size: Int
        get() = file?.length()?.toInt() ?: 0

    actual val isOpened: Boolean
        get() = file != null

    private var file: File? = null
    private var outputStream: BufferedOutputStream? = null
    private var inputStream: BufferedInputStream? = null

    init {
        open()
    }

    actual fun open() {
        if (isOpened) return

        val file = File(filepath)
        if (mode != FileMode.OPEN_EXISTING) {
            file.parentFile?.mkdirs()
            if (!file.exists()) {
                file.createNewFile()
            }
        }

        this.file = file
    }

    actual fun write(bytes: ByteArray, offset: Int, size: Int): Int {
        if (!isOpened) return 0

        inputStream?.let { it.close(); inputStream = null }

        if (outputStream == null) {
            outputStream = file?.outputStream()?.buffered()
        }

        outputStream?.write(bytes, offset, size)
        return size
    }

    actual fun read(bytes: ByteArray, offset: Int, size: Int): Int {
        if (!isOpened) return 0

        outputStream?.let { it.flush(); it.close(); outputStream = null }

        if (inputStream == null) {
            inputStream = file?.inputStream()?.buffered()
        }

        return inputStream?.read(bytes, offset, size) ?: 0
    }

    actual fun flush() {
        outputStream?.flush()
    }

    actual override fun close() {
        outputStream?.let { stream ->
            stream.flush()
            stream.close()
        }
        inputStream?.close()

        outputStream = null
        inputStream = null
        file = null
    }

    actual fun delete() {
        file?.deleteRecursively()
    }
}