/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.math.matrices

import com.cws.std.math.CoordinateSystem
import com.cws.std.math.MathConfig
import com.cws.std.math.operators.Degree
import com.cws.std.math.operators.cross
import com.cws.std.math.operators.dot
import com.cws.std.math.operators.inverse
import com.cws.std.math.operators.normalize
import com.cws.std.math.operators.radians
import com.cws.std.math.operators.transpose
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import com.cws.std.math.vectors.Quaternion
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.sizeBytes
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun Mat4.sizeBytes(layout: MemoryLayout) = 16 * Float.sizeBytes(layout)
fun Mat4.sizeBytesPacked(layout: MemoryLayout) = 16 * Float.sizeBytes(layout)

data class Mat4(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m02: Float = 0f,
    var m03: Float = 0f,

    var m10: Float = 0f,
    var m11: Float = 1f,
    var m12: Float = 0f,
    var m13: Float = 0f,

    var m20: Float = 0f,
    var m21: Float = 0f,
    var m22: Float = 1f,
    var m23: Float = 0f,

    var m30: Float = 0f,
    var m31: Float = 0f,
    var m32: Float = 0f,
    var m33: Float = 1f,
) {

    constructor(q: Quaternion) : this() {
        val xx = q.x * q.x
        val yy = q.y * q.y
        val zz = q.z * q.z

        val xy = q.x * q.y
        val xz = q.x * q.z
        val yz = q.y * q.z

        val wx = q.w * q.x
        val wy = q.w * q.y
        val wz = q.w * q.z

        m00 = 1f - 2f * (yy + zz)
        m01 = 2f * (xy + wz)
        m02 = 2f * (xz - wy)
        m03 = 0f

        m10 = 2f * (xy - wz)
        m11 = 1f - 2f * (xx + zz)
        m12 = 2f * (yz + wx)
        m13 = 0f

        m20 = 2f * (xz + wy)
        m21 = 2f * (yz - wx)
        m22 = 1f - 2f * (xx + yy)
        m23 = 0f

        m30 = 0f
        m31 = 0f
        m32 = 0f
        m33 = 1f
    }

    fun identity(): Mat4 {
        m00 = 1f
        m01 = 0f
        m02 = 0f
        m03 = 0f

        m10 = 0f
        m11 = 1f
        m12 = 0f
        m13 = 0f

        m20 = 0f
        m21 = 0f
        m22 = 1f
        m23 = 0f

        m30 = 0f
        m31 = 0f
        m32 = 0f
        m33 = 1f

        return this
    }

    fun transpose(): Mat4 = transpose(this, this)

    fun inverse(): Mat4 = inverse(this, this)

    fun translate(v: Float3): Mat4 = translate(v.x, v.y, v.z)

    fun translate(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f,
    ): Mat4 {
        m03 += x
        m13 += y
        m23 += z
        return this
    }

    fun scale(v: Float3): Mat4 = scale(v.x, v.y, v.z)

    fun scale(
        x: Float = 1f,
        y: Float = 1f,
        z: Float = 1f,
    ): Mat4 {
        m00 *= x
        m11 *= y
        m22 *= z
        return this
    }

    fun rotateX(angle: Float): Mat4 {
        val s = sin(angle)
        val c = cos(angle)

        val y0 = m10
        val y1 = m11
        val y2 = m12
        val y3 = m13

        val z0 = m20
        val z1 = m21
        val z2 = m22
        val z3 = m23

        m10 = c * y0 + s * z0
        m11 = c * y1 + s * z1
        m12 = c * y2 + s * z2
        m13 = c * y3 + s * z3

        m20 = c * z0 - s * y0
        m21 = c * z1 - s * y1
        m22 = c * z2 - s * y2
        m23 = c * z3 - s * y3

        return this
    }

    fun rotateY(angle: Float): Mat4 {
        val s = sin(angle)
        val c = cos(angle)

        val x0 = m00
        val x1 = m01
        val x2 = m02
        val x3 = m03

        val z0 = m20
        val z1 = m21
        val z2 = m22
        val z3 = m23

        // Y-axis rotation matches Ry rotation matrix, which is the opposite to Rx, Rz rotation matrices

        m00 = c * x0 - s * z0
        m01 = c * x1 - s * z1
        m02 = c * x2 - s * z2
        m03 = c * x3 - s * z3

        m20 = c * z0 + s * x0
        m21 = c * z1 + s * x1
        m22 = c * z2 + s * x2
        m23 = c * z3 + s * x3

        return this
    }

    fun rotateZ(angle: Float): Mat4 {
        val s = sin(angle)
        val c = cos(angle)

        val x0 = m00
        val x1 = m01
        val x2 = m02
        val x3 = m03

        val y0 = m10
        val y1 = m11
        val y2 = m12
        val y3 = m13

        m00 = c * x0 + s * y0
        m01 = c * x1 + s * y1
        m02 = c * x2 + s * y2
        m03 = c * x3 + s * y3

        m10 = c * y0 - s * x0
        m11 = c * y1 - s * x1
        m12 = c * y2 - s * x2
        m13 = c * y3 - s * x3

        return this
    }

    fun rotate(quaternion: Quaternion): Mat4 = this * Mat4(quaternion)

    operator fun plus(v: Float) = Mat4(
        m00 + v, m01 + v, m02 + v, m03 + v,
        m10 + v, m11 + v, m12 + v, m13 + v,
        m20 + v, m21 + v, m22 + v, m23 + v,
        m30 + v, m31 + v, m32 + v, m33 + v,
    )

    operator fun minus(v: Float) = Mat4(
        m00 - v, m01 - v, m02 - v, m03 - v,
        m10 - v, m11 - v, m12 - v, m13 - v,
        m20 - v, m21 - v, m22 - v, m23 - v,
        m30 - v, m31 - v, m32 - v, m33 - v,
    )

    operator fun times(v: Float) = Mat4(
        m00 * v, m01 * v, m02 * v, m03 * v,
        m10 * v, m11 * v, m12 * v, m13 * v,
        m20 * v, m21 * v, m22 * v, m23 * v,
        m30 * v, m31 * v, m32 * v, m33 * v,
    )

    operator fun div(v: Float) = Mat4(
        m00 / v, m01 / v, m02 / v, m03 / v,
        m10 / v, m11 / v, m12 / v, m13 / v,
        m20 / v, m21 / v, m22 / v, m23 / v,
        m30 / v, m31 / v, m32 / v, m33 / v,
    )

    operator fun plus(m: Mat4) = Mat4(
        m00 + m.m00, m01 + m.m01, m02 + m.m02, m03 + m.m03,
        m10 + m.m10, m11 + m.m11, m12 + m.m12, m13 + m.m13,
        m20 + m.m20, m21 + m.m21, m22 + m.m22, m23 + m.m23,
        m30 + m.m30, m31 + m.m31, m32 + m.m32, m33 + m.m33,
    )

    operator fun minus(m: Mat4) = Mat4(
        m00 - m.m00, m01 - m.m01, m02 - m.m02, m03 - m.m03,
        m10 - m.m10, m11 - m.m11, m12 - m.m12, m13 - m.m13,
        m20 - m.m20, m21 - m.m21, m22 - m.m22, m23 - m.m23,
        m30 - m.m30, m31 - m.m31, m32 - m.m32, m33 - m.m33,
    )

    operator fun div(m: Mat4) = Mat4(
        m00 / m.m00, m01 / m.m01, m02 / m.m02, m03 / m.m03,
        m10 / m.m10, m11 / m.m11, m12 / m.m12, m13 / m.m13,
        m20 / m.m20, m21 / m.m21, m22 / m.m22, m23 / m.m23,
        m30 / m.m30, m31 / m.m31, m32 / m.m32, m33 / m.m33,
    )

    operator fun times(m: Mat4): Mat4 {
        val a00 = m00; val a01 = m01; val a02 = m02; val a03 = m03
        val a10 = m10; val a11 = m11; val a12 = m12; val a13 = m13
        val a20 = m20; val a21 = m21; val a22 = m22; val a23 = m23
        val a30 = m30; val a31 = m31; val a32 = m32; val a33 = m33

        val b00 = m.m00; val b01 = m.m01; val b02 = m.m02; val b03 = m.m03
        val b10 = m.m10; val b11 = m.m11; val b12 = m.m12; val b13 = m.m13
        val b20 = m.m20; val b21 = m.m21; val b22 = m.m22; val b23 = m.m23
        val b30 = m.m30; val b31 = m.m31; val b32 = m.m32; val b33 = m.m33

        return Mat4(
            a00 * b00 + a01 * b10 + a02 * b20 + a03 * b30,
            a00 * b01 + a01 * b11 + a02 * b21 + a03 * b31,
            a00 * b02 + a01 * b12 + a02 * b22 + a03 * b32,
            a00 * b03 + a01 * b13 + a02 * b23 + a03 * b33,

            a10 * b00 + a11 * b10 + a12 * b20 + a13 * b30,
            a10 * b01 + a11 * b11 + a12 * b21 + a13 * b31,
            a10 * b02 + a11 * b12 + a12 * b22 + a13 * b32,
            a10 * b03 + a11 * b13 + a12 * b23 + a13 * b33,

            a20 * b00 + a21 * b10 + a22 * b20 + a23 * b30,
            a20 * b01 + a21 * b11 + a22 * b21 + a23 * b31,
            a20 * b02 + a21 * b12 + a22 * b22 + a23 * b32,
            a20 * b03 + a21 * b13 + a22 * b23 + a23 * b33,

            a30 * b00 + a31 * b10 + a32 * b20 + a33 * b30,
            a30 * b01 + a31 * b11 + a32 * b21 + a33 * b31,
            a30 * b02 + a31 * b12 + a32 * b22 + a33 * b32,
            a30 * b03 + a31 * b13 + a32 * b23 + a33 * b33,
        )
    }

    operator fun unaryMinus() = Mat4(
        -m00, -m01, -m02, -m03,
        -m10, -m11, -m12, -m13,
        -m20, -m21, -m22, -m23,
        -m30, -m31, -m32, -m33,
    )

    // Treats Float4 as a column vector.
    operator fun times(v: Float4) = Float4(
        m00 * v.x + m01 * v.y + m02 * v.z + m03 * v.w,
        m10 * v.x + m11 * v.y + m12 * v.z + m13 * v.w,
        m20 * v.x + m21 * v.y + m22 * v.z + m23 * v.w,
        m30 * v.x + m31 * v.y + m32 * v.z + m33 * v.w,
    )

}

