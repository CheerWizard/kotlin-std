package com.cws.std.math

import com.cws.std.math.operators.dot
import com.cws.std.math.operators.length
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test

class DotProductTests {
 
    @Test fun dot_self_equals_length_squared_float2() {
        val v = Float2(3f, 4f)
        assertNear(dot(v, v), length(v) * length(v))
    }
 
    @Test fun dot_self_equals_length_squared_float3() {
        val v = Float3(1f, 2f, 3f)
        assertNear(dot(v, v), length(v) * length(v))
    }
 
    @Test fun dot_self_equals_length_squared_float4() {
        val v = Float4(1f, 2f, 3f, 4f)
        assertNear(dot(v, v), length(v) * length(v))
    }
 
    @Test fun dot_commutative_float3() {
        val a = Float3(1f, 2f, 3f); val b = Float3(4f, 5f, 6f)
        assertNear(dot(a, b), dot(b, a))
    }
 
    @Test fun dot_perpendicular_float3() {
        assertNear(0f, dot(Float3(1f, 0f, 0f), Float3(0f, 1f, 0f)))
        assertNear(0f, dot(Float3(1f, 0f, 0f), Float3(0f, 0f, 1f)))
        assertNear(0f, dot(Float3(0f, 1f, 0f), Float3(0f, 0f, 1f)))
    }

}