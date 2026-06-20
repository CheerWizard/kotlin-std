package com.cws.std.math.operators

import com.cws.std.math.vectors.Quaternion
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import com.cws.std.math.vectors.Int2
import com.cws.std.math.vectors.Int3
import com.cws.std.math.vectors.Int4
import com.cws.std.math.vectors.UInt2
import com.cws.std.math.vectors.UInt3
import com.cws.std.math.vectors.UInt4

fun normalize(v: Float2, out: Float2): Float2 {
    val l = v.length
    if (l == 0f) return out
    out.x = v.x / l
    out.y = v.y / l
    return out
}

fun normalize(v: Int2, out: Int2): Int2 {
    val l = v.length
    if (l == 0) return out
    out.x = v.x / l
    out.y = v.y / l
    return out
}

fun normalize(v: UInt2, out: UInt2): UInt2 {
    val l = v.length
    if (l == 0u) return out
    out.x = v.x / l
    out.y = v.y / l
    return out
}

fun normalize(v: Float3, out: Float3): Float3 {
    val l = v.length
    if (l == 0f) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    return out
}

fun normalize(v: Int3, out: Int3): Int3 {
    val l = v.length
    if (l == 0) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    return out
}

fun normalize(v: UInt3, out: UInt3): UInt3 {
    val l = v.length
    if (l == 0u) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    return out
}

fun normalize(v: Float4, out: Float4): Float4 {
    val l = v.length
    if (l == 0f) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    out.w = v.w / l
    return out
}

fun normalize(v: Int4, out: Int4): Int4 {
    val l = v.length
    if (l == 0) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    out.w = v.w / l
    return out
}

fun normalize(v: UInt4, out: UInt4): UInt4 {
    val l = v.length
    if (l == 0u) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    out.w = v.w / l
    return out
}

fun normalize(v: Quaternion, out: Quaternion): Quaternion {
    val l = v.length
    if (l == 0f) return out
    out.x = v.x / l
    out.y = v.y / l
    out.z = v.z / l
    return out
}
