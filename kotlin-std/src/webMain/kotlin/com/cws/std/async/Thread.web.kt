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
package com.cws.std.async

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.js.ExperimentalWasmJsInterop

// General purpose thread is not possible to implement with JS worker, because we need exact context and purpose of job of this thread
actual open class Thread actual constructor(
    start: Boolean,
    actual val name: String,
    actual val priority: Int,
    private val task: () -> Unit,
) {
    init {
        if (start) {
            start()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    actual fun start() {
        task()
    }

    actual fun join() {
        // no-op
    }
}
