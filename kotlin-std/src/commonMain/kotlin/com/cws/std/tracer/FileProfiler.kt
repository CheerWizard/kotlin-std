package com.cws.std.tracer

import com.cws.print.getCurrentTimeMillis
import com.cws.std.async.getCurrentProcessId
import com.cws.std.async.getCurrentThreadId
import com.cws.std.io.File
import com.cws.std.io.write
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration

class FileProfiler(filepath: String) : Profiler {

    private val traceEvents = TraceEvents(mutableListOf())
    private val lock = ReentrantLock()
    private val file = File(filepath)

    private var currentID: Int = 0

    override fun open() {
        lock.withLock {
            file.open()
        }
    }

    override fun close() {
        lock.withLock {
            file.write(Json.encodeToString(traceEvents))
            file.close()
        }
    }

    override fun profile(
        scope: TraceScope,
        phase: TracePhase,
        color: TraceColor,
        category: String,
        functionName: String,
        startTime: Long,
        endTime: Long,
        duration: Long,
        expectedDuration: Long,
    ) {
        lock.withLock {
            val currentTime = getCurrentTimeMillis()
            traceEvents.events.add(
                TraceEvent(
                    id = currentID++,
                    category = category,
                    name = functionName,
                    durationNanos = duration,
                    timestamp = currentTime,
                    scope = scope.value,
                    phase = phase.value,
                    color = color.value,
                    threadId = getCurrentThreadId(),
                    processId = getCurrentProcessId(),
                )
            )
        }
    }

}