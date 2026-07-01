package com.cws.std.math.vectors

import com.cws.std.math.matrices.Mat3
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

    constructor(v: Float)              : this(v, v, v)
    constructor(xy: Float2, z: Float)  : this(xy.x, xy.y, z)
    constructor(x: Float, yz: Float2)  : this(x, yz.x, yz.y)

    // Swizzle — Float2
    val xx get() = Float2(x, x)
    val xy get() = Float2(x, y)
    val xz get() = Float2(x, z)
    val yx get() = Float2(y, x)
    val yy get() = Float2(y, y)
    val yz get() = Float2(y, z)
    val zx get() = Float2(z, x)
    val zy get() = Float2(z, y)
    val zz get() = Float2(z, z)

    // Swizzle — Float3 (common ones)
    val xxx get() = Float3(x, x, x)
    val xxy get() = Float3(x, x, y)
    val xxz get() = Float3(x, x, z)
    val xyx get() = Float3(x, y, x)
    val xyy get() = Float3(x, y, y)
    val xyz get() = Float3(x, y, z)
    val xzx get() = Float3(x, z, x)
    val xzy get() = Float3(x, z, y)
    val xzz get() = Float3(x, z, z)
    val yxx get() = Float3(y, x, x)
    val yxy get() = Float3(y, x, y)
    val yxz get() = Float3(y, x, z)
    val yyx get() = Float3(y, y, x)
    val yyy get() = Float3(y, y, y)
    val yyz get() = Float3(y, y, z)
    val yzx get() = Float3(y, z, x)
    val yzy get() = Float3(y, z, y)
    val yzz get() = Float3(y, z, z)
    val zxx get() = Float3(z, x, x)
    val zxy get() = Float3(z, x, y)
    val zxz get() = Float3(z, x, z)
    val zyx get() = Float3(z, y, x)
    val zyy get() = Float3(z, y, y)
    val zyz get() = Float3(z, y, z)
    val zzx get() = Float3(z, z, x)
    val zzy get() = Float3(z, z, y)
    val zzz get() = Float3(z, z, z)

    // Swizzle — Float4 (most useful ones)
    val xyzz get() = Float4(x, y, z, z)
    val xyzx get() = Float4(x, y, z, x)
    val xyzy get() = Float4(x, y, z, y)
    val xxxx get() = Float4(x, x, x, x)
    val yyyy get() = Float4(y, y, y, y)
    val zzzz get() = Float4(z, z, z, z)

    // RGB aliases
    val r get() = x;  val g get() = y;  val b get() = z
    val rg  get() = Float2(x, y)
    val rb  get() = Float2(x, z)
    val gb  get() = Float2(y, z)
    val rgb get() = Float3(x, y, z)

}
