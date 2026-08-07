package com.cws.std.lists

import com.cws.std.memory.NativeData
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@NativeData
class FloatList(
    array: FloatArray,
    size: Int = 0,
) {

    // constructor must be inlined to force NOT heap allocate "init" lambda
    @Suppress("WRONG_MODIFIER_TARGET")
    inline constructor(
        capacity: Int = 16,
        init: (Int) -> Float = { 0f }
    ) : this(FloatArray(capacity, init))

    var array: FloatArray = array
        private set

    var size = size
        private set

    val capacity: Int
        get() = array.size

    val isEmpty: Boolean
        get() = size == 0

    val isNotEmpty: Boolean
        get() = size != 0

    val indices: IntRange
        get() = 0 until size

    val lastIndex: Int
        get() = size - 1

    fun clear() {
        size = 0
    }

    fun first(): Float {
        check(size > 0)
        return array[0]
    }

    fun last(): Float {
        check(size > 0)
        return array[size - 1]
    }

    operator fun get(index: Int): Float {
        check(index in 0 until size)
        return array[index]
    }

    operator fun set(index: Int, value: Float) {
        check(index in 0 until size)
        array[index] = value
    }

    fun add(value: Float) {
        ensureCapacity(size + 1)
        array[size++] = value
    }

    fun addAll(values: FloatArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        ensureCapacity(size + valuesSize)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    fun addFrom(source: FloatList, index: Int) {
        ensureCapacity(index + source.size)
        source.array.copyInto(
            destination = array,
            destinationOffset = index,
            startIndex = 0,
            endIndex = source.size,
        )
        size += source.size
    }

    fun addAll(values: FloatList) = addAll(values.array, 0, values.size)

    fun push(value: Float) = add(value)

    fun pop(): Float {
        check(size > 0)
        return array[--size]
    }

    fun removeLast(): Float = pop()

    fun ensureCapacity(newCapacity: Int) {
        if (newCapacity <= array.size) return
        array = array.copyOf((newCapacity * 1.1f).roundToInt())
    }

    fun trimToSize() {
        if (size != capacity) {
            array = array.copyOf(size)
        }
    }

    fun reserve(capacity: Int) {
        ensureCapacity(capacity)
    }

    fun removeAtSwap(index: Int): Float {
        check(index in 0 until size)

        val removed = array[index]
        val last = --size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    fun clone(): FloatList {
        val copy = FloatList(array.copyOf(), size)
        return copy
    }

    inline fun forEach(block: (Float) -> Unit) {
        for (i in 0 until size) {
            block(array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, Float) -> Unit) {
        for (i in 0 until size) {
            block(i, array[i])
        }
    }

    inline fun find(block: (Float) -> Boolean): Float? {
        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (Float) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (Float) -> Boolean): FloatList {
        val result = FloatList(size)

        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                result.add(value)
            }
        }

        return result
    }

    fun sort() {
        array.sort(0, size)
    }

    fun sortDescending() {
        array.sortDescending(0, size)
    }

    fun sorted(): FloatList =
        clone().apply { sort() }

    fun sortedDescending(): FloatList =
        clone().apply { sortDescending() }

    fun sortWith(comparator: (Float, Float) -> Int) {

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

    inline fun sortBy(crossinline selector: (Float) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (Float, Float) -> Int): FloatList =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (Float) -> Int): FloatList =
        clone().apply {
            sortBy(selector)
        }

    fun shuffle(random: Random = Random) {
        for (i in lastIndex downTo 1) {
            val j = random.nextInt(i + 1)

            val tmp = array[i]
            array[i] = array[j]
            array[j] = tmp
        }
    }

    fun shuffled(random: Random = Random): FloatList =
        clone().apply {
            shuffle(random)
        }

    fun fill(value: Float) {
        for (i in 0 until size) {
            array[i] = value
        }
    }
}