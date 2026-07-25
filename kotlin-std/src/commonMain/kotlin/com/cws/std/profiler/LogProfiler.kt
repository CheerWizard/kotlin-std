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
