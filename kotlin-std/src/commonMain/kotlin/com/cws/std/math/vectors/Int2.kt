package com.cws.std.math.vectors

import com.cws.std.math.operators.sqrt
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES

data class Int2(
    var x: Int = 0,
    var y: Int = 0
) {

    companion object {
        val SIZE_BYTES = Int.SIZE_BYTES * 2
        val STD140_SIZE_BYTES = Int.STD140_SIZE_BYTES * 2
        val STD430_SIZE_BYTES = Int.STD430_SIZE_BYTES * 2
    }

    operator fun get(i: Int): Int {
        return when (i) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    operator fun set(i: Int, v: Int) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    val length: Int get() {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

    operator fun plus(v: Int): Int2 {
        return Int2(x + v, y + v)
    }

    operator fun minus(v: Int): Int2 {
        return Int2(x - v, y - v)
    }

    operator fun times(v: Int): Int2 {
        return Int2(x * v, y * v)
    }

    operator fun div(v: Int): Int2 {
        return Int2(x / v, y / v)
    }

    operator fun plus(v: Int2): Int2 {
        return Int2(x + v.x, y + v.y)
    }

    operator fun minus(v: Int2): Int2 {
        return Int2(x - v.x, y - v.y)
    }

    operator fun times(v: Int2): Int2 {
        return Int2(x * v.x, y * v.y)
    }

    operator fun div(v: Int2): Int2 {
        return Int2(x / v.x, y / v.y)
    }

    operator fun unaryMinus(): Int2 {
        return Int2(-x, -y)
    }

    val xx get() = Int2(x, x);  val xy get() = Int2(x, y)
    val yx get() = Int2(y, x);  val yy get() = Int2(y, y)
    val xyz get() = Int3(x, y, 0)

}
