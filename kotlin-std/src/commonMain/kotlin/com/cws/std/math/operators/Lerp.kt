package com.cws.std.math.operators

fun lerp(a: UInt, b: UInt, x: UInt): UInt {
    return x * (b - a) + a
}

fun lerp(a: Int, b: Int, x: Int): Int {
    return x * (b - a) + a
}

fun lerp(a: Long, b: Long, x: Long): Long {
    return x * (b - a) + a
}

fun lerp(a: Float, b: Float, x: Float): Float {
    return x * (b - a) + a
}

fun lerp(a: Double, b: Double, x: Double): Double {
    return x * (b - a) + a
}