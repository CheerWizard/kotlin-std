package com.cws.std.math.operators

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4

fun inverse(m: Mat2, out: Mat2): Mat2 {
    val d = det(m)
    out[0][0] = m[1][1] / d
    out[0][1] =-m[1][0] / d
    out[1][0] =-m[0][1] / d
    out[1][1] = m[0][0] / d
    return out
}

fun inverse(m: Mat3, out: Mat3): Mat3 {
    val d = det(m)

    out[0][0] = det(
        m[1][1] / d, m[1][2] / d,
        m[2][1] / d, m[2][2] / d
    )

    out[0][1] = -det(
        m[1][0] / d, m[1][2] / d,
        m[2][0] / d, m[2][2] / d
    )

    out[0][2] = det(
        m[1][0] / d, m[1][1] / d,
        m[2][0] / d, m[2][1] / d
    )

    out[1][0] = -det(
        m[0][0] / d, m[0][1] / d,
        m[2][0] / d, m[2][1] / d
    )

    out[1][1] = det(
        m[0][0] / d, m[0][2] / d,
        m[2][0] / d, m[2][2] / d
    )

    out[1][2] = -det(
        m[0][0] / d, m[0][1] / d,
        m[2][0] / d, m[2][1] / d
    )

    out[2][0] = det(
        m[0][1] / d, m[0][2] / d,
        m[1][1] / d, m[1][2] / d
    )

    out[2][1] = -det(
        m[0][0] / d, m[0][2] / d,
        m[1][0] / d, m[1][2] / d
    )

    out[2][2] = det(
        m[0][0] / d, m[0][1] / d,
        m[1][0] / d, m[1][1] / d
    )

    return out
}

fun inverse(m: Mat4, out: Mat4): Mat4 {
    val d = det(m)

    out[0][0] = det(
        m[1][1] / d, m[1][2] / d, m[1][3] / d,
        m[2][1] / d, m[2][2] / d, m[2][3] / d,
        m[3][1] / d, m[3][2] / d, m[3][3] / d
    )

    out[0][1] = -det(
        m[1][0] / d, m[1][2] / d, m[1][3] / d,
        m[2][0] / d, m[2][2] / d, m[2][3] / d,
        m[3][0] / d, m[3][2] / d, m[3][3] / d
    )

    out[0][2] = det(
        m[1][0] / d, m[1][1] / d, m[1][3] / d,
        m[2][0] / d, m[2][1] / d, m[2][3] / d,
        m[3][0] / d, m[3][1] / d, m[3][3] / d
    )

    out[0][3] = -det(
        m[1][0] / d, m[1][1] / d, m[1][2] / d,
        m[2][0] / d, m[2][1] / d, m[2][2] / d,
        m[3][0] / d, m[3][1] / d, m[3][2] / d
    )

    out[1][0] = -det(
        m[0][1] / d, m[0][2] / d, m[0][3] / d,
        m[2][1] / d, m[2][2] / d, m[2][3] / d,
        m[3][1] / d, m[3][2] / d, m[3][3] / d
    )

    out[1][1] = det(
        m[0][0] / d, m[0][2] / d, m[0][3] / d,
        m[2][0] / d, m[2][2] / d, m[2][3] / d,
        m[3][0] / d, m[3][2] / d, m[3][3] / d
    )

    out[1][2] = -det(
        m[0][0] / d, m[0][1] / d, m[0][3] / d,
        m[2][0] / d, m[2][1] / d, m[2][3] / d,
        m[3][0] / d, m[3][1] / d, m[3][3] / d
    )

    out[1][3] = det(
        m[0][0] / d, m[0][1] / d, m[0][2] / d,
        m[2][0] / d, m[2][1] / d, m[2][2] / d,
        m[3][0] / d, m[3][1] / d, m[3][2] / d
    )

    out[2][0] = det(
        m[0][1] / d, m[0][2] / d, m[0][3] / d,
        m[1][1] / d, m[1][2] / d, m[1][3] / d,
        m[3][1] / d, m[3][2] / d, m[3][3] / d
    )

    out[2][1] = -det(
        m[0][0] / d, m[0][2] / d, m[0][3] / d,
        m[1][0] / d, m[1][2] / d, m[1][3] / d,
        m[3][0] / d, m[3][2] / d, m[3][3] / d
    )

    out[2][2] = det(
        m[0][0] / d, m[0][1] / d, m[0][3] / d,
        m[1][0] / d, m[1][1] / d, m[1][3] / d,
        m[3][0] / d, m[3][1] / d, m[3][3] / d
    )

    out[2][3] = -det(
        m[0][0] / d, m[0][1] / d, m[0][2] / d,
        m[1][0] / d, m[1][1] / d, m[1][2] / d,
        m[3][0] / d, m[3][1] / d, m[3][2] / d
    )

    out[3][0] = -det(
        m[0][1] / d, m[0][2] / d, m[0][3] / d,
        m[1][1] / d, m[1][2] / d, m[1][3] / d,
        m[2][1] / d, m[2][2] / d, m[2][3] / d
    )

    out[3][1] = det(
        m[0][0] / d, m[0][2] / d, m[0][3] / d,
        m[1][0] / d, m[1][2] / d, m[1][3] / d,
        m[2][0] / d, m[2][2] / d, m[2][3] / d
    )

    out[3][2] = -det(
        m[0][0] / d, m[0][1] / d, m[0][3] / d,
        m[1][0] / d, m[1][1] / d, m[1][3] / d,
        m[2][0] / d, m[2][1] / d, m[2][3] / d
    )

    out[3][3] = det(
        m[0][0] / d, m[0][1] / d, m[0][2] / d,
        m[1][0] / d, m[1][1] / d, m[1][2] / d,
        m[2][0] / d, m[2][1] / d, m[2][2] / d
    )

    return out
}
