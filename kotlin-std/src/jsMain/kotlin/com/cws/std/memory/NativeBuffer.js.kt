package com.cws.std.memory

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.DataView
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Float64Array
import org.khronos.webgl.Int16Array
import org.khronos.webgl.Int32Array
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint16Array
import org.khronos.webgl.Uint32Array
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import kotlin.js.unsafeCast

actual open class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
) {

    actual constructor(address: Long, capacity: Int, memoryLayout: MemoryLayout, endian: Endian) : this(capacity, memoryLayout, endian) {

    }

    constructor(buffer: ArrayBuffer, memoryLayout: MemoryLayout = MemoryLayout.KOTLIN) : this(buffer.byteLength, memoryLayout) {
        this.buffer = buffer
    }

    actual val endian: Endian = endian
    actual val memoryLayout: MemoryLayout = memoryLayout

    actual var position: Int = 0
    actual val capacity: Int get() = buffer.byteLength
    actual val address: Long get() = 0L

    var buffer = ArrayBuffer(capacity)
    // all preallocated views for buffer
    private var int8     = Int8Array(buffer)
    private var int16    = Int16Array(buffer)
    private var int32    = Int32Array(buffer)
    private var float32  = Float32Array(buffer)
    private var float64  = Float64Array(buffer)
    private var uint8    = Uint8Array(buffer)
    private var uint16   = Uint16Array(buffer)
    private var uint32   = Uint32Array(buffer)

    actual fun view(): Any = int8

    actual fun resize(newCapacity: Int) {
        buffer = ArrayBuffer(newCapacity)
        int8     = Int8Array(buffer)
        int16    = Int16Array(buffer)
        int32    = Int32Array(buffer)
        float32  = Float32Array(buffer)
        float64  = Float64Array(buffer)
        uint8    = Uint8Array(buffer)
        uint16   = Uint16Array(buffer)
        uint32   = Uint32Array(buffer)
    }

    actual fun clone(): NativeBuffer {
        return NativeBuffer(capacity)
    }

    actual fun release() = Unit

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        val byteView = dest.view() as Int8Array
        byteView.set(this.int8.subarray(srcIndex, srcIndex + size), destIndex)
    }

    actual fun setTo(value: Byte, destIndex: Int, size: Int) {
        repeat(size) { i -> int8[destIndex + i] = value }
    }

    actual fun setByte(index: Int, value: Byte) {
        int8[index] = value
    }

    actual fun getByte(index: Int): Byte {
        return int8[index]
    }

    actual fun setByteArray(index: Int, array: ByteArray) {
        int8.set(array.toTypedArray(), index)
    }

    actual fun setCharArray(index: Int, array: CharArray) {
        val shorts = ShortArray(array.size) { array[it].code.toShort() }
        setShortArray(index, shorts)
    }

    actual fun setShortArray(index: Int, array: ShortArray) {
        int16.set(array.toTypedArray(), index)
    }

    actual fun setIntArray(index: Int, array: IntArray) {
        int32.set(array.toTypedArray(), index)
    }

    actual fun setFloatArray(index: Int, array: FloatArray) {
        float32.set(array.toTypedArray(), index)
    }

    actual fun setLongArray(index: Int, array: LongArray) {
        array.forEachIndexed { i, value ->
            jsSetLong(index + i * 8, value)
        }
    }

    actual fun setDoubleArray(index: Int, array: DoubleArray) {
        float64.set(array.toTypedArray(), index)
    }

    actual fun copyToByteArray(array: ByteArray, offset: Int, size: Int): ByteArray {
        return int8.subarray(offset, offset + size).unsafeCast<ByteArray>()
    }

    actual fun copyToCharArray(array: CharArray, offset: Int, size: Int): CharArray {
        val shorts = ShortArray(size) { array[it].code.toShort() }
        copyToShortArray(shorts, offset, size)
        for (i in shorts.indices) {
            array[i] = shorts[i].toInt().toChar()
        }
        return array
    }

    actual fun copyToShortArray(array: ShortArray, offset: Int, size: Int): ShortArray {
        return int16.subarray(offset, offset + size).unsafeCast<ShortArray>()
    }

    actual fun copyToIntArray(array: IntArray, offset: Int, size: Int): IntArray {
        return int32.subarray(offset, offset + size).unsafeCast<IntArray>()
    }

    actual fun copyToFloatArray(array: FloatArray, offset: Int, size: Int): FloatArray {
        return float32.subarray(offset, offset + size).unsafeCast<FloatArray>()
    }

    actual fun copyToLongArray(array: LongArray, offset: Int, size: Int): LongArray {
        return LongArray(size) { jsGetLong(offset + it * 8) }
    }

    actual fun copyToDoubleArray(array: DoubleArray, offset: Int, size: Int): DoubleArray {
        return float64.subarray(offset, offset + size).unsafeCast<DoubleArray>()
    }

    private fun jsSetLong(index: Int, value: Long) {
        int32[index / 4]     = (value and 0xFFFFFFFFL).toInt()
        int32[index / 4 + 1] = (value ushr 32).toInt()
    }

    private fun jsGetLong(index: Int): Long {
        val low  = int32[index / 4].toLong() and 0xFFFFFFFFL
        val high = int32[index / 4 + 1].toLong() and 0xFFFFFFFFL
        return low or (high shl 32)
    }

}