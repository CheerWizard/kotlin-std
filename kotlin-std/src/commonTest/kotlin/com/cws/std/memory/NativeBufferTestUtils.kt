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

import kotlin.test.assertEquals

inline fun forEachConfiguration(block: (MemoryLayout, Endian, NativeBuffer) -> Unit) {
    for (layout in MemoryLayout.entries) {
        for (endian in Endian.entries) {
            block(
                layout,
                endian,
                NativeBuffer(
                    capacity = 1024,
                    memoryLayout = layout,
                    endian = endian,
                ),
            )
        }
    }
}

inline fun forEachEndian(block: (NativeBuffer) -> Unit) {
    Endian.entries.forEach { endian ->
        block(
            NativeBuffer(
                capacity = 16 * 1024,
                memoryLayout = MemoryLayout.KOTLIN,
                endian = endian,
            ),
        )
    }
}

fun assertFloatArrayEquals(
    expected: FloatArray,
    actual: FloatArray,
) {
    assertEquals(expected.size, actual.size)
    expected.indices.forEach {
        assertEquals(
            expected[it].toBits(),
            actual[it].toBits(),
        )
    }
}

fun assertDoubleArrayEquals(
    expected: DoubleArray,
    actual: DoubleArray,
) {
    assertEquals(expected.size, actual.size)
    expected.indices.forEach {
        assertEquals(
            expected[it].toBits(),
            actual[it].toBits(),
        )
    }
}
