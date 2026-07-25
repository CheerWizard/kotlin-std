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
@file:OptIn(ExperimentalWasmJsInterop::class)
package com.cws.std.async

import org.w3c.dom.MessageEvent
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

external class JsWorker(
    scriptURL: String,
) : JsAny {
    // message here could be a Task name
    fun postMessage(message: String)

    fun terminate()

    var onmessage: ((MessageEvent) -> Unit)?
    var onerror: ((JsAny) -> Unit)?
}

external interface JsMessageEvent : JsAny {
    val data: JsAny
}
