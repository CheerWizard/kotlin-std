@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.async

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.js.ExperimentalWasmJsInterop

// General purpose thread is not possible to implement with JS worker, because we need exact context and purpose of job of this thread
actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    private val task: () -> Unit,
) {

    init {
        if (start) {
            start()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    actual fun start() {
        task()
    }

    actual fun join() {
        // no-op
    }

}
