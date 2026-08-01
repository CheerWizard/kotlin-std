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
package com.cws.std.memory

import js.buffer.ArrayBuffer
import js.buffer.DataView
import js.numbers.JsNumbers.toJsByte
import js.numbers.JsNumbers.toKotlinByte
import js.typedarrays.Int8Array
import js.typedarrays.toInt8Array

actual class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
    memoryBoundary: MemoryBoundary
) {

    // not supported on web buffers
    actual constructor(
        address: Long,
        capacity: Int,
        memoryLayout: MemoryLayout,
        endian: Endian,
    ) : this(0, memoryLayout, endian, MemoryBoundary.EXTERNAL)

    actual constructor(
        buffer: ByteArray,
        memoryLayout: MemoryLayout,
        endian: Endian
    ) : this(0, memoryLayout, endian, MemoryBoundary.KOTLIN_HEAP) {
        val bytes = buffer.toInt8Array()
        this.bytes = bytes
        this.buffer = bytes.buffer
        dataView = DataView(bytes.buffer)
        limit = buffer.size
    }

    constructor(
        buffer: ArrayBuffer,
        memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
        endian: Endian = Endian.LITTLE
    ) : this(0, memoryLayout, endian, MemoryBoundary.KOTLIN_HEAP) {
        this.buffer = buffer
        this.bytes = Int8Array(buffer)
        dataView = DataView(buffer)
        limit = buffer.byteLength
    }

    actual val endian: Endian = endian
    actual val memoryLayout: MemoryLayout = memoryLayout
    actual var memoryBoundary: MemoryBoundary = memoryBoundary
        private set

    actual var position: Int = 0
        internal set

    actual val address: Long get() = 0L

    var buffer: ArrayBuffer? = null
    private var bytes: Int8Array<ArrayBuffer>? = null
    internal var dataView: DataView<ArrayBuffer>? = null

    actual var limit: Int = 0
        private set

    private val capacity: Int get() = buffer?.byteLength ?: 0

    init {
        if (capacity > 0) {
            val buffer = ArrayBuffer(capacity)
            this.buffer = buffer
            bytes = Int8Array(buffer)
            dataView = DataView(buffer)
            limit = buffer.byteLength
        }
    }

    actual fun view(): Any? = buffer

    actual fun resize(newCapacity: Int) {
        val oldPosition = position
        when {
            newCapacity == capacity || newCapacity == limit -> return
            newCapacity < capacity || newCapacity < limit -> {
                // shrinking only reallocates view to buffer to smaller size, buffer stays unchanged
                bytes?.let { bytes ->
                    this.bytes = bytes.subarray(0, newCapacity)
                    dataView = DataView(bytes.buffer)
                    limit = newCapacity
                }
            }
            else -> {
                // for growing reallocating new buffer with views
                val newBuffer = ArrayBuffer(newCapacity)
                val newBytes = Int8Array(newBuffer)
                bytes?.let { bytes ->
                    newBytes.set(bytes)
                }
                buffer = newBytes.buffer
                bytes = newBytes
                dataView = DataView(newBytes.buffer)
                limit = newCapacity
            }
        }
        position = minOf(oldPosition, newCapacity)
    }

    actual fun release() {
        buffer = null
        bytes = null
        dataView = null
        limit = 0
        position = 0
    }

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        sizeBytes: Int,
    ) {
        val bytes = this.bytes
        val destBytes = dest.bytes
        if (bytes == null || destBytes == null) return
        destBytes.set(bytes.subarray(srcIndex, srcIndex + sizeBytes), destIndex)
    }

    actual fun setTo(value: Byte, destIndex: Int, sizeBytes: Int) {
        val bytes = bytes ?: return
        repeat(sizeBytes) { i -> bytes[destIndex + i] = value.toJsByte() }
    }

    actual fun setByte(index: Int, value: Byte) {
        val bytes = bytes ?: return
        bytes[index] = value.toJsByte()
    }

    actual fun getByte(index: Int): Byte {
        val bytes = bytes ?: return 0
        return bytes[index].toKotlinByte()
    }

    actual fun setByteArray(index: Int, array: ByteArray) {
        bytes?.set(array.toInt8Array(), index)
    }

    actual fun setCharArray(index: Int, array: CharArray) {
        val dataView = dataView ?: return
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            dataView.setInt16(index + i * 2, array[i].code.toShort(), littleEndian)
        }
    }

    actual fun setShortArray(index: Int, array: ShortArray) {
        val dataView = dataView ?: return
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            dataView.setInt16(index + i * 2, array[i], littleEndian)
        }
    }

    actual fun setIntArray(index: Int, array: IntArray) {
        val dataView = dataView ?: return
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            dataView.setInt32(index + i * 4, array[i], littleEndian)
        }
    }

    actual fun setFloatArray(index: Int, array: FloatArray) {
        val dataView = dataView ?: return
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            dataView.setFloat32(index + i * 4, array[i], littleEndian)
        }
    }

    actual fun setLongArray(index: Int, array: LongArray) {
        val dataView = dataView ?: return
        array.forEachIndexed { i, value ->
            jsSetLong(dataView, index + i * 8, value)
        }
    }

    actual fun setDoubleArray(index: Int, array: DoubleArray) {
        val dataView = dataView ?: return
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            dataView.setFloat64(index + i * 8, array[i], littleEndian)
        }
    }

    actual fun copyToByteArray(array: ByteArray, offset: Int, sizeBytes: Int): ByteArray {
        val bytes = bytes ?: return byteArrayOf()
        for (i in 0 until sizeBytes) {
            array[i] = bytes[offset + i].toKotlinByte()
        }
        return array
    }

    actual fun copyToCharArray(array: CharArray, offset: Int, sizeBytes: Int): CharArray {
        val dataView = dataView ?: return charArrayOf()
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            array[i] = dataView.getInt16(offset + i * 2, littleEndian).toInt().toChar()
        }
        return array
    }

    actual fun copyToShortArray(array: ShortArray, offset: Int, sizeBytes: Int): ShortArray {
        val dataView = dataView ?: return shortArrayOf()
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            array[i] = dataView.getInt16(offset + i * 2, littleEndian)
        }
        return array
    }

    actual fun copyToIntArray(array: IntArray, offset: Int, sizeBytes: Int): IntArray {
        val dataView = dataView ?: return intArrayOf()
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            array[i] = dataView.getInt32(offset + i * 4, littleEndian)
        }
        return array
    }

    actual fun copyToFloatArray(array: FloatArray, offset: Int, sizeBytes: Int): FloatArray {
        val dataView = dataView ?: return floatArrayOf()
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            array[i] = dataView.getFloat32(offset + i * 4, littleEndian)
        }
        return array
    }

    actual fun copyToLongArray(array: LongArray, offset: Int, sizeBytes: Int): LongArray {
        val dataView = dataView ?: return longArrayOf()
        repeat(array.size) { i ->
            array[i] = jsGetLong(dataView, offset + i * 8)
        }
        return array
    }

    actual fun copyToDoubleArray(array: DoubleArray, offset: Int, sizeBytes: Int): DoubleArray {
        val dataView = dataView ?: return doubleArrayOf()
        val littleEndian = endian == Endian.LITTLE
        repeat(array.size) { i ->
            array[i] = dataView.getFloat64(offset + i * 8, littleEndian)
        }
        return array
    }

    private fun jsSetLong(dataView: DataView<ArrayBuffer>, offset: Int, value: Long) {
        val littleEndian = endian == Endian.LITTLE
        dataView.setInt32(offset, (value and 0xFFFFFFFFL).toInt(), littleEndian)
        dataView.setInt32(offset + 4, (value ushr 32).toInt(), littleEndian)
    }

    private fun jsGetLong(dataView: DataView<ArrayBuffer>, offset: Int): Long {
        val littleEndian = endian == Endian.LITTLE
        val low  = dataView.getInt32(offset, littleEndian).toLong() and 0xFFFFFFFFL
        val high = dataView.getInt32(offset + 4, littleEndian).toLong() and 0xFFFFFFFFL
        return low or (high shl 32)
    }

}