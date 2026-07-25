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
package com.cws.std.memory

import com.cws.std.platform.PlatformInfo
import com.cws.std.platform.fetchCurrentThreadName

class Stack(
    private val handle: MemoryHandle,
    private val capacity: Int,
) {
    companion object {
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
    private val stacks =
        Array(PlatformInfo.maxThreadCount) {
            Stack(
                handle = Heap.allocate(Stack.SIZE_BYTES),
                capacity = Stack.SIZE_BYTES,
            )
        }

    fun getStack(): Stack = stacks[getIndex()]

    private fun getIndex(): Int = (PlatformInfo.fetchCurrentThreadName().hashCode() and Int.MAX_VALUE) % stacks.size
}

inline fun stackScope(block: Stack.() -> Unit) {
    val stack = StackManager.getStack()
    val begin = stack.position
    block(stack)
    val end = stack.position
    stack.pop(end - begin)
}

inline fun <R> stackPush(block: Stack.() -> R): R = block(StackManager.getStack())
