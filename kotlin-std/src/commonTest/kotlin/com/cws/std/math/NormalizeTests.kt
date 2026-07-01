package com.cws.std.math

import com.cws.std.math.operators.length
import com.cws.std.math.operators.normalize
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test
import kotlin.test.assertTrue

class NormalizeTests {
 
    @Test fun normalize_times_length_equals_original_float2() {
        val v = Float2(3f, 4f)
        val r = normalize(v) * length(v)
        assertNear(v.x, r.x); assertNear(v.y, r.y)
    }
 
    @Test fun normalize_times_length_equals_original_float3() {
        val v = Float3(3f, 4f, 0f)
        val r = normalize(v) * length(v)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z)
    }
 
    @Test fun normalize_unit_length_float2() { assertNear(1f, length(normalize(Float2(3f, 4f)))) }
    @Test fun normalize_unit_length_float3() { assertNear(1f, length(normalize(Float3(1f, 2f, 3f)))) }
    @Test fun normalize_unit_length_float4() { assertNear(1f, length(normalize(
        Float4(
            1f,
            2f,
            3f,
            4f
        )
    ))) }
 
    @Test fun normalize_already_unit() {
        val v = Float3(1f, 0f, 0f)
        val n = normalize(v)
        assertNear(1f, n.x); assertNear(0f, n.y); assertNear(0f, n.z)
    }
 
    @Test fun normalize_zero_vector_does_not_crash() {
        val n = normalize(Float3(0f))
        assertTrue(n.x.isNaN() || n.x == 0f)
    }

}