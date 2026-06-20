package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun cross(v1: Float2, v2: Float2): Float {
    return v1.x * v2.y - v1.y * v2.x
}

fun cross(v1: Float3, v2: Float3): Float3 {
    return Float3(
        v1.y * v2.z - v1.z * v2.y,
        v1.z * v2.x - v1.x * v2.z,
        v1.x * v2.y - v1.y * v2.x
    )
}

fun cross(v1: Float4, v2: Float4): Float4 {
    val x = v1.y * v2.z - v1.z * v2.y
    val y = v1.z * v2.x - v1.x * v2.z
    val z = v1.x * v2.y - v1.y * v2.x
    return Float4(x, y, z, 0f)
}
