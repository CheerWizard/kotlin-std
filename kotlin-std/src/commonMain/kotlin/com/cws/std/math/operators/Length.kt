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

fun length(v: Float2) = kotlin.math.sqrt(v.x * v.x + v.y * v.y)

fun length(v: Float3) = kotlin.math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z)

fun length(v: Float4) = kotlin.math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z + v.w * v.w)
