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

import com.cws.std.memory.NativeBuffer
import com.cws.std.memory.isEmpty

expect enum class FileMode {
    CREATE_IF_NOT_EXIST, // creates file recursively if it doesn't exist, otherwise works as OPEN_EXISTING
    CLEAR_WHEN_OPEN, // every time when file is opened it will wipe out its previous content
    OPEN_EXISTING, // guarantees that files exists so no need to check, just open it
}

expect enum class FileAccess {
    READ_ONLY,
    WRITE_ONLY,
    READ_WRITE,
}

expect class File(
    filepath: String,
    mode: FileMode = FileMode.CREATE_IF_NOT_EXIST,
    access: FileAccess = FileAccess.READ_WRITE,
) {

    val size: Int
    val isOpened: Boolean
    val access: FileAccess
    var mapped: NativeBuffer?
        private set

    internal suspend fun openImpl()

    internal suspend fun closeImpl()

    internal suspend fun mapImpl(offset: Int = 0, size: Int = this.size): NativeBuffer?

    internal suspend fun unmapImpl()

    internal suspend fun writeImpl(
        bytes: ByteArray,
        offset: Int = 0,
        size: Int = bytes.size,
    ): Int

    internal suspend fun writeImpl(
        buffer: NativeBuffer,
        offset: Int = 0,
        size: Int = buffer.limit,
    ): Int

    internal suspend fun readImpl(
        bytes: ByteArray,
        offset: Int = 0,
        size: Int = bytes.size,
    ): Int

    internal suspend fun readImpl(
        buffer: NativeBuffer,
        offset: Int = 0,
        size: Int = buffer.limit,
    ): Int

    internal suspend fun flushImpl()

    suspend fun delete()
}

val File.writable: Boolean get() = access == FileAccess.READ_WRITE || access == FileAccess.WRITE_ONLY

val File.readable: Boolean get() = access == FileAccess.READ_WRITE || access == FileAccess.READ_ONLY

suspend fun File.open(): File {
    if (isOpened) return this
    openImpl()
    return this
}

suspend fun File.close(): File {
    if (!isOpened) return this
    flushImpl()
    closeImpl()
    return this
}

suspend inline fun File.use(block: File.() -> Unit) {
    open()
    block()
    close()
}

suspend fun File.write(text: String): Int {
    if (!isOpened || !writable || text.isEmpty()) return 0
    val bytes = text.encodeToByteArray()
    return writeImpl(bytes, 0, bytes.size)
}

suspend fun File.readText(): String {
    if (!isOpened || size <= 0 || !readable) return ""
    val bytes = ByteArray(size)
    val bytesRead = readImpl(bytes)
    return bytes.decodeToString(0, bytesRead)
}

suspend fun File.write(
    bytes: ByteArray,
    offset: Int = 0,
    size: Int = bytes.size,
): Int {
    if (!isOpened || !writable || bytes.isEmpty() || offset < 0 || size <= 0) return 0
    return writeImpl(bytes, offset, size)
}

suspend fun File.write(
    buffer: NativeBuffer,
    offset: Int = 0,
    size: Int = buffer.limit,
): Int {
    if (!isOpened || !writable || buffer.isEmpty() || offset < 0 || size <= 0) return 0
    return writeImpl(buffer, offset, size)
}

suspend fun File.read(
    bytes: ByteArray,
    offset: Int = 0,
    size: Int = bytes.size,
): Int {
    if (!isOpened || !readable || bytes.isEmpty() || offset < 0 || size <= 0) return 0
    return readImpl(bytes, offset, size)
}

suspend fun File.read(
    buffer: NativeBuffer,
    offset: Int = 0,
    size: Int = buffer.limit,
): Int {
    if (!isOpened || !readable || buffer.isEmpty() || offset < 0 || size <= 0) return 0
    return readImpl(buffer, offset, size)
}

suspend fun File.flush(): File {
    if (!isOpened) return this
    flushImpl()
    return this
}

suspend fun File.map(offset: Int = 0, size: Int = this.size): NativeBuffer? {
    if (!isOpened || this.size <= 0 || offset < 0 || size <= 0 || mapped != null) return null
    return mapImpl()
}

suspend fun File.unmap(): File {
    if (!isOpened || this.size <= 0 || mapped == null) return this
    unmapImpl()
    return this
}