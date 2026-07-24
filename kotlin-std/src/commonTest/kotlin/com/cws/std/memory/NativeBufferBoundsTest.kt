package com.cws.std.memory

import kotlin.test.Test
import kotlin.test.assertFailsWith

class NativeBufferBoundsTest {

    @Test
    fun `setByte negative index throws`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.setByte(-1, 1)
            }

            buffer.release()
        }
    }

    @Test
    fun `getByte negative index throws`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.getByte(-1)
            }

            buffer.release()
        }
    }

    @Test
    fun `setByte at capacity throws`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.setByte(buffer.limit, 1)
            }

            buffer.release()
        }
    }

    @Test
    fun `getByte at capacity throws`() {
        forEachConfiguration { _, _, buffer ->

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.getByte(buffer.limit)
            }

            buffer.release()
        }
    }

    @Test
    fun `push beyond capacity throws`() {
        forEachConfiguration { layout, _, buffer ->

            repeat(buffer.limit / Byte.sizeBytes(layout)) {
                buffer.pushByte(1)
            }

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.pushByte(2)
            }

            buffer.release()
        }
    }

    @Test
    fun `next beyond capacity throws`() {
        forEachConfiguration { layout, _, buffer ->

            repeat(buffer.limit / Byte.sizeBytes(layout)) {
                buffer.pushByte(1)
            }

            buffer.flip()

            repeat(buffer.limit / Byte.sizeBytes(layout)) {
                buffer.nextByte()
            }

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.nextByte()
            }

            buffer.release()
        }
    }

    @Test
    fun `copy source out of bounds throws`() {
        forEachConfiguration { _, _, src ->

            val dst = src.clone()

            assertFailsWith<IndexOutOfBoundsException> {
                src.copyTo(
                    dest = dst,
                    srcIndex = src.limit,
                    destIndex = 0,
                    sizeBytes = 1
                )
            }

            src.release()
            dst.release()
        }
    }

    @Test
    fun `copy destination out of bounds throws`() {
        forEachConfiguration { _, _, src ->

            val dst = src.clone()

            assertFailsWith<IndexOutOfBoundsException> {
                src.copyTo(
                    dest = dst,
                    srcIndex = 0,
                    destIndex = dst.limit,
                    sizeBytes = 1
                )
            }

            src.release()
            dst.release()
        }
    }

    @Test
    fun `resize to zero capacity`() {
        forEachConfiguration { _, _, buffer ->

            buffer.resize(0)

            assertFailsWith<IndexOutOfBoundsException> {
                buffer.pushByte(1)
            }

            buffer.release()
        }
    }
}