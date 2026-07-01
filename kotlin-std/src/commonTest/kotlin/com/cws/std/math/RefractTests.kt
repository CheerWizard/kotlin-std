package com.cws.std.math

import com.cws.std.math.operators.normalize
import com.cws.std.math.operators.refract
import com.cws.std.math.vectors.Float3
import kotlin.test.Test

class RefractTests {
 
    @Test fun refract_same_medium_eta_1() {
        val i = normalize(Float3(1f, -1f, 0f))
        val n = Float3(0f, 1f, 0f)
        val r = refract(i, n, 1f)
        assertNear(i.x, r.x); assertNear(i.y, r.y)
    }
 
    @Test fun refract_total_internal_reflection_returns_zero() {
        // High eta + grazing angle → total internal reflection → zero vector
        val i = normalize(Float3(1f, -0.1f, 0f))
        val n = Float3(0f, 1f, 0f)
        val r = refract(i, n, 2f)
        assertNear(0f, r.x); assertNear(0f, r.y); assertNear(0f, r.z)
    }
 
    @Test fun refract_perpendicular_incidence() {
        // Straight-on incidence — no bending regardless of eta
        val i = Float3(0f, -1f, 0f)
        val n = Float3(0f,  1f, 0f)
        val r = refract(i, n, 1.5f)
        assertNear(0f, r.x); assertNear(-1f, r.y); assertNear(0f, r.z)
    }
}