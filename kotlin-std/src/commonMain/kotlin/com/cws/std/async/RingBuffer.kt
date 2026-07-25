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
package com.cws.std.async

open class RingBuffer<T>(
    private val capacity: Int,
) {
    private val buffer = arrayOfNulls<Any?>(capacity)
    private var head = 0
    private var tail = 0

    val size: Int
        get() {
            return if (head >= tail) head - tail else capacity - (tail - head)
        }

    val isEmpty: Boolean get() = size <= 0
    val isFull: Boolean get() = size >= capacity

    open fun push(item: T): Boolean {
        val next = (head + 1) % capacity
        if (next == tail) return false
        buffer[head] = item
        head = next
        return true
    }

    @Suppress("UNCHECKED_CAST")
    open fun pop(): T? {
        if (tail == head) return null
        val value = buffer[tail] as? T?
        buffer[tail] = null
        tail = (tail + 1) % capacity
        return value
    }

    @Suppress("UNCHECKED_CAST")
    operator fun get(index: Int): T? {
        val actualIndex = (tail + index) % capacity
        return buffer.getOrNull(actualIndex) as T?
    }

    fun removeFirst() {
        if (tail != head) {
            buffer[tail] = null
            tail = (tail + 1) % capacity
        }
    }

    fun removeLast() {
        if (tail != head) {
            head = if (head == 0) capacity - 1 else head - 1
            buffer[head] = null
        }
    }
}
