package com.cws.std.memory

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class NativeBufferStressTest {

    @Test
    fun random_primitive_round_trip() {
        repeat(100) {

            forEachConfiguration { _, _, buffer ->

                val random = Random(it)

                val bytes = ByteArray(256) { random.nextInt().toByte() }
                val ints = IntArray(128) { random.nextInt() }
                val longs = LongArray(64) { random.nextLong() }
                val floats = FloatArray(64) { random.nextFloat() }
                val doubles = DoubleArray(32) { random.nextDouble() }

                buffer.pushByteArray(bytes)
                buffer.pushIntArray(ints)
                buffer.pushLongArray(longs)
                buffer.pushFloatArray(floats)
                buffer.pushDoubleArray(doubles)

                buffer.flip()

                assertContentEquals(bytes, buffer.nextByteArray())
                assertContentEquals(ints, buffer.nextIntArray())
                assertContentEquals(longs, buffer.nextLongArray())
                assertContentEquals(floats, buffer.nextFloatArray())
                assertContentEquals(doubles, buffer.nextDoubleArray())

                buffer.release()
            }
        }
    }

    @Test
    fun many_clear_reuse_cycles() {
        forEachConfiguration { _, _, buffer ->

            repeat(1000) { i ->

                buffer.clear()

                buffer.pushInt(i)
                buffer.pushLong(i.toLong())
                buffer.pushFloat(i.toFloat())
                buffer.pushDouble(i.toDouble())

                buffer.flip()

                assertEquals(i, buffer.nextInt())
                assertEquals(i.toLong(), buffer.nextLong())
                assertEquals(i.toFloat(), buffer.nextFloat())
                assertEquals(i.toDouble(), buffer.nextDouble())
            }

            buffer.release()
        }
    }

    @Test
    fun many_clone_cycles() {
        forEachConfiguration { _, _, buffer ->

            repeat(100) { i ->

                buffer.clear()

                buffer.pushInt(i)
                buffer.pushLong(i.toLong())

                val clone = buffer.clone()

                buffer.copyTo(
                    dest = clone,
                    srcIndex = 0,
                    destIndex = 0,
                    sizeBytes = buffer.position
                )

                clone.flip()

                assertEquals(i, clone.nextInt())
                assertEquals(i.toLong(), clone.nextLong())

                clone.release()
            }

            buffer.release()
        }
    }

    @Test
    fun many_resize_cycles() {
        forEachConfiguration { _, _, buffer ->

            repeat(100) { i ->

                val newCapacity = 128 + i * 32

                buffer.resize(newCapacity)

                assertEquals(newCapacity, buffer.limit)

                buffer.clear()

                buffer.pushInt(i)
                buffer.flip()

                assertEquals(i, buffer.nextInt())
            }

            buffer.release()
        }
    }

    @Test
    fun mixed_operations_do_not_corrupt_position() {
        forEachConfiguration { _, _, buffer ->

            repeat(500) {

                buffer.clear()

                buffer.pushInt(1)
                buffer.pushFloat(2f)
                buffer.pushLong(3)

                val written = buffer.position

                buffer.flip()

                buffer.nextInt()
                buffer.nextFloat()
                buffer.nextLong()

                assertEquals(written, buffer.position)
            }

            buffer.release()
        }
    }
}