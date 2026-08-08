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

inline fun FloatList(
    capacity: Int = 16,
    noinline init: (Int) -> Float = { 0f }
) = FloatList(capacity, FloatArray(capacity, init))

@NativeData
class FloatList(
    var size: Int = 0,
    array: FloatArray,
) {

    var array: FloatArray = array

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

    inline fun first(): Float {
        return array[0]
    }

    inline fun last(): Float {
        return array[size - 1]
    }

    inline operator fun get(index: Int): Float {
        return array[index]
    }

    inline operator fun set(index: Int, value: Float) {
        array[index] = value
    }

    inline fun add(value: Float) {
        ensureCapacity(size + 1)
        addUnsafe(value)
    }

    inline fun addUnsafe(value: Float) {
        array[size++] = value
    }

    inline fun addAll(values: FloatArray, start: Int = 0, end: Int = values.size) {
        ensureCapacity(size + abs(end - start))
        addAllUnsafe(values, start, end)
    }

    inline fun addAllUnsafe(values: FloatArray, start: Int = 0, end: Int = values.size) {
        val valuesSize = abs(end - start)
        values.copyInto(
            destination = array,
            destinationOffset = size,
            startIndex = start,
            endIndex = end,
        )
        size += valuesSize
    }

    inline fun addAll(values: FloatList) = addAll(values.array, 0, values.size)

    inline fun push(value: Float) = add(value)

    inline fun pop(): Float {
        return array[--size]
    }

    inline fun removeLast(): Float = pop()

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

    inline fun removeAtSwap(index: Int): Float {
        val removed = array[index]
        val last = --size

        if (index != last) {
            array[index] = array[last]
        }

        return removed
    }

    inline fun clone(): FloatList {
        val copy = FloatList(size, array.copyOf())
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

    inline fun sort() {
        array.sort(0, size)
    }

    inline fun sortDescending() {
        array.sortDescending(0, size)
    }

    inline fun sorted(): FloatList =
        clone().apply { sort() }

    inline fun sortedDescending(): FloatList =
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

    inline fun shuffle(random: Random = Random) {
        for (i in lastIndex downTo 1) {
            val j = random.nextInt(i + 1)

            val tmp = array[i]
            array[i] = array[j]
            array[j] = tmp
        }
    }

    inline fun shuffled(random: Random = Random): FloatList =
        clone().apply {
            shuffle(random)
        }

    inline fun fill(value: Float) {
        for (i in 0 until size) {
            array[i] = value
        }
    }

    inline fun addFrom(source: FloatList, index: Int) {
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