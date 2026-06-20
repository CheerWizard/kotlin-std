package com.cws.std.io

expect class File(filepath: String) : AutoCloseable {
    val size: Int
    val isOpened: Boolean

    override fun close()
    fun open()
    fun write(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int
    fun read(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int
    fun flush()
}

fun File.write(text: String): Int {
    val bytes = text.encodeToByteArray()
    return write(bytes, 0, bytes.size)
}
