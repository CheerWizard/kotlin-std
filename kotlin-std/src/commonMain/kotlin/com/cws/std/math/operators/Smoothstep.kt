package com.cws.std.math.operators

fun smoothstep(a: Int, b: Int, x: Int): Int {
    val t = clamp(0, 1, (x - a) / (b - a))
    val y = -2 * t * t * t + 3 * t * t
    return y
}

fun smoothstep(a: Long, b: Long, x: Long): Long {
    val t = clamp(0L, 1L, (x - a) / (b - a))
    val y = -2L * t * t * t + 3L * t * t
    return y
}

fun smoothstep(a: Float, b: Float, x: Float): Float {
    val t = clamp(0f, 1f, (x - a) / (b - a))
    val y = -2f * t * t * t + 3f * t * t
    return y
}

fun smoothstep(a: Double, b: Double, x: Double): Double {
    val t = clamp(0.0, 1.0, (x - a) / (b - a))
    val y = -2.0 * t * t * t + 3.0 * t * t
    return y
}