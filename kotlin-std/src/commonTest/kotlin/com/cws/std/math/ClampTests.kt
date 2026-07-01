package com.cws.std.math

import com.cws.std.math.operators.clamp
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test

class ClampTests {
 
    @Test fun clamp_below_min()         { assertNear(0f,   clamp(0f, 1f, -1f)) }
    @Test fun clamp_above_max()         { assertNear(1f,   clamp(0f, 1f,  2f)) }
    @Test fun clamp_in_range()          { assertNear(0.5f, clamp(0f, 1f,  0.5f)) }
    @Test fun clamp_at_min_boundary()   { assertNear(0f,   clamp(0f, 1f,  0f)) }
    @Test fun clamp_at_max_boundary()   { assertNear(1f,   clamp(0f, 1f,  1f)) }
    @Test fun clamp_min_equals_max()    { assertNear(0.5f, clamp(0.5f, 0.5f, 0.5f)) }
 
    @Test fun clamp_float2_scalar() {
        val r = clamp(Float2(-1f, 2f), 0f, 1f)
        assertNear(0f, r.x); assertNear(1f, r.y)
    }
 
    @Test fun clamp_float3_scalar() {
        val r = clamp(Float3(-1f, 0.5f, 2f), 0f, 1f)
        assertNear(0f, r.x); assertNear(0.5f, r.y); assertNear(1f, r.z)
    }
 
    @Test fun clamp_float4_scalar() {
        val r = clamp(Float4(-1f, 0.5f, 2f, 0.25f), 0f, 1f)
        assertNear(0f, r.x); assertNear(0.5f, r.y); assertNear(1f, r.z); assertNear(0.25f, r.w)
    }
 
    @Test fun clamp_float3_vector_bounds() {
        val r = clamp(Float3(-1f, 0.5f, 2f), Float3(0f, 0f, 0f), Float3(1f, 1f, 1f))
        assertNear(0f, r.x); assertNear(0.5f, r.y); assertNear(1f, r.z)
    }
}