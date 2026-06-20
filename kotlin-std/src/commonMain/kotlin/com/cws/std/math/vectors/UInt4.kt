package com.cws.std.math.vectors

import com.cws.std.math.operators.normalize
import com.cws.std.math.sqrt

data class UInt4(
    var x: UInt = 0u,
    var y: UInt = 0u,
    var z: UInt = 0u,
    var w: UInt = 0u,
) {

    operator fun get(i: Int): UInt {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    operator fun set(i: Int, v: UInt) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            3 -> w = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    val length: UInt get() {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): UInt4 {
        return normalize(this, this)
    }

    operator fun plus(v: UInt): UInt4 {
        return UInt4(x + v, y + v, z + v, w + v)
    }

    operator fun minus(v: UInt): UInt4 {
        return UInt4(x - v, y - v, z - v, w - v)
    }

    operator fun times(v: UInt): UInt4 {
        return UInt4(x * v, y * v, z * v, w * v)
    }

    operator fun div(v: UInt): UInt4 {
        return UInt4(x / v, y / v, z / v, w / v)
    }

    operator fun plus(v: UInt4): UInt4 {
        return UInt4(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: UInt4): UInt4 {
        return UInt4(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(v: UInt4): UInt4 {
        return UInt4(x * v.x, y * v.y, z * v.z, w * v.w)
    }

    operator fun div(v: UInt4): UInt4 {
        return UInt4(x / v.x, y / v.y, z / v.z, w / v.w)
    }

}