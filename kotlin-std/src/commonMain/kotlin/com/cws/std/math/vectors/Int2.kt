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

import com.cws.std.math.operators.sqrt
import com.cws.std.memory.NativeData
import com.cws.std.memory.NativeList
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES

@NativeData
@NativeList
data class Int2(
    var x: Int = 0,
    var y: Int = 0,
) {
    operator fun get(i: Int): Int =
        when (i) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
        }

    operator fun set(
        i: Int,
        v: Int,
    ) = when (i) {
        0 -> x = v
        1 -> y = v
        else -> throw IndexOutOfBoundsException("i=$i out of range [0, 1]")
    }

    val length: Int get() {
        val x = x
        val y = y
        return sqrt(x * x + y * y)
    }

    operator fun plus(v: Int): Int2 = Int2(x + v, y + v)

    operator fun minus(v: Int): Int2 = Int2(x - v, y - v)

    operator fun times(v: Int): Int2 = Int2(x * v, y * v)

    operator fun div(v: Int): Int2 = Int2(x / v, y / v)

    operator fun plus(v: Int2): Int2 = Int2(x + v.x, y + v.y)

    operator fun minus(v: Int2): Int2 = Int2(x - v.x, y - v.y)

    operator fun times(v: Int2): Int2 = Int2(x * v.x, y * v.y)

    operator fun div(v: Int2): Int2 = Int2(x / v.x, y / v.y)

    operator fun unaryMinus(): Int2 = Int2(-x, -y)

    val xx get() = Int2(x, x)
    val xy get() = Int2(x, y)
    val yx get() = Int2(y, x)
    val yy get() = Int2(y, y)
    val xyz get() = Int3(x, y, 0)
}
