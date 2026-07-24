@file:OptIn(ExperimentalWasmJsInterop::class)

package com.cws.std.async

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.js

external interface JsNavigator : JsAny {
    val hardwareConcurrency: Int
    val deviceMemory: Int
}

fun JsNavigator(): JsNavigator = js("navigator")
