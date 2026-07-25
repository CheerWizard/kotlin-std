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

class NativeBufferEndianTest {
    @Test
    fun `short is stored in little endian`() {
        val buffer = NativeBuffer(2, endian = Endian.LITTLE)

        buffer.setShort(0, 0x1234)

        assertBytes(
            buffer,
            0x34,
            0x12,
        )

        buffer.release()
    }

    @Test
    fun `short is stored in big endian`() {
        val buffer = NativeBuffer(2, endian = Endian.BIG)

        buffer.setShort(0, 0x1234)

        assertBytes(
            buffer,
            0x12,
            0x34,
        )

        buffer.release()
    }

    @Test
    fun `char is stored in little endian`() {
        val buffer = NativeBuffer(2, endian = Endian.LITTLE)

        buffer.setChar(0, 0x1234.toChar())

        assertBytes(
            buffer,
            0x34,
            0x12,
        )

        buffer.release()
    }

    @Test
    fun `char is stored in big endian`() {
        val buffer = NativeBuffer(2, endian = Endian.BIG)

        buffer.setChar(0, 0x1234.toChar())

        assertBytes(
            buffer,
            0x12,
            0x34,
        )

        buffer.release()
    }

    @Test
    fun `int is stored in little endian`() {
        val buffer = NativeBuffer(4, endian = Endian.LITTLE)

        buffer.setInt(0, 0x12345678)

        assertBytes(
            buffer,
            0x78,
            0x56,
            0x34,
            0x12,
        )

        buffer.release()
    }

    @Test
    fun `int is stored in big endian`() {
        val buffer = NativeBuffer(4, endian = Endian.BIG)

        buffer.setInt(0, 0x12345678)

        assertBytes(
            buffer,
            0x12,
            0x34,
            0x56,
            0x78,
        )

        buffer.release()
    }

    @Test
    fun `long is stored in little endian`() {
        val buffer = NativeBuffer(8, endian = Endian.LITTLE)

        buffer.setLong(
            0,
            0x1122334455667788L,
        )

        assertBytes(
            buffer,
            0x88,
            0x77,
            0x66,
            0x55,
            0x44,
            0x33,
            0x22,
            0x11,
        )

        buffer.release()
    }

    @Test
    fun `long is stored in big endian`() {
        val buffer = NativeBuffer(8, endian = Endian.BIG)

        buffer.setLong(
            0,
            0x1122334455667788L,
        )

        assertBytes(
            buffer,
            0x11,
            0x22,
            0x33,
            0x44,
            0x55,
            0x66,
            0x77,
            0x88,
        )

        buffer.release()
    }

    @Test
    fun `float byte order matches int bits`() {
        for (endian in Endian.entries) {
            val floatBuffer = NativeBuffer(4, endian = endian)
            val intBuffer = NativeBuffer(4, endian = endian)

            val value = 123.456f

            floatBuffer.setFloat(0, value)
            intBuffer.setInt(0, value.toBits())

            assertContentEquals(
                floatBuffer.copyToByteArray(ByteArray(4), 0, 4),
                intBuffer.copyToByteArray(ByteArray(4), 0, 4),
            )

            floatBuffer.release()
            intBuffer.release()
        }
    }

    @Test
    fun `double byte order matches long bits`() {
        for (endian in Endian.entries) {
            val doubleBuffer = NativeBuffer(8, endian = endian)
            val longBuffer = NativeBuffer(8, endian = endian)

            val value = 123456.789

            doubleBuffer.setDouble(0, value)
            longBuffer.setLong(0, value.toBits())

            assertContentEquals(
                doubleBuffer.copyToByteArray(ByteArray(8), 0, 8),
                longBuffer.copyToByteArray(ByteArray(8), 0, 8),
            )

            doubleBuffer.release()
            longBuffer.release()
        }
    }

    @Test
    fun `short is read in little endian`() {
        val buffer = NativeBuffer(2, endian = Endian.LITTLE)

        buffer.setByte(0, 0x34)
        buffer.setByte(1, 0x12)

        assertEquals(0x1234.toShort(), buffer.getShort(0))

        buffer.release()
    }

    @Test
    fun `short is read in big endian`() {
        val buffer = NativeBuffer(2, endian = Endian.BIG)

        buffer.setByte(0, 0x12)
        buffer.setByte(1, 0x34)

        assertEquals(0x1234.toShort(), buffer.getShort(0))

        buffer.release()
    }

    private fun assertBytes(
        buffer: NativeBuffer,
        vararg expected: Int,
    ) {
        val actual =
            buffer.copyToByteArray(
                ByteArray(expected.size),
                0,
                expected.size,
            )

        assertContentEquals(
            expected.map { it.toByte() }.toByteArray(),
            actual,
        )
    }
}
