package com.cws.std.async

import com.cws.std.async.Task
import kotlinx.coroutines.DelicateCoroutinesApi
import org.w3c.dom.Worker
import kotlin.math.max

actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    private val task: Task,
) {

    private val worker = Worker("worker.js")

    init {
        worker.onmessage = { msg ->
            when (msg.data) {
                "start" -> task()
            }
        }

        if (start) {
            start()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    actual fun start() {
        worker.postMessage("start")
    }

    actual fun join() {
        worker.terminate()
    }

}

actual fun getCurrentThreadName(): String {
    // TODO can't really implement it right now!
    return "main"
}

actual fun getMaxThreadCount(): Int {
    val navigator = js("navigator")
    val cores = navigator?.hardwareConcurrency as? Int ?: 1
    return max(6, cores - 1)
}

actual fun getCurrentThreadId(): Int {
    // TODO can't really implement it right now!
    return 1
}

actual fun getCurrentProcessId(): Int {
    return js("self.__pid || (self.__pid = Math.floor(Math.random()*1e9))") as Int
}