fun ModelMatrix(translation: Float3, rx: Float, ry: Float, rz: Float, scalar: Float3): Mat4 {
    return Mat4()
        .translate(translation)
        .rotateX(rx)
        .rotateY(ry)
        .rotateZ(rz)
        .scale(scalar)
}

fun ModelMatrix(translation: Float3, quaternion: Quaternion, scalar: Float3): Mat4 {
    return Mat4()
        .translate(translation)
        .rotate(quaternion)
        .scale(scalar)
}

fun RigidMatrix(translation: Float3, rx: Float, ry: Float, rz: Float): Mat4 {
    return Mat4()
        .translate(translation)
        .rotateX(rx)
        .rotateY(ry)
        .rotateZ(rz)
}

fun RigidMatrix(translation: Float3, quaternion: Quaternion): Mat4 {
    return Mat4()
        .translate(translation)
        .rotate(quaternion)
}

fun ViewMatrix(position: Float3, front: Float3, up: Float3, coordinateSystem: CoordinateSystem = MathConfig.coordinateSystem): Mat4 {
    return when (coordinateSystem) {
        CoordinateSystem.LEFT_HANDED -> {
            val f = normalize(front)
            val r = normalize(cross(up, f))
            val u = cross(f, r)
            Mat4(
                r.x, r.y, r.z, -dot(r, position),
                u.x, u.y, u.z, -dot(u, position),
                f.x, f.y, f.z, -dot(f, position),
                0f,  0f,  0f,  1f
            )
        }
        CoordinateSystem.RIGHT_HANDED -> {
            val f = normalize(front)
            val r = normalize(cross(f, up))
            val u = cross(r, f)
            Mat4(
                r.x,  r.y,  r.z,  -dot(r, position),
                u.x,  u.y,  u.z,  -dot(u, position),
                -f.x, -f.y, -f.z,   dot(f, position),
                0f,   0f,   0f,    1f
            )
        }
    }
}

