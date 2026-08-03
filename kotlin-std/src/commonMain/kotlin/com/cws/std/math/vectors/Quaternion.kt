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
package com.cws.std.math.vectors

import com.cws.std.math.operators.Radians
import com.cws.std.math.operators.dot
import com.cws.std.math.operators.normalize
import com.cws.std.memory.NativeData
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@NativeData
data class Quaternion(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 1f,
) {
    operator fun get(i: Int): Float =
        when (i) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }

    operator fun set(
        i: Int,
        v: Float,
    ) = when (i) {
        0 -> x = v
        1 -> y = v
        2 -> z = v
        3 -> w = v
        else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
    }

    val length: Float get() {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Quaternion = normalize(this, this)

    operator fun plus(v: Float): Quaternion = Quaternion(x + v, y + v, z + v, w + v)

    operator fun minus(v: Float): Quaternion = Quaternion(x - v, y - v, z - v, w - v)

    operator fun times(v: Float): Quaternion = Quaternion(x * v, y * v, z * v, w * v)

    operator fun div(v: Float): Quaternion = Quaternion(x / v, y / v, z / v, w / v)

    operator fun plus(v: Quaternion): Quaternion = Quaternion(x + v.x, y + v.y, z + v.z, w + v.w)

    operator fun minus(v: Quaternion): Quaternion = Quaternion(x - v.x, y - v.y, z - v.z, w - v.w)

    operator fun times(v: Quaternion): Quaternion =
        Quaternion(
            w * v.x + x * v.w + y * v.z - z * v.y,
            w * v.y - x * v.z + y * v.w + z * v.x,
            w * v.z + x * v.y - y * v.x + z * v.w,
            w * v.w - x * v.x - y * v.y - z * v.z,
        )

    operator fun div(v: Quaternion): Quaternion = Quaternion(x / v.x, y / v.y, z / v.z, w / v.w)

    operator fun unaryMinus(): Quaternion = Quaternion(-x, -y, -z, -w)

    fun conjugate(): Quaternion = Quaternion(-x, -y, -z, w)

    fun fromAngle(
        n: Float3,
        r: Radians,
    ): Quaternion {
        val n = normalize(n)

        val half = r.value * 0.5f
        val s = sin(half)

        x = n.x * s
        y = n.y * s
        z = n.z * s
        w = cos(half)

        return this
    }

    fun rotate(v: Float3): Float3 {
        val p = Quaternion(v.x, v.y, v.z, 0f)
        val r = this * p * conjugate()
        return Float3(r.x, r.y, r.z)
    }

    fun slerp(
        q: Quaternion,
        t: Float,
    ): Quaternion {
        val q1 = this
        val q2 = q

        val dot = dot(q1, q2).coerceIn(-1f, 1f)
        var theta = acos(dot)
        if (theta < 0.0) {
            theta = -theta
        }

        val st = sin(theta)
        val sut = sin(t * theta)
        val sout = sin((1 - t) * theta)
        val coeff1 = sout / st
        val coeff2 = sut / st

        return Quaternion(
            coeff1 * q1.x + coeff2 * q2.x,
            coeff1 * q1.y + coeff2 * q2.y,
            coeff1 * q1.z + coeff2 * q2.z,
            coeff1 * q1.w + coeff2 * q2.w,
        ).normalize()
    }

    fun inverse(): Quaternion {
        val len2 = x*x + y*y + z*z + w*w
        return Quaternion(
            -x / len2,
            -y / len2,
            -z / len2,
            w / len2
        )
    }
}
