package com.cws.std.lists

import kotlin.random.Random
import kotlin.test.*

class GenericListTest {

    @Test
    fun constructor() {
        val list = GenericList<String>()

        assertEquals(0, list.size)
        assertEquals(16, list.capacity)
        assertTrue(list.isEmpty())
        assertFalse(list.isNotEmpty)
    }

    @Test
    fun addAndGet() {
        val list = GenericList<String>()

        list.add("A")
        list.add("B")

        assertEquals(2, list.size)
        assertEquals("A", list[0])
        assertEquals("B", list[1])
    }

    @Test
    fun grows() {
        val list = GenericList<String>(1)

        repeat(1000) {
            list.add(it.toString())
        }

        assertEquals(1000, list.size)

        repeat(1000) {
            assertEquals(it.toString(), list[it])
        }
    }

    @Test
    fun addAllArray() {
        val list = GenericList<String>()

        list.addAll(arrayOf("A", "B", "C"))

        assertEquals(3, list.size)
        assertEquals("A", list[0])
        assertEquals("B", list[1])
        assertEquals("C", list[2])
    }

    @Test
    fun addAllArrayRange() {
        val list = GenericList<String>()

        list.addAll(
            values = arrayOf("A", "B", "C", "D"),
            start = 1,
            end = 3
        )

        assertEquals(2, list.size)
        assertEquals("B", list[0])
        assertEquals("C", list[1])
    }

    @Test
    fun addAllList() {
        val a = GenericList<String>()
        val b = GenericList<String>()

        a.add("A")

        b.add("B")
        b.add("C")

        a.addAll(b)

        assertEquals(3, a.size)
        assertEquals("A", a[0])
        assertEquals("B", a[1])
        assertEquals("C", a[2])
    }

    @Test
    fun pushPop() {
        val list = GenericList<String>()

        list.push("A")
        list.push("B")

        assertEquals("B", list.pop())
        assertEquals("A", list.pop())
        assertTrue(list.isEmpty())
    }

    @Test
    fun removeAtSwap() {
        val list = GenericList<String>()

        list.add("A")
        list.add("B")
        list.add("C")

        val removed = list.removeAtSwap(1)

        assertEquals("B", removed)
        assertEquals(2, list.size)
        assertEquals("A", list[0])
        assertEquals("C", list[1])
    }

    @Test
    fun reserveAndTrim() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        list.reserve(100)

        assertTrue(list.capacity >= 100)

        list.trimToSize()

        assertEquals(10, list.capacity)
    }

    @Test
    fun clone() {
        val list = GenericList<String>()

        repeat(20) {
            list.add(it.toString())
        }

        val clone = list.clone()

        assertEquals(list.size, clone.size)

        repeat(list.size) {
            assertEquals(list[it], clone[it])
        }
    }

    @Test
    fun find() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        assertEquals("5", list.find { it == "5" })
        assertNull(list.find { it == "100" })
    }

    @Test
    fun findIndex() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        assertEquals(7, list.findIndex { it == "7" })
        assertEquals(-1, list.findIndex { it == "100" })
    }

    @Test
    fun sortWith() {
        val list = GenericList<String>()

        list.add("CCC")
        list.add("A")
        list.add("BB")

        list.sortWith { a, b ->
            a?.length?.compareTo(b?.length ?: 0) ?: 0
        }

        assertEquals("A", list[0])
        assertEquals("BB", list[1])
        assertEquals("CCC", list[2])
    }

    @Test
    fun sortedWith() {
        val list = GenericList<String>()

        list.add("CCC")
        list.add("A")
        list.add("BB")

        val sorted = list.sortedWith { a, b ->
            a?.length?.compareTo(b?.length ?: 0) ?: 0
        }

        assertEquals("CCC", list[0])
        assertEquals("A", sorted[0])
        assertEquals("BB", sorted[1])
        assertEquals("CCC", sorted[2])
    }

    @Test
    fun sortBy() {
        val list = GenericList<String>()

        list.add("CCC")
        list.add("A")
        list.add("BB")

        list.sortBy { it?.length ?: 0 }

        assertEquals("A", list[0])
        assertEquals("BB", list[1])
        assertEquals("CCC", list[2])
    }

    @Test
    fun shuffleAndShuffled() {
        val list = GenericList<String>()

        repeat(100) {
            list.add(it.toString())
        }

        val shuffled = list.shuffled(Random(123))

        assertEquals(list.size, shuffled.size)

        list.shuffle(Random(123))

        assertEquals(100, list.size)
    }

    @Test
    fun fill() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        list.fill("X")

        repeat(list.size) {
            assertEquals("X", list[it])
        }
    }

    @Test
    fun forEach() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        val result = mutableListOf<String>()

        list.forEach {
            result.add(it.orEmpty())
        }

        assertEquals((0..9).map(Int::toString), result)
    }

    @Test
    fun forEachIndexed() {
        val list = GenericList<String>()

        repeat(10) {
            list.add(it.toString())
        }

        list.forEachIndexed { i, value ->
            assertEquals(i.toString(), value)
        }
    }
}
