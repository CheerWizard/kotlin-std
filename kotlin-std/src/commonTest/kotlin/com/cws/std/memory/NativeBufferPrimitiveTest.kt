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
import kotlin.test.assertEquals

class NativeBufferPrimitiveTest {
    @Test
    fun `byte round trip`() {
        testRoundTrip(
            value = 0x5A.toByte(),
            set = { setByte(0, it) },
            get = { getByte(0) },
            push = { pushByte(it) },
            next = { nextByte() },
        )
    }

    @Test
    fun `boolean round trip`() {
        testRoundTrip(
            value = true,
            set = { setBoolean(0, it) },
            get = { getBoolean(0) },
            push = { pushBoolean(it) },
            next = { nextBoolean() },
        )

        testRoundTrip(
            value = false,
            set = { setBoolean(0, it) },
            get = { getBoolean(0) },
            push = { pushBoolean(it) },
            next = { nextBoolean() },
        )
    }

    @Test
    fun `short round trip`() {
        listOf(
            Short.MIN_VALUE,
            (-1).toShort(),
            0,
            1,
            Short.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setShort(0, it) },
                get = { getShort(0) },
                push = { pushShort(it) },
                next = { nextShort() },
            )
        }
    }

    @Test
    fun `ushort round trip`() {
        listOf<UShort>(
            0u,
            1u,
            UShort.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setUShort(0, it) },
                get = { getUShort(0) },
                push = { pushUShort(it) },
                next = { nextUShort() },
            )
        }
    }

    @Test
    fun `char round trip`() {
        listOf(
            '\u0000',
            'A',
            'Ж',
            '中',
            Char.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setChar(0, it) },
                get = { getChar(0) },
                push = { pushChar(it) },
                next = { nextChar() },
            )
        }
    }

    @Test
    fun `int round trip`() {
        listOf(
            Int.MIN_VALUE,
            -1,
            0,
            1,
            Int.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setInt(0, it) },
                get = { getInt(0) },
                push = { pushInt(it) },
                next = { nextInt() },
            )
        }
    }

    @Test
    fun `uint round trip`() {
        listOf<UInt>(
            0u,
            1u,
            UInt.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setUInt(0, it) },
                get = { getUInt(0) },
                push = { pushUInt(it) },
                next = { nextUInt() },
            )
        }
    }

    @Test
    fun `long round trip`() {
        listOf(
            Long.MIN_VALUE,
            -1L,
            0L,
            1L,
            Long.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setLong(0, it) },
                get = { getLong(0) },
                push = { pushLong(it) },
                next = { nextLong() },
            )
        }
    }

    @Test
    fun `ulong round trip`() {
        listOf<ULong>(
            0u,
            1u,
            ULong.MAX_VALUE,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setULong(0, it) },
                get = { getULong(0) },
                push = { pushULong(it) },
                next = { nextULong() },
            )
        }
    }

    @Test
    fun `float round trip`() {
        listOf(
            Float.MIN_VALUE,
            -123.5f,
            -0f,
            0f,
            1.5f,
            Float.MAX_VALUE,
            Float.POSITIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setFloat(0, it) },
                get = { getFloat(0) },
                push = { pushFloat(it) },
                next = { nextFloat() },
                assert = { expected, actual ->
                    assertEquals(expected.toBits(), actual.toBits())
                },
            )
        }

        val buffer = NativeBuffer(16)

        buffer.setFloat(0, Float.NaN)
        assertEquals(Float.NaN.toBits(), buffer.getFloat(0).toBits())

        buffer.clear()

        buffer.pushFloat(Float.NaN)
        buffer.flip()

        assertEquals(Float.NaN.toBits(), buffer.nextFloat().toBits())

        buffer.release()
    }

    @Test
    fun `double round trip`() {
        listOf(
            Double.MIN_VALUE,
            -123.5,
            -0.0,
            0.0,
            1.5,
            Double.MAX_VALUE,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
        ).forEach {
            testRoundTrip(
                value = it,
                set = { setDouble(0, it) },
                get = { getDouble(0) },
                push = { pushDouble(it) },
                next = { nextDouble() },
            )
        }

        val buffer = NativeBuffer(32)

        buffer.setDouble(0, Double.NaN)
        assertEquals(Double.NaN.toBits(), buffer.getDouble(0).toBits())

        buffer.clear()

        buffer.pushDouble(Double.NaN)
        buffer.flip()

        assertEquals(Double.NaN.toBits(), buffer.nextDouble().toBits())

        buffer.release()
    }

    private inline fun <T> testRoundTrip(
        value: T,
        crossinline set: NativeBuffer.(T) -> Unit,
        crossinline get: NativeBuffer.() -> T,
        crossinline push: NativeBuffer.(T) -> Unit,
        crossinline next: NativeBuffer.() -> T,
        crossinline assert: (expected: T, actual: T) -> Unit = { e, a ->
            assertEquals(e, a)
        },
    ) {
        for (endian in Endian.entries) {
            val buffer =
                NativeBuffer(
                    capacity = 64,
                    endian = endian,
                )

            buffer.set(value)
            assert(value, buffer.get())

            buffer.clear()

            buffer.push(value)
            buffer.flip()

            assert(value, buffer.next())

            buffer.release()
        }
    }
}
