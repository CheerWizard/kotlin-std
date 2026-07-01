package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun refract(i: Float2, n: Float2, eta: Float): Float2 {
    val d = dot(n, i)
    val k = 1f - eta * eta * (1f - d * d)
    return if (k < 0f) Float2(0f) else i * eta - n * (eta * d + kotlin.math.sqrt(k))
}

fun refract(i: Float3, n: Float3, eta: Float): Float3 {
    val d = dot(n, i)
    val k = 1f - eta * eta * (1f - d * d)
    return if (k < 0f) Float3(0f) else i * eta - n * (eta * d + kotlin.math.sqrt(k))
}

fun refract(i: Float4, n: Float4, eta: Float): Float4 {
    val d = dot(n, i)
    val k = 1f - eta * eta * (1f - d * d)
    return if (k < 0f) Float4(0f) else i * eta - n * (eta * d + kotlin.math.sqrt(k))
}