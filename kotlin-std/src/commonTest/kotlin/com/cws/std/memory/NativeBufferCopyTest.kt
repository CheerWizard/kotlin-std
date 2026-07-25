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

class NativeBufferCopyTest {
    @Test
    fun copy_entire_buffer() {
        forEachConfiguration { _, _, src ->

            src.pushBoolean(true)
            src.pushInt(42)
            src.pushFloat(3.14f)
            src.pushLong(123456789L)

            val written = src.position
            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = 0,
                sizeBytes = written,
            )

            dst.flip()

            assertEquals(true, dst.nextBoolean())
            assertEquals(42, dst.nextInt())
            assertEquals(3.14f.toBits(), dst.nextFloat().toBits())
            assertEquals(123456789L, dst.nextLong())
            assertEquals(written, dst.position)

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_partial_buffer() {
        forEachConfiguration { _, _, src ->

            src.pushInt(111)

            val copyStart = src.position

            src.pushFloat(1.5f)
            src.pushLong(999L)

            val copySize = src.position - copyStart

            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = copyStart,
                destIndex = 0,
                sizeBytes = copySize,
            )

            dst.flip()

            assertEquals(1.5f.toBits(), dst.nextFloat().toBits())
            assertEquals(999L, dst.nextLong())
            assertEquals(copySize, dst.position)

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_with_destination_offset() {
        forEachConfiguration { _, _, src ->

            src.pushInt(123)
            src.pushBoolean(true)

            val payloadSize = src.position

            val dst = src.clone()

            dst.pushLong(777L)
            val destinationOffset = dst.position

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = destinationOffset,
                sizeBytes = payloadSize,
            )

            dst.flip()

            assertEquals(777L, dst.nextLong())
            assertEquals(123, dst.nextInt())
            assertEquals(true, dst.nextBoolean())

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_zero_bytes() {
        forEachConfiguration { _, _, src ->

            src.pushInt(42)

            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = 0,
                sizeBytes = 0,
            )

            dst.flip()

            assertEquals(0, dst.position)

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_variable_byte_array() {
        forEachConfiguration { _, _, src ->

            val expected = ByteArray(128) { it.toByte() }

            src.pushByteArray(expected)

            val written = src.position

            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = 0,
                sizeBytes = written,
            )

            dst.flip()

            assertContentEquals(expected, dst.nextByteArray())
            assertEquals(written, dst.position)

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_utf8_string() {
        forEachConfiguration { _, _, src ->

            val expected = "Hello 😀 Привіт こんにちは"

            src.pushStringUtf8(expected)

            val written = src.position

            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = 0,
                sizeBytes = written,
            )

            dst.flip()

            assertEquals(expected, dst.nextStringUtf8())
            assertEquals(written, dst.position)

            src.release()
            dst.release()
        }
    }

    @Test
    fun copy_complex_payload() {
        forEachConfiguration { _, _, src ->

            src.pushBoolean(true)
            src.pushInt(42)
            src.pushFloat(3.14f)
            src.pushStringUtf8("NativeBuffer")
            src.pushByteArray(byteArrayOf(1, 2, 3, 4))
            src.pushLong(987654321L)

            val written = src.position

            val dst = src.clone()

            src.copyTo(
                dest = dst,
                srcIndex = 0,
                destIndex = 0,
                sizeBytes = written,
            )

            dst.flip()

            assertEquals(true, dst.nextBoolean())
            assertEquals(42, dst.nextInt())
            assertEquals(3.14f.toBits(), dst.nextFloat().toBits())
            assertEquals("NativeBuffer", dst.nextStringUtf8())
            assertContentEquals(byteArrayOf(1, 2, 3, 4), dst.nextByteArray())
            assertEquals(987654321L, dst.nextLong())
            assertEquals(written, dst.position)

            src.release()
            dst.release()
        }
    }
}
