package com.cws.std.math

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test
import kotlin.test.assertEquals

class VectorArithmeticTests {
    @Test fun float2_plus_vec()     { val r = Float2(1f, 2f) + Float2(3f, 4f);    assertEquals(4f, r.x); assertEquals(6f, r.y) }
    @Test fun float2_minus_vec()    { val r = Float2(3f, 4f) - Float2(1f, 2f);    assertEquals(2f, r.x); assertEquals(2f, r.y) }
    @Test fun float2_times_vec()    { val r = Float2(2f, 3f) * Float2(4f, 5f);    assertEquals(8f, r.x); assertEquals(15f, r.y) }
    @Test fun float2_div_vec()      { val r = Float2(8f, 6f) / Float2(2f, 3f);    assertEquals(4f, r.x); assertEquals(2f, r.y) }
    @Test fun float2_plus_scalar()  { val r = Float2(1f, 2f) + 3f;                assertEquals(4f, r.x); assertEquals(5f, r.y) }
    @Test fun float2_times_scalar() { val r = Float2(2f, 3f) * 4f;                assertEquals(8f, r.x); assertEquals(12f, r.y) }
    @Test fun float2_unary_minus()  { val r = -Float2(1f, -2f);                   assertEquals(-1f, r.x); assertEquals(2f, r.y) }

    @Test fun float3_plus_vec()     { val r = Float3(1f, 2f, 3f) + Float3(4f, 5f, 6f); assertEquals(5f, r.x); assertEquals(9f, r.z) }
    @Test fun float3_minus_vec()    { val r = Float3(4f, 5f, 6f) - Float3(1f, 2f, 3f); assertEquals(3f, r.x); assertEquals(3f, r.z) }
    @Test fun float3_times_vec()    { val r = Float3(2f, 3f, 4f) * Float3(5f, 6f, 7f); assertEquals(10f, r.x); assertEquals(28f, r.z) }
    @Test fun float3_div_vec()      { val r = Float3(10f, 6f, 8f) / Float3(2f, 3f, 4f); assertEquals(5f, r.x); assertEquals(2f, r.z) }
    @Test fun float3_times_scalar() { val r = Float3(1f, 2f, 3f) * 2f;           assertEquals(2f, r.x); assertEquals(6f, r.z) }
    @Test fun float3_unary_minus()  { val r = -Float3(1f, -2f, 3f);              assertEquals(-1f, r.x); assertEquals(2f, r.y); assertEquals(-3f, r.z) }

    @Test fun float4_plus_vec()     { val r = Float4(1f, 2f, 3f, 4f) + Float4(1f, 1f, 1f, 1f); assertEquals(2f, r.x); assertEquals(5f, r.w) }
    @Test fun float4_times_scalar() { val r = Float4(1f, 2f, 3f, 4f) * 2f;      assertEquals(2f, r.x); assertEquals(8f, r.w) }
    @Test fun float4_unary_minus()  { val r = -Float4(1f, 2f, 3f, 4f);           assertEquals(-1f, r.x); assertEquals(-4f, r.w) }
}