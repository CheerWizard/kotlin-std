package com.cws.std.math.operators

fun step(edge: UInt, x: UInt): UInt {
    return if (x < edge) 0u else 1u
}

fun step(edge: Int, x: Int): Int {
    return if (x < edge) 0 else 1
}

fun step(edge: Long, x: Long): Long {
    return if (x < edge) 0L else 1L
}

fun step(edge: Float, x: Float): Float {
    return if (x < edge) 0f else 1f
}

fun step(edge: Double, x: Double): Double {
    return if (x < edge) 0.0 else 1.0
}