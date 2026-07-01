package com.cws.std.math

import com.cws.std.math.operators.mix
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test

class MixTests {
 
    @Test fun mix_at_0_returns_a()      { assertNear(1f,   mix(1f, 3f, 0f)) }
    @Test fun mix_at_1_returns_b()      { assertNear(3f,   mix(1f, 3f, 1f)) }
    @Test fun mix_at_half_is_midpoint() { assertNear(2f,   mix(1f, 3f, 0.5f)) }
    @Test fun mix_extrapolate()         { assertNear(5f,   mix(1f, 3f, 2f)) }
    @Test fun mix_negative_t()          { assertNear(-1f,  mix(1f, 3f, -1f)) }
 
    @Test fun mix_float3_at_half() {
        val r = mix(Float3(0f), Float3(2f, 4f, 6f), 0.5f)
        assertNear(1f, r.x); assertNear(2f, r.y); assertNear(3f, r.z)
    }
 
    @Test fun mix_float4_at_0() {
        val a = Float4(1f, 2f, 3f, 4f); val b = Float4(5f, 6f, 7f, 8f)
        val r = mix(a, b, 0f)
        assertNear(a.x, r.x); assertNear(a.w, r.w)
    }
 
    @Test fun mix_commutative_at_half() {
        val a = 2f; val b = 4f
        assertNear(mix(a, b, 0.5f), mix(b, a, 0.5f))
    }
}