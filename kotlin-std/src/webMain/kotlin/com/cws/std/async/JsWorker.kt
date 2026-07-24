@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.async

import org.w3c.dom.MessageEvent
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny

external class JsWorker(scriptURL: String) : JsAny {
    // message here could be a Task name
    fun postMessage(message: String)
    fun terminate()
    var onmessage: ((MessageEvent) -> Unit)?
    var onerror: ((JsAny) -> Unit)?
}

external interface JsMessageEvent : JsAny {
    val data: JsAny
}
