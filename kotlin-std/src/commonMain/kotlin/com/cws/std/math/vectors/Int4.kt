package com.cws.std.math.vectors

import com.cws.std.math.sqrt

data class Int4(
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0,
    var w: Int = 0,
) {

    operator fun get(i: Int): Int {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    operator fun set(i: Int, v: Int) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            3 -> w = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    val length: Int get() {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Int4 {
        return com.cws.std.math.operators.normalize(this, this)
    }

    operator fun plus(v: Int): Int4 {
        return Int4(x + v, y + v, z + v, w + v)
    }

    operator fun minus(v: Int): Int4 {
        return Int4(x - v, y - v, z - v, w - v)
    }

    operator fun times(v: Int): Int4 {
        return Int4(x * v, y * v, z * v, w * v)
    }

    operator fun div(v: Int): Int4 {
        return Int4(x / v, y / v, z / v, w / v)
    }

    operator fun plus(v: Int4): Int4 {
        return Int4(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: Int4): Int4 {
        return Int4(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(v: Int4): Int4 {
        return Int4(x * v.x, y * v.y, z * v.z, w * v.w)
    }

    operator fun div(v: Int4): Int4 {
        return Int4(x / v.x, y / v.y, z / v.z, w / v.w)
    }

    operator fun unaryMinus(): Int4 {
        return Int4(-x, -y, -z, -w)
    }

}