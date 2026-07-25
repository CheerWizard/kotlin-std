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

fun cross(
    v1: Float2,
    v2: Float2,
): Float = v1.x * v2.y - v1.y * v2.x

fun cross(
    v1: Float3,
    v2: Float3,
): Float3 =
    Float3(
        v1.y * v2.z - v1.z * v2.y,
        v1.z * v2.x - v1.x * v2.z,
        v1.x * v2.y - v1.y * v2.x,
    )

fun cross(
    v1: Float4,
    v2: Float4,
): Float4 {
    val x = v1.y * v2.z - v1.z * v2.y
    val y = v1.z * v2.x - v1.x * v2.z
    val z = v1.x * v2.y - v1.y * v2.x
    return Float4(x, y, z, 0f)
}
