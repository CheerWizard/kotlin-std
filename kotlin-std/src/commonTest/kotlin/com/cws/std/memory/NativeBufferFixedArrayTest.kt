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

import kotlin.test.*

class NativeBufferFixedArrayTest {

    @Test
    fun `byte array exact size round trip`() {
        testByteArray(ByteArray(8) { it.toByte() }, 8)
    }

    @Test
    fun `byte array rejects null`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IllegalArgumentException> {
                buffer.pushFixedByteArray(null, 8)
            }

            buffer.release()
        }
    }

    @Test
    fun `byte array rejects smaller array`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IllegalArgumentException> {
                buffer.pushFixedByteArray(ByteArray(7), 8)
            }

            buffer.release()
        }
    }

    @Test
    fun `byte array rejects bigger array`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IllegalArgumentException> {
                buffer.pushFixedByteArray(ByteArray(9), 8)
            }

            buffer.release()
        }
    }

    @Test
    fun `boolean array exact size round trip`() {
        testBooleanArray(BooleanArray(8) { (it % 2) == 0 }, 8)
    }

    @Test
    fun `short array exact size round trip`() {
        testShortArray(ShortArray(8) { it.toShort() }, 8)
    }

    @Test
    fun `int array exact size round trip`() {
        testIntArray(IntArray(8) { it }, 8)
    }

    @Test
    fun `long array exact size round trip`() {
        testLongArray(LongArray(8) { it.toLong() }, 8)
    }

    @Test
    fun `float array exact size round trip`() {
        testFloatArray(FloatArray(8) { it.toFloat() }, 8)
    }

    @Test
    fun `double array exact size round trip`() {
        testDoubleArray(DoubleArray(8) { it.toDouble() }, 8)
    }

    private fun testByteArray(values: ByteArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->
            buffer.pushFixedByteArray(values, fixedSize)

            assertEquals(
                fixedSize * Byte.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertContentEquals(
                values,
                buffer.nextByteArray(fixedSize)
            )

            buffer.release()
        }

    private fun testBooleanArray(values: BooleanArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->
            buffer.pushFixedBooleanArray(values, fixedSize)

            assertEquals(
                fixedSize * Byte.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertContentEquals(
                values,
                buffer.nextBooleanArray(fixedSize)
            )

            buffer.release()
        }

    private fun testShortArray(values: ShortArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->

            buffer.pushFixedShortArray(values, fixedSize)

            assertEquals(
                fixedSize * Short.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertContentEquals(
                values,
                buffer.nextShortArray(fixedSize)
            )

            buffer.release()
        }

    private fun testIntArray(values: IntArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->

            buffer.pushFixedIntArray(values, fixedSize)

            assertEquals(
                fixedSize * Int.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertContentEquals(
                values,
                buffer.nextIntArray(fixedSize)
            )

            buffer.release()
        }

    private fun testLongArray(values: LongArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->

            buffer.pushFixedLongArray(values, fixedSize)

            assertEquals(
                fixedSize * Long.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertContentEquals(
                values,
                buffer.nextLongArray(fixedSize)
            )

            buffer.release()
        }

    private fun testFloatArray(values: FloatArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->

            buffer.pushFixedFloatArray(values, fixedSize)

            assertEquals(
                fixedSize * Float.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertFloatArrayEquals(
                values,
                buffer.nextFloatArray(fixedSize)
            )

            buffer.release()
        }

    private fun testDoubleArray(values: DoubleArray, fixedSize: Int) =
        forEachConfiguration { layout, _, buffer ->

            buffer.pushFixedDoubleArray(values, fixedSize)

            assertEquals(
                fixedSize * Double.sizeBytes(layout),
                buffer.position
            )

            buffer.flip()

            assertDoubleArrayEquals(
                values,
                buffer.nextDoubleArray(fixedSize)
            )

            buffer.release()
        }
}