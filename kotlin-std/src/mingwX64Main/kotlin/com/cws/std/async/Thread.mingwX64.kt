@file:OptIn(ExperimentalForeignApi::class)

package com.cws.std.async

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.alloc
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import platform.posix.pthread_create
import platform.posix.pthread_join
import platform.posix.pthread_setname_np
import platform.posix.pthread_tVar

actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    private val task: () -> Unit,
) {

    private val thread = nativeHeap.alloc<pthread_tVar>()
    private var running = false

    init {
        if (start) {
            start()
        }
    }

    actual fun start() {
        if (running) return
        running = true
        val thisRef = StableRef.create(this)
        pthread_create(
            thread.ptr,
            null,
            staticCFunction { arg ->
                val ref = arg!!.asStableRef<Thread>()
                val thread = ref.get()
                pthread_setname_np(this@Thread.thread.value, thread.name)
                thread.task()
                ref.dispose()
                null
            },
            thisRef.asCPointer()
        )
    }

    actual fun join() {
        pthread_join(thread.value, null)
        running = false
    }

}