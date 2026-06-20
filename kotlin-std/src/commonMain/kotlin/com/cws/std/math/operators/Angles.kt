package com.cws.std.math.operators

import kotlin.jvm.JvmInline
import kotlin.math.PI

@JvmInline
value class Radians(val value: Float) {

    operator fun plus(v: Float): Float = value + v
    operator fun minus(v: Float): Float = value - v
    operator fun times(v: Float): Float = value * v
    operator fun div(v: Float): Float = value / v

}

@JvmInline
value class Degree(val value: Float) {

    operator fun plus(v: Float): Float = value + v
    operator fun minus(v: Float): Float = value - v
    operator fun times(v: Float): Float = value * v
    operator fun div(v: Float): Float = value / v

}

val Float.radians: Radians get() = Radians(this)
val Degree.radians: Radians get() = Radians((value * PI / 180.0).toFloat())

val Float.degree: Degree get() = Degree(this)
val Radians.degree: Degree get() = Degree((value * 180 / PI).toFloat())