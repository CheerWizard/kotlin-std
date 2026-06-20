package com.cws.std.memory

import com.cws.std.async.getCurrentThreadName
import com.cws.std.async.getMaxThreadCount

class Stack(
    private val handle: MemoryHandle,
    private val capacity: Int
) {

    companion object Companion {
        const val SIZE_BYTES = 64 * 1024
    }

    var position: Int = 0
        private set

    fun push(size: Int): MemoryHandle {
        if (position == capacity) {
            throw RuntimeException("Stack overflow error, size = $size bytes")
        }
        position += size
        return handle + position
    }

    fun pop(size: Int) {
        if (position < size) {
            throw RuntimeException("Stack underflow error, size = $size bytes")
        } else {
            position -= size
        }
    }

    fun reset() {
        Heap.reset(handle, capacity)
    }

}

object StackManager {

    private val stacks = Array(getMaxThreadCount()) {
        Stack(
            handle = Heap.allocate(Stack.SIZE_BYTES),
            capacity = Stack.SIZE_BYTES
        )
    }

    fun getStack(): Stack = stacks[getIndex()]

    private fun getIndex(): Int {
        return (getCurrentThreadName().hashCode() and Int.MAX_VALUE) % stacks.size
    }

}

inline fun stackScope(block: Stack.() -> Unit) {
    val stack = StackManager.getStack()
    val begin = stack.position
    block(stack)
    val end = stack.position
    stack.pop(end - begin)
}

inline fun <R> stackPush(block: Stack.() -> R): R {
    return block(StackManager.getStack())
}