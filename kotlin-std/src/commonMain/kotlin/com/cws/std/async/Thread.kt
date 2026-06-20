package com.cws.std.async

expect open class Thread(
    start: Boolean = false,
    name: String,
    priority: Int,
    task: Task,
) {
    val name: String
    val priority: Int

    fun start()
    fun join()
}

expect fun getCurrentThreadName(): String
expect fun getMaxThreadCount(): Int
expect fun getCurrentThreadId(): Int
expect fun getCurrentProcessId(): Int