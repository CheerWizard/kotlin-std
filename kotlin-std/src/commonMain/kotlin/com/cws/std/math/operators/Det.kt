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

fun det(m: Mat2): Float =
    det(
        m.m00,
        m.m01,
        m.m10,
        m.m11,
    )

fun det(
    m00: Float,
    m01: Float,
    m10: Float,
    m11: Float,
): Float =
    m00 * m11 - m10 * m01

fun det(m: Mat3): Float =
    det(
        m.m00,
        m.m01,
        m.m02,
        m.m10,
        m.m11,
        m.m12,
        m.m20,
        m.m21,
        m.m22,
    )

fun det(
    m00: Float,
    m01: Float,
    m02: Float,
    m10: Float,
    m11: Float,
    m12: Float,
    m20: Float,
    m21: Float,
    m22: Float,
): Float =
    m00 * m11 * m22 +
    m01 * m12 * m20 +
    m02 * m10 * m21 -
    m02 * m11 * m20 -
    m01 * m10 * m22 -
    m00 * m12 * m21

fun det(m: Mat4): Float =
    det(
        m.m00,
        m.m01,
        m.m02,
        m.m03,
        m.m10,
        m.m11,
        m.m12,
        m.m13,
        m.m20,
        m.m21,
        m.m22,
        m.m23,
        m.m30,
        m.m31,
        m.m32,
        m.m33,
    )

fun det(
    m00: Float,
    m01: Float,
    m02: Float,
    m03: Float,
    m10: Float,
    m11: Float,
    m12: Float,
    m13: Float,
    m20: Float,
    m21: Float,
    m22: Float,
    m23: Float,
    m30: Float,
    m31: Float,
    m32: Float,
    m33: Float,
): Float =
    m00 * m11 * m22 * m33 + m01 * m12 * m23 * m30 + m02 * m13 * m20 * m31 - m30 * m21 * m12 * m03 - m31 * m22 * m13 * m00 -
        m32 * m23 * m10 * m01
