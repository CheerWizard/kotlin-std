package com.cws.std.math.operators

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4

fun transpose(m: Mat2, out: Mat2): Mat2 {
    out.swap(0, 1, 1, 0)
    return out
}

fun transpose(m: Mat2): Mat2 = transpose(m, m.copy())

fun transpose(m: Mat3, out: Mat3): Mat3 {
    out.swap(0, 1, 1, 0)
    out.swap(0, 2, 2, 0)
    out.swap(1, 2, 2, 1)
    return out
}

fun transpose(m: Mat3): Mat3 = transpose(m, m.copy())

fun transpose(m: Mat4, out: Mat4): Mat4 {
    out.swap(0, 1, 1, 0)
    out.swap(0, 2, 2, 0)
    out.swap(0, 3, 3, 0)

    out.swap(1, 2, 2, 1)
    out.swap(1, 3, 3, 1)

    out.swap(2, 3, 3, 2)
    return out
}

fun transpose(m: Mat4): Mat4 = transpose(m, m.copy())