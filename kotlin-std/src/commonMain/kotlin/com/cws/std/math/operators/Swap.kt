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

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4

fun Mat2.swap(
    i1: Int,
    j1: Int,
    i2: Int,
    j2: Int,
) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}

fun Mat3.swap(
    i1: Int,
    j1: Int,
    i2: Int,
    j2: Int,
) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}

fun Mat4.swap(
    i1: Int,
    j1: Int,
    i2: Int,
    j2: Int,
) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}
