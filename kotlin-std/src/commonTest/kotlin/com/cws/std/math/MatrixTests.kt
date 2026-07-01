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
        Float4(1f, 0f, 0f, 0f), Float4(0f, 1f, 0f, 0f),
        Float4(0f, 0f, 1f, 0f), Float4(0f, 0f, 0f, 1f)
    )
 
    @Test fun mat4_identity_times_vec() {
        val r = identity4() * Float4(1f, 2f, 3f, 4f)
        assertNear(1f, r.x); assertNear(2f, r.y); assertNear(3f, r.z); assertNear(4f, r.w)
    }
 
    @Test fun mat4_identity_times_mat() {
        val m = Mat4(Float4(1f,2f,3f,4f), Float4(5f,6f,7f,8f), Float4(9f,10f,11f,12f), Float4(13f,14f,15f,16f))
        val r = identity4() * m
        assertNear(1f, r.v1.x); assertNear(6f, r.v2.y); assertNear(11f, r.v3.z); assertNear(16f, r.v4.w)
    }
 
    @Test fun mat4_scale_vec() {
        val scale = Mat4(Float4(2f,0f,0f,0f), Float4(0f,3f,0f,0f), Float4(0f,0f,4f,0f), Float4(0f,0f,0f,1f))
        val r = scale * Float4(1f, 1f, 1f, 1f)
        assertNear(2f, r.x); assertNear(3f, r.y); assertNear(4f, r.z); assertNear(1f, r.w)
    }
 
    @Test fun mat4_scalar_multiply() {
        val r = identity4() * 3f
        assertNear(3f, r.v1.x); assertNear(3f, r.v2.y); assertNear(0f, r.v1.y)
    }
 
    @Test fun mat4_unary_minus() {
        val r = -identity4()
        assertNear(-1f, r.v1.x); assertNear(-1f, r.v2.y); assertNear(0f, r.v1.y)
    }
 
    @Test fun mat2_identity_times_vec() {
        val i = Mat2(Float2(1f, 0f), Float2(0f, 1f))
        val r = i * Float2(3f, 4f)
        assertNear(3f, r.x); assertNear(4f, r.y)
    }
 
    @Test fun mat3_identity_times_vec() {
        val i = Mat3(Float3(1f, 0f, 0f), Float3(0f, 1f, 0f), Float3(0f, 0f, 1f))
        val r = i * Float3(1f, 2f, 3f)
        assertNear(1f, r.x); assertNear(2f, r.y); assertNear(3f, r.z)
    }
}