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

fun mix(
    a: Float,
    b: Float,
    t: Float,
) = a + (b - a) * t

fun mix(
    a: Float2,
    b: Float2,
    t: Float,
) = Float2(mix(a.x, b.x, t), mix(a.y, b.y, t))

fun mix(
    a: Float3,
    b: Float3,
    t: Float,
) = Float3(mix(a.x, b.x, t), mix(a.y, b.y, t), mix(a.z, b.z, t))

fun mix(
    a: Float4,
    b: Float4,
    t: Float,
) = Float4(mix(a.x, b.x, t), mix(a.y, b.y, t), mix(a.z, b.z, t), mix(a.w, b.w, t))

fun mix(
    a: Float2,
    b: Float2,
    t: Float2,
) = Float2(mix(a.x, b.x, t.x), mix(a.y, b.y, t.y))

fun mix(
    a: Float3,
    b: Float3,
    t: Float3,
) = Float3(mix(a.x, b.x, t.x), mix(a.y, b.y, t.y), mix(a.z, b.z, t.z))

fun mix(
    a: Float4,
    b: Float4,
    t: Float4,
) = Float4(mix(a.x, b.x, t.x), mix(a.y, b.y, t.y), mix(a.z, b.z, t.z), mix(a.w, b.w, t.w))
