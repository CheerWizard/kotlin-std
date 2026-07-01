package com.cws.std.math

import com.cws.std.math.operators.clamp
import com.cws.std.math.operators.cross
import com.cws.std.math.operators.dot
import com.cws.std.math.operators.fract
import com.cws.std.math.operators.length
import com.cws.std.math.operators.mix
import com.cws.std.math.operators.normalize
import com.cws.std.math.operators.pow
import com.cws.std.math.operators.reflect
import com.cws.std.math.operators.smoothstep
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertTrue

class LinearAlgebraTests {

    @Test
    fun dot_float2()            { assertNear(11f, dot(Float2(1f, 2f), Float2(3f, 4f))) }

    @Test fun dot_float3()            { assertNear(32f, dot(Float3(1f, 2f, 3f), Float3(4f, 5f, 6f))) }

    @Test fun dot_float4()            { assertNear(70f, dot(Float4(1f, 2f, 3f, 4f), Float4(5f, 6f, 7f, 8f))) }

    @Test fun dot_perpendicular()     { assertNear(0f,  dot(Float2(1f, 0f), Float2(0f, 1f))) }

    @Test fun cross_basic() {
        val r = cross(Float3(1f, 0f, 0f), Float3(0f, 1f, 0f))
        assertNear(0f, r.x); assertNear(0f, r.y); assertNear(1f, r.z)
    }

    @Test fun cross_anticommutative() {
        val a = Float3(1f, 2f, 3f); val b = Float3(4f, 5f, 6f)
        val ab = cross(a, b); val ba = cross(b, a)
        assertNear(-ab.x, ba.x); assertNear(-ab.y, ba.y); assertNear(-ab.z, ba.z)
    }

    @Test fun length_float2()         { assertNear(5f, length(Float2(3f, 4f))) }

    @Test fun length_float3()         { assertNear(sqrt(14f), length(Float3(1f, 2f, 3f))) }

    @Test fun length_unit()           { assertNear(1f, length(Float3(1f, 0f, 0f))) }

    @Test fun normalize_float2() {
        val n = normalize(Float2(3f, 4f))
        assertNear(1f, length(n)); assertNear(0.6f, n.x); assertNear(0.8f, n.y)
    }

    @Test fun normalize_float3() {
        val n = normalize(Float3(2f, 3f, 4f))
        assertNear(1f, length(n))
    }

    @Test fun reflect_flat_surface() {
        val r = reflect(Float3(0f, -1f, 0f), Float3(0f, 1f, 0f))
        assertNear(0f, r.x); assertNear(1f, r.y); assertNear(0f, r.z)
    }

    @Test fun reflect_45_degrees() {
        val i = normalize(Float3(1f, -1f, 0f))
        val r = reflect(i, Float3(0f, 1f, 0f))
        assertNear(i.x, r.x); assertNear(-i.y, r.y)
    }

    @Test fun mix_float_0()           { assertNear(1f,   mix(1f, 3f, 0f)) }

    @Test fun mix_float_1()           { assertNear(3f,   mix(1f, 3f, 1f)) }

    @Test fun mix_float_half()        { assertNear(2f,   mix(1f, 3f, 0.5f)) }

    @Test fun mix_float3() {
        val r = mix(Float3(0f), Float3(2f, 4f, 6f), 0.5f)
        assertNear(1f, r.x); assertNear(2f, r.y); assertNear(3f, r.z)
    }

    @Test fun clamp_below_min() { assertNear(0f, clamp(0f, 1f, -1f)) }

    @Test fun clamp_above_max() { assertNear(1f, clamp(0f, 1f,  2f)) }

    @Test fun clamp_in_range()  { assertNear(0.5f, clamp(0f, 1f, 0.5f)) }

    @Test fun clamp_float3() {
        val r = clamp(Float3(-1f, 0.5f, 2f), 0f, 1f)
        assertNear(0f, r.x); assertNear(0.5f, r.y); assertNear(1f, r.z)
    }

    @Test fun smoothstep_edge0()      { assertNear(0f,   smoothstep(0f, 1f, 0f)) }

    @Test fun smoothstep_edge1()      { assertNear(1f,   smoothstep(0f, 1f, 1f)) }

    @Test fun smoothstep_half()       { assertNear(0.5f, smoothstep(0f, 1f, 0.5f)) }

    @Test fun smoothstep_below()      { assertNear(0f,   smoothstep(0f, 1f, -1f)) }

    @Test fun smoothstep_above()      { assertNear(1f,   smoothstep(0f, 1f, 2f)) }

    @Test fun fract_positive()        { assertNear(0.5f, fract(1.5f)) }

    @Test fun fract_whole()           { assertNear(0f,   fract(3f)) }

    @Test fun fract_float3() {
        val r = fract(Float3(1.1f, 2.2f, 3.3f))
        assertNear(0.1f, r.x); assertNear(0.2f, r.y); assertNear(0.3f, r.z)
    }

    @Test fun pow_scalar()            { assertNear(8f,  pow(2f, 3f)) }

    @Test fun pow_float3_scalar() {
        val r = pow(Float3(2f, 3f, 4f), 2f)
        assertNear(4f, r.x); assertNear(9f, r.y); assertNear(16f, r.z)
    }

    @Test fun pow_float3_vec() {
        val r = pow(Float3(2f, 3f, 4f), Float3(1f, 2f, 3f))
        assertNear(2f, r.x); assertNear(9f, r.y); assertNear(64f, r.z)
    }

    @Test fun pow_gamma_encode() {
        val c = Float3(0.5f); val g = pow(c, 1f / 2.2f)
        assertTrue(g.x > c.x)
    }

    @Test fun pow_gamma_decode() {
        val c = Float3(0.5f); val l = pow(c, 2.2f)
        assertTrue(l.x < c.x)
    }

    @Test fun kotlin_math_sin()       { assertNear(0f, sin(0f)) }
    @Test fun kotlin_math_cos()       { assertNear(1f, cos(0f)) }
    @Test fun kotlin_math_sqrt()      { assertNear(2f, sqrt(4f)) }
    @Test fun kotlin_math_abs()       { assertNear(3f, abs(-3f)) }
    @Test fun kotlin_math_floor()     { assertNear(2f, floor(2.7f)) }
    @Test fun kotlin_math_ceil()      { assertNear(3f, ceil(2.1f)) }
    @Test fun kotlin_math_min()       { assertNear(1f, min(1f, 2f)) }
    @Test fun kotlin_math_max()       { assertNear(2f, max(1f, 2f)) }
}