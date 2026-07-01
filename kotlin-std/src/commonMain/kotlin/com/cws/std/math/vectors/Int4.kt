package com.cws.std.math.vectors

import com.cws.std.math.operators.sqrt

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

    constructor(v: Int)                    : this(v, v, v, v)
    constructor(xyz: Int3, w: Int)         : this(xyz.x, xyz.y, xyz.z, w)
    constructor(x: Int, yzw: Int3)         : this(x, yzw.x, yzw.y, yzw.z)
    constructor(xy: Int2, zw: Int2)        : this(xy.x, xy.y, zw.x, zw.y)
    constructor(xy: Int2, z: Int, w: Int)  : this(xy.x, xy.y, z, w)
    constructor(x: Int, y: Int, zw: Int2)  : this(x, y, zw.x, zw.y)
    constructor(x: Int, yz: Int2, w: Int)  : this(x, yz.x, yz.y, w)

    val xy  get() = Int2(x, y);   val xyz get() = Int3(x, y, z)
    val xyzw get() = Int4(x, y, z, w)

}