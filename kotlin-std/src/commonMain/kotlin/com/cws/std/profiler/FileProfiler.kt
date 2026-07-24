package com.cws.std.profiler

import com.cws.print.Print
import com.cws.print.getCurrentTimeMillis
import com.cws.std.io.File
import com.cws.std.io.write
import com.cws.std.platform.PlatformInfo
import com.cws.std.platform.fetchCurrentProcessId
import com.cws.std.platform.fetchCurrentThreadId
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.serialization.json.Json

class FileProfiler(private val filepath: String) : Profiler {

    companion object {
        private const val TAG = "FileProfiler"
    }

    private val traceEvents = TraceEvents(mutableListOf())
    private val lock = ReentrantLock()
    private val file = File(filepath)

    private var currentID: Int = 0

    override fun close() {
        lock.withLock {
            try {
                file.write(Json.encodeToString(traceEvents))
                traceEvents.events.clear()
            } catch (e: Exception) {
                Print.e(TAG, "Failed to save JSON trace events into $filepath", e)
            } finally {
                file.close()
            }
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
                    threadId = PlatformInfo.fetchCurrentThreadId(),
                    processId = PlatformInfo.fetchCurrentProcessId(),
                )
            )
        }
    }

}