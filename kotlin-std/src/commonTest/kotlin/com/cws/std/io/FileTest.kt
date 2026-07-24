package com.cws.std.io

import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.*

class FileTest {

    private val testPath = "test-${Random.nextUInt().toString().removeSuffix("u")}.txt"

    @Test
    fun testInitialStateAndLifecycle() {
        val file = File(testPath)

        assertTrue(file.isOpened, "File should be automatically marked as opened on initialization")

        file.close()
        assertFalse(file.isOpened, "File should be marked as closed after calling close()")
    }

    @Test
    fun testFileDeletionLifecycle() {
        val sampleText = "Data to be deleted"

        // 1. Create a file and write data to it
        File(testPath).use { file ->
            file.write(sampleText)
        }

        // 2. Instantiate a fresh File handle to verify it exists and contains data
        File(testPath).use { file ->
            assertTrue(file.size > 0, "File should have a valid size before deletion")
            file.delete()
        }

        // 3. Open the file path once more to confirm it is clean and empty
        File(testPath).use { file ->
            assertEquals(0, file.size, "File size should be 0 after calling delete()")
            assertEquals("", file.readText(), "Reading a deleted file path should return an empty string")
        }
    }

    @Test
    fun testWriteAndReadBinary() {
        val dataToWrite = byteArrayOf(10, 20, 30, 40, 50)

        File(testPath).use { file ->
            val bytesWritten = file.write(dataToWrite)
            assertEquals(5, bytesWritten)
        }

        File(testPath).use { file ->
            assertTrue(file.size >= 5)

            val readBuffer = ByteArray(5)
            val bytesRead = file.read(readBuffer)

            assertEquals(5, bytesRead)
            assertContentEquals(dataToWrite, readBuffer)
        }
    }

    @Test
    fun testTextExtensionFunctions() {
        val sampleText = "Hello, Kotlin Multiplatform I/O!"

        File(testPath).use { file ->
            file.write(sampleText)
        }

        File(testPath).use { file ->
            val recoveredText = file.readText()
            assertEquals(sampleText, recoveredText)
        }
    }

    @Test
    fun testEmptyFileReadText() {
        File("empty_file.txt").use { file ->
            assertEquals("", file.readText())
        }
    }

    @Test
    fun testPartialWriteAndReadOffsets() {
        val source = byteArrayOf(11, 22, 33, 44, 55)

        File(testPath).use { file ->
            file.write(source, offset = 1, size = 3)
        }

        File(testPath).use { file ->
            val destination = ByteArray(5)
            val read = file.read(destination, offset = 2, size = 3)
            assertEquals(3, read)

            val expectedDestination = byteArrayOf(0, 0, 22, 33, 44)
            assertContentEquals(expectedDestination, destination)
        }
    }
}
