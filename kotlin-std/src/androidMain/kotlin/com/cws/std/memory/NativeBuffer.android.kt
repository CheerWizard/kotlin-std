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

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

actual class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
    memoryBoundary: MemoryBoundary,
) {
    actual var memoryBoundary: MemoryBoundary = memoryBoundary
        private set
    actual val memoryLayout: MemoryLayout = memoryLayout
    actual val endian: Endian = endian

    actual constructor(
        address: Long,
        capacity: Int,
        memoryLayout: MemoryLayout,
        endian: Endian,
    ) : this(capacity, memoryLayout, endian, MemoryBoundary.EXTERNAL) {
        this.buffer = CMemory.toByteBuffer(address, capacity)?.order(endian.toByteOrder())
            ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes from address $address")
    }

    actual constructor(
        buffer: ByteArray,
        memoryLayout: MemoryLayout,
        endian: Endian,
    ) : this(buffer.size, memoryLayout, endian, MemoryBoundary.KOTLIN_HEAP) {
        this.buffer = ByteBuffer.wrap(buffer).order(endian.toByteOrder())
    }

    var buffer: ByteBuffer =
        if (isHeapBoundary(capacity)) {
            ByteBuffer.allocate(capacity).order(endian.toByteOrder())
        } else {
            CMemory.malloc(capacity)?.order(endian.toByteOrder())
                ?: throw RuntimeException("Failed to allocate for NativeBuffer $capacity bytes")
        }

    actual val address: Long = if (isHeapBoundary(capacity)) 0L else CMemory.addressOf(buffer)

    actual var position: Int
        internal set(value) {
            buffer.position(value)
        }
        get() = buffer.position()

    actual var limit: Int get() = buffer.limit()
        private set(value) {
            buffer.limit(value)
        }

    val size: Int get() = buffer.remaining()

    actual fun release() {
        if (!isHeapBoundary()) {
            CMemory.free(buffer)
        }
    }

    actual fun view(): Any? = buffer

    actual fun resize(newCapacity: Int) {
        val capacity = buffer.capacity()
        val oldPosition = position
        when {
            newCapacity <= limit || newCapacity <= capacity -> {
                buffer.limit(newCapacity)
            }

            isHeapBoundary() && isHeapBoundary(newCapacity) -> {
                buffer =
                    ByteBuffer
                        .wrap(buffer.array().copyOf(newCapacity))
                        .order(endian.toByteOrder())
                memoryBoundary = MemoryBoundary.KOTLIN_HEAP
            }

            !isHeapBoundary() && !isHeapBoundary(newCapacity) -> {
                buffer = CMemory
                    .realloc(buffer, newCapacity)
                    ?.order(endian.toByteOrder())
                    ?: throw RuntimeException("CMemory.realloc failed to resize NativeBuffer to new capacity $newCapacity")
                memoryBoundary = MemoryBoundary.EXTERNAL
            }

            isHeapBoundary() && !isHeapBoundary(newCapacity) -> {
                val newBuffer =
                    CMemory
                        .malloc(newCapacity)
                        ?.order(endian.toByteOrder())
                        ?: throw RuntimeException("CMemory.malloc failed to malloc new NativeBuffer during resize to $newCapacity")
                newBuffer.put(buffer.array())
                buffer = newBuffer
                memoryBoundary = MemoryBoundary.EXTERNAL
            }

            !isHeapBoundary() && isHeapBoundary(newCapacity) -> {
                val newBuffer =
                    ByteBuffer
                        .allocate(newCapacity)
                        .order(endian.toByteOrder())
                newBuffer.put(buffer.duplicate())
                CMemory.free(buffer)
                buffer = newBuffer
                memoryBoundary = MemoryBoundary.KOTLIN_HEAP
            }
        }
        position = minOf(oldPosition, newCapacity)
    }

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        sizeBytes: Int,
    ) {
        val src = buffer.duplicate()
        src.position(srcIndex)
        src.limit(srcIndex + sizeBytes)

        val dst = dest.buffer.duplicate()
        dst.position(destIndex)

        dst.put(src)
    }

    actual fun setByteArray(
        index: Int,
        array: ByteArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setCharArray(
        index: Int,
        array: CharArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asCharBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setShortArray(
        index: Int,
        array: ShortArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asShortBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setIntArray(
        index: Int,
        array: IntArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asIntBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setFloatArray(
        index: Int,
        array: FloatArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asFloatBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setLongArray(
        index: Int,
        array: LongArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asLongBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setDoubleArray(
        index: Int,
        array: DoubleArray,
    ) {
        val oldPosition = buffer.position()
        try {
            buffer.position(index)
            buffer.asDoubleBuffer().put(array)
        } finally {
            buffer.position(oldPosition)
        }
    }

    actual fun setTo(
        value: Byte,
        destIndex: Int,
        sizeBytes: Int,
    ) {
        repeat(sizeBytes) { i -> buffer.put(destIndex + i, value) }
    }

    actual fun setByte(
        index: Int,
        value: Byte,
    ) {
        buffer.put(index, value)
    }

    actual fun getByte(index: Int): Byte = buffer.get(index)

    fun copy(shortBuffer: ShortBuffer) {
        buffer.asShortBuffer().put(shortBuffer)
    }

    actual fun copyToByteArray(
        array: ByteArray,
        offset: Int,
        sizeBytes: Int,
    ): ByteArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Byte.sizeBytes(MemoryLayout.KOTLIN))
        dup.get(array)
        return array
    }

    actual fun copyToCharArray(
        array: CharArray,
        offset: Int,
        sizeBytes: Int,
    ): CharArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Char.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asCharBuffer().get(array)
        return array
    }

    actual fun copyToShortArray(
        array: ShortArray,
        offset: Int,
        sizeBytes: Int,
    ): ShortArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Short.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asShortBuffer().get(array)
        return array
    }

    actual fun copyToIntArray(
        array: IntArray,
        offset: Int,
        sizeBytes: Int,
    ): IntArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Int.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asIntBuffer().get(array)
        return array
    }

    actual fun copyToFloatArray(
        array: FloatArray,
        offset: Int,
        sizeBytes: Int,
    ): FloatArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Float.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asFloatBuffer().get(array)
        return array
    }

    actual fun copyToLongArray(
        array: LongArray,
        offset: Int,
        sizeBytes: Int,
    ): LongArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Long.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asLongBuffer().get(array)
        return array
    }

    actual fun copyToDoubleArray(
        array: DoubleArray,
        offset: Int,
        sizeBytes: Int,
    ): DoubleArray {
        val dup = buffer.duplicate()
        dup.position(offset)
        dup.limit(offset + sizeBytes * Double.sizeBytes(MemoryLayout.KOTLIN))
        val sliced = dup.slice()
        sliced.order(endian.toByteOrder())
        sliced.asDoubleBuffer().get(array)
        return array
    }

    private fun Endian.toByteOrder() =
        when (this) {
            Endian.LITTLE -> ByteOrder.LITTLE_ENDIAN
            Endian.BIG -> ByteOrder.BIG_ENDIAN
        }

    private fun ByteOrder.toEndian() =
        when (this) {
            ByteOrder.LITTLE_ENDIAN -> Endian.LITTLE
            ByteOrder.BIG_ENDIAN -> Endian.BIG
            else -> error("Unsupported ByteOrder=$this")
        }
}
