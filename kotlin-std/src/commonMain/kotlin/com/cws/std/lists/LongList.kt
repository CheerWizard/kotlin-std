package com.cws.std.lists

import com.cws.std.memory.NativeData

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@NativeData
class LongList(
    var size: Int = 0,
    array: LongArray,
) {

    // constructor must be inlined to force NOT heap allocate "init" lambda
    @Suppress("WRONG_MODIFIER_TARGET")
    inline constructor(
        capacity: Int = 16,
        init: (Int) -> Long = { 0L }
    ) : this(capacity, LongArray(capacity, init))

    var array: LongArray = array

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

    inline fun first(): Long {
        return array[0]
    }

    inline fun last(): Long {
        return array[size - 1]
    }

    inline operator fun get(index: Int): Long {
        return array[index]
    }

    inline operator fun set(index: Int, value: Long) {
        array[index] = value
    }

    inline fun add(value: Long) {
        ensureCapacity(size + 1)
        addUnsafe(value)
    }

    inline fun addUnsafe(value: Long) {
        array[size++] = value
    }

    inline fun addAll(values: LongArray, start: Int = 0, end: Int = values.size) {
        ensureCapacity(size + abs(end - start))
        addAllUnsafe(values, start, end)
    }

    inline fun addAllUnsafe(values: LongArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    inline fun addAll(values: LongList) = addAll(values.array, 0, values.size)

    inline fun push(value: Long) = add(value)

    inline fun pop(): Long {
        return array[--size]
    }

    inline fun removeLast(): Long = pop()

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

    inline fun removeAtSwap(index: Int): Long {
        val removed = array[index]
        val last = --size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    inline fun clone(): LongList {
        val copy = LongList(size, array.copyOf())
        return copy
    }

    inline fun forEach(block: (Long) -> Unit) {
        for (i in 0 until size) {
            block(array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, Long) -> Unit) {
        for (i in 0 until size) {
            block(i, array[i])
        }
    }

    inline fun find(block: (Long) -> Boolean): Long? {
        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (Long) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (Long) -> Boolean): LongList {
        val result = LongList(size)

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

    inline fun sorted(): LongList =
        clone().apply { sort() }

    inline fun sortedDescending(): LongList =
        clone().apply { sortDescending() }

    fun sortWith(comparator: (Long, Long) -> Int) {

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

    inline fun sortBy(crossinline selector: (Long) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (Long, Long) -> Int): LongList =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (Long) -> Int): LongList =
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

    inline fun shuffled(random: Random = Random): LongList =
        clone().apply {
            shuffle(random)
        }

    inline fun fill(value: Long) {
        for (i in 0 until size) {
            array[i] = value
        }
    }

    inline fun addFrom(source: LongList, index: Int) {
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