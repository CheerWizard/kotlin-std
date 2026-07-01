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
    private fun assertNear(expected: Float, actual: Float) =
        assertTrue(abs(expected - actual) < eps)

    @Test fun mat2_times_scalar() {
        val m = Mat2(Float2(1f, 2f), Float2(3f, 4f)) * 2f
        assertNear(2f, m.v1.x); assertNear(8f, m.v2.y)
    }

    @Test fun mat2_times_identity_vec() {
        val identity = Mat2(Float2(1f, 0f), Float2(0f, 1f))
        val r = identity * Float2(3f, 4f)
        assertNear(3f, r.x); assertNear(4f, r.y)
    }

    @Test fun mat2_unary_minus() {
        val r = -Mat2(Float2(1f, 2f), Float2(3f, 4f))
        assertNear(-1f, r.v1.x); assertNear(-4f, r.v2.y)
    }

    @Test fun mat4_times_identity_vec() {
        val i = Mat4(Float4(1f,0f,0f,0f), Float4(0f,1f,0f,0f), Float4(0f,0f,1f,0f), Float4(0f,0f,0f,1f))
        val r = i * Float4(1f, 2f, 3f, 4f)
        assertNear(1f, r.x); assertNear(2f, r.y); assertNear(3f, r.z); assertNear(4f, r.w)
    }

    @Test fun mat4_times_scalar() {
        val i = Mat4(Float4(1f,0f,0f,0f), Float4(0f,1f,0f,0f), Float4(0f,0f,1f,0f), Float4(0f,0f,0f,1f))
        val r = i * 3f
        assertNear(3f, r.v1.x); assertNear(3f, r.v2.y); assertNear(0f, r.v1.y)
    }

    @Test fun mat4_times_identity_mat() {
        val i = Mat4(Float4(1f,0f,0f,0f), Float4(0f,1f,0f,0f), Float4(0f,0f,1f,0f), Float4(0f,0f,0f,1f))
        val m = Mat4(
            Float4(1f, 2f, 3f, 4f), Float4(5f, 6f, 7f, 8f), Float4(9f, 10f, 11f, 12f),
            Float4(13f, 14f, 15f, 16f)
        )
        val r = i * m
        assertNear(1f, r.v1.x); assertNear(6f, r.v2.y); assertNear(11f, r.v3.z); assertNear(16f, r.v4.w)
    }
}