@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.async

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.js

external interface JsMemoryInfo : JsAny {
    val usedJSHeapSize: Double
    val totalJSHeapSize: Double
    val jsHeapSizeLimit: Double
}

external interface JsPerformance : JsAny {
    val memory: JsMemoryInfo
}

fun JsPerformance(): JsPerformance = js("performance")