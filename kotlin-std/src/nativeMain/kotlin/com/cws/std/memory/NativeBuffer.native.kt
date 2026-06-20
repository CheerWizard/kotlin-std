package com.cws.std.memory

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.ShortVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.get
import kotlinx.cinterop.plus
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import platform.posix.memset
import platform.posix.realloc

@OptIn(ExperimentalForeignApi::class)
actual open class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
) {

    actual constructor(address: Long, capacity: Int, memoryLayout: MemoryLayout, endian: Endian) : this(capacity) {
        this.buffer = address.toCPointer()
            ?: throw RuntimeException("Failed to convert Long ptr to Native CPointer!")
    }

    actual val memoryLayout: MemoryLayout = memoryLayout
    actual val endian: Endian = endian

    actual var position: Int = 0
    actual val capacity: Int get() = _capacity

    var buffer: CPointer<ByteVar> = malloc(capacity.toULong()) as CPointer<ByteVar>
    actual val address: Long = buffer.rawValue.toLong()

    private var _capacity = capacity

    actual fun release() {
        free(buffer)
        _capacity = 0
    }

    actual fun view(): Any = buffer

    actual fun resize(newCapacity: Int) {
        buffer = realloc(buffer, newCapacity.toULong()) as CPointer<ByteVar>
        _capacity = newCapacity
    }

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    ) {
        memcpy(
            dest.buffer + destIndex,
            buffer + srcIndex,
            size.toULong()
        )
    }

    actual fun clone(): NativeBuffer {
        return NativeBuffer(capacity)
    }

    actual fun setTo(value: Byte, destIndex: Int, size: Int) {
        memset(buffer + destIndex, value.toInt(), size.toULong())
    }

    actual fun setByte(index: Int, value: Byte) {
        buffer[index] = value
    }

    actual fun getByte(index: Int): Byte {
        return buffer[index]
    }

    actual fun setByteArray(index: Int, array: ByteArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 1).toULong())
        }
    }

    actual fun setCharArray(index: Int, array: CharArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 2).toULong())
        }
    }

    actual fun setShortArray(index: Int, array: ShortArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 2).toULong())
        }
    }

    actual fun setIntArray(index: Int, array: IntArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 4).toULong())
        }
    }

    actual fun setFloatArray(index: Int, array: FloatArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 4).toULong())
        }
    }

    actual fun setLongArray(index: Int, array: LongArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 8).toULong())
        }
    }

    actual fun setDoubleArray(index: Int, array: DoubleArray) {
        array.usePinned { pinned ->
            memcpy(buffer + index, pinned.addressOf(0), (array.size * 8).toULong())
        }
    }

    actual fun copyToByteArray(array: ByteArray, offset: Int, size: Int): ByteArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 1).toULong())
        }
        return array
    }

    actual fun copyToCharArray(array: CharArray, offset: Int, size: Int): CharArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 2).toULong())
        }
        return array
    }

    actual fun copyToShortArray(array: ShortArray, offset: Int, size: Int): ShortArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 2).toULong())
        }
        return array
    }

    actual fun copyToIntArray(array: IntArray, offset: Int, size: Int): IntArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 4).toULong())
        }
        return array
    }

    actual fun copyToFloatArray(array: FloatArray, offset: Int, size: Int): FloatArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 4).toULong())
        }
        return array
    }

    actual fun copyToLongArray(array: LongArray, offset: Int, size: Int): LongArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 8).toULong())
        }
        return array
    }

    actual fun copyToDoubleArray(array: DoubleArray, offset: Int, size: Int): DoubleArray {
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), buffer + offset, (size * 8).toULong())
        }
        return array
    }

}