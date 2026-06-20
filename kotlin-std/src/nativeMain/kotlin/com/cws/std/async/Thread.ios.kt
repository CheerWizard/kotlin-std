@file:OptIn(ExperimentalForeignApi::class)

package com.cws.std.async

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import platform.posix.getpid
import platform.posix.pthread_create
import platform.posix.pthread_getname_np
import platform.posix.pthread_join
import platform.posix.pthread_self
import platform.posix.pthread_setname_np
import platform.posix.pthread_tVar
import platform.posix.pthread_threadid_np
import platform.posix.syscall
import kotlin.experimental.ExperimentalNativeApi

actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    private val task: Task,
) {

    private val thread = nativeHeap.alloc<pthread_tVar>()

    init {
        if (start) {
            start()
        }
    }

    actual fun start() {
        // create reference to Kotlin task function
        val stableRef = StableRef.create(task)
        // provide it to C function to be called from C
        pthread_create(
            thread.ptr,
            null,
            staticCFunction { arg ->
                val ref = arg!!.asStableRef<Task>()
                val task = ref.get()
                pthread_setname_np(task.name)
                task.action()
                ref.dispose()
                null
            },
            stableRef.asCPointer()
        )
    }

    actual fun join() {
        pthread_join(thread.value, null)
    }

}

actual fun getCurrentThreadName(): String {
    val buffer = ByteArray(256)
    pthread_getname_np(pthread_self(), buffer.refTo(0), buffer.size.toULong())
    return buffer.decodeToString().trimEnd('\u0000')
}

@OptIn(ExperimentalNativeApi::class)
actual fun getMaxThreadCount(): Int {
    return maxOf(1, Platform.getAvailableProcessors() - 1)
}

actual fun getCurrentThreadId(): Int {
    return memScoped {
        val tid = alloc<ULongVar>()
        pthread_threadid_np(pthread_self(), tid.ptr)
        tid.value.toInt()
    }
}

actual fun getCurrentProcessId(): Int {
    return getpid()
}