/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.profiler

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
        job =
            scope.launch {
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
        function: () -> Unit,
    ) {
        trace(
            scope = scope,
            phase = phase,
            color = color,
            category = category,
            functionName = functionName,
            expectedDuration = 0.nanoseconds,
            function = function,
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
        function: () -> Unit,
    ) {
        trace(
            scope = scope,
            phase = phase,
            color = color,
            category = category,
            functionName = functionName,
            expectedDuration = expectedDuration,
            function = function,
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
