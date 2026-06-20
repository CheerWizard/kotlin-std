package com.cws.std.io

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
import kotlin.math.max

actual class File actual constructor(private val filepath: String) : AutoCloseable {

    actual val size: Int
        get() = buffer?.byteLength ?: 0

    actual val isOpened: Boolean
        get() = buffer != null

    private var buffer: ArrayBuffer? = null
    private var view: Uint8Array? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    actual override fun close() {
        buffer = null
        view = null
    }

    actual fun open() {
        scope.launch {
            val response = window.fetch(filepath).await()
            val buffer = response.arrayBuffer().await()
            this@File.buffer = buffer
            view = Uint8Array(buffer)
        }
    }

    actual fun write(bytes: ByteArray, offset: Int, size: Int): Int {
        val minSize = max(this.size - offset, 0)
        val usedSize = if (minSize > size) size else minSize
        view?.let { view ->
            repeat(usedSize) { i ->
                view[i + offset] = bytes[i]
            }
        }
        return usedSize
    }

    actual fun read(bytes: ByteArray, offset: Int, size: Int): Int {
        val minSize = max(this.size - offset, 0)
        val usedSize = if (minSize > size) size else minSize
        view?.let { view ->
            repeat(usedSize) { i ->
                bytes[i + offset] = view[i]
            }
        }
        return usedSize
    }

    actual fun flush() {
        view?.let { view ->
            val blob = Blob(Array<Byte>(view.byteLength) { view[it] })
            window.fetch(filepath, object : RequestInit {
                override var method: String? = "POST"
                override var body: Any? = blob
            })
        }
    }

}
