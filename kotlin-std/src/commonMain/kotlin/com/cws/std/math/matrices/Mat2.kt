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
import com.cws.std.math.vectors.Float2
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.sizeBytes

fun Mat2.sizeBytes(layout: MemoryLayout) = 4 * Float.sizeBytes(layout)
fun Mat2.sizeBytesPacked(layout: MemoryLayout) = 4 * Float.sizeBytes(layout)


data class Mat2(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m10: Float = 0f,
    var m11: Float = 1f,
) {

    fun identity(): Mat2 {
        m00 = 1f
        m01 = 0f
        m10 = 0f
        m11 = 1f
        return this
    }

    fun transpose(): Mat2 = transpose(this, this)

    fun inverse(): Mat2 = inverse(this, this)

    operator fun plus(v: Float) = Mat2(
        m00 + v, m01 + v,
        m10 + v, m11 + v
    )

    operator fun minus(v: Float) = Mat2(
        m00 - v, m01 - v,
        m10 - v, m11 - v
    )

    operator fun times(v: Float) = Mat2(
        m00 * v, m01 * v,
        m10 * v, m11 * v
    )

    operator fun div(v: Float) = Mat2(
        m00 / v, m01 / v,
        m10 / v, m11 / v
    )

    operator fun plus(m: Mat2) = Mat2(
        m00 + m.m00, m01 + m.m01,
        m10 + m.m10, m11 + m.m11
    )

    operator fun minus(m: Mat2) = Mat2(
        m00 - m.m00, m01 - m.m01,
        m10 - m.m10, m11 - m.m11
    )

    operator fun div(m: Mat2) = Mat2(
        m00 / m.m00, m01 / m.m01,
        m10 / m.m10, m11 / m.m11
    )

    operator fun times(m: Mat2): Mat2 {
        val a00 = m00
        val a01 = m01
        val a10 = m10
        val a11 = m11

        val b00 = m.m00
        val b01 = m.m01
        val b10 = m.m10
        val b11 = m.m11

        return Mat2(
            a00 * b00 + a01 * b10,
            a00 * b01 + a01 * b11,

            a10 * b00 + a11 * b10,
            a10 * b01 + a11 * b11
        )
    }

    operator fun unaryMinus() = Mat2(
        -m00, -m01,
        -m10, -m11
    )

    // Treats Float2 as a column vector.
    operator fun times(v: Float2) = Float2(
        m00 * v.x + m01 * v.y,
        m10 * v.x + m11 * v.y
    )
}
