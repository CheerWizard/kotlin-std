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
package com.cws.std.math

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat4
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float4
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class MatrixArithmeticTests {

    private val eps = 0.0001f

    private fun assertNear(
        expected: Float,
        actual: Float,
    ) = assertTrue(abs(expected - actual) < eps)

    @Test
    fun mat2_times_scalar() {
        val m = Mat2(
            1f, 2f,
            3f, 4f
        ) * 2f

        assertNear(2f, m.m00)
        assertNear(4f, m.m01)
        assertNear(6f, m.m10)
        assertNear(8f, m.m11)
    }

    @Test
    fun mat2_times_identity_vec() {
        val identity = Mat2(
            1f, 0f,
            0f, 1f
        )

        val r = identity * Float2(3f, 4f)

        assertNear(3f, r.x)
        assertNear(4f, r.y)
    }

    @Test
    fun mat2_unary_minus() {
        val r = -Mat2(
            1f, 2f,
            3f, 4f
        )

        assertNear(-1f, r.m00)
        assertNear(-2f, r.m01)
        assertNear(-3f, r.m10)
        assertNear(-4f, r.m11)
    }

    @Test
    fun mat4_times_identity_vec() {
        val i = Mat4(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        val r = i * Float4(1f, 2f, 3f, 4f)

        assertNear(1f, r.x)
        assertNear(2f, r.y)
        assertNear(3f, r.z)
        assertNear(4f, r.w)
    }

    @Test
    fun mat4_times_scalar() {
        val i = Mat4(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        val r = i * 3f

        assertNear(3f, r.m00)
        assertNear(3f, r.m11)
        assertNear(3f, r.m22)
        assertNear(3f, r.m33)

        assertNear(0f, r.m01)
        assertNear(0f, r.m10)
    }

    @Test
    fun mat4_times_identity_mat() {
        val i = Mat4(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f
        )

        val m = Mat4(
            1f,  2f,  3f,  4f,
            5f,  6f,  7f,  8f,
            9f, 10f, 11f, 12f,
            13f,14f,15f,16f
        )

        val r = i * m

        assertNear(1f, r.m00)
        assertNear(2f, r.m01)
        assertNear(3f, r.m02)
        assertNear(4f, r.m03)

        assertNear(5f, r.m10)
        assertNear(6f, r.m11)
        assertNear(7f, r.m12)
        assertNear(8f, r.m13)

        assertNear(9f, r.m20)
        assertNear(10f, r.m21)
        assertNear(11f, r.m22)
        assertNear(12f, r.m23)

        assertNear(13f, r.m30)
        assertNear(14f, r.m31)
        assertNear(15f, r.m32)
        assertNear(16f, r.m33)
    }
}