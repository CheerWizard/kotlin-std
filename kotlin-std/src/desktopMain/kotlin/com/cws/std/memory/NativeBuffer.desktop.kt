package com.cws.std.memory

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

actual open class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
) {

    actual val memoryLayout: MemoryLayout = memoryLayout
    actual val endian: Endian = endian

    constructor(buffer: ByteBuffer) : this(buffer.capacity()) {
        this.buffer = buffer
    }

    actual constructor(address: Long, capacity: Int, memoryLayout: MemoryLayout, endian: Endian) : this(capacity, memoryLayout, endian) {
        this.buffer = ByteBuffer.allocateDirect(capacity)
            ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes from address $address")
    }

    var buffer: ByteBuffer = ByteBuffer.allocateDirect(capacity)
        ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes")

    //    actual val address: Long = CMemory.addressOf(buffer)
    actual val address: Long = 0

    actual var position: Int
        set(value) {
            buffer.position(value)
        }
        get() = buffer.position()

    actual val capacity: Int get() = buffer.capacity()
    val size: Int get() = buffer.remaining()

    actual fun release() {
//        CMemory.free(buffer)
    }

    actual fun view(): Any = buffer

    actual fun resize(newCapacity: Int) {
        buffer = ByteBuffer.allocateDirect(newCapacity)
            ?: throw RuntimeException("Failed to reallocate for NativeBuffer $newCapacity bytes")
    }

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        val srcSlice = buffer.duplicate()
        srcSlice.position(srcIndex)
        srcSlice.limit(srcIndex + size)
        val destLastPosition = dest.position
        dest.position = destIndex
        dest.buffer.put(srcSlice)
        dest.position = destLastPosition
    }

    actual fun setByteArray(index: Int, array: ByteArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setCharArray(index: Int, array: CharArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asCharBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setShortArray(index: Int, array: ShortArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asShortBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setIntArray(index: Int, array: IntArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asIntBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setFloatArray(index: Int, array: FloatArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asFloatBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setLongArray(index: Int, array: LongArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asLongBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setDoubleArray(index: Int, array: DoubleArray) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index).asDoubleBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun clone(): NativeBuffer {
        return NativeBuffer(capacity)
    }

    actual fun setTo(value: Byte, destIndex: Int, size: Int) {
        repeat(size) { i -> buffer.put(destIndex + i, value) }
    }

    actual fun setByte(index: Int, value: Byte) {
        buffer.put(index, value)
    }

    actual fun getByte(index: Int): Byte = buffer.get(index)

    fun copy(shortBuffer: ShortBuffer) {
        buffer.asShortBuffer().put(shortBuffer)
    }

    actual fun copyToByteArray(array: ByteArray, offset: Int, size: Int): ByteArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .get(array)
        return array
    }

    actual fun copyToCharArray(array: CharArray, offset: Int, size: Int): CharArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asCharBuffer()
            .get(array)
        return array
    }

    actual fun copyToShortArray(array: ShortArray, offset: Int, size: Int): ShortArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asShortBuffer()
            .get(array)
        return array
    }

    actual fun copyToIntArray(array: IntArray, offset: Int, size: Int): IntArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asIntBuffer()
            .get(array)
        return array
    }

    actual fun copyToFloatArray(array: FloatArray, offset: Int, size: Int): FloatArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asFloatBuffer()
            .get(array)
        return array
    }

    actual fun copyToLongArray(array: LongArray, offset: Int, size: Int): LongArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asLongBuffer()
            .get(array)
        return array
    }

    actual fun copyToDoubleArray(array: DoubleArray, offset: Int, size: Int): DoubleArray {
        buffer.duplicate()
            .position(offset)
            .limit(offset + size)
            .slice()
            .order(endian.toByteOrder())
            .asDoubleBuffer()
            .get(array)
        return array
    }

    private fun Endian.toByteOrder() = when (this) {
        Endian.LITTLE -> ByteOrder.LITTLE_ENDIAN
        Endian.BIG -> ByteOrder.BIG_ENDIAN
    }

    private fun ByteOrder.toEndian() = when (this) {
        ByteOrder.LITTLE_ENDIAN -> Endian.LITTLE
        ByteOrder.BIG_ENDIAN -> Endian.BIG
        else -> error("Unsupported ByteOrder=$this")
    }

}