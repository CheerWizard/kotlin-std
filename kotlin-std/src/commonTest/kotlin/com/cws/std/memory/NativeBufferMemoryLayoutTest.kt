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

class NativeBufferMemoryLayoutTest {
    @Test
    fun `byte size matches layout`() {
        assertEquals(1, Byte.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Byte.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Byte.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `boolean size matches layout`() {
        assertEquals(1, Boolean.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Boolean.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Boolean.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `short size matches layout`() {
        assertEquals(2, Short.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Short.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Short.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `ushort size matches layout`() {
        assertEquals(2, UShort.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, UShort.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, UShort.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `char size matches layout`() {
        assertEquals(2, Char.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Char.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Char.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `int size matches layout`() {
        assertEquals(4, Int.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Int.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Int.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `uint size matches layout`() {
        assertEquals(4, UInt.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, UInt.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, UInt.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `float size matches layout`() {
        assertEquals(4, Float.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(4, Float.sizeBytes(MemoryLayout.STD140))
        assertEquals(4, Float.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `long size matches layout`() {
        assertEquals(8, Long.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(8, Long.sizeBytes(MemoryLayout.STD140))
        assertEquals(8, Long.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `ulong size matches layout`() {
        assertEquals(8, ULong.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(8, ULong.sizeBytes(MemoryLayout.STD140))
        assertEquals(8, ULong.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `double size matches layout`() {
        assertEquals(8, Double.sizeBytes(MemoryLayout.KOTLIN))
        assertEquals(8, Double.sizeBytes(MemoryLayout.STD140))
        assertEquals(8, Double.sizeBytes(MemoryLayout.STD430))
    }

    @Test
    fun `primitive offsets in kotlin layout`() {
        var offset = 0

        assertEquals(0, offset)
        offset += Byte.sizeBytes(MemoryLayout.KOTLIN)

        assertEquals(1, offset)
        offset += Short.sizeBytes(MemoryLayout.KOTLIN)

        assertEquals(3, offset)
        offset += Int.sizeBytes(MemoryLayout.KOTLIN)

        assertEquals(7, offset)
        offset += Long.sizeBytes(MemoryLayout.KOTLIN)

        assertEquals(15, offset)
    }

    @Test
    fun `primitive offsets in std140 layout`() {
        var offset = 0

        assertEquals(0, offset)
        offset += Byte.sizeBytes(MemoryLayout.STD140)

        assertEquals(4, offset)
        offset += Short.sizeBytes(MemoryLayout.STD140)

        assertEquals(8, offset)
        offset += Int.sizeBytes(MemoryLayout.STD140)

        assertEquals(12, offset)
        offset += Long.sizeBytes(MemoryLayout.STD140)

        assertEquals(20, offset)
    }

    @Test
    fun `primitive offsets in std430 layout`() {
        var offset = 0

        assertEquals(0, offset)
        offset += Byte.sizeBytes(MemoryLayout.STD430)

        assertEquals(4, offset)
        offset += Short.sizeBytes(MemoryLayout.STD430)

        assertEquals(8, offset)
        offset += Int.sizeBytes(MemoryLayout.STD430)

        assertEquals(12, offset)
        offset += Long.sizeBytes(MemoryLayout.STD430)

        assertEquals(20, offset)
    }

    @Test
    fun `std140 and std430 have identical primitive sizes`() {
        for (type in listOf(
            Byte.sizeBytes(MemoryLayout.STD140) to Byte.sizeBytes(MemoryLayout.STD430),
            Boolean.sizeBytes(MemoryLayout.STD140) to Boolean.sizeBytes(MemoryLayout.STD430),
            Short.sizeBytes(MemoryLayout.STD140) to Short.sizeBytes(MemoryLayout.STD430),
            UShort.sizeBytes(MemoryLayout.STD140) to UShort.sizeBytes(MemoryLayout.STD430),
            Char.sizeBytes(MemoryLayout.STD140) to Char.sizeBytes(MemoryLayout.STD430),
            Int.sizeBytes(MemoryLayout.STD140) to Int.sizeBytes(MemoryLayout.STD430),
            UInt.sizeBytes(MemoryLayout.STD140) to UInt.sizeBytes(MemoryLayout.STD430),
            Float.sizeBytes(MemoryLayout.STD140) to Float.sizeBytes(MemoryLayout.STD430),
            Long.sizeBytes(MemoryLayout.STD140) to Long.sizeBytes(MemoryLayout.STD430),
            ULong.sizeBytes(MemoryLayout.STD140) to ULong.sizeBytes(MemoryLayout.STD430),
            Double.sizeBytes(MemoryLayout.STD140) to Double.sizeBytes(MemoryLayout.STD430),
        )) {
            assertEquals(type.first, type.second)
        }
    }
}
