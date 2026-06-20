package com.cws.std.memory

import kotlin.math.min

class FreeBlocks(size: Int) {

    private val handles = MemoryHandleArray(size)
    private val sizes = IntArray(size)
    private var position = -1

    fun push(handle: MemoryHandle, size: Int) {
        position++
        handles[position] = handle
        sizes[position] = size
    }

    fun pop(size: Int): MemoryHandle {
        if (position < 0) return NULL
        var freeSize = 0
        while (freeSize < size) {
            freeSize = sizes[position]
            if (freeSize == size) {
                return handles[position--]
            } else if (freeSize > size) {
                val freeHandle: MemoryHandle = handles[position]
                handles[position] = freeHandle + size
                sizes[position] = freeSize - size
                return freeHandle
            }
        }
        return NULL
    }

}

object Heap : NativeBuffer(
    capacity = getMemorySize(10f),
) {

    val totalSize get() = capacity

    val freeSize get() = totalSize - usedSize

    var usedSize: Long = 0
        private set

    var allocations: Long = 0
        private set

    private val freeBlocks = FreeBlocks(100)

    fun allocate(size: Int): MemoryHandle {
        val freeHandle = freeBlocks.pop(size)
        if (freeHandle != NULL) {
            reset(freeHandle, size)
            return freeHandle
        }

        val handle = position
        val capacity = capacity
        if (handle == capacity) {
            resize(capacity * 2)
        }

        reset(handle, size)

        allocations++
        usedSize += size
        position = handle + size

        return handle
    }

    fun free(handle: MemoryHandle, size: Int) {
        if (handle == NULL) return
        freeBlocks.push(handle, size)
        usedSize -= size
    }

    fun reset(handle: MemoryHandle, size: Int) {
        setTo(0, handle, size)
    }

    private fun initCapacity(): Long {
        val capacity = (getMemoryInfo().totalPhysicalSize * 0.10f).toLong()
        return min(capacity, Int.MAX_VALUE.toLong())
    }

}