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

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class NativeBufferVariableArrayTest {
    @Test
    fun `byte array round trip`() {
        testByteArray(null)
        testByteArray(byteArrayOf())
        testByteArray(byteArrayOf(42))
        testByteArray(byteArrayOf(1, 2, 3, 4, 5))
    }

    @Test
    fun `boolean array round trip`() {
        testBooleanArray(null)
        testBooleanArray(booleanArrayOf())
        testBooleanArray(booleanArrayOf(true, false))
        testBooleanArray(booleanArrayOf(true, false, true, true, false, false, true))
    }

    @Test
    fun `short array round trip`() {
        testShortArray(null)
        testShortArray(shortArrayOf())
        testShortArray(shortArrayOf(42))
        testShortArray(shortArrayOf(1, -2, 3, Short.MAX_VALUE))
    }

    @Test
    fun `int array round trip`() {
        testIntArray(null)
        testIntArray(intArrayOf())
        testIntArray(intArrayOf(42))
        testIntArray(intArrayOf(1, -2, 3, Int.MAX_VALUE))
    }

    @Test
    fun `long array round trip`() {
        testLongArray(null)
        testLongArray(longArrayOf())
        testLongArray(longArrayOf(42))
        testLongArray(longArrayOf(1, -2, 3, Long.MAX_VALUE))
    }

    @Test
    fun `float array round trip`() {
        testFloatArray(null)
        testFloatArray(floatArrayOf())
        testFloatArray(floatArrayOf(42f))
        testFloatArray(floatArrayOf(1f, -2.5f, Float.NaN, Float.POSITIVE_INFINITY))
    }

    @Test
    fun `double array round trip`() {
        testDoubleArray(null)
        testDoubleArray(doubleArrayOf())
        testDoubleArray(doubleArrayOf(42.0))
        testDoubleArray(doubleArrayOf(1.0, -2.5, Double.NaN, Double.NEGATIVE_INFINITY))
    }

    private fun testByteArray(values: ByteArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: ByteArray(0)

            buffer.pushByteArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertContentEquals(
                expected,
                buffer.nextByteArray(),
            )

            assertEquals(buffer.limit.coerceAtLeast(0), buffer.limit) // keep buffer referenced
            buffer.release()
        }

    private fun testBooleanArray(values: BooleanArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: BooleanArray(0)

            buffer.pushBooleanArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertContentEquals(
                expected,
                buffer.nextBooleanArray(),
            )

            buffer.release()
        }

    private fun testShortArray(values: ShortArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: ShortArray(0)

            buffer.pushShortArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertContentEquals(
                expected,
                buffer.nextShortArray(),
            )

            buffer.release()
        }

    private fun testIntArray(values: IntArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: IntArray(0)

            buffer.pushIntArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertContentEquals(
                expected,
                buffer.nextIntArray(),
            )

            buffer.release()
        }

    private fun testLongArray(values: LongArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: LongArray(0)

            buffer.pushLongArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertContentEquals(
                expected,
                buffer.nextLongArray(),
            )

            buffer.release()
        }

    private fun testFloatArray(values: FloatArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: FloatArray(0)

            buffer.pushFloatArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertFloatArrayEquals(
                expected,
                buffer.nextFloatArray(),
            )

            buffer.release()
        }

    private fun testDoubleArray(values: DoubleArray?) =
        forEachConfiguration { layout, _, buffer ->

            val expected = values ?: DoubleArray(0)

            buffer.pushDoubleArray(values)

            assertEquals(
                Int.sizeBytes(layout) + expected.sizeBytes(layout),
                buffer.position,
            )

            buffer.flip()

            assertDoubleArrayEquals(
                expected,
                buffer.nextDoubleArray(),
            )

            buffer.release()
        }
}
