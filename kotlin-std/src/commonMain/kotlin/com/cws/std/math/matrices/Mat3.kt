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

import com.cws.std.math.operators.inverse
import com.cws.std.math.operators.transpose
import com.cws.std.math.vectors.Float3
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.NativeData
import com.cws.std.memory.sizeBytes

fun Mat3.sizeBytes(layout: MemoryLayout) = 9 * Float.sizeBytes(layout)
fun Mat3.sizeBytesPacked(layout: MemoryLayout) = 9 * Float.sizeBytes(layout)

data class Mat3(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m02: Float = 0f,

    var m10: Float = 0f,
    var m11: Float = 1f,
    var m12: Float = 0f,

    var m20: Float = 0f,
    var m21: Float = 0f,
    var m22: Float = 1f,
) {
    constructor(m: Mat4) : this(
        m.m00, m.m01, m.m02,
        m.m10, m.m11, m.m12,
        m.m20, m.m21, m.m22,
    )

    fun identity(): Mat3 {
        m00 = 1f
        m01 = 0f
        m02 = 0f

        m10 = 0f
        m11 = 1f
        m12 = 0f

        m20 = 0f
        m21 = 0f
        m22 = 1f

        return this
    }

    fun transpose(): Mat3 = transpose(this, this)

    fun inverse(): Mat3 = inverse(this, this)

    operator fun plus(v: Float) = Mat3(
        m00 + v, m01 + v, m02 + v,
        m10 + v, m11 + v, m12 + v,
        m20 + v, m21 + v, m22 + v,
    )

    operator fun minus(v: Float) = Mat3(
        m00 - v, m01 - v, m02 - v,
        m10 - v, m11 - v, m12 - v,
        m20 - v, m21 - v, m22 - v,
    )

    operator fun times(v: Float) = Mat3(
        m00 * v, m01 * v, m02 * v,
        m10 * v, m11 * v, m12 * v,
        m20 * v, m21 * v, m22 * v,
    )

    operator fun div(v: Float) = Mat3(
        m00 / v, m01 / v, m02 / v,
        m10 / v, m11 / v, m12 / v,
        m20 / v, m21 / v, m22 / v,
    )

    operator fun plus(m: Mat3) = Mat3(
        m00 + m.m00, m01 + m.m01, m02 + m.m02,
        m10 + m.m10, m11 + m.m11, m12 + m.m12,
        m20 + m.m20, m21 + m.m21, m22 + m.m22,
    )

    operator fun minus(m: Mat3) = Mat3(
        m00 - m.m00, m01 - m.m01, m02 - m.m02,
        m10 - m.m10, m11 - m.m11, m12 - m.m12,
        m20 - m.m20, m21 - m.m21, m22 - m.m22,
    )

    operator fun div(m: Mat3) = Mat3(
        m00 / m.m00, m01 / m.m01, m02 / m.m02,
        m10 / m.m10, m11 / m.m11, m12 / m.m12,
        m20 / m.m20, m21 / m.m21, m22 / m.m22,
    )

    operator fun times(m: Mat3): Mat3 {
        val a00 = m00
        val a01 = m01
        val a02 = m02

        val a10 = m10
        val a11 = m11
        val a12 = m12

        val a20 = m20
        val a21 = m21
        val a22 = m22

        val b00 = m.m00
        val b01 = m.m01
        val b02 = m.m02

        val b10 = m.m10
        val b11 = m.m11
        val b12 = m.m12

        val b20 = m.m20
        val b21 = m.m21
        val b22 = m.m22

        return Mat3(
            a00 * b00 + a01 * b10 + a02 * b20,
            a00 * b01 + a01 * b11 + a02 * b21,
            a00 * b02 + a01 * b12 + a02 * b22,

            a10 * b00 + a11 * b10 + a12 * b20,
            a10 * b01 + a11 * b11 + a12 * b21,
            a10 * b02 + a11 * b12 + a12 * b22,

            a20 * b00 + a21 * b10 + a22 * b20,
            a20 * b01 + a21 * b11 + a22 * b21,
            a20 * b02 + a21 * b12 + a22 * b22,
        )
    }

    operator fun unaryMinus() = Mat3(
        -m00, -m01, -m02,
        -m10, -m11, -m12,
        -m20, -m21, -m22,
    )

    // Treats Float3 as a column vector.
    operator fun times(v: Float3) = Float3(
        m00 * v.x + m01 * v.y + m02 * v.z,
        m10 * v.x + m11 * v.y + m12 * v.z,
        m20 * v.x + m21 * v.y + m22 * v.z,
    )
}
