package com.cws.std.lists

import com.cws.std.memory.NativeData
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@NativeData
class UByteList(
    array: UByteArray,
    size: Int = 0,
) {

    // constructor must be inlined to force NOT heap allocate "init" lambda
    @Suppress("WRONG_MODIFIER_TARGET")
    inline constructor(
        capacity: Int = 16,
        init: (Int) -> UByte = { 0u }
    ) : this(UByteArray(capacity, init))

    var array: UByteArray = array
        private set

    var size = size
        private set

    val capacity: Int
        get() = this@UByteList.array.size

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

    fun first(): UByte {
        check(size > 0)
        return this@UByteList.array[0]
    }

    fun last(): UByte {
        check(size > 0)
        return this@UByteList.array[size - 1]
    }

    operator fun get(index: Int): UByte {
        check(index in 0 until size)
        return this@UByteList.array[index]
    }

    operator fun set(index: Int, value: UByte) {
        check(index in 0 until size)
        this@UByteList.array[index] = value
    }

    fun add(value: UByte) {
        ensureCapacity(size + 1)
        this@UByteList.array[size++] = value
    }

    fun addAll(values: UByteArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        ensureCapacity(size + valuesSize)
        values.copyInto(
            destination = this@UByteList.array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    fun addFrom(source: UByteList, index: Int) {
        ensureCapacity(index + source.size)
        source.array.copyInto(
            destination = this@UByteList.array,
            destinationOffset = index,
            startIndex = 0,
            endIndex = source.size,
        )
        size += source.size
    }

    fun addAll(values: UByteList) = addAll(values.array, 0, values.size)

    fun push(value: UByte) = add(value)

    fun pop(): UByte {
        check(size > 0)
        return this@UByteList.array[--size]
    }

    fun removeLast(): UByte = pop()

    fun ensureCapacity(newCapacity: Int) {
        if (newCapacity <= this@UByteList.array.size) return
        this@UByteList.array = this@UByteList.array.copyOf((newCapacity * 1.1f).roundToInt())
    }

    fun trimToSize() {
        if (size != capacity) {
            this@UByteList.array = this@UByteList.array.copyOf(size)
        }
    }

    fun reserve(capacity: Int) {
        ensureCapacity(capacity)
    }

    fun removeAtSwap(index: Int): UByte {
        check(index in 0 until size)

        val removed = this@UByteList.array[index]
        val last = --size

        if (index != last) {
            this@UByteList.array[index] = this@UByteList.array[last]
        }

        return removed
    }

    fun clone(): UByteList {
        val copy = UByteList(this@UByteList.array.copyOf(), size)
        return copy
    }

    inline fun forEach(block: (UByte) -> Unit) {
        for (i in 0 until size) {
            block(this@UByteList.array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, UByte) -> Unit) {
        for (i in 0 until size) {
            block(i, this@UByteList.array[i])
        }
    }

    inline fun find(block: (UByte) -> Boolean): UByte? {
        for (i in 0 until size) {
            val value = this@UByteList.array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (UByte) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(this@UByteList.array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (UByte) -> Boolean): UByteList {
        val result = UByteList(size)

        for (i in 0 until size) {
            val value = this@UByteList.array[i]
            if (block(value)) {
                result.add(value)
            }
        }

        return result
    }

    fun sort() {
        this@UByteList.array.sort(0, size)
    }

    fun sortDescending() {
        this@UByteList.array.sortDescending(0, size)
    }

    fun sorted(): UByteList =
        clone().apply { sort() }

    fun sortedDescending(): UByteList =
        clone().apply { sortDescending() }

    fun sortWith(comparator: (UByte, UByte) -> Int) {

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

    inline fun sortBy(crossinline selector: (UByte) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (UByte, UByte) -> Int): UByteList =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (UByte) -> Int): UByteList =
        clone().apply {
            sortBy(selector)
        }

    fun shuffle(random: Random = Random) {
        for (i in lastIndex downTo 1) {
            val j = random.nextInt(i + 1)

            val tmp = this@UByteList.array[i]
            this@UByteList.array[i] = this@UByteList.array[j]
            this@UByteList.array[j] = tmp
        }
    }

    fun shuffled(random: Random = Random): UByteList =
        clone().apply {
            shuffle(random)
        }

    fun fill(value: UByte) {
        for (i in 0 until size) {
            this@UByteList.array[i] = value
        }
    }
}