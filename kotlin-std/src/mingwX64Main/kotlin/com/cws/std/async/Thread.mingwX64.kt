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
@file:OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
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
import kotlin.experimental.ExperimentalNativeApi

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
                pthread_setname_np(thread.thread.value, thread.name)
                thread.task()
                ref.dispose()
                null
            },
            thisRef.asCPointer(),
        )
    }

    actual fun join() {
        pthread_join(thread.value, null)
        running = false
    }
}
