package com.cws.std.math

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test
import kotlin.test.assertEquals

class VectorSwizzleTests {
    @Test fun float2_swizzle_xx()    { val v = Float2(1f, 2f);         assertEquals(1f, v.xx.x); assertEquals(1f, v.xx.y) }
    @Test fun float2_swizzle_yx()    { val v = Float2(1f, 2f);         assertEquals(2f, v.yx.x); assertEquals(1f, v.yx.y) }
    @Test fun float2_swizzle_xxx()   { val v = Float2(1f, 2f);         assertEquals(1f, v.xxx.x); assertEquals(1f, v.xxx.z) }

    @Test fun float3_swizzle_xy()    { val v = Float3(1f, 2f, 3f);     assertEquals(1f, v.xy.x); assertEquals(2f, v.xy.y) }
    @Test fun float3_swizzle_xz()    { val v = Float3(1f, 2f, 3f);     assertEquals(1f, v.xz.x); assertEquals(3f, v.xz.y) }
    @Test fun float3_swizzle_yz()    { val v = Float3(1f, 2f, 3f);     assertEquals(2f, v.yz.x); assertEquals(3f, v.yz.y) }
    @Test fun float3_swizzle_xyz()   { val v = Float3(1f, 2f, 3f);     assertEquals(1f, v.xyz.x); assertEquals(2f, v.xyz.y); assertEquals(3f, v.xyz.z) }
    @Test fun float3_swizzle_zyx()   { val v = Float3(1f, 2f, 3f);     assertEquals(3f, v.zyx.x); assertEquals(1f, v.zyx.z) }
    @Test fun float3_swizzle_rgb()   { val v = Float3(1f, 2f, 3f);     assertEquals(1f, v.r); assertEquals(2f, v.g); assertEquals(3f, v.b) }

    @Test fun float4_swizzle_xy()    { val v = Float4(1f, 2f, 3f, 4f); assertEquals(1f, v.xy.x); assertEquals(2f, v.xy.y) }
    @Test fun float4_swizzle_xyz()   { val v = Float4(1f, 2f, 3f, 4f); assertEquals(1f, v.xyz.x); assertEquals(3f, v.xyz.z) }
    @Test fun float4_swizzle_xyzw()  { val v = Float4(1f, 2f, 3f, 4f); assertEquals(4f, v.xyzw.w) }
    @Test fun float4_swizzle_zw()    { val v = Float4(1f, 2f, 3f, 4f); assertEquals(3f, v.zw.x); assertEquals(4f, v.zw.y) }
    @Test fun float4_swizzle_wzyx()  { val v = Float4(1f, 2f, 3f, 4f); assertEquals(4f, v.wzyx.x); assertEquals(1f, v.wzyx.w) }
    @Test fun float4_swizzle_rgba()  { val v = Float4(1f, 2f, 3f, 4f); assertEquals(1f, v.r); assertEquals(4f, v.a) }
    @Test fun float4_swizzle_rgb()   { val v = Float4(1f, 2f, 3f, 4f); assertEquals(3f, v.rgb.z) }
}