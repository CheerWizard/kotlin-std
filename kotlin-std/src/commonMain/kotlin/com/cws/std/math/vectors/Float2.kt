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

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.operators.normalize
import com.cws.std.memory.NativeData
import com.cws.std.memory.NativeList
import kotlin.math.sqrt

@NativeData
@NativeList
data class Float2(
    var x: Float = 0f,
    var y: Float = 0f,
) {
    operator fun get(i: Int): Float =
        when (i) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }

    operator fun set(
        i: Int,
        v: Float,
    ) = when (i) {
        0 -> x = v
        1 -> y = v
        else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
    }

    val length: Float get() {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

    operator fun plus(v: Float): Float2 = Float2(x + v, y + v)

    operator fun minus(v: Float): Float2 = Float2(x - v, y - v)

    operator fun times(v: Float): Float2 = Float2(x * v, y * v)

    operator fun div(v: Float): Float2 = Float2(x / v, y / v)

    operator fun plus(v: Float2): Float2 = Float2(x + v.x, y + v.y)

    operator fun minus(v: Float2): Float2 = Float2(x - v.x, y - v.y)

    operator fun times(v: Float2): Float2 = Float2(x * v.x, y * v.y)

    operator fun div(v: Float2): Float2 = Float2(x / v.x, y / v.y)

    operator fun unaryMinus(): Float2 = Float2(-x, -y)

    // Treats Float2 as a row vector.
    operator fun times(m: Mat2) =
        Float2(
            x * m.m00 + y * m.m10,
            x * m.m01 + y * m.m11,
        )

    // Swizzle — Float2
    val xx get() = Float2(x, x)
    val xy get() = Float2(x, y)
    val yx get() = Float2(y, x)
    val yy get() = Float2(y, y)

    // Swizzle — Float3
    val xxx get() = Float3(x, x, x)
    val xxy get() = Float3(x, x, y)
    val xyx get() = Float3(x, y, x)
    val xyy get() = Float3(x, y, y)
    val yxx get() = Float3(y, x, x)
    val yxy get() = Float3(y, x, y)
    val yyx get() = Float3(y, y, x)
    val yyy get() = Float3(y, y, y)

    // Swizzle — Float4
    val xxxx get() = Float4(x, x, x, x)
    val xxxy get() = Float4(x, x, x, y)
    val xxyx get() = Float4(x, x, y, x)
    val xxyy get() = Float4(x, x, y, y)
    val xyxx get() = Float4(x, y, x, x)
    val xyxy get() = Float4(x, y, x, y)
    val xyyx get() = Float4(x, y, y, x)
    val xyyy get() = Float4(x, y, y, y)
    val yxxx get() = Float4(y, x, x, x)
    val yxxy get() = Float4(y, x, x, y)
    val yxyx get() = Float4(y, x, y, x)
    val yxyy get() = Float4(y, x, y, y)
    val yyxx get() = Float4(y, y, x, x)
    val yyxy get() = Float4(y, y, x, y)
    val yyyx get() = Float4(y, y, y, x)
    val yyyy get() = Float4(y, y, y, y)
}
