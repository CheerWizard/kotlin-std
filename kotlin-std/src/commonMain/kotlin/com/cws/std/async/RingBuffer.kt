package com.cws.std.async

open class RingBuffer<T>(private val capacity: Int) {

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
