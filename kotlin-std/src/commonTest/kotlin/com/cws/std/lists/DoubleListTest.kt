package com.cws.std.lists

import kotlin.random.Random
import kotlin.test.*

// the point is to test all primitive lists generated from KSP NativeProcessor class.
// if DoubleListTest passed, then all other primitive lists are passed as well, they all are generated from same template file.
class DoubleListTest {

    @Test
    fun constructor() {
        val list = DoubleList()

        assertEquals(0, list.size)
        assertEquals(16, list.capacity)
        assertTrue(list.isEmpty)
        assertFalse(list.isNotEmpty)
    }

    @Test
    fun add() {
        val list = DoubleList()

        list.add(1.0)
        list.add(2.0)

        assertEquals(2, list.size)
        assertEquals(1.0, list[0])
        assertEquals(2.0, list[1])
    }

    @Test
    fun addAllArray() {
        val list = DoubleList()

        list.addAll(doubleArrayOf(1.0, 2.0, 3.0))

        assertEquals(3, list.size)
        assertContentEquals(
            doubleArrayOf(1.0, 2.0, 3.0),
            list.array.copyOf(list.size)
        )
    }

    @Test
    fun addAllList() {
        val a = DoubleList()
        a.add(1.0)

        val b = DoubleList()
        b.add(2.0)
        b.add(3.0)

        a.addAll(b)

        assertEquals(3, a.size)
        assertEquals(3.0, a.last())
    }

    @Test
    fun pushPop() {
        val list = DoubleList()

        list.push(10.0)
        list.push(20.0)

        assertEquals(20.0, list.pop())
        assertEquals(10.0, list.pop())
        assertTrue(list.isEmpty)
    }

    @Test
    fun firstLast() {
        val list = DoubleList()

        list.add(5.0)
        list.add(7.0)

        assertEquals(5.0, list.first())
        assertEquals(7.0, list.last())
    }

    @Test
    fun setGet() {
        val list = DoubleList()

        list.add(1.0)

        list[0] = 42.0

        assertEquals(42.0, list[0])
    }

    @Test
    fun clear() {
        val list = DoubleList()

        list.add(1.0)
        list.clear()

        assertEquals(0, list.size)
        assertTrue(list.isEmpty)
    }

    @Test
    fun removeAtSwap() {
        val list = DoubleList()

        list.add(1.0)
        list.add(2.0)
        list.add(3.0)

        val removed = list.removeAtSwap(1)

        assertEquals(2.0, removed)
        assertEquals(2, list.size)
        assertEquals(3.0, list[1])
    }

    @Test
    fun reserve() {
        val list = DoubleList(2)

        list.reserve(100)

        assertTrue(list.capacity >= 100)
    }

    @Test
    fun trimToSize() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        list.reserve(100)
        list.trimToSize()

        assertEquals(10, list.capacity)
    }

    @Test
    fun clone() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        val clone = list.clone()

        assertEquals(list.size, clone.size)

        repeat(list.size) {
            assertEquals(list[it], clone[it])
        }
    }

    @Test
    fun find() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        assertEquals(5.0, list.find { it == 5.0 })
        assertNull(list.find { it == 100.0 })
    }

    @Test
    fun findIndex() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        assertEquals(7, list.findIndex { it == 7.0 })
        assertEquals(-1, list.findIndex { it == 100.0 })
    }

    @Test
    fun filter() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        val even = list.filter {
            it.toInt() % 2 == 0
        }

        assertEquals(5, even.size)

        assertEquals(0.0, even[0])
        assertEquals(2.0, even[1])
        assertEquals(4.0, even[2])
        assertEquals(6.0, even[3])
        assertEquals(8.0, even[4])
    }

    @Test
    fun sort() {
        val list = DoubleList()

        list.add(5.0)
        list.add(1.0)
        list.add(3.0)

        list.sort()

        assertEquals(1.0, list[0])
        assertEquals(3.0, list[1])
        assertEquals(5.0, list[2])
    }

    @Test
    fun sortDescending() {
        val list = DoubleList()

        list.add(5.0)
        list.add(1.0)
        list.add(3.0)

        list.sortDescending()

        assertEquals(5.0, list[0])
        assertEquals(3.0, list[1])
        assertEquals(1.0, list[2])
    }

    @Test
    fun sortWith() {
        val list = DoubleList()

        list.add(1.2)
        list.add(5.6)
        list.add(3.4)

        list.sortWith { a, b ->
            b.compareTo(a)
        }

        assertEquals(5.6, list[0])
        assertEquals(3.4, list[1])
        assertEquals(1.2, list[2])
    }

    @Test
    fun sortBy() {
        val list = DoubleList()

        list.add(2.7)
        list.add(1.2)
        list.add(3.9)

        list.sortBy {
            it.toInt()
        }

        assertEquals(1.2, list[0])
        assertEquals(2.7, list[1])
        assertEquals(3.9, list[2])
    }

    @Test
    fun shuffled() {
        val list = DoubleList()

        repeat(100) {
            list.add(it.toDouble())
        }

        val shuffled = list.shuffled(Random(123))

        assertEquals(list.size, shuffled.size)
    }

    @Test
    fun fill() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        list.fill(42.0)

        repeat(list.size) {
            assertEquals(42.0, list[it])
        }
    }

    @Test
    fun forEach() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        var sum = 0.0

        list.forEach {
            sum += it
        }

        assertEquals(45.0, sum)
    }

    @Test
    fun forEachIndexed() {
        val list = DoubleList()

        repeat(10) {
            list.add(it.toDouble())
        }

        var sum = 0.0

        list.forEachIndexed { i, v ->
            sum += i + v
        }

        assertEquals(90.0, sum)
    }

    @Test
    fun growsCorrectly() {
        val list = DoubleList(1)

        repeat(1000) {
            list.add(it.toDouble())
        }

        assertEquals(1000, list.size)

        repeat(1000) {
            assertEquals(it.toDouble(), list[it])
        }
    }
}