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
import kotlin.test.assertNotSame

class NativeBufferCloneTest {
    @Test
    fun `clone copies metadata`() {
        forEachConfiguration { layout, endian, src ->

            val clone = src.clone()

            assertEquals(src.limit, clone.limit)
            assertEquals(layout, clone.memoryLayout)
            assertEquals(endian, clone.endian)
            assertEquals(src.memoryBoundary, clone.memoryBoundary)

            src.release()
            clone.release()
        }
    }

    @Test
    fun `clone copies bytes`() {
        forEachConfiguration { _, _, src ->

            src.pushBoolean(true)
            src.pushInt(42)
            src.pushFloat(3.14f)
            src.pushLong(123456789L)

            val written = src.position

            val clone = src.clone()
            src.copyTo(clone, 0, 0, written)

            repeat(written) { i ->
                assertEquals(src.getByte(i), clone.getByte(i))
            }

            src.release()
            clone.release()
        }
    }

    @Test
    fun `clone round trip`() {
        forEachConfiguration { _, _, src ->

            src.pushBoolean(true)
            src.pushInt(42)
            src.pushFloat(3.14f)
            src.pushLong(123456789L)

            val written = src.position

            val clone = src.clone()
            src.copyTo(clone, 0, 0, written)

            clone.flip()

            assertEquals(true, clone.nextBoolean())
            assertEquals(42, clone.nextInt())
            assertEquals(3.14f.toBits(), clone.nextFloat().toBits())
            assertEquals(123456789L, clone.nextLong())
            assertEquals(written, clone.position)

            src.release()
            clone.release()
        }
    }

    @Test
    fun `clone is independent`() {
        forEachConfiguration { _, _, src ->

            src.pushInt(42)

            val clone = src.clone()
            src.copyTo(clone, 0, 0, src.position)

            clone.setByte(0, 99)

            assertEquals(42, src.getInt(0))
            assertEquals(99.toByte(), clone.getByte(0))

            src.release()
            clone.release()
        }
    }

    @Test
    fun `clone has independent position`() {
        forEachConfiguration { _, _, src ->

            src.pushInt(42)

            val clone = src.clone()
            src.copyTo(clone, 0, 0, src.position)

            clone.flip()
            clone.nextInt()

            assertEquals(Int.sizeBytes(src.memoryLayout), clone.position)
            assertEquals(Int.sizeBytes(src.memoryLayout), src.position)

            src.release()
            clone.release()
        }
    }

    @Test
    fun `clone creates different instance`() {
        forEachConfiguration { _, _, src ->

            val clone = src.clone()

            assertNotSame(src, clone)

            src.release()
            clone.release()
        }
    }
}
