package com.cws.std.math.vectors

import com.cws.std.math.operators.normalize
import kotlin.math.sqrt

data class Float2(
    var x: Float = 0f,
    var y: Float = 0f
) {

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    operator fun set(i: Int, v: Float) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    val length: Float get() {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

    fun normalize(): Float2 {
        return normalize(this, this)
    }

    operator fun plus(v: Float): Float2 {
        return Float2(x + v, y + v)
    }

    operator fun minus(v: Float): Float2 {
        return Float2(x - v, y - v)
    }

    operator fun times(v: Float): Float2 {
        return Float2(x * v, y * v)
    }

    operator fun div(v: Float): Float2 {
        return Float2(x / v, y / v)
    }

    operator fun plus(v: Float2): Float2 {
        return Float2(x + v.x, y + v.y)
    }

    operator fun minus(v: Float2): Float2 {
        return Float2(x - v.x, y - v.y)
    }

    operator fun times(v: Float2): Float2 {
        return Float2(x * v.x, y * v.y)
    }

    operator fun div(v: Float2): Float2 {
        return Float2(x / v.x, y / v.y)
    }

    operator fun unaryMinus(): Float2 {
        return Float2(-x, -y)
    }

}
