package com.cws.std.async

expect open class Thread(
    start: Boolean = false,
    name: String,
    priority: Int,
    task: () -> Unit,
) {
    val name: String
    val priority: Int

    fun start()
    fun join()
}