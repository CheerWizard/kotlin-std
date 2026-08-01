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
import com.cws.std.memory.NativeData

@NativeData
data class Mat2(
    var v1: Float2 = Float2(),
    var v2: Float2 = Float2(),
) {
    constructor(
        m00: Float,
        m01: Float,
        m10: Float,
        m11: Float,
    ) : this() {
        v1.x = m00
        v1.y = m01

        v2.x = m10
        v2.y = m11
    }

    operator fun get(i: Int): Float2 =
        when (i) {
            0 -> v1
            1 -> v2
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }

    fun identity(): Mat2 {
        v1.x = 1f
        v1.y = 0f
        v2.x = 0f
        v2.y = 1f
        return this
    }

    fun transpose(): Mat2 = transpose(this, this)

    fun inverse(): Mat2 = inverse(this, this)

    operator fun minus(v: Float): Mat2 = Mat2(v1 - v, v2 - v)

    operator fun times(v: Float): Mat2 = Mat2(v1 * v, v2 * v)

    operator fun div(v: Float): Mat2 = Mat2(v1 / v, v2 / v)

    operator fun plus(m: Mat2): Mat2 = Mat2(v1 + m.v1, v2 + m.v2)

    operator fun minus(m: Mat2): Mat2 = Mat2(v1 - m.v1, v2 - m.v2)

    operator fun div(m: Mat2): Mat2 = Mat2(v1 / m.v1, v2 / m.v2)

    operator fun times(m: Mat2): Mat2 {
        val m1 = this
        val m2 = m
        val m3 = Mat2()
        for (r in 0..1) {
            for (c in 0..1) {
                for (i in 0..1) {
                    m3[r][c] += m1[r][i] * m2[i][c]
                }
            }
        }
        return m3
    }

    operator fun unaryMinus(): Mat2 {
        val m1 = this
        val m2 = Mat2()
        for (r in 0..1) {
            for (c in 0..1) {
                m2[r][c] = -m1[r][c]
            }
        }
        return m2
    }

    operator fun times(v: Float2) =
        Float2(
            v1.x * v.x + v1.y * v.y,
            v2.x * v.x + v2.y * v.y,
        )
}
