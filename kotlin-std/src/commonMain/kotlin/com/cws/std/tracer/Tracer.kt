package com.cws.std.tracer

import com.cws.print.getCurrentTimeMillis
import com.cws.print.getCurrentTimeNanos
import kotlinx.atomicfu.locks.ReentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.ExperimentalTime

object Tracer {

    const val TAG = "Tracer"

    var enabled: Boolean = false

    var active = false
        private set

    val profilers = mutableSetOf<Profiler>()

    private val lock = ReentrantLock()

    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    fun init(
        enabled: Boolean = false,
        profilers: List<Profiler> = emptyList(),
    ) {
        this.enabled = enabled
        this.profilers.clear()
        this.profilers.addAll(profilers)
    }

    fun close() {
        profilers.forEach { profiler ->
            profiler.close()
        }
        profilers.clear()
    }

    fun begin() {
        if (active) return
        lock.withLock {
            active = true
            this.profilers.forEach { profiler ->
                profiler.open()
            }
        }
    }

    fun end() {
        if (!active) return
        lock.withLock {
            active = false
            this.profilers.forEach { profiler ->
                profiler.close()
            }
        }
    }

    fun launchFor(duration: Duration) {
        if (job?.isActive == true) return
        job = scope.launch {
            begin()
            delay(duration)
            end()
        }
    }

    @OptIn(ExperimentalTime::class)
    inline fun trace(
        scope: TraceScope = TraceScope.THREAD,
        phase: TracePhase = TracePhase.COMPLETE,
        color: TraceColor = TraceColor.HIGHLIGHT,
        category: String,
        functionName: String,
        function: () -> Unit
    ) {
        trace(
            scope = scope,
            phase = phase,
            color = color,
            category = category,
            functionName = functionName,
            expectedDuration = 0.nanoseconds,
            function = function
        )
    }

    @OptIn(ExperimentalTime::class)
    inline fun traceExpect(
        scope: TraceScope = TraceScope.THREAD,
        phase: TracePhase = TracePhase.COMPLETE,
        color: TraceColor = TraceColor.HIGHLIGHT,
        category: String,
        functionName: String,
        expectedDuration: Duration,
        function: () -> Unit
    ) {
        trace(
            scope = scope,
            phase = phase,
            color = color,
            category = category,
            functionName = functionName,
            expectedDuration = expectedDuration,
            function = function
        )
    }

    @OptIn(ExperimentalTime::class)
    inline fun trace(
        scope: TraceScope = TraceScope.THREAD,
        phase: TracePhase = TracePhase.COMPLETE,
        color: TraceColor = TraceColor.HIGHLIGHT,
        category: String,
        functionName: String,
        expectedDuration: Duration,
        function: () -> Unit,
    ) {
        if (!enabled || !active) {
            function()
            return
        }

        val startTime = getCurrentTimeNanos()
        val endTime: Long

        try {
            function()
        } finally {
            endTime = getCurrentTimeNanos()
        }

        val duration = endTime - startTime

        profilers.forEach { profiler ->
            profiler.profile(
                scope = scope,
                phase = phase,
                color = color,
                category = category,
                functionName = functionName,
                startTime = startTime,
                endTime = endTime,
                duration = duration,
                expectedDuration = expectedDuration.inWholeNanoseconds,
            )
        }
    }

}
