package com.cws.std.async

class ThreadPool(
    name: String,
    priority: Int,
    size: Int,
    private var start: Boolean = false,
) {

    private var running = false
    private val tasks = ConcurrentRingBuffer<Task>(100)
    private val threads: Array<Thread> = Array(size) {
        Thread(
            name = "$name-1",
            priority = priority,
            task = Task("$name-1", ::runLoop),
            start = start,
        )
    }

    fun start() {
        if (!start) {
            start = true
            threads.forEach { thread ->
                thread.start()
            }
        }
    }

    fun release() {
        start = false
        running = false
    }

    fun submit(task: Task) {
        tasks.push(task)
    }

    private fun runLoop() {
        running = true
        while (running) {
            val task = tasks.pop()
            task?.action?.invoke()
        }
    }

}