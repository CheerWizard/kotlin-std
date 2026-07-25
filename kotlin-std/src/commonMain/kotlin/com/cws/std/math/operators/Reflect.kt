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

fun reflect(
    i: Float2,
    n: Float2,
) = i - n * (2f * dot(n, i))

fun reflect(
    i: Float3,
    n: Float3,
) = i - n * (2f * dot(n, i))

fun reflect(
    i: Float4,
    n: Float4,
) = i - n * (2f * dot(n, i))
