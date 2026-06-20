package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import com.cws.std.math.vectors.Quaternion

fun dot(v1: Float2, v2: Float2): Float {
    return v1.x * v2.x + v1.y * v2.y
}

fun dot(v1: Float3, v2: Float3): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
}

fun dot(v1: Float4, v2: Float4): Float {
    return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w
}

fun dot(q1: Quaternion, q2: Quaternion): Float {
    return q1.x * q2.x + q1.y * q2.y + q1.z * q2.z + q1.w * q2.w
}