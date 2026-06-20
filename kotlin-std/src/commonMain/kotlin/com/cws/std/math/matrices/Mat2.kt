package com.cws.std.math.matrices

import com.cws.std.math.operators.inverse
import com.cws.std.math.operators.transpose
import com.cws.std.math.vectors.Float2

data class Mat2(
    var v1: Float2 = Float2(),
    var v2: Float2 = Float2(),
) {

    operator fun get(i: Int): Float2 {
        return when (i) {
            0 -> v1
            1 -> v2
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

}

fun Mat2.identity(): Mat2 {
    v1.x = 1f
    v1.y = 0f
    v2.x = 0f
    v2.y = 1f
    return this
}

fun Mat2.transpose(): Mat2 = transpose(this, this)

fun Mat2.inverse(): Mat2 = inverse(this, this)

operator fun Mat2.minus(v: Float): Mat2 {
    return Mat2(v1 - v, v2 - v)
}

operator fun Mat2.times(v: Float): Mat2 {
    return Mat2(v1 * v, v2 * v)
}

operator fun Mat2.div(v: Float): Mat2 {
    return Mat2(v1 / v, v2 / v)
}

operator fun Mat2.plus(m: Mat2): Mat2 {
    return Mat2(v1 + m.v1, v2 + m.v2)
}

operator fun Mat2.minus(m: Mat2): Mat2 {
    return Mat2(v1 - m.v1, v2 - m.v2)
}

operator fun Mat2.div(m: Mat2): Mat2 {
    return Mat2(v1 / m.v1, v2 / m.v2)
}

operator fun Mat2.times(m: Mat2): Mat2 {
    val m1 = this@times
    val m2 = m
    val m3 = Mat2()
    for (r in 0..1) {
        for (c in 0..1) {
            for (i in 0..1) {
                m3[r][c] += m1[r][i] * m2[i][c]
            }
        }
    }
    return m3
}

operator fun Mat2.unaryMinus(): Mat2 {
    val m1 = this@unaryMinus
    val m2 = Mat2()
    for (r in 0..1) {
        for (c in 0..1) {
            m2[r][c] = -m1[r][c]
        }
    }
    return m2
}