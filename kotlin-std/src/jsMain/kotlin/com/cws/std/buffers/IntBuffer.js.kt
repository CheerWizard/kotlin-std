package com.cws.std.buffers

import org.khronos.webgl.*

actual class IntBuffer actual constructor(capacity: Int) {

    actual var position: Int
        set(value) {
            _position = value
        }
        get() = _position

    actual val capacity: Int get() = buffer.byteLength

    var buffer = Int32Array(capacity)
    protected var _position: Int = 0

    actual fun view(): Any = buffer

    actual fun resize(newCapacity: Int) {
        buffer = Int32Array(newCapacity)
    }

    actual fun release() = Unit

    actual fun copyTo(
        dest: IntBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        val view = dest.view() as Int32Array
        view.set(buffer.subarray(srcIndex, srcIndex + size), destIndex)
    }

    actual fun setTo(value: Int, destIndex: Int, size: Int) {
        repeat(size) { i -> buffer[destIndex + i] = value }
    }

    actual fun setArray(index: Int, intArray: IntArray) {
        for (i in intArray.indices) {
            buffer[i + index] = intArray[i]
        }
    }

    actual operator fun set(index: Int, value: Int) { buffer[index] = value }

    actual operator fun get(index: Int): Int = buffer[index]

}