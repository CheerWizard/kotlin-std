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
@file:Suppress("NOTHING_TO_INLINE")
@file:OptIn(ExperimentalUnsignedTypes::class)

package com.cws.std.lists

import com.cws.std.memory.NativeData

import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

inline fun IntList(
    capacity: Int = 16,
    noinline init: (Int) -> Int = { 0 }
) = IntList(capacity, IntArray(capacity, init))

@NativeData
class IntList(
    var size: Int = 0,
    array: IntArray,
) {

    var array: IntArray = array

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

    inline fun first(): Int {
        return array[0]
    }

    inline fun last(): Int {
        return array[size - 1]
    }

    inline operator fun get(index: Int): Int {
        return array[index]
    }

    inline operator fun set(index: Int, value: Int) {
        array[index] = value
    }

    inline fun add(value: Int) {
        ensureCapacity(size + 1)
        addUnsafe(value)
    }

    inline fun addUnsafe(value: Int) {
        array[size++] = value
    }

    inline fun addAll(values: IntArray, start: Int = 0, end: Int = values.size) {
        ensureCapacity(size + abs(end - start))
        addAllUnsafe(values, start, end)
    }

    inline fun addAllUnsafe(values: IntArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    inline fun addAll(values: IntList) = addAll(values.array, 0, values.size)

    inline fun push(value: Int) = add(value)

    inline fun pop(): Int {
        return array[--size]
    }

    inline fun removeLast(): Int = pop()

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

    inline fun removeAtSwap(index: Int): Int {
        val removed = array[index]
        val last = --size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    inline fun clone(): IntList {
        val copy = IntList(size, array.copyOf())
        return copy
    }

    inline fun forEach(block: (Int) -> Unit) {
        for (i in 0 until size) {
            block(array[i])
        }
    }

    inline fun forEachIndexed(block: (Int, Int) -> Unit) {
        for (i in 0 until size) {
            block(i, array[i])
        }
    }

    inline fun find(block: (Int) -> Boolean): Int? {
        for (i in 0 until size) {
            val value = array[i]
            if (block(value)) {
                return value
            }
        }
        return null
    }

    inline fun findIndex(block: (Int) -> Boolean): Int {
        for (i in 0 until size) {
            if (block(array[i])) {
                return i
            }
        }
        return -1
    }

    inline fun filter(block: (Int) -> Boolean): IntList {
        val result = IntList(size)

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

    inline fun sorted(): IntList =
        clone().apply { sort() }

    inline fun sortedDescending(): IntList =
        clone().apply { sortDescending() }

    fun sortWith(comparator: (Int, Int) -> Int) {

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

    inline fun sortBy(crossinline selector: (Int) -> Int) {
        sortWith { a, b ->
            selector(a).compareTo(selector(b))
        }
    }

    fun sortedWith(comparator: (Int, Int) -> Int): IntList =
        clone().apply {
            sortWith(comparator)
        }

    inline fun sortedBy(crossinline selector: (Int) -> Int): IntList =
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

    inline fun shuffled(random: Random = Random): IntList =
        clone().apply {
            shuffle(random)
        }

    inline fun fill(value: Int) {
        for (i in 0 until size) {
            array[i] = value
        }
    }

    inline fun addFrom(source: IntList, index: Int) {
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