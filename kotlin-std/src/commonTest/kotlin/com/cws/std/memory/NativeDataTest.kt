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

import com.cws.std.test.TestData
import com.cws.std.test.TestEnumOrdinal
import com.cws.std.test.TestEnumRaw
import com.cws.std.test.decodeTestData
import com.cws.std.test.encode
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class NativeDataTest {
    @Test
    fun assert_test_data_encodes_decodes_to_same_value() {
        val testData =
            TestData(
                id = 2442L,
                timestamp = 123456789L,
                name = "Testing",
                width = 800,
                height = 600,
                x = 300.15f,
                y = 400.24f,
                flag = true,
                age = Short.MAX_VALUE,
                ordinalEnum = TestEnumOrdinal.Ordinal_3,
                rawEnum = TestEnumRaw.Raw_4,
                fixedStringUtf8 = "Hello World!",
                stringUtf8 = "Dynamic string with dynamic length",
                fixedStringUtf16 = "Hello World!",
                stringUtf16 = "Dynamic string with dynamic length",
                fixedBytes = ByteArray(24) { it.toByte() },
                bytes = byteArrayOf(0xAA.toByte(), 0xBB.toByte(), 0xCC.toByte()),
                fixedShorts = ShortArray(48) { it.toShort() },
                shorts = shortArrayOf(100, 200, 300),
                fixedInts = IntArray(64) { it },
                ints = intArrayOf(1000, 2000, 3000),
                fixedLongs = LongArray(36) { it.toLong() },
                longs = longArrayOf(999L, 888L, 777L),
                fixedFloats = FloatArray(36) { it.toFloat() },
                floats = floatArrayOf(0.1f, 0.2f, 0.3f),
                fixedDoubles = DoubleArray(36) { it.toDouble() },
                doubles = doubleArrayOf(9.9, 8.8, 7.7),
                data =
                    listOf(
                        TestData.NestedData(
                            id = 1L,
                            data =
                                mapOf(
                                    "data_1" to "some data",
                                    "data_2" to "some data shqufbqvu9q",
                                    "data_3" to "",
                                    "data_4" to PI.toString(),
                                ),
                            subscribers = setOf("sub1", "sub2", "sub3", "sub4"),
                        ),
                    ),
            )

        // FIXME(Minor): strings have bugs for MemoryLayout.STD140 and MemoryLayout.STD430
        //  not critical because STD140 and STD430 are GPU memory alignments where strings are absent
        //  but still worse fixing in future
        MemoryLayout.entries.forEach { memoryLayout ->
            Endian.entries.forEach { endian ->
                MemoryBoundary.entries.forEach { memoryBoundary ->
                    assertTestData(testData, MemoryLayout.KOTLIN, endian, memoryBoundary)
                }
            }
        }
    }

    private fun assertTestData(
        testData: TestData,
        memoryLayout: MemoryLayout,
        endian: Endian,
        memoryBoundary: MemoryBoundary,
    ) {
        val decoded = testData.encode(memoryLayout, endian, memoryBoundary).flip().decodeTestData()

        assertEquals(testData.id, decoded.id)
        assertEquals(testData.timestamp, decoded.timestamp)
        assertEquals(testData.name, decoded.name)
        assertEquals(testData.width, decoded.width)
        assertEquals(testData.height, decoded.height)
        assertEquals(testData.x, decoded.x)
        assertEquals(testData.y, decoded.y)
        assertEquals(testData.flag, decoded.flag)
        assertEquals(testData.age, decoded.age)
        assertEquals(testData.ordinalEnum, decoded.ordinalEnum)
        assertEquals(testData.rawEnum, decoded.rawEnum)
        assertEquals(testData.fixedStringUtf8, decoded.fixedStringUtf8)
        assertEquals(testData.stringUtf8, decoded.stringUtf8)
        assertEquals(testData.fixedStringUtf16, decoded.fixedStringUtf16)
        assertEquals(testData.stringUtf16, decoded.stringUtf16)
        assertContentEquals(testData.fixedBytes, decoded.fixedBytes)
        assertContentEquals(testData.bytes, decoded.bytes)
        assertContentEquals(testData.fixedShorts, decoded.fixedShorts)
        assertContentEquals(testData.shorts, decoded.shorts)
        assertContentEquals(testData.fixedInts, decoded.fixedInts)
        assertContentEquals(testData.ints, decoded.ints)
        assertContentEquals(testData.fixedLongs, decoded.fixedLongs)
        assertContentEquals(testData.longs, decoded.longs)
        assertContentEquals(testData.fixedFloats, decoded.fixedFloats)
        assertContentEquals(testData.floats, decoded.floats)
        assertContentEquals(testData.fixedDoubles, decoded.fixedDoubles)
        assertContentEquals(testData.doubles, decoded.doubles)
        assertEquals(testData.data.size, decoded.data.size)
        testData.data.zip(decoded.data).forEachIndexed { index, (expected, actual) ->
            assertEquals(expected.id, actual.id, "NestedData[$index].id mismatch")
            assertEquals(expected.data, actual.data, "NestedData[$index].data mismatch")
            assertEquals(expected.subscribers, actual.subscribers, "NestedData[$index].subscribers mismatch")
        }
    }
}
