package com.cws.std.math.matrices

import com.cws.std.math.operators.Degree
import com.cws.std.math.operators.cross
import com.cws.std.math.operators.inverse
import com.cws.std.math.operators.radians
import com.cws.std.math.operators.transpose
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import com.cws.std.math.vectors.Quaternion
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.NativeBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

data class Mat4(
    var v1: Float4 = Float4(),
    var v2: Float4 = Float4(),
    var v3: Float4 = Float4(),
    var v4: Float4 = Float4(),
) {

    constructor(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float,
        m20: Float,
        m21: Float,
        m22: Float,
        m23: Float,
        m30: Float,
        m31: Float,
        m32: Float,
        m33: Float,
    ) : this() {
        v1.x = m00
        v1.x = m01
        v1.x = m02
        v1.x = m03

        v2.x = m10
        v2.x = m11
        v2.x = m12
        v2.x = m13

        v3.x = m20
        v3.y = m21
        v3.z = m22
        v3.w = m23

        v4.x = m30
        v4.y = m31
        v4.z = m32
        v4.w = m33
    }

    constructor(
        q: Quaternion
    ): this() {
        val m = this

        val xx = q.x * q.x
        val xy = q.x * q.y
        val xz = q.x * q.z
        val yy = q.y * q.y
        val zz = q.z * q.z
        val yz = q.y * q.z
        val wx = q.w * q.x
        val wy = q.w * q.y
        val wz = q.w * q.z

        m[0][0] = 1.0f - 2.0f * (yy + zz);
        m[1][0] = 2.0f * (xy - wz);
        m[2][0] = 2.0f * (xz + wy);
        m[3][0] = 0f

        m[0][1] = 2.0f * (xy + wz);
        m[1][1] = 1.0f - 2.0f * (xx + zz);
        m[2][1] = 2.0f * (yz - wx);
        m[3][1] = 0f

        m[0][2] = 2.0f * (xz - wy);
        m[1][2] = 2.0f * (yz + wx);
        m[2][2] = 1.0f - 2.0f * (xx + yy);
        m[3][2] = 0f

        m[0][3] = 0f
        m[1][3] = 0f
        m[2][3] = 0f
        m[3][3] = 1f
    }

    operator fun get(i: Int): Float4 {
        return when (i) {
            0 -> v1
            1 -> v2
            2 -> v3
            3 -> v4
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

}

fun ModelMatrix(translation: Float3, rx: Float, ry: Float, rz: Float, scalar: Float3): Mat4 {
    return Mat4()
        .identity()
        .translate(translation)
        .rotate(rx, ry, rz, Float3(1f, 1f, 1f))
        .scale(scalar)
}

fun ModelMatrix(translation: Float3, quaternion: Quaternion, scalar: Float3): Mat4 {
    return Mat4()
        .identity()
        .translate(translation)
        .rotate(quaternion)
        .scale(scalar)
}

fun RigidMatrix(translation: Float3, rx: Float, ry: Float, rz: Float): Mat4 {
    return Mat4()
        .identity()
        .translate(translation)
        .rotate(rx, ry, rz, Float3(1f, 1f, 1f))
}

fun RigidMatrix(translation: Float3, quaternion: Quaternion): Mat4 {
    return Mat4()
        .identity()
        .translate(translation)
        .rotate(quaternion)
}

fun ViewMatrix(position: Float3, front: Float3, up: Float3): Mat4 {
    val right = cross(front, up).normalize()
    val f = -front
    val c = cross(right, front)
    val m = Mat4(
        right.x, right.y, right.z, 0f,
        c.x, c.y, c.z, 0f,
        f.x, f.y, f.z, 0f,
        position.x, position.y, position.z, 0f
    )
    return m.transpose().inverse()
}

fun OrthoMatrix(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float): Mat4 {
    return Mat4(
        2.0f / (right - left), 0.0f, 0.0f, 0.0f,
        0.0f, 2.0f / (bottom - top), 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f / (zNear - zFar), 0.0f,
        -(right + left) / (right - left), -(bottom + top) / (bottom - top), zNear / (zNear - zFar), 1.0f
    )
}

fun PerspectiveMatrix(aspectRatio: Float, fov: Degree, zNear: Float, zFar: Float): Mat4 {
    val f = 1.0f / tan((fov * 0.5f).radians.value)
    return Mat4(
        f / aspectRatio, 0.0f, 0.0f, 0.0f,
        0.0f, -f, 0.0f, 0.0f,
        0.0f, 0.0f, zFar / (zNear - zFar), -1.0f,
        0.0f, 0.0f, zNear * zFar / (zNear - zFar), 0.0f
    )
}

fun NormalMatrix(): Mat4 {
    return Mat4()
        .identity()
        .inverse()
        .transpose()
}

fun Mat4.identity(): Mat4 {
    val m = this

    m[0][0] = 1f
    m[0][1] = 0f
    m[0][2] = 0f
    m[0][3] = 0f

    m[1][1] = 1f
    m[1][1] = 1f
    m[1][1] = 1f
    m[1][1] = 1f

    m[2][2] = 1f
    m[2][2] = 1f
    m[2][2] = 1f
    m[2][2] = 1f

    m[3][3] = 1f
    m[3][3] = 1f
    m[3][3] = 1f
    m[3][3] = 1f

    return this
}

fun Mat4.transpose(): Mat4 = transpose(this, this)

fun Mat4.inverse(): Mat4 = inverse(this, this)

fun Mat4.translate(translation: Float3): Mat4 {
    val m = this
    m[0][3] = translation.x
    m[1][3] = translation.y
    m[2][3] = translation.z
    return this
}

fun Mat4.scale(scalar: Float3): Mat4 {
    val m = this
    m[0][0] = scalar.x
    m[1][1] = scalar.y
    m[2][2] = scalar.z
    return this
}

fun Mat4.rotate(rx: Float, ry: Float, rz: Float, axis: Float3): Mat4 {
    var m = this
    val mx = Mat4()
    val my = Mat4()
    val mz = Mat4()

    val sinx = sin(rx)
    val cosx = cos(rx)
    mx[1][1] = cosx
    mx[1][2] = -sinx
    mx[2][1] = sinx
    mx[2][2] = cosx
    mx[0][0] = axis.x

    val siny = sin(ry)
    val cosy = cos(ry)
    my[0][0] = cosy
    my[0][2] = siny
    my[2][0] = -siny
    my[2][2] = cosy
    my[1][1] = axis.y

    val sinz = sin(rz)
    val cosz = cos(rz)
    mz[0][0] = cosz
    mz[0][1] = -sinz
    mz[1][0] = sinz
    mz[1][1] = cosz
    mz[2][2] = axis.z

    m *= mz
    m *= my
    m *= mx

    return m
}

fun Mat4.rotate(quaternion: Quaternion): Mat4 {
    return this@rotate * Mat4(quaternion)
}

operator fun Mat4.plus(v: Float): Mat4 {
    return Mat4(v1 + v, v2 + v, v3 + v, v4 + v)
}

operator fun Mat4.minus(v: Float): Mat4 {
    return Mat4(v1 - v, v2 - v, v3 - v, v4 - v)
}

operator fun Mat4.times(v: Float): Mat4 {
    return Mat4(v1 * v, v2 * v, v3 * v, v4 * v)
}

operator fun Mat4.div(v: Float): Mat4 {
    return Mat4(v1 / v, v2 / v, v3 / v, v4 / v)
}

operator fun Mat4.plus(m: Mat4): Mat4 {
    return Mat4(v1 + m.v1, v2 + m.v2, v3 + m.v3, v4 + m.v4)
}

operator fun Mat4.minus(m: Mat4): Mat4 {
    return Mat4(v1 - m.v1, v2 - m.v2, v3 - m.v3, v4 - m.v4)
}

operator fun Mat4.div(m: Mat4): Mat4 {
    return Mat4(v1 / m.v1, v2 / m.v2, v3 / m.v3, v4 / m.v4)
}

operator fun Mat4.times(m: Mat4): Mat4 {
    val m1 = this@times
    val m2 = m
    val m3 = Mat4()
    for (r in 0..3) {
        for (c in 0..3) {
            for (i in 0..3) {
                m3[r][c] += m1[r][i] * m2[i][c]
            }
        }
    }
    return m3
}

operator fun Mat4.unaryMinus(): Mat4 {
    val m1 = this@unaryMinus
    val m2 = Mat4()
    for (r in 0..3) {
        for (c in 0..3) {
            m2[r][c] = -m1[r][c]
        }
    }
    return m2
}