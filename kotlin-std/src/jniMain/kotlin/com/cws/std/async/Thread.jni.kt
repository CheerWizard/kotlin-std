package com.cws.std.async

import kotlin.concurrent.thread
import java.lang.Thread

actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    task: () -> Unit,
) {

    private val thread = thread(
        start = start,
        name = name,
        priority = priority,
        block = task,
    )

    actual fun start() {
        thread.start()
    }

    actual fun join() {
        thread.join()
    }
}