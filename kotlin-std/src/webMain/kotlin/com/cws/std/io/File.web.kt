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

import io.ktor.util.toJsArray
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.fetch.RequestInit
import org.w3c.files.Blob
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.math.max

actual class File actual constructor(
    private val filepath: String,
    mode: FileMode, // not used
) : AutoCloseable {

    actual val size: Int
        get() = buffer?.byteLength ?: 0

    actual val isOpened: Boolean
        get() = buffer != null

    private var buffer: ArrayBuffer? = null
    private var view: Uint8Array? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        open()
    }

    actual override fun close() {
        flush()
        buffer = null
        view = null
    }

    actual fun open() {
        if (isOpened) return
        scope.launch {
            val response = window.fetch(filepath, RequestInit()).await()
            val buffer = response.arrayBuffer().await()
            this@File.buffer = buffer
            view = Uint8Array(buffer)
        }
    }

    actual fun write(bytes: ByteArray, offset: Int, size: Int): Int {
        val minSize = max(this.size - offset, 0)
        val usedSize = if (minSize > size) size else minSize
        val jsBytes = bytes.toJsArray()
        view?.let { view ->
            repeat(usedSize) { i ->
                view[i + offset] = jsBytes[i]
            }
        }
        return usedSize
    }

    actual fun read(bytes: ByteArray, offset: Int, size: Int): Int {
        val minSize = max(this.size - offset, 0)
        val usedSize = if (minSize > size) size else minSize
        val jsBytes = bytes.toJsArray()
        view?.let { view ->
            repeat(usedSize) { i ->
                jsBytes[i + offset] = view[i]
            }
        }
        return usedSize
    }

    actual fun flush() {
        view?.let { view ->
            val blob = Blob()
            window.fetch(filepath, PostBlob(blob))
        }
    }

    actual fun delete() {
        // FIXME: figure out how to delete file blob from browser
    }

}

fun PostBlob(blob: Blob): RequestInit = RequestInit().apply {
    method = "POST"
    body = blob
}
