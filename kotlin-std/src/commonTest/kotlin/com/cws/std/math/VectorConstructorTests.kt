package com.cws.std.math

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test
import kotlin.test.assertEquals

class VectorConstructorTests {
    @Test fun float2_default()       { val v = Float2();            assertEquals(0f, v.x); assertEquals(0f, v.y) }
    @Test fun float2_xy()            { val v = Float2(1f, 2f);      assertEquals(1f, v.x); assertEquals(2f, v.y) }

    @Test fun float3_default()       { val v = Float3();            assertEquals(0f, v.x); assertEquals(0f, v.y); assertEquals(0f, v.z) }
    @Test fun float3_xyz()           { val v = Float3(1f, 2f, 3f);  assertEquals(1f, v.x); assertEquals(2f, v.y); assertEquals(3f, v.z) }
    @Test fun float3_scalar()        { val v = Float3(5f);          assertEquals(5f, v.x); assertEquals(5f, v.y); assertEquals(5f, v.z) }
    @Test fun float3_float2_float()  { val v = Float3(Float2(1f, 2f), 3f); assertEquals(1f, v.x); assertEquals(2f, v.y); assertEquals(3f, v.z) }
    @Test fun float3_float_float2()  { val v = Float3(1f, Float2(2f, 3f)); assertEquals(1f, v.x); assertEquals(2f, v.y); assertEquals(3f, v.z) }

    @Test fun float4_default()       { val v = Float4();                           assertEquals(0f, v.x); assertEquals(0f, v.w) }
    @Test fun float4_xyzw()          { val v = Float4(1f, 2f, 3f, 4f);            assertEquals(1f, v.x); assertEquals(4f, v.w) }
    @Test fun float4_scalar()        { val v = Float4(7f);                         assertEquals(7f, v.x); assertEquals(7f, v.w) }
    @Test fun float4_float3_float()  { val v = Float4(Float3(1f, 2f, 3f), 4f);    assertEquals(1f, v.x); assertEquals(3f, v.z); assertEquals(4f, v.w) }
    @Test fun float4_float_float3()  { val v = Float4(1f, Float3(2f, 3f, 4f));    assertEquals(1f, v.x); assertEquals(2f, v.y); assertEquals(4f, v.w) }
    @Test fun float4_float2_float2() { val v = Float4(Float2(1f, 2f), Float2(3f, 4f)); assertEquals(1f, v.x); assertEquals(3f, v.z) }
    @Test fun float4_float2_ff()     { val v = Float4(Float2(1f, 2f), 3f, 4f);    assertEquals(1f, v.x); assertEquals(2f, v.y); assertEquals(3f, v.z); assertEquals(4f, v.w) }
    @Test fun float4_ff_float2()     { val v = Float4(1f, 2f, Float2(3f, 4f));    assertEquals(3f, v.z); assertEquals(4f, v.w) }
    @Test fun float4_f_float2_f()    { val v = Float4(1f, Float2(2f, 3f), 4f);    assertEquals(2f, v.y); assertEquals(3f, v.z) }
}