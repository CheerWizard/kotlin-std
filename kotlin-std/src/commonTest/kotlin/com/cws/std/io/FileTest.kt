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
package com.cws.std.io

import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.*

class FileTest {

    private val testPath = "test-${Random.nextUInt().toString().removeSuffix("u")}.txt"

    @Test
    fun testInitialStateAndLifecycle() = runTest {
        val file = File(testPath)

        assertTrue(file.isOpened, "File should be automatically marked as opened on initialization")

        file.close()
        assertFalse(file.isOpened, "File should be marked as closed after calling close()")
    }

    @Test
    fun testFileDeletionLifecycle() = runTest {
        val sampleText = "Data to be deleted"

        // 1. Create a file and write data to it
        File(testPath).use {
            write(sampleText)
        }

        // 2. Instantiate a fresh File handle to verify it exists and contains data
        File(testPath).use {
            assertTrue(size > 0, "File should have a valid size before deletion")
            delete()
        }

        // 3. Open the file path once more to confirm it is clean and empty
        File(testPath).use {
            assertEquals(0, size, "File size should be 0 after calling delete()")
            assertEquals("", readText(), "Reading a deleted file path should return an empty string")
        }
    }

    @Test
    fun testWriteAndReadBinary() = runTest  {
        val dataToWrite = byteArrayOf(10, 20, 30, 40, 50)

        File(testPath).use {
            val bytesWritten = write(dataToWrite)
            assertEquals(5, bytesWritten)
        }

        File(testPath).use {
            assertTrue(size >= 5)

            val readBuffer = ByteArray(5)
            val bytesRead = read(readBuffer)

            assertEquals(5, bytesRead)
            assertContentEquals(dataToWrite, readBuffer)
        }
    }

    @Test
    fun testTextExtensionFunctions() = runTest  {
        val sampleText = "Hello, Kotlin Multiplatform I/O!"

        File(testPath).use {
            write(sampleText)
        }

        File(testPath).use {
            val recoveredText = readText()
            assertEquals(sampleText, recoveredText)
        }
    }

    @Test
    fun testEmptyFileReadText() = runTest  {
        File("empty_file.txt").use {
            assertEquals("", readText())
        }
    }

    @Test
    fun testPartialWriteAndReadOffsets() = runTest  {
        val source = byteArrayOf(11, 22, 33, 44, 55)

        File(testPath).use {
            write(source, offset = 1, size = 3)
        }

        File(testPath).use {
            val destination = ByteArray(5)
            val read = read(destination, offset = 2, size = 3)
            assertEquals(3, read)

            val expectedDestination = byteArrayOf(0, 0, 22, 33, 44)
            assertContentEquals(expectedDestination, destination)
        }
    }
}
