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

fun inverse(
    m: Mat2,
    out: Mat2,
): Mat2 {
    val d = det(m)

    out.m00 =  m.m11 / d
    out.m01 = -m.m10 / d
    out.m10 = -m.m01 / d
    out.m11 =  m.m00 / d

    return out
}

fun inverse(
    m: Mat3,
    out: Mat3,
): Mat3 {
    val d = det(m)

    out.m00 =  det(m.m11 / d, m.m12 / d,
        m.m21 / d, m.m22 / d)

    out.m01 = -det(m.m10 / d, m.m12 / d,
        m.m20 / d, m.m22 / d)

    out.m02 =  det(m.m10 / d, m.m11 / d,
        m.m20 / d, m.m21 / d)

    out.m10 = -det(m.m01 / d, m.m02 / d,
        m.m21 / d, m.m22 / d)

    out.m11 =  det(m.m00 / d, m.m02 / d,
        m.m20 / d, m.m22 / d)

    out.m12 = -det(m.m00 / d, m.m01 / d,
        m.m20 / d, m.m21 / d)

    out.m20 =  det(m.m01 / d, m.m02 / d,
        m.m11 / d, m.m12 / d)

    out.m21 = -det(m.m00 / d, m.m02 / d,
        m.m10 / d, m.m12 / d)

    out.m22 =  det(m.m00 / d, m.m01 / d,
        m.m10 / d, m.m11 / d)

    return out
}

fun inverse(
    m: Mat4,
    out: Mat4,
): Mat4 {
    val m00 = m.m00; val m01 = m.m01; val m02 = m.m02; val m03 = m.m03
    val m10 = m.m10; val m11 = m.m11; val m12 = m.m12; val m13 = m.m13
    val m20 = m.m20; val m21 = m.m21; val m22 = m.m22; val m23 = m.m23
    val m30 = m.m30; val m31 = m.m31; val m32 = m.m32; val m33 = m.m33

    val invD = 1f / det(
        m00, m01, m02, m03,
        m10, m11, m12, m13,
        m20, m21, m22, m23,
        m30, m31, m32, m33
    )

    out.m00 = det(
        m11 * invD, m12 * invD, m13 * invD,
        m21 * invD, m22 * invD, m23 * invD,
        m31 * invD, m32 * invD, m33 * invD
    )

    out.m01 = -det(
        m10 * invD, m12 * invD, m13 * invD,
        m20 * invD, m22 * invD, m23 * invD,
        m30 * invD, m32 * invD, m33 * invD
    )

    out.m02 = det(
        m10 * invD, m11 * invD, m13 * invD,
        m20 * invD, m21 * invD, m23 * invD,
        m30 * invD, m31 * invD, m33 * invD
    )

    out.m03 = -det(
        m10 * invD, m11 * invD, m12 * invD,
        m20 * invD, m21 * invD, m22 * invD,
        m30 * invD, m31 * invD, m32 * invD
    )

    out.m10 = -det(
        m01 * invD, m02 * invD, m03 * invD,
        m21 * invD, m22 * invD, m23 * invD,
        m31 * invD, m32 * invD, m33 * invD
    )

    out.m11 = det(
        m00 * invD, m02 * invD, m03 * invD,
        m20 * invD, m22 * invD, m23 * invD,
        m30 * invD, m32 * invD, m33 * invD
    )

    out.m12 = -det(
        m00 * invD, m01 * invD, m03 * invD,
        m20 * invD, m21 * invD, m23 * invD,
        m30 * invD, m31 * invD, m33 * invD
    )

    out.m13 = det(
        m00 * invD, m01 * invD, m02 * invD,
        m20 * invD, m21 * invD, m22 * invD,
        m30 * invD, m31 * invD, m32 * invD
    )

    out.m20 = det(
        m01 * invD, m02 * invD, m03 * invD,
        m11 * invD, m12 * invD, m13 * invD,
        m31 * invD, m32 * invD, m33 * invD
    )

    out.m21 = -det(
        m00 * invD, m02 * invD, m03 * invD,
        m10 * invD, m12 * invD, m13 * invD,
        m30 * invD, m32 * invD, m33 * invD
    )

    out.m22 = det(
        m00 * invD, m01 * invD, m03 * invD,
        m10 * invD, m11 * invD, m13 * invD,
        m30 * invD, m31 * invD, m33 * invD
    )

    out.m23 = -det(
        m00 * invD, m01 * invD, m02 * invD,
        m10 * invD, m11 * invD, m12 * invD,
        m30 * invD, m31 * invD, m32 * invD
    )

    out.m30 = -det(
        m01 * invD, m02 * invD, m03 * invD,
        m11 * invD, m12 * invD, m13 * invD,
        m21 * invD, m22 * invD, m23 * invD
    )

    out.m31 = det(
        m00 * invD, m02 * invD, m03 * invD,
        m10 * invD, m12 * invD, m13 * invD,
        m20 * invD, m22 * invD, m23 * invD
    )

    out.m32 = -det(
        m00 * invD, m01 * invD, m03 * invD,
        m10 * invD, m11 * invD, m13 * invD,
        m20 * invD, m21 * invD, m23 * invD
    )

    out.m33 = det(
        m00 * invD, m01 * invD, m02 * invD,
        m10 * invD, m11 * invD, m12 * invD,
        m20 * invD, m21 * invD, m22 * invD
    )

    return out
}
