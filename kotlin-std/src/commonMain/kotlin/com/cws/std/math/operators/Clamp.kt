package com.cws.std.math.operators

fun clamp(a: UInt, b: UInt, x: UInt): UInt {
    return if (x < a) a
    else if (x > b) b
    else x
}

fun clamp(a: Int, b: Int, x: Int): Int {
    return if (x < a) a
    else if (x > b) b
    else x
}

fun clamp(a: Long, b: Long, x: Long): Long {
    return if (x < a) a
    else if (x > b) b
    else x
}

fun clamp(a: Float, b: Float, x: Float): Float {
    return if (x < a) a
    else if (x > b) b
    else x
}

fun clamp(a: Double, b: Double, x: Double): Double {
    return if (x < a) a
    else if (x > b) b
    else x
}