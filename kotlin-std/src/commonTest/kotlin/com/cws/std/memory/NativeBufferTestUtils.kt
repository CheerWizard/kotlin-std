package com.cws.std.memory

import kotlin.test.assertEquals

inline fun forEachConfiguration(
    block: (MemoryLayout, Endian, NativeBuffer) -> Unit
) {
    for (layout in MemoryLayout.entries) {
        for (endian in Endian.entries) {
            block(
                layout,
                endian,
                NativeBuffer(
                    capacity = 1024,
                    memoryLayout = layout,
                    endian = endian
                )
            )
        }
    }
}

inline fun forEachEndian(
    block: (NativeBuffer) -> Unit
) {
    Endian.entries.forEach { endian ->
        block(
            NativeBuffer(
                capacity = 16 * 1024,
                memoryLayout = MemoryLayout.KOTLIN,
                endian = endian
            )
        )
    }
}

fun assertFloatArrayEquals(
    expected: FloatArray,
    actual: FloatArray
) {
    assertEquals(expected.size, actual.size)
    expected.indices.forEach {
        assertEquals(
            expected[it].toBits(),
            actual[it].toBits()
        )
    }
}

fun assertDoubleArrayEquals(
    expected: DoubleArray,
    actual: DoubleArray
) {
    assertEquals(expected.size, actual.size)
    expected.indices.forEach {
        assertEquals(
            expected[it].toBits(),
            actual[it].toBits()
        )
    }
}