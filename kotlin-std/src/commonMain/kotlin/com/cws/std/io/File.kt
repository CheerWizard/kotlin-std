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

enum class FileMode {
    CREATE_IF_NOT_EXIST, // creates file recursively if it doesn't exist, otherwise works as OPEN_EXISTING
    CLEAR_WHEN_OPEN, // every time when file is opened it will wipe out its previous content
    OPEN_EXISTING, // guarantees that files exists so no need to check, just open it
}

expect class File(
    filepath: String,
    mode: FileMode = FileMode.CREATE_IF_NOT_EXIST,
) : AutoCloseable {
    val size: Int
    val isOpened: Boolean

    override fun close()

    fun open()

    fun write(
        bytes: ByteArray,
        offset: Int = 0,
        size: Int = bytes.size,
    ): Int

    fun read(
        bytes: ByteArray,
        offset: Int = 0,
        size: Int = bytes.size,
    ): Int

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
