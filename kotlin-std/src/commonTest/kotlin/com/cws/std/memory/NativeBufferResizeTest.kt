package com.cws.std.memory

import kotlin.math.min
import kotlin.test.Test
import kotlin.test.assertEquals

class NativeBufferResizeTest {

    @Test
    fun resize_grow_preserves_data() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushBoolean(true)
            buffer.pushInt(42)
            buffer.pushFloat(3.14f)
            buffer.pushLong(123456789L)

            val written = buffer.position
            val oldCapacity = buffer.limit

            buffer.resize(oldCapacity * 2)

            assertEquals(oldCapacity * 2, buffer.limit)
            assertEquals(written, buffer.position)

            buffer.flip()

            assertEquals(true, buffer.nextBoolean())
            assertEquals(42, buffer.nextInt())
            assertEquals(3.14f.toBits(), buffer.nextFloat().toBits())
            assertEquals(123456789L, buffer.nextLong())

            buffer.release()
        }
    }

    @Test
    fun resize_shrink_preserves_prefix() {
        forEachConfiguration { _, _, buffer ->

            repeat(min(buffer.limit, 64)) { i ->
                buffer.setByte(i, i.toByte())
            }

            buffer.resize(64)

            repeat(buffer.limit) { i ->
                assertEquals(i.toByte(), buffer.getByte(i))
            }

            buffer.release()
        }
    }

    @Test
    fun resize_same_capacity_preserves_data() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushInt(42)
            buffer.pushFloat(1.5f)

            val written = buffer.position
            val capacity = buffer.limit

            buffer.resize(capacity)

            assertEquals(capacity, buffer.limit)
            assertEquals(written, buffer.position)

            buffer.flip()

            assertEquals(42, buffer.nextInt())
            assertEquals(1.5f.toBits(), buffer.nextFloat().toBits())

            buffer.release()
        }
    }

    @Test
    fun resize_preserves_written_bytes() {
        forEachConfiguration { _, _, buffer ->

            repeat(128) {
                buffer.setByte(it, it.toByte())
            }

            buffer.position = 128

            buffer.resize(buffer.limit * 2)

            repeat(128) {
                assertEquals(it.toByte(), buffer.getByte(it))
            }

            assertEquals(128, buffer.position)

            buffer.release()
        }
    }

    @Test
    fun resize_grow_zero_initializes_new_region() {
        forEachConfiguration { _, _, buffer ->

            val oldCapacity = buffer.limit

            repeat(oldCapacity) {
                buffer.setByte(it, 1)
            }

            buffer.resize(oldCapacity * 2)

            for (i in oldCapacity until buffer.limit) {
                assertEquals(0, buffer.getByte(i))
            }

            buffer.release()
        }
    }

    @Test
    fun multiple_resizes_preserve_data() {
        forEachConfiguration { _, _, buffer ->

            buffer.pushLong(123456789L)

            repeat(5) {
                buffer.resize(buffer.limit * 2)
            }

            buffer.flip()

            assertEquals(123456789L, buffer.nextLong())

            buffer.release()
        }
    }
}