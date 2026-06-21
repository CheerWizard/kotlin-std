package com.cws.std.math.vectors

import com.cws.std.math.matrices.Mat3
import com.cws.std.math.operators.normalize
import kotlin.math.sqrt

data class Float3(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
) {

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    operator fun set(i: Int, v: Float) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    val length: Float get() {
        val x = x
        val y = y
        val z = z
        return sqrt(x * x + y * y + z * z)
    }

    fun normalize(): Float3 {
        return normalize(this, this)
    }

    operator fun plus(v: Float): Float3 {
        return Float3(x + v, y + v, z + v)
    }

    operator fun minus(v: Float): Float3 {
        return Float3(x - v, y - v, z - v)
    }

    operator fun times(v: Float): Float3 {
        return Float3(x * v, y * v, z * v)
    }

    operator fun div(v: Float): Float3 {
        return Float3(x / v, y / v, z / v)
    }

    operator fun plus(v: Float3): Float3 {
        return Float3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: Float3): Float3 {
        return Float3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(v: Float3): Float3 {
        return Float3(x * v.x, y * v.y, z * v.z)
    }

    operator fun div(v: Float3): Float3 {
        return Float3(x / v.x, y / v.y, z / v.z)
    }

    operator fun unaryMinus(): Float3 {
        return Float3(-x, -y, -z)
    }

    operator fun times(m: Mat3) = Float3(
        x * m.v1.x + y * m.v2.x + z * m.v3.x,
        x * m.v1.y + y * m.v2.y + z * m.v3.y,
        x * m.v1.z + y * m.v2.z + z * m.v3.z
    )

}
