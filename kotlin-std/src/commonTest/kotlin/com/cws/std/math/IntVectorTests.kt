package com.cws.std.math

import com.cws.std.math.vectors.Int2
import com.cws.std.math.vectors.Int3
import com.cws.std.math.vectors.Int4
import kotlin.test.Test
import kotlin.test.assertEquals

class IntVectorTests {
    @Test fun int2_constructor()      { val v = Int2(3, 4);          assertEquals(3, v.x); assertEquals(4, v.y) }
    @Test fun int3_from_int2()        { val v = Int3(Int2(1, 2), 3); assertEquals(1, v.x); assertEquals(3, v.z) }
    @Test fun int3_from_scalar()      { val v = Int3(5);             assertEquals(5, v.x); assertEquals(5, v.z) }
    @Test fun int4_from_int3()        { val v = Int4(Int3(1, 2, 3), 4); assertEquals(1, v.x); assertEquals(4, v.w) }
    @Test fun int4_from_int2_int2()   { val v = Int4(Int2(1,2), Int2(3,4)); assertEquals(1, v.x); assertEquals(4, v.w) }
    @Test fun int3_arithmetic()       { val r = Int3(1,2,3) + Int3(4,5,6); assertEquals(5, r.x); assertEquals(9, r.z) }
    @Test fun int4_arithmetic()       { val r = Int4(2,4,6,8) / Int4(2,2,2,2); assertEquals(1, r.x); assertEquals(4, r.w) }
    @Test fun int3_unary_minus()      { val r = -Int3(1,2,3); assertEquals(-1, r.x); assertEquals(-3, r.z) }
}