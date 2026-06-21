package com.cws.std.math.matrices

import com.cws.std.math.operators.inverse
import com.cws.std.math.operators.transpose
import com.cws.std.math.vectors.Float3

data class Mat3(
    var v1: Float3 = Float3(),
    var v2: Float3 = Float3(),
    var v3: Float3 = Float3(),
) {

    constructor(
        m00: Float,
        m01: Float,
        m02: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m20: Float,
        m21: Float,
        m22: Float
    ) : this() {
        v1.x = m00
        v1.y = m01
        v1.z = m02

        v2.x = m10
        v2.y = m11
        v2.z = m12

        v3.x = m20
        v3.y = m21
        v3.z = m22
    }

    operator fun get(i: Int): Float3 {
        return when (i) {
            0 -> v1
            1 -> v2
            2 -> v3
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    fun identity(): Mat3 {
        v1.x = 1f
        v1.y = 0f
        v1.z = 0f

        v2.x = 0f
        v2.y = 1f
        v2.z = 0f

        v3.x = 0f
        v3.y = 0f
        v3.z = 1f

        return this
    }

    fun transpose(): Mat3 = transpose(this, this)

    fun inverse(): Mat3 = inverse(this, this)

    operator fun minus(v: Float): Mat3 {
        return Mat3(v1 - v, v2 - v, v3 - v)
    }

    operator fun times(v: Float): Mat3 {
        return Mat3(v1 * v, v2 * v, v3 * v)
    }

    operator fun div(v: Float): Mat3 {
        return Mat3(v1 / v, v2 / v, v3 / v)
    }

    operator fun plus(m: Mat3): Mat3 {
        return Mat3(v1 + m.v1, v2 + m.v2, v3 + m.v3)
    }

    operator fun minus(m: Mat3): Mat3 {
        return Mat3(v1 - m.v1, v2 - m.v2, v3 - m.v3)
    }

    operator fun div(m: Mat3): Mat3 {
        return Mat3(v1 / m.v1, v2 / m.v2, v3 / m.v3)
    }

    operator fun times(m: Mat3): Mat3 {
        val m1 = this
        val m2 = m
        val m3 = Mat3()
        for (r in 0..2) {
            for (c in 0..2) {
                for (i in 0..2) {
                    m3[r][c] += m1[r][i] * m2[i][c]
                }
            }
        }
        return m3
    }

    operator fun unaryMinus(): Mat3 {
        val m1 = this
        val m2 = Mat3()
        for (r in 0..2) {
            for (c in 0..2) {
                m2[r][c] = -m1[r][c]
            }
        }
        return m2
    }

    operator fun times(v: Float3) = Float3(
        v1.x * v.x + v1.y * v.y + v1.z * v.z,
        v2.x * v.x + v2.y * v.y + v2.z * v.z,
        v3.x * v.x + v3.y * v.y + v3.z * v.z
    )

}