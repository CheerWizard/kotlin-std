package com.cws.std.lists

import com.cws.std.memory.NativeData

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@NativeData
class ByteList(
    var size: Int = 0,
    array: ByteArray,
) {

    // constructor must be inlined to force NOT heap allocate "init" lambda
    @Suppress("WRONG_MODIFIER_TARGET")
    inline constructor(
        capacity: Int = 16,
        init: (Int) -> Byte = { 0 }
    ) : this(capacity, ByteArray(capacity, init))

    var array: ByteArray = array

    inline val capacity: Int
        get() = array.size

    inline val isEmpty: Boolean
        get() = size == 0

    inline val isNotEmpty: Boolean
        get() = size != 0

    inline val indices: IntRange
        get() = 0 until size

    inline val lastIndex: Int
        get() = size - 1

    inline fun clear() {
        size = 0
    }

    inline fun first(): Byte {
        return array[0]
    }

    inline fun last(): Byte {
        return array[size - 1]
    }

    inline operator fun get(index: Int): Byte {
        return array[index]
    }

    inline operator fun set(index: Int, value: Byte) {
        array[index] = value
    }

    inline fun add(value: Byte) {
        ensureCapacity(size + 1)
        addUnsafe(value)
    }

    inline fun addUnsafe(value: Byte) {
        array[size++] = value
    }

    inline fun addAll(values: ByteArray, start: Int = 0, end: Int = values.size) {
        ensureCapacity(size + abs(end - start))
        addAllUnsafe(values, start, end)
    }

    inline fun addAllUnsafe(values: ByteArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    inline fun addAll(values: ByteList) = addAll(values.array, 0, values.size)

    inline fun push(value: Byte) = add(value)

    inline fun pop(): Byte {
        return array[--size]
    }

    inline fun removeLast(): Byte = pop()

    inline fun ensureCapacity(newCapacity: Int) {
        if (newCapacity <= array.size) return
        array = array.copyOf((newCapacity * 1.1f).roundToInt())
    }

    inline fun trimToSize() {
        if (size != capacity) {
            array = array.copyOf(size)
        }
    }

    inline fun reserve(capacity: Int) {
        ensureCapacity(capacity)
    }

    inline fun removeAtSwap(index: Int): Byte {
        val removed = array[index]
        val last = --size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    inline fun clone(): ByteList {
        val copy = ByteList(size, array.copyOf())
        return copy
    }

    inline fun forEach(block: (Byte) -> Unit) {
        for (i in 0 until size) {
            block(array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, Byte) -> Unit) {
        for (i in 0 until size) {
            block(i, array[i])
        }
    }

    inline fun find(block: (Byte) -> Boolean): Byte? {
        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (Byte) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (Byte) -> Boolean): ByteList {
        val result = ByteList(size)

        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                result.add(value)
            }
        }

        return result
    }

    inline fun sort() {
        array.sort(0, size)
    }

    inline fun sortDescending() {
        array.sortDescending(0, size)
    }

    inline fun sorted(): ByteList =
        clone().apply { sort() }

    inline fun sortedDescending(): ByteList =
        clone().apply { sortDescending() }

    fun sortWith(comparator: (Byte, Byte) -> Int) {

        fun quicksort(from: Int, to: Int) {
            if (from >= to) return

            val pivot = this[(from + to) ushr 1]

            var i = from
            var j = to

            while (i <= j) {

                while (comparator(this[i], pivot) < 0) i++

                while (comparator(this[j], pivot) > 0) j--

                if (i <= j) {
                    val tmp = this[i]
                    this[i] = this[j]
                    this[j] = tmp
                    i++
                    j--
                }
            }

            if (from < j) quicksort(from, j)
            if (i < to) quicksort(i, to)
        }

        if (size > 1) {
            quicksort(0, size - 1)
        }
    }

    inline fun sortBy(crossinline selector: (Byte) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (Byte, Byte) -> Int): ByteList =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (Byte) -> Int): ByteList =
        clone().apply {
            sortBy(selector)
        }

    inline fun shuffle(random: Random = Random) {
        for (i in lastIndex downTo 1) {
            val j = random.nextInt(i + 1)

            val tmp = array[i]
            array[i] = array[j]
            array[j] = tmp
        }
    }

    inline fun shuffled(random: Random = Random): ByteList =
        clone().apply {
            shuffle(random)
        }

    inline fun fill(value: Byte) {
        for (i in 0 until size) {
            array[i] = value
        }
    }

    inline fun addFrom(source: ByteList, index: Int) {
        ensureCapacity(index + source.size)
        source.array.copyInto(
            destination = array,
            destinationOffset = index,
            startIndex = 0,
            endIndex = source.size,
        )
        size += source.size
    }
}