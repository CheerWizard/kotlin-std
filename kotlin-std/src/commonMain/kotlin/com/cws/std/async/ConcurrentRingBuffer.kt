package com.cws.std.async

import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock

class ConcurrentRingBuffer<T>(capacity: Int) : RingBuffer<T>(capacity) {

    private val lock = ReentrantLock()

    override fun push(item: T): Boolean {
        return lock.withLock {
            super.push(item)
        }
    }

    override fun pop(): T? {
        return lock.withLock {
            super.pop()
        }
    }

}