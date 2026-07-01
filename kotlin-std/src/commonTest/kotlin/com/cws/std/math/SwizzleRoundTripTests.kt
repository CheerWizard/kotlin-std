package com.cws.std.math

import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test

class SwizzleRoundTripTests {
 
    @Test fun float3_reconstruct_from_xy_z() {
        val v = Float3(1f, 2f, 3f)
        val r = Float3(v.xy, v.z)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z)
    }
 
    @Test fun float3_reconstruct_from_x_yz() {
        val v = Float3(1f, 2f, 3f)
        val r = Float3(v.x, v.yz)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z)
    }
 
    @Test fun float4_reconstruct_from_xy_zw() {
        val v = Float4(1f, 2f, 3f, 4f)
        val r = Float4(v.xy, v.zw)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z); assertNear(v.w, r.w)
    }
 
    @Test fun float4_reconstruct_from_xyz_w() {
        val v = Float4(1f, 2f, 3f, 4f)
        val r = Float4(v.xyz, v.w)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z); assertNear(v.w, r.w)
    }
 
    @Test fun float4_reconstruct_from_x_yzw() {
        val v = Float4(1f, 2f, 3f, 4f)
        val r = Float4(v.x, v.yzw)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z); assertNear(v.w, r.w)
    }
 
    @Test fun float4_reconstruct_from_x_yz_w() {
        val v = Float4(1f, 2f, 3f, 4f)
        val r = Float4(v.x, v.yz, v.w)
        assertNear(v.x, r.x); assertNear(v.y, r.y); assertNear(v.z, r.z); assertNear(v.w, r.w)
    }
 
    @Test fun swizzle_reverse_float3() {
        val v = Float3(1f, 2f, 3f)
        val r = v.zyx
        assertNear(3f, r.x); assertNear(2f, r.y); assertNear(1f, r.z)
    }
 
    @Test fun swizzle_reverse_float4() {
        val v = Float4(1f, 2f, 3f, 4f)
        val r = v.wzyx
        assertNear(4f, r.x); assertNear(3f, r.y); assertNear(2f, r.z); assertNear(1f, r.w)
    }
}