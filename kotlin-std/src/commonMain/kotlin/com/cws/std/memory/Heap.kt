package com.cws.std.memory

import com.cws.std.platform.PlatformInfo
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

object Heap {

    private val buffer = NativeBuffer(PlatformInfo.getMemorySize(10f))

    val totalSize get() = buffer.limit

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

        val handle = buffer.position
        val capacity = buffer.limit
        if (handle == capacity) {
            buffer.resize(capacity * 2)
        }

        reset(handle, size)

        allocations++
        usedSize += size
//        buffer.position = handle + size

        return handle
    }

    fun free(handle: MemoryHandle, size: Int) {
        if (handle == NULL) return
        freeBlocks.push(handle, size)
        usedSize -= size
    }

    fun reset(handle: MemoryHandle, size: Int) {
        buffer.setTo(0, handle, size)
    }

    private fun initCapacity(): Long {
        val capacity = (PlatformInfo.memoryInfo.totalPhysicalSize * 0.10f).toLong()
        return min(capacity, Int.MAX_VALUE.toLong())
    }

}