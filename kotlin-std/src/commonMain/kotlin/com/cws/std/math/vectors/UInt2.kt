package com.cws.std.math.vectors

import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES
import kotlin.math.sqrt

data class UInt2(
    var x: UInt = 0u,
    var y: UInt = 0u
) {

    companion object {
        val SIZE_BYTES = UInt.SIZE_BYTES * 2
        val STD140_SIZE_BYTES = UInt.STD140_SIZE_BYTES * 2
        val STD430_SIZE_BYTES = UInt.STD430_SIZE_BYTES * 2
    }

    operator fun get(i: UInt): UInt {
        return when (i) {
            0u -> x
            1u -> y
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    operator fun set(i: UInt, v: UInt) {
        return when (i) {
            0u -> x = v
            1u -> y = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }
    }

    val length: UInt get() {
        val x = x
        val y = y
        return sqrt((x * x + y * y).toDouble()).roundToUInt()
    }

    operator fun plus(v: UInt): UInt2 {
        return UInt2(x + v, y + v)
    }

    operator fun minus(v: UInt): UInt2 {
        return UInt2(x - v, y - v)
    }

    operator fun times(v: UInt): UInt2 {
        return UInt2(x * v, y * v)
    }

    operator fun div(v: UInt): UInt2 {
        return UInt2(x / v, y / v)
    }

    operator fun plus(v: UInt2): UInt2 {
        return UInt2(x + v.x, y + v.y)
    }

    operator fun minus(v: UInt2): UInt2 {
        return UInt2(x - v.x, y - v.y)
    }

    operator fun times(v: UInt2): UInt2 {
        return UInt2(x * v.x, y * v.y)
    }

    operator fun div(v: UInt2): UInt2 {
        return UInt2(x / v.x, y / v.y)
    }

    constructor(xy: Int2) : this(xy.x.toUInt(), xy.y.toUInt())

    val xx get() = UInt2(x, x);  val xy get() = UInt2(x, y)
    val yx get() = UInt2(y, x);  val yy get() = UInt2(y, y)

}
