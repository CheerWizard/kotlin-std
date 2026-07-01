package com.cws.std.math

import com.cws.std.math.operators.cross
import com.cws.std.math.operators.dot
import com.cws.std.math.vectors.Float3
import kotlin.test.Test

class CrossProductTests {
 
    @Test fun cross_perpendicular_to_both_inputs() {
        val a = Float3(1f, 2f, 3f); val b = Float3(4f, 5f, 6f)
        val c = cross(a, b)
        assertNear(0f, dot(c, a))
        assertNear(0f, dot(c, b))
    }
 
    @Test fun cross_x_y_equals_z() {
        val r = cross(Float3(1f, 0f, 0f), Float3(0f, 1f, 0f))
        assertNear(0f, r.x); assertNear(0f, r.y); assertNear(1f, r.z)
    }
 
    @Test fun cross_anticommutative() {
        val a = Float3(1f, 2f, 3f); val b = Float3(4f, 5f, 6f)
        val ab = cross(a, b); val ba = cross(b, a)
        assertNear(-ab.x, ba.x); assertNear(-ab.y, ba.y); assertNear(-ab.z, ba.z)
    }
 
    @Test fun cross_parallel_vectors_is_zero() {
        val r = cross(Float3(1f, 0f, 0f), Float3(2f, 0f, 0f))
        assertNear(0f, r.x); assertNear(0f, r.y); assertNear(0f, r.z)
    }
}