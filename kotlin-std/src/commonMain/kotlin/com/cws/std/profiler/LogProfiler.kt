package com.cws.std.profiler

import com.cws.print.Print

class LogProfiler : Profiler {

    companion object {
        private const val TAG = "LogProfiler"
    }

    override fun close() {
        // no-op
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
        if (duration > expectedDuration && expectedDuration != 0L) {
            Print.w(TAG, "Scope=$scope Phase=$phase Function=$functionName() - spent $duration ns, expected $expectedDuration ns")
        } else {
            Print.d(TAG, "Scope=$scope Phase=$phase Function=$functionName() - spent $duration ns")
        }
    }

}