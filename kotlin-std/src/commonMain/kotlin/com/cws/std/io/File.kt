package com.cws.std.io

enum class FileMode {
    CREATE_IF_NOT_EXIST, // creates file recursively if it doesn't exist, otherwise works as OPEN_EXISTING
    CLEAR_WHEN_OPEN, // every time when file is opened it will wipe out its previous content
    OPEN_EXISTING // guarantees that files exists so no need to check, just open it
}

expect class File(
    filepath: String,
    mode: FileMode = FileMode.CREATE_IF_NOT_EXIST,
) : AutoCloseable {
    val size: Int
    val isOpened: Boolean

    override fun close()
    fun open()
    fun write(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int
    fun read(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int
    fun flush()
    fun delete()
}

fun File.write(text: String): Int {
    val bytes = text.encodeToByteArray()
    return write(bytes, 0, bytes.size)
}

fun File.readText(): String {
    if (size == 0) return ""
    val bytes = ByteArray(size)
    val bytesRead = read(bytes)
    return bytes.decodeToString(0, bytesRead)
}
