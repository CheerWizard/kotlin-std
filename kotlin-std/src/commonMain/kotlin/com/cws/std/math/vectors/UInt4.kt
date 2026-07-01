package com.cws.std.math.vectors

import com.cws.std.math.operators.sqrt

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

    constructor(v: UInt)                     : this(v, v, v, v)
    constructor(xyz: UInt3, w: UInt)         : this(xyz.x, xyz.y, xyz.z, w)
    constructor(x: UInt, yzw: UInt3)         : this(x, yzw.x, yzw.y, yzw.z)
    constructor(xy: UInt2, zw: UInt2)        : this(xy.x, xy.y, zw.x, zw.y)
    constructor(xy: UInt2, z: UInt, w: UInt) : this(xy.x, xy.y, z, w)
    constructor(x: UInt, y: UInt, zw: UInt2) : this(x, y, zw.x, zw.y)
    constructor(x: UInt, yz: UInt2, w: UInt) : this(x, yz.x, yz.y, w)

    val xy   get() = UInt2(x, y)
    val xyz  get() = UInt3(x, y, z)
    val xyzw get() = UInt4(x, y, z, w)

}