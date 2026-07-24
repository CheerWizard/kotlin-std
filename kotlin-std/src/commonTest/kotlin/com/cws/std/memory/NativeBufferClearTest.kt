package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class NativeBufferClearTest {

    @Test
    fun clear_resets_position() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushBoolean(true)
            buffer.pushInt(42)
            buffer.pushFloat(3.14f)
            buffer.pushLong(123456789L)

            buffer.clear()

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun clear_zeros_entire_buffer() {
        forEachConfiguration { _, _, buffer ->

            repeat(buffer.limit) {
                buffer.setByte(it, 0x7F)
            }

            buffer.clear()

            repeat(buffer.limit) {
                assertEquals(0, buffer.getByte(it))
            }

            buffer.release()
        }
    }

    @Test
    fun clear_after_primitive_writes() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushByte(1)
            buffer.pushShort(2)
            buffer.pushInt(3)
            buffer.pushLong(4)
            buffer.pushFloat(5f)
            buffer.pushDouble(6.0)

            buffer.clear()

            repeat(buffer.limit) {
                assertEquals(0, buffer.getByte(it))
            }

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun clear_after_array_writes() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushIntArray(intArrayOf(1, 2, 3, 4))
            buffer.pushFloatArray(floatArrayOf(1f, 2f, 3f))

            buffer.clear()

            repeat(buffer.limit) {
                assertEquals(0, buffer.getByte(it))
            }

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun clear_after_string_writes() {
        val strings = listOf(
            "",
            "Hello",
            "Привіт",
            "こんにちは",
            "😀 Kotlin"
        )

        for (value in strings) {
            val buffer = NativeBuffer(256)

            buffer.pushStringUtf8(value)

            buffer.clear()

            repeat(buffer.limit) {
                assertEquals(0, buffer.getByte(it))
            }

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun clear_allows_reuse() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushInt(123)
            buffer.pushLong(456L)

            buffer.clear()

            buffer.pushFloat(3.14f)
            buffer.pushBoolean(true)

            buffer.flip()

            assertEquals(3.14f.toBits(), buffer.nextFloat().toBits())
            assertEquals(true, buffer.nextBoolean())

            buffer.release()
        }
    }

    @Test
    fun clear_is_idempotent() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushInt(42)

            buffer.clear()
            buffer.clear()

            assertEquals(0, buffer.position)

            repeat(buffer.limit) {
                assertEquals(0, buffer.getByte(it))
            }

            buffer.release()
        }
    }
}