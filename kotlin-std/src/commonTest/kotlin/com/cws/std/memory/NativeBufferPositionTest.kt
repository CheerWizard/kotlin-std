package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class NativeBufferPositionTest {

    @Test
    fun `clear resets position`() {
        for (layout in MemoryLayout.entries) {
            val buffer = NativeBuffer(128, memoryLayout = layout)

            buffer.pushInt(123)
            buffer.pushLong(456)

            buffer.clear()

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun `flip resets position`() {
        for (layout in MemoryLayout.entries) {
            val buffer = NativeBuffer(128, memoryLayout = layout)

            buffer.pushInt(123)
            buffer.pushLong(456)

            val expected =
                Int.sizeBytes(layout) +
                        Long.sizeBytes(layout)

            assertEquals(expected, buffer.position)

            buffer.flip()

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun `push methods advance position correctly`() {
        for (layout in MemoryLayout.entries) {

            assertPush(layout, Byte.sizeBytes(layout)) {
                pushByte(1)
            }

            assertPush(layout, Boolean.sizeBytes(layout)) {
                pushBoolean(true)
            }

            assertPush(layout, Short.sizeBytes(layout)) {
                pushShort(1)
            }

            assertPush(layout, UShort.sizeBytes(layout)) {
                pushUShort(1u)
            }

            assertPush(layout, Char.sizeBytes(layout)) {
                pushChar('A')
            }

            assertPush(layout, Int.sizeBytes(layout)) {
                pushInt(123)
            }

            assertPush(layout, UInt.sizeBytes(layout)) {
                pushUInt(123u)
            }

            assertPush(layout, Float.sizeBytes(layout)) {
                pushFloat(1.5f)
            }

            assertPush(layout, Long.sizeBytes(layout)) {
                pushLong(123L)
            }

            assertPush(layout, ULong.sizeBytes(layout)) {
                pushULong(123u)
            }

            assertPush(layout, Double.sizeBytes(layout)) {
                pushDouble(123.0)
            }
        }
    }

    @Test
    fun `next methods advance position correctly`() {
        for (layout in MemoryLayout.entries) {

            assertNext(layout, Byte.sizeBytes(layout), {
                pushByte(1)
            }) {
                nextByte()
            }

            assertNext(layout, Boolean.sizeBytes(layout), {
                pushBoolean(true)
            }) {
                nextBoolean()
            }

            assertNext(layout, Short.sizeBytes(layout), {
                pushShort(1)
            }) {
                nextShort()
            }

            assertNext(layout, UShort.sizeBytes(layout), {
                pushUShort(1u)
            }) {
                nextUShort()
            }

            assertNext(layout, Char.sizeBytes(layout), {
                pushChar('A')
            }) {
                nextChar()
            }

            assertNext(layout, Int.sizeBytes(layout), {
                pushInt(123)
            }) {
                nextInt()
            }

            assertNext(layout, UInt.sizeBytes(layout), {
                pushUInt(123u)
            }) {
                nextUInt()
            }

            assertNext(layout, Float.sizeBytes(layout), {
                pushFloat(1.5f)
            }) {
                nextFloat()
            }

            assertNext(layout, Long.sizeBytes(layout), {
                pushLong(123L)
            }) {
                nextLong()
            }

            assertNext(layout, ULong.sizeBytes(layout), {
                pushULong(123u)
            }) {
                nextULong()
            }

            assertNext(layout, Double.sizeBytes(layout), {
                pushDouble(123.0)
            }) {
                nextDouble()
            }
        }
    }

    @Test
    fun `multiple pushes accumulate position`() {
        for (layout in MemoryLayout.entries) {

            val buffer = NativeBuffer(
                capacity = 256,
                memoryLayout = layout
            )

            buffer.pushByte(1)
            buffer.pushShort(2)
            buffer.pushInt(3)
            buffer.pushLong(4)
            buffer.pushFloat(5f)
            buffer.pushDouble(6.0)

            val expected =
                Byte.sizeBytes(layout) +
                        Short.sizeBytes(layout) +
                        Int.sizeBytes(layout) +
                        Long.sizeBytes(layout) +
                        Float.sizeBytes(layout) +
                        Double.sizeBytes(layout)

            assertEquals(expected, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun `multiple nexts accumulate position`() {
        for (layout in MemoryLayout.entries) {

            val buffer = NativeBuffer(
                capacity = 256,
                memoryLayout = layout
            )

            buffer.pushByte(1)
            buffer.pushShort(2)
            buffer.pushInt(3)
            buffer.pushLong(4)

            buffer.flip()

            buffer.nextByte()
            buffer.nextShort()
            buffer.nextInt()
            buffer.nextLong()

            val expected =
                Byte.sizeBytes(layout) +
                        Short.sizeBytes(layout) +
                        Int.sizeBytes(layout) +
                        Long.sizeBytes(layout)

            assertEquals(expected, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun `manual position assignment`() {
        for (layout in MemoryLayout.entries) {

            val buffer = NativeBuffer(
                capacity = 64,
                memoryLayout = layout
            )

            buffer.position = 17

            assertEquals(17, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun `flip is idempotent`() {
        for (layout in MemoryLayout.entries) {

            val buffer = NativeBuffer(
                capacity = 64,
                memoryLayout = layout
            )

            buffer.pushInt(1)

            buffer.flip()
            buffer.flip()

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    private inline fun assertPush(
        layout: MemoryLayout,
        expected: Int,
        crossinline push: NativeBuffer.() -> Unit
    ) {
        val buffer = NativeBuffer(
            capacity = 64,
            memoryLayout = layout
        )

        buffer.push()

        assertEquals(expected, buffer.position)

        buffer.release()
    }

    private inline fun <T> assertNext(
        layout: MemoryLayout,
        expected: Int,
        crossinline push: NativeBuffer.() -> Unit,
        crossinline next: NativeBuffer.() -> T
    ) {
        val buffer = NativeBuffer(
            capacity = 64,
            memoryLayout = layout
        )

        buffer.push()
        buffer.flip()

        buffer.next()

        assertEquals(expected, buffer.position)

        buffer.release()
    }
}