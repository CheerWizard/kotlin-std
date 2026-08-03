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
@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.io

import com.cws.std.memory.NativeBuffer
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import kotlin.js.ExperimentalWasmJsInterop

// FIXME: currently its just in-memory file implementation, maybe will need to add real file caching
actual class File actual constructor(
    private val filepath: String,
    mode: FileMode,
    access: FileAccess,
) {

    actual val size: Int
        get() = buffer?.limit ?: 0

    actual val isOpened: Boolean
        get() = buffer != null

    actual val access: FileAccess = access

    private var buffer: NativeBuffer? = null

    actual var mapped: NativeBuffer? = null
        private set

    actual suspend fun closeImpl() {
        buffer = null
    }

    actual suspend fun openImpl() {
        val response = window.fetch(filepath, RequestInit()).await()
        val buffer = response.arrayBuffer().await() as? js.buffer.ArrayBuffer ?: return
        this.buffer = NativeBuffer(buffer)
    }

    actual suspend fun writeImpl(bytes: ByteArray, offset: Int, size: Int): Int {
        this.buffer?.let { buffer ->
            buffer.setByteArray(offset, bytes.sliceArray(0..size))
            return size
        }
        return 0
    }

    actual suspend fun writeImpl(buffer: NativeBuffer, offset: Int, size: Int): Int {
        val dst = this.buffer ?: return 0
        buffer.copyTo(dst, 0, offset, size)
        return size
    }

    actual suspend fun readImpl(bytes: ByteArray, offset: Int, size: Int): Int {
        val src = this.buffer ?: return 0
        src.copyToByteArray(bytes, offset, size)
        return size
    }

    actual suspend fun readImpl(buffer: NativeBuffer, offset: Int, size: Int): Int {
        val src = this.buffer ?: return 0
        src.copyTo(buffer, offset, 0, size)
        return size
    }

    actual suspend fun flushImpl() {
        val blob = Blob()
        window.fetch(filepath, PostBlob(blob))
    }

    actual suspend fun delete() {
        // FIXME: figure out how to delete file blob from browser
        val blob = Blob()
        window.fetch(filepath, DeleteBlob(blob))
    }

    internal actual suspend fun mapImpl(offset: Int, size: Int): NativeBuffer? {
        val buffer = buffer ?: return null
        val slice = buffer.buffer?.slice(offset, size) ?: return null
        mapped = NativeBuffer(slice)
        return mapped
    }

    internal actual suspend fun unmapImpl() {
        mapped = null
    }

}

fun PostBlob(blob: Blob): RequestInit = RequestInit().apply {
    method = "POST"
    body = blob
}

fun DeleteBlob(blob: Blob): RequestInit = RequestInit().apply {
    method = "DELETE"
    body = blob
}
