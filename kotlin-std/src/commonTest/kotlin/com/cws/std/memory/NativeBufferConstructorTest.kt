package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NativeBufferConstructorTest {

    @Test
    fun `create buffer with every constructor configuration`() {
        for (layout in MemoryLayout.entries) {
            for (endian in Endian.entries) {
                for (boundary in MemoryBoundary.entries) {

                    val buffer = NativeBuffer(
                        capacity = 256,
                        memoryLayout = layout,
                        endian = endian,
                        memoryBoundary = boundary
                    )

                    assertEquals(256, buffer.limit)
                    assertEquals(layout, buffer.memoryLayout)
                    assertEquals(endian, buffer.endian)
                    assertEquals(boundary, buffer.memoryBoundary)
                    assertEquals(0, buffer.position)

                    buffer.release()
                }
            }
        }
    }

    @Test
    fun `create heap buffer from byte array`() {
        val bytes = byteArrayOf(
            1, 2, 3, 4,
            5, 6, 7, 8
        )

        for (layout in MemoryLayout.entries) {
            for (endian in Endian.entries) {

                val buffer = NativeBuffer(
                    buffer = bytes,
                    memoryLayout = layout,
                    endian = endian
                )

                assertEquals(bytes.size, buffer.limit)
                assertEquals(layout, buffer.memoryLayout)
                assertEquals(endian, buffer.endian)
                assertEquals(MemoryBoundary.KOTLIN_HEAP, buffer.memoryBoundary)
                assertEquals(0, buffer.position)

                val copied = buffer.copyToByteArray(
                    ByteArray(bytes.size),
                    0,
                    bytes.size
                )

                assertContentEquals(bytes, copied)

                buffer.release()
            }
        }
    }

    @Test
    fun `wrap external memory address`() {
        for (layout in MemoryLayout.entries) {
            for (endian in Endian.entries) {

                val buffer = NativeBuffer(
                    address = 123456789L,
                    capacity = 512,
                    memoryLayout = layout,
                    endian = endian
                )

//                assertEquals(123456789L, buffer.address)
                assertEquals(512, buffer.limit)
                assertEquals(layout, buffer.memoryLayout)
                assertEquals(endian, buffer.endian)
                assertEquals(MemoryBoundary.EXTERNAL, buffer.memoryBoundary)
                assertEquals(0, buffer.position)

                // Don't dereference an arbitrary address.
                // This constructor test only verifies the wrapper contract.
            }
        }
    }

    @Test
    fun `new buffer starts with position zero`() {
        val buffer = NativeBuffer(64)

        assertEquals(0, buffer.position)

        buffer.release()
    }

    @Test
    fun `view returns non null object`() {
        val buffer = NativeBuffer(32)

        assertTrue(buffer.view() != null)

        buffer.release()
    }

    @Test
    fun `capacity is preserved for different sizes`() {
        val capacities = listOf(
            8,
            16,
            32,
            64,
            128,
            256,
            512,
            1024,
            4096
        )

        for (capacity in capacities) {
            val buffer = NativeBuffer(capacity)

            assertEquals(capacity, buffer.limit)

            buffer.release()
        }
    }

    @Test
    fun `memory boundary helper returns expected value`() {
        val heap = NativeBuffer(
            capacity = 1024,
            memoryBoundary = MemoryBoundary.KOTLIN_HEAP
        )

        assertTrue(heap.isHeapBoundary())

        heap.release()

        val external = NativeBuffer(
            capacity = 1024,
            memoryBoundary = MemoryBoundary.EXTERNAL
        )

        assertTrue(!external.isHeapBoundary())

        external.release()
    }

    @Test
    fun `large kotlin heap buffer is not considered heap boundary`() {
        val buffer = NativeBuffer(
            capacity = KOTLIN_HEAP_MAX_CAPACITY,
            memoryBoundary = MemoryBoundary.KOTLIN_HEAP
        )

        assertTrue(!buffer.isHeapBoundary())

        buffer.release()
    }
}