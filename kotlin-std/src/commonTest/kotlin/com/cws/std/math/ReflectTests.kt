package com.cws.std.math

import com.cws.std.math.operators.length
import com.cws.std.math.operators.normalize
import com.cws.std.math.operators.reflect
import com.cws.std.math.vectors.Float3
import kotlin.test.Test

class ReflectTests {
 
    @Test fun reflect_straight_down_off_flat_surface() {
        val r = reflect(Float3(0f, -1f, 0f), Float3(0f, 1f, 0f))
        assertNear(0f, r.x); assertNear(1f, r.y); assertNear(0f, r.z)
    }
 
    @Test fun reflect_45_degrees() {
        val i = normalize(Float3(1f, -1f, 0f))
        val r = reflect(i, Float3(0f, 1f, 0f))
        assertNear(i.x, r.x); assertNear(-i.y, r.y)
    }
 
    @Test fun reflect_twice_is_identity() {
        val i = normalize(Float3(1f, -1f, 0f))
        val n = Float3(0f, 1f, 0f)
        val r = reflect(reflect(i, n), n)
        assertNear(i.x, r.x); assertNear(i.y, r.y); assertNear(i.z, r.z)
    }
 
    @Test fun reflect_already_aligned_with_normal() {
        val r = reflect(Float3(0f, 1f, 0f), Float3(0f, 1f, 0f))
        assertNear(-1f, r.y)
    }
 
    @Test fun reflect_preserves_length() {
        val i = Float3(1f, -2f, 0.5f)
        val r = reflect(normalize(i), Float3(0f, 1f, 0f))
        assertNear(1f, length(r))
    }
}