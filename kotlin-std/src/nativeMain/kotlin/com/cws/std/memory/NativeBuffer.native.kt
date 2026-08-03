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

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.get
import kotlinx.cinterop.plus
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.set
import kotlinx.cinterop.toCPointer
import kotlinx.cinterop.usePinned
import platform.posix.free
import platform.posix.malloc
import platform.posix.memcpy
import platform.posix.memset
import platform.posix.realloc

@OptIn(ExperimentalForeignApi::class)
actual class NativeBuffer actual constructor(
    capacity: Int,
    memoryLayout: MemoryLayout,
    endian: Endian,
    memoryBoundary: MemoryBoundary,
) {

    actual constructor(address: Long, capacity: Int, memoryLayout: MemoryLayout, endian: Endian): this(0, memoryLayout, endian, MemoryBoundary.EXTERNAL) {
        this.buffer = address.toCPointer()
            ?: throw RuntimeException("Failed to convert Long ptr to Native CPointer!")
        this.heapBuffer = null
    }

    actual constructor(buffer: ByteArray, memoryLayout: MemoryLayout, endian: Endian)
            : this(0, memoryLayout, endian, MemoryBoundary.KOTLIN_HEAP) {
        this.buffer = null
        this.heapBuffer = buffer
    }

    constructor(
        ptr: CPointer<ByteVar>,
        capacity: Int,
        memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
        endian: Endian = Endian.LITTLE
    ): this(0, memoryLayout, endian, MemoryBoundary.EXTERNAL) {
        this.buffer = ptr
        this._capacity = capacity
        this.heapBuffer = null
    }

    actual var memoryBoundary: MemoryBoundary = memoryBoundary
        private set
    actual val memoryLayout: MemoryLayout = memoryLayout
    actual val endian: Endian = endian

    actual var position: Int = 0
        internal set

    private var _capacity = capacity

    actual var limit = _capacity

    var buffer: CPointer<ByteVar>? =
        when {
            capacity <= 0 || isHeapBoundary(capacity) -> null
            else -> malloc(capacity.toULong())?.reinterpret()
        }

    var heapBuffer: ByteArray? =
        when {
            capacity <= 0 || !isHeapBoundary(capacity) -> null
            else -> ByteArray(capacity)
        }

    actual val address: Long = buffer?.rawValue?.toLong() ?: 0L

    actual fun release() {
        if (isHeapBoundary()) {
            heapBuffer = null
        } else {
            free(buffer)
            buffer = null
        }
        _capacity = 0
    }

    actual fun view(): Any? = buffer ?: heapBuffer

    actual fun resize(newCapacity: Int) {
        val capacity = _capacity
        val oldPosition = position
        when {
            newCapacity <= limit || newCapacity <= capacity -> limit = newCapacity
            isHeapBoundary() && isHeapBoundary(newCapacity) -> {
                heapBuffer = heapBuffer?.copyOf(newCapacity)
                buffer = null
                memoryBoundary = MemoryBoundary.KOTLIN_HEAP
                _capacity = newCapacity
            }
            !isHeapBoundary() && !isHeapBoundary(newCapacity) -> {
                heapBuffer = null
                buffer = realloc(buffer, newCapacity.toULong())?.reinterpret()
                    ?: throw RuntimeException("realloc failed to resize NativeBuffer to new capacity $newCapacity")
                memoryBoundary = MemoryBoundary.EXTERNAL
                _capacity = newCapacity
            }
            isHeapBoundary() && !isHeapBoundary(newCapacity) -> {
                buffer = malloc(newCapacity.toULong())?.reinterpret()
                    ?: throw RuntimeException("malloc failed to allocate new NativeBuffer during resize to $newCapacity")
                heapBuffer?.usePinned { pinned ->
                    memcpy(buffer, pinned.addressOf(0), heapBuffer!!.size.toULong())
                }
                heapBuffer = null
                memoryBoundary = MemoryBoundary.EXTERNAL
                _capacity = newCapacity
            }
            !isHeapBoundary() && isHeapBoundary(newCapacity) -> {
                val newHeapBuffer = ByteArray(newCapacity)
                newHeapBuffer.usePinned { pinned ->
                    memcpy(pinned.addressOf(0), buffer, limit.toULong())
                }
                free(buffer)
                buffer = null
                heapBuffer = newHeapBuffer
                memoryBoundary = MemoryBoundary.KOTLIN_HEAP
                _capacity = newCapacity
            }
        }
        limit = newCapacity
        position = minOf(oldPosition, newCapacity)
    }

    actual fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        sizeBytes: Int,
    ) {
        when {
            isHeapBoundary() && dest.isHeapBoundary() -> {
                heapBuffer?.usePinned { srcPinned ->
                    dest.heapBuffer?.usePinned { destPinned ->
                        memcpy(
                            destPinned.addressOf(destIndex),
                            srcPinned.addressOf(srcIndex),
                            sizeBytes.toULong()
                        )
                    }
                }
            }
            isHeapBoundary() && !dest.isHeapBoundary() -> {
                heapBuffer?.usePinned { srcPinned ->
                    dest.buffer?.let { destBuffer ->
                        memcpy(
                            destBuffer + destIndex,
                            srcPinned.addressOf(srcIndex),
                            sizeBytes.toULong()
                        )
                    }
                }
            }
            !isHeapBoundary() && dest.isHeapBoundary() -> {
                buffer?.let { srcBuffer ->
                    dest.heapBuffer?.usePinned { destPinned ->
                        memcpy(
                            destPinned.addressOf(destIndex),
                            srcBuffer + srcIndex,
                            sizeBytes.toULong()
                        )
                    }
                }
            }
            !isHeapBoundary() && !dest.isHeapBoundary() -> {
                buffer?.let { srcBuffer ->
                    dest.buffer?.let { destBuffer ->
                        memcpy(
                            destBuffer + destIndex,
                            srcBuffer + srcIndex,
                            sizeBytes.toULong()
                        )
                    }
                }
            }
        }
    }

    actual fun setTo(value: Byte, destIndex: Int, sizeBytes: Int) {
        if (isHeapBoundary()) {
            heapBuffer?.let { heapBuffer ->
                for (i in destIndex..<destIndex + sizeBytes) {
                    heapBuffer[i] = value
                }
            }
        } else {
            memset(buffer + destIndex, value.toInt(), sizeBytes.toULong())
        }
    }

    actual fun setByte(index: Int, value: Byte) {
        if (isHeapBoundary()) {
            heapBuffer?.set(index, value)
        } else {
            buffer?.set(index, value)
        }
    }

    actual fun getByte(index: Int): Byte {
        return if (isHeapBoundary()) {
            heapBuffer?.get(index) ?: 0
        } else {
            buffer?.get(index) ?: 0
        }
    }

    actual fun setByteArray(index: Int, array: ByteArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 1).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 1).toULong())
            }
        }
    }

    actual fun setCharArray(index: Int, array: CharArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 2).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 2).toULong())
            }
        }
    }

    actual fun setShortArray(index: Int, array: ShortArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 2).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 2).toULong())
            }
        }
    }

    actual fun setIntArray(index: Int, array: IntArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 4).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 4).toULong())
            }
        }
    }

    actual fun setFloatArray(index: Int, array: FloatArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 4).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 4).toULong())
            }
        }
    }

    actual fun setLongArray(index: Int, array: LongArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 8).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 8).toULong())
            }
        }
    }

    actual fun setDoubleArray(index: Int, array: DoubleArray) {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { destPinned ->
                    memcpy(destPinned.addressOf(index), pinned.addressOf(0), (array.size * 8).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(buffer + index, pinned.addressOf(0), (array.size * 8).toULong())
            }
        }
    }

    actual fun copyToByteArray(array: ByteArray, offset: Int, sizeBytes: Int): ByteArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 1).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 1).toULong())
            }
        }
        return array
    }

    actual fun copyToCharArray(array: CharArray, offset: Int, sizeBytes: Int): CharArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 2).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 2).toULong())
            }
        }
        return array
    }

    actual fun copyToShortArray(array: ShortArray, offset: Int, sizeBytes: Int): ShortArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 2).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 2).toULong())
            }
        }
        return array
    }

    actual fun copyToIntArray(array: IntArray, offset: Int, sizeBytes: Int): IntArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 4).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 4).toULong())
            }
        }
        return array
    }

    actual fun copyToFloatArray(array: FloatArray, offset: Int, sizeBytes: Int): FloatArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 4).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 4).toULong())
            }
        }
        return array
    }

    actual fun copyToLongArray(array: LongArray, offset: Int, sizeBytes: Int): LongArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 8).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 8).toULong())
            }
        }
        return array
    }

    actual fun copyToDoubleArray(array: DoubleArray, offset: Int, sizeBytes: Int): DoubleArray {
        if (isHeapBoundary()) {
            array.usePinned { pinned ->
                heapBuffer?.usePinned { srcPinned ->
                    memcpy(pinned.addressOf(0), srcPinned.addressOf(offset), (sizeBytes * 8).toULong())
                }
            }
        } else {
            array.usePinned { pinned ->
                memcpy(pinned.addressOf(0), buffer + offset, (sizeBytes * 8).toULong())
            }
        }
        return array
    }

}