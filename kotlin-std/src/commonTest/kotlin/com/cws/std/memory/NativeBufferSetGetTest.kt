package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertEquals

class NativeBufferSetGetTest {

    @Test
    fun byte_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setByte(0, 123)

            assertEquals(123, buffer.getByte(0))

            buffer.release()
        }
    }

    @Test
    fun boolean_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setBoolean(0, true)

            assertEquals(true, buffer.getBoolean(0))

            buffer.release()
        }
    }

    @Test
    fun short_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setShort(0, 12345)

            assertEquals(12345, buffer.getShort(0))

            buffer.release()
        }
    }

    @Test
    fun ushort_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setUShort(0, 12345u)

            assertEquals(12345u, buffer.getUShort(0))

            buffer.release()
        }
    }

    @Test
    fun char_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setChar(0, 'A')

            assertEquals('A', buffer.getChar(0))

            buffer.release()
        }
    }

    @Test
    fun int_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setInt(0, 123456789)

            assertEquals(123456789, buffer.getInt(0))

            buffer.release()
        }
    }

    @Test
    fun uint_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setUInt(0, 123456789u)

            assertEquals(123456789u, buffer.getUInt(0))

            buffer.release()
        }
    }

    @Test
    fun float_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setFloat(0, 3.14f)

            assertEquals(3.14f.toBits(), buffer.getFloat(0).toBits())

            buffer.release()
        }
    }

    @Test
    fun long_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setLong(0, 123456789L)

            assertEquals(123456789L, buffer.getLong(0))

            buffer.release()
        }
    }

    @Test
    fun ulong_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setULong(0, 123456789uL)

            assertEquals(123456789uL, buffer.getULong(0))

            buffer.release()
        }
    }

    @Test
    fun double_round_trip() {
        forEachConfiguration { _, _, buffer ->

            buffer.setDouble(0, 123.456)

            assertEquals(123.456, buffer.getDouble(0))

            buffer.release()
        }
    }

    @Test
    fun set_get_does_not_modify_position() {
        forEachConfiguration { _, _, buffer ->

            buffer.setInt(0, 42)
            buffer.setFloat(32, 3.14f)

            assertEquals(0, buffer.position)

            assertEquals(42, buffer.getInt(0))
            assertEquals(3.14f.toBits(), buffer.getFloat(32).toBits())

            assertEquals(0, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun overwrite_value() {
        forEachConfiguration { _, _, buffer ->

            buffer.setInt(0, 1)
            buffer.setInt(0, 2)

            assertEquals(2, buffer.getInt(0))

            buffer.release()
        }
    }

    @Test
    fun multiple_values_different_offsets() {
        forEachConfiguration { layout, _, buffer ->

            val intOffset = 0
            val floatOffset = intOffset + Int.sizeBytes(layout)
            val longOffset = floatOffset + Float.sizeBytes(layout)

            buffer.setInt(intOffset, 42)
            buffer.setFloat(floatOffset, 1.5f)
            buffer.setLong(longOffset, 123456789L)

            assertEquals(42, buffer.getInt(intOffset))
            assertEquals(1.5f, buffer.getFloat(floatOffset))
            assertEquals(123456789L, buffer.getLong(longOffset))

            buffer.release()
        }
    }
}