fun PerspectiveMatrix(aspectRatio: Float, fov: Degree, zNear: Float, zFar: Float, coordinateSystem: CoordinateSystem = MathConfig.coordinateSystem): Mat4 {
    return when (coordinateSystem) {
        CoordinateSystem.LEFT_HANDED -> {
            val f = 1.0f / tan((fov * 0.5f).radians.value)
            val a = zFar / (zFar - zNear)
            val b = -zNear * zFar / (zFar - zNear)
            Mat4(
                f / aspectRatio, 0f, 0f, 0f,
                0f, -f, 0f, 0f,
                0f, 0f, a, b,
                0f, 0f, 1f, 0f,
            )
        }
        CoordinateSystem.RIGHT_HANDED -> {
            val f = 1.0f / tan((fov * 0.5f).radians.value)
            val a = zFar / (zNear - zFar)
            val b = zNear * zFar / (zNear - zFar)
            Mat4(
                f / aspectRatio, 0f, 0f, 0f,
                0f, -f, 0f, 0f,
                0f, 0f, a, b,
                0f, 0f, -1f, 0f,
            )
        }
    }
}

fun OrthoMatrix(left: Float, right: Float, bottom: Float, top: Float, zNear: Float, zFar: Float, coordinateSystem: CoordinateSystem = MathConfig.coordinateSystem): Mat4 {
    return when (coordinateSystem) {
        CoordinateSystem.LEFT_HANDED -> Mat4(
            2.0f / (right - left), 0.0f, 0.0f, 0.0f,
            0.0f, 2.0f / (bottom - top), 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f / (zFar - zNear), 0.0f,
            -(right + left) / (right - left), -(bottom + top) / (bottom - top), -zNear / (zFar - zNear), 1.0f
        )
        CoordinateSystem.RIGHT_HANDED -> Mat4(
            2.0f / (right - left), 0.0f, 0.0f, 0.0f,
            0.0f, 2.0f / (bottom - top), 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f / (zNear - zFar), 0.0f,
            -(right + left) / (right - left), -(bottom + top) / (bottom - top), zNear / (zNear - zFar), 1.0f
        )
    }
}

fun NormalMatrix4(model: Mat4): Mat4 {
    return model.inverse().transpose()
}

fun NormalMatrix3(model: Mat4): Mat3 {
    return Mat3(model).inverse().transpose()
}