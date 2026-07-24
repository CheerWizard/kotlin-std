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