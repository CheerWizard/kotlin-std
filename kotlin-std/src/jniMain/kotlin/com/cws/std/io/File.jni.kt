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

import com.cws.std.memory.MemoryBoundary
import com.cws.std.memory.NativeBuffer
import com.cws.std.memory.toEndian
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.OpenOption
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path

actual enum class FileMode {
    CREATE_IF_NOT_EXIST,
    CLEAR_WHEN_OPEN,
    OPEN_EXISTING,
}

actual enum class FileAccess(val mapMode: FileChannel.MapMode) {
    READ_ONLY(FileChannel.MapMode.READ_ONLY),
    WRITE_ONLY(FileChannel.MapMode.READ_WRITE),
    READ_WRITE(FileChannel.MapMode.READ_WRITE),
}

actual class File actual constructor(
    private val filepath: String,
    private val mode: FileMode,
    actual val access: FileAccess,
) {

    actual val size: Int
        get() = fileChannel?.size()?.toInt() ?: 0

    actual val isOpened: Boolean
        get() = fileChannel?.isOpen != null

    private var fileChannel: FileChannel? = null

    actual var mapped: NativeBuffer? = null
        private set

    internal actual suspend fun openImpl() {
        val openOptions = buildSet<OpenOption> {
            when (access) {
                FileAccess.READ_ONLY -> add(StandardOpenOption.READ)
                FileAccess.WRITE_ONLY -> add(StandardOpenOption.WRITE)
                FileAccess.READ_WRITE -> {
                    add(StandardOpenOption.READ)
                    add(StandardOpenOption.WRITE)
                }
            }
            when (mode) {
                FileMode.CLEAR_WHEN_OPEN -> add(StandardOpenOption.TRUNCATE_EXISTING)
                FileMode.OPEN_EXISTING -> add(StandardOpenOption.APPEND)
                FileMode.CREATE_IF_NOT_EXIST -> add(StandardOpenOption.CREATE)
            }
        }

        val fileChannel = FileChannel.open(Path(filepath), openOptions)
        this.fileChannel = fileChannel
    }

    actual suspend fun writeImpl(
        bytes: ByteArray,
        offset: Int,
        size: Int,
    ): Int {
        return fileChannel?.write(ByteBuffer.wrap(bytes, offset, size)) ?: size
    }

    actual suspend fun writeImpl(
        buffer: NativeBuffer,
        offset: Int,
        size: Int,
    ): Int {
        val buffer = buffer.buffer ?: return 0
        return fileChannel?.write(buffer) ?: size
    }

    actual suspend fun readImpl(
        bytes: ByteArray,
        offset: Int,
        size: Int,
    ): Int {
        return fileChannel?.read(ByteBuffer.wrap(bytes, offset, size)) ?: size
    }

    actual suspend fun readImpl(
        buffer: NativeBuffer,
        offset: Int,
        size: Int,
    ): Int {
        val buffer = buffer.buffer ?: return 0
        return fileChannel?.read(buffer) ?: size
    }

    internal actual suspend fun flushImpl() {
    }

    internal actual suspend fun closeImpl() {
        fileChannel?.close()
        fileChannel = null
    }

    actual suspend fun delete() {
        java.io.File(filepath).deleteRecursively()
    }

    internal actual suspend fun mapImpl(offset: Int, size: Int): NativeBuffer? {
        val buffer = fileChannel?.map(access.mapMode, offset.toLong(), size.toLong()) ?: return null
        mapped = NativeBuffer(buffer, endian = buffer.order().toEndian(), memoryBoundary = MemoryBoundary.EXTERNAL)
        return mapped
    }

    internal actual suspend fun unmapImpl() {
        mapped = null
    }

}
