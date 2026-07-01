package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

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

fun clamp(v: Float2, min: Float, max: Float) = Float2(clamp(min, max, v.x), clamp(min, max, v.y))
fun clamp(v: Float3, min: Float, max: Float) = Float3(clamp(min, max, v.x), clamp(min, max, v.y), clamp(min, max, v.z))
fun clamp(v: Float4, min: Float, max: Float) = Float4(clamp(min, max, v.x), clamp(min, max, v.y), clamp(min, max, v.z), clamp(min, max, v.w))

fun clamp(v: Float2, min: Float2, max: Float2) = Float2(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y))
fun clamp(v: Float3, min: Float3, max: Float3) = Float3(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y), clamp(min.z, max.z, v.z))
fun clamp(v: Float4, min: Float4, max: Float4) = Float4(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y), clamp(min.z, max.z, v.z), clamp(min.w, max.w, v.w))