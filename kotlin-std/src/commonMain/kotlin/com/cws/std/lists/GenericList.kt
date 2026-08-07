package com.cws.std.lists

import com.cws.std.memory.NativeBuffer
import kotlin.jvm.JvmStatic
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

inline fun <reified T> GenericList(
    capacity: Int = 16,
    noinline init: (Int) -> T? = { null },
) = GenericList(Array(capacity, init), init)

// generic list implementation for SoA, which is less optimized because it uses heap allocations and object references during read/write
class GenericList<T>(
    array: Array<T>,
    private val init: (Int) -> T,
    size: Int = 0,
) : Collection<T> {

    var array: Array<T> = array
        private set

    private var _size = size

    override val size: Int get() = _size

    val capacity: Int
        get() = array.size

    val isNotEmpty: Boolean
        get() = _size > 0

    val indices: IntRange
        get() = 0 until size

    val lastIndex: Int
        get() = size - 1

    override fun isEmpty(): Boolean = _size <= 0

    override fun contains(element: T): Boolean = array.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean {
        var contains = 0
        elements.forEach {
            if (array.contains(it)) contains++
        }
        return contains == elements.size
    }

    // FIXME: not really used and implemented at the moment.
    override fun iterator(): Iterator<T> = iterator {}

    fun clear() {
        _size = 0
    }

    fun first(): T {
        check(size > 0)
        return array[0]
    }

    fun last(): T {
        check(size > 0)
        return array[size - 1]
    }

    operator fun get(index: Int): T {
        check(index in 0 until size)
        return array[index]
    }

    operator fun set(index: Int, value: T) {
        check(index in 0 until size)
        array[index] = value
    }

    fun add(value: T) {
        ensureCapacity(size + 1)
        array[_size++] = value
    }

    fun addAll(values: Array<T>, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        ensureCapacity(size + valuesSize)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        _size += valuesSize
    }

    fun addAll(values: GenericList<T>) = addAll(values.array, 0, values.size)

    fun push(value: T) = add(value)

    fun pop(): T {
        check(size > 0)
        return array[--_size]
    }

    fun removeLast(): T = pop()

    @OptIn(ExperimentalStdlibApi::class)
    fun ensureCapacity(newCapacity: Int) {
        if (newCapacity <= array.size) return
        array = array.copyOf((newCapacity * 1.1f).roundToInt(), init)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun trimToSize() {
        if (size != capacity) {
            array = array.copyOf(size, init)
        }
    }

    fun reserve(capacity: Int) {
        ensureCapacity(capacity)
    }

    fun removeAtSwap(index: Int): T {
        check(index in 0 until size)

        val removed = array[index]
        val last = --_size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    fun clone(): GenericList<T> {
        val copy = GenericList(array.copyOf(), init, size)
        return copy
    }

    inline fun forEach(block: (T) -> Unit) {
        for (i in 0 until size) {
            block(array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, T) -> Unit) {
        for (i in 0 until size) {
            block(i, array[i])
        }
    }

    inline fun find(block: (T) -> Boolean): T? {
        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (T) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (T) -> Boolean): GenericList<T> {
        val result = clone()

        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                result.add(value)
            }
        }

        return result
    }

    fun sortWith(comparator: (T, T) -> Int) {

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

    inline fun sortBy(crossinline selector: (T) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (T, T) -> Int): GenericList<T> =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (T) -> Int): GenericList<T> =
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

    fun shuffled(random: Random = Random): GenericList<T> =
        clone().apply {
            shuffle(random)
        }

    fun fill(value: T) {
        for (i in 0 until size) {
            array[i] = value
        }
    }

    fun addFrom(source: GenericList<T>, index: Int) {
        ensureCapacity(index + source.size)
        source.array.copyInto(
            destination = array,
            destinationOffset = index,
            startIndex = 0,
            endIndex = source.size,
        )
        _size += source.size
    }
}