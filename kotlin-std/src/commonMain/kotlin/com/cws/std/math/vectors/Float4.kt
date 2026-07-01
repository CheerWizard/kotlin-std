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

    constructor(v: Float)                    : this(v, v, v, v)
    constructor(xyz: Float3, w: Float)       : this(xyz.x, xyz.y, xyz.z, w)
    constructor(x: Float, yzw: Float3)       : this(x, yzw.x, yzw.y, yzw.z)
    constructor(xy: Float2, zw: Float2)      : this(xy.x, xy.y, zw.x, zw.y)
    constructor(xy: Float2, z: Float, w: Float) : this(xy.x, xy.y, z, w)
    constructor(x: Float, y: Float, zw: Float2) : this(x, y, zw.x, zw.y)
    constructor(x: Float, yz: Float2, w: Float) : this(x, yz.x, yz.y, w)

    // Swizzle — Float2
    val xx get() = Float2(x, x);  val xy get() = Float2(x, y);  val xz get() = Float2(x, z);  val xw get() = Float2(x, w)
    val yx get() = Float2(y, x);  val yy get() = Float2(y, y);  val yz get() = Float2(y, z);  val yw get() = Float2(y, w)
    val zx get() = Float2(z, x);  val zy get() = Float2(z, y);  val zz get() = Float2(z, z);  val zw get() = Float2(z, w)
    val wx get() = Float2(w, x);  val wy get() = Float2(w, y);  val wz get() = Float2(w, z);  val ww get() = Float2(w, w)

    // Swizzle — Float3 (most common)
    val xyz get() = Float3(x, y, z)
    val xyw get() = Float3(x, y, w)
    val xzy get() = Float3(x, z, y)
    val xzw get() = Float3(x, z, w)
    val xwy get() = Float3(x, w, y)
    val xwz get() = Float3(x, w, z)
    val yxz get() = Float3(y, x, z)
    val yxw get() = Float3(y, x, w)
    val yzx get() = Float3(y, z, x)
    val yzw get() = Float3(y, z, w)
    val ywx get() = Float3(y, w, x)
    val ywz get() = Float3(y, w, z)
    val zxy get() = Float3(z, x, y)
    val zxw get() = Float3(z, x, w)
    val zyx get() = Float3(z, y, x)
    val zyw get() = Float3(z, y, w)
    val zwx get() = Float3(z, w, x)
    val zwy get() = Float3(z, w, y)
    val wxy get() = Float3(w, x, y)
    val wxz get() = Float3(w, x, z)
    val wyx get() = Float3(w, y, x)
    val wyz get() = Float3(w, y, z)
    val wzx get() = Float3(w, z, x)
    val wzy get() = Float3(w, z, y)
    val xxx get() = Float3(x, x, x)
    val yyy get() = Float3(y, y, y)
    val zzz get() = Float3(z, z, z)
    val www get() = Float3(w, w, w)

    // Swizzle — Float4 (most common)
    val xyzw get() = Float4(x, y, z, w)
    val xywz get() = Float4(x, y, w, z)
    val xzyw get() = Float4(x, z, y, w)
    val xzwy get() = Float4(x, z, w, y)
    val xwyz get() = Float4(x, w, y, z)
    val xwzy get() = Float4(x, w, z, y)
    val yxzw get() = Float4(y, x, z, w)
    val yxwz get() = Float4(y, x, w, z)
    val yzxw get() = Float4(y, z, x, w)
    val yzwx get() = Float4(y, z, w, x)
    val ywxz get() = Float4(y, w, x, z)
    val ywzx get() = Float4(y, w, z, x)
    val zxyw get() = Float4(z, x, y, w)
    val zxwy get() = Float4(z, x, w, y)
    val zyxw get() = Float4(z, y, x, w)
    val zywx get() = Float4(z, y, w, x)
    val zwxy get() = Float4(z, w, x, y)
    val zwyx get() = Float4(z, w, y, x)
    val wxyz get() = Float4(w, x, y, z)
    val wxzy get() = Float4(w, x, z, y)
    val wyxz get() = Float4(w, y, x, z)
    val wyzx get() = Float4(w, y, z, x)
    val wzxy get() = Float4(w, z, x, y)
    val wzyx get() = Float4(w, z, y, x)
    val xxxx get() = Float4(x, x, x, x)
    val yyyy get() = Float4(y, y, y, y)
    val zzzz get() = Float4(z, z, z, z)
    val wwww get() = Float4(w, w, w, w)

    // RGBA aliases
    val r get() = x;  val g get() = y;  val b get() = z;  val a = w
    val rg   get() = Float2(x, y)
    val rb   get() = Float2(x, z)
    val ra   get() = Float2(x, w)
    val gb   get() = Float2(y, z)
    val ga   get() = Float2(y, w)
    val ba   get() = Float2(z, w)
    val rgb  get() = Float3(x, y, z)
    val rga  get() = Float3(x, y, w)
    val rba  get() = Float3(x, z, w)
    val gba  get() = Float3(y, z, w)
    val rgba get() = Float4(x, y, z, w)

}