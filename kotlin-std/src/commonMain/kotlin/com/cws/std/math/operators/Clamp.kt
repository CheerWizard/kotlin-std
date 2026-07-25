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
package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun clamp(
    a: UInt,
    b: UInt,
    x: UInt,
): UInt =
    if (x < a) {
        a
    } else if (x > b) {
        b
    } else {
        x
    }

fun clamp(
    a: Int,
    b: Int,
    x: Int,
): Int =
    if (x < a) {
        a
    } else if (x > b) {
        b
    } else {
        x
    }

fun clamp(
    a: Long,
    b: Long,
    x: Long,
): Long =
    if (x < a) {
        a
    } else if (x > b) {
        b
    } else {
        x
    }

fun clamp(
    a: Float,
    b: Float,
    x: Float,
): Float =
    if (x < a) {
        a
    } else if (x > b) {
        b
    } else {
        x
    }

fun clamp(
    a: Double,
    b: Double,
    x: Double,
): Double =
    if (x < a) {
        a
    } else if (x > b) {
        b
    } else {
        x
    }

fun clamp(
    v: Float2,
    min: Float,
    max: Float,
) = Float2(clamp(min, max, v.x), clamp(min, max, v.y))

fun clamp(
    v: Float3,
    min: Float,
    max: Float,
) = Float3(clamp(min, max, v.x), clamp(min, max, v.y), clamp(min, max, v.z))

fun clamp(
    v: Float4,
    min: Float,
    max: Float,
) = Float4(clamp(min, max, v.x), clamp(min, max, v.y), clamp(min, max, v.z), clamp(min, max, v.w))

fun clamp(
    v: Float2,
    min: Float2,
    max: Float2,
) = Float2(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y))

fun clamp(
    v: Float3,
    min: Float3,
    max: Float3,
) = Float3(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y), clamp(min.z, max.z, v.z))

fun clamp(
    v: Float4,
    min: Float4,
    max: Float4,
) = Float4(clamp(min.x, max.x, v.x), clamp(min.y, max.y, v.y), clamp(min.z, max.z, v.z), clamp(min.w, max.w, v.w))
