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
package com.cws.std.math.operators

import kotlin.jvm.JvmInline
import kotlin.math.PI

@JvmInline
value class Radians(
    val value: Float,
) {
    operator fun plus(v: Float): Float = value + v

    operator fun minus(v: Float): Float = value - v

    operator fun times(v: Float): Float = value * v

    operator fun div(v: Float): Float = value / v
}

@JvmInline
value class Degree(
    val value: Float,
) {
    operator fun plus(v: Float): Float = value + v

    operator fun minus(v: Float): Float = value - v

    operator fun times(v: Float): Float = value * v

    operator fun div(v: Float): Float = value / v
}

val Float.radians: Radians get() = Radians(this)
val Degree.radians: Radians get() = Radians((value * PI / 180.0).toFloat())

val Float.degree: Degree get() = Degree(this)
val Radians.degree: Degree get() = Degree((value * 180 / PI).toFloat())
