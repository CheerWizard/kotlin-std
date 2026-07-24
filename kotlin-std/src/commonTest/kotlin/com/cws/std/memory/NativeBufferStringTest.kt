package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class NativeBufferStringTest {

    @Test
    fun utf8_round_trip() {
        testUtf8("")
        testUtf8("Hello")
        testUtf8("Привіт")
        testUtf8("こんにちは")
        testUtf8("你好")
        testUtf8("😀")
        testUtf8("😀🚀🔥🎉")
        testUtf8("The quick brown fox jumps over the lazy dog")
        testUtf8("A".repeat(4096))
    }

    @Test
    fun utf16_round_trip() {
        testUtf16("")
        testUtf16("Hello")
        testUtf16("Привіт")
        testUtf16("こんにちは")
        testUtf16("你好")
        testUtf16("😀")
        testUtf16("😀🚀🔥🎉")
        testUtf16("The quick brown fox jumps over the lazy dog")
        testUtf16("A".repeat(4096))
    }

    @Test
    fun utf8_null_round_trip() {
        forEachEndian { buffer ->

            buffer.pushStringUtf8(null)

            buffer.flip()

            assertEquals("", buffer.nextStringUtf8())

            buffer.release()
        }
    }

    @Test
    fun utf16_null_round_trip() {
        forEachEndian { buffer ->

            buffer.pushStringUtf16(null)

            buffer.flip()

            assertEquals("", buffer.nextStringUtf16())

            buffer.release()
        }
    }

    @Test
    fun utf8_sequential_round_trip() {
        forEachEndian { buffer ->

            val values = listOf(
                "",
                "Hello",
                "Привіт",
                "😀",
                "こんにちは",
                "The quick brown fox jumps over the lazy dog"
            )

            values.forEach(buffer::pushStringUtf8)

            buffer.flip()

            values.forEach {
                assertEquals(it, buffer.nextStringUtf8())
            }

            buffer.release()
        }
    }

    @Test
    fun utf16_sequential_round_trip() {
        forEachEndian { buffer ->

            val values = listOf(
                "",
                "Hello",
                "Привіт",
                "😀",
                "こんにちは",
                "The quick brown fox jumps over the lazy dog"
            )

            values.forEach(buffer::pushStringUtf16)

            buffer.flip()

            values.forEach {
                assertEquals(it, buffer.nextStringUtf16())
            }

            buffer.release()
        }
    }

    @Test
    fun mixed_utf8_utf16_round_trip() {
        forEachEndian { buffer ->

            buffer.pushStringUtf8("Hello")
            buffer.pushStringUtf16("Привіт")
            buffer.pushStringUtf8("😀")
            buffer.pushStringUtf16("こんにちは")

            buffer.flip()

            assertEquals("Hello", buffer.nextStringUtf8())
            assertEquals("Привіт", buffer.nextStringUtf16())
            assertEquals("😀", buffer.nextStringUtf8())
            assertEquals("こんにちは", buffer.nextStringUtf16())

            buffer.release()
        }
    }

    private fun testUtf8(value: String) =
        forEachEndian { buffer ->

            buffer.pushStringUtf8(value)

            buffer.flip()

            assertEquals(value, buffer.nextStringUtf8())

            buffer.release()
        }

    private fun testUtf16(value: String) =
        forEachEndian { buffer ->

            buffer.pushStringUtf16(value)

            buffer.flip()

            assertEquals(value, buffer.nextStringUtf16())

            buffer.release()
        }
}