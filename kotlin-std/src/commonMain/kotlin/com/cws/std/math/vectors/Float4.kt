package com.cws.std.math.vectors

import com.cws.std.math.matrices.Mat4
import kotlin.math.sqrt

data class Float4(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f,
) {

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    operator fun set(i: Int, v: Float) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            3 -> w = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    val length: Float get() {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Float4 {
        return com.cws.std.math.operators.normalize(this, this)
    }

    operator fun plus(v: Float): Float4 {
        return Float4(x + v, y + v, z + v, w + v)
    }

    operator fun minus(v: Float): Float4 {
        return Float4(x - v, y - v, z - v, w - v)
    }

    operator fun times(v: Float): Float4 {
        return Float4(x * v, y * v, z * v, w * v)
    }

    operator fun div(v: Float): Float4 {
        return Float4(x / v, y / v, z / v, w / v)
    }

    operator fun plus(v: Float4): Float4 {
        return Float4(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: Float4): Float4 {
        return Float4(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(v: Float4): Float4 {
        return Float4(x * v.x, y * v.y, z * v.z, w * v.w)
    }

    operator fun div(v: Float4): Float4 {
        return Float4(x / v.x, y / v.y, z / v.z, w / v.w)
    }

    operator fun unaryMinus(): Float4 {
        return Float4(-x, -y, -z, -w)
    }

    operator fun times(m: Mat4) = Float4(
        x * m.v1.x + y * m.v2.x + z * m.v3.x + w * m.v4.x,
        x * m.v1.y + y * m.v2.y + z * m.v3.y + w * m.v4.y,
        x * m.v1.z + y * m.v2.z + z * m.v3.z + w * m.v4.z,
        x * m.v1.w + y * m.v2.w + z * m.v3.w + w * m.v4.w
    )

}