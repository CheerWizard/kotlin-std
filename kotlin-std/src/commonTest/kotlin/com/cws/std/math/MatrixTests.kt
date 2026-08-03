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
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test

class MatrixTests {

    private fun identity4() = Mat4(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )

    @Test
    fun mat4_identity_times_vec() {
        val r = identity4() * Float4(1f, 2f, 3f, 4f)

        assertNear(1f, r.x)
        assertNear(2f, r.y)
        assertNear(3f, r.z)
        assertNear(4f, r.w)
    }

    @Test
    fun mat4_identity_times_mat() {
        val m = Mat4(
            1f, 2f, 3f, 4f,
            5f, 6f, 7f, 8f,
            9f, 10f, 11f, 12f,
            13f, 14f, 15f, 16f
        )

        val r = identity4() * m

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

    @Test
    fun mat4_scale_vec() {
        val scale = Mat4(
            2f, 0f, 0f, 0f,
            0f, 3f, 0f, 0f,
            0f, 0f, 4f, 0f,
            0f, 0f, 0f, 1f
        )

        val r = scale * Float4(1f, 1f, 1f, 1f)

        assertNear(2f, r.x)
        assertNear(3f, r.y)
        assertNear(4f, r.z)
        assertNear(1f, r.w)
    }

    @Test
    fun mat4_scalar_multiply() {
        val r = identity4() * 3f

        assertNear(3f, r.m00)
        assertNear(3f, r.m11)
        assertNear(3f, r.m22)
        assertNear(3f, r.m33)

        assertNear(0f, r.m01)
        assertNear(0f, r.m10)
    }

    @Test
    fun mat4_unary_minus() {
        val r = -identity4()

        assertNear(-1f, r.m00)
        assertNear(-1f, r.m11)
        assertNear(-1f, r.m22)
        assertNear(-1f, r.m33)

        assertNear(0f, r.m01)
        assertNear(0f, r.m10)
    }

    @Test
    fun mat2_identity_times_vec() {
        val i = Mat2(
            1f, 0f,
            0f, 1f
        )

        val r = i * Float2(3f, 4f)

        assertNear(3f, r.x)
        assertNear(4f, r.y)
    }

    @Test
    fun mat3_identity_times_vec() {
        val i = Mat3(
            1f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f
        )

        val r = i * Float3(1f, 2f, 3f)

        assertNear(1f, r.x)
        assertNear(2f, r.y)
        assertNear(3f, r.z)
    }
}
