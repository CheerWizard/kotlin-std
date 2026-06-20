package com.cws.std.math.operators

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4

fun det(m: Mat2): Float {
    return det(
        m[0][0], m[0][1],
        m[1][0], m[1][1],
    )
}

fun det(
    m00: Float, m01: Float,
    m10: Float, m11: Float
): Float {
    return m00 * m11 - m10 * m01
}

fun det(m: Mat3): Float {
    return det(
        m[0][0], m[0][1], m[0][2],
        m[1][0], m[1][1], m[1][2],
        m[2][0], m[2][1], m[2][2],
    )
}

fun det(
    m00: Float, m01: Float, m02: Float,
    m10: Float, m11: Float, m12: Float,
    m20: Float, m21: Float, m22: Float,
): Float {
    return m00 * m11 * m22 + m01 * m12 * m20 + m02 * m10 * m21 - m20 * m11 * m02 - m21 * m12 * m00 - m22 * m10 * m01
}


fun det(m: Mat4): Float {
    return det(
        m[0][0], m[0][1], m[0][2], m[0][3],
        m[1][0], m[1][1], m[1][2], m[1][3],
        m[2][0], m[2][1], m[2][2], m[2][3],
        m[3][0], m[3][1], m[3][2], m[3][3],
    )
}

fun det(
    m00: Float, m01: Float, m02: Float, m03: Float,
    m10: Float, m11: Float, m12: Float, m13: Float,
    m20: Float, m21: Float, m22: Float, m23: Float,
    m30: Float, m31: Float, m32: Float, m33: Float,
): Float {
    return m00 * m11 * m22 * m33 + m01 * m12 * m23 * m30 + m02 * m13 * m20 * m31 - m30 * m21 * m12 * m03 - m31 * m22 * m13 * m00 - m32 * m23 * m10 * m01
}