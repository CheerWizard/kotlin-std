package com.cws.std.profiler

interface Profiler : AutoCloseable {
    override fun close()
    fun profile(
        scope: TraceScope,
        phase: TracePhase,
        color: TraceColor,
        category: String,
        functionName: String,
        startTime: Long,
        endTime: Long,
        duration: Long,
        expectedDuration: Long,
    )
}