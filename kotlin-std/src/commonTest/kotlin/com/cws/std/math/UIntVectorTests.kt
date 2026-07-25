/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.math

import com.cws.std.math.vectors.UInt2
import com.cws.std.math.vectors.UInt3
import com.cws.std.math.vectors.UInt4
import kotlin.test.Test
import kotlin.test.assertEquals

class UIntVectorTests {
    @Test fun uint3_constructor() {
        val v = UInt3(1u, 2u, 3u)
        assertEquals(1u, v.x)
        assertEquals(3u, v.z)
    }

    @Test fun uint3_from_uint2() {
        val v = UInt3(UInt2(1u, 2u), 3u)
        assertEquals(1u, v.x)
        assertEquals(3u, v.z)
    }

    @Test fun uint3_from_scalar() {
        val v = UInt3(5u)
        assertEquals(5u, v.x)
        assertEquals(5u, v.z)
    }

    @Test fun uint4_from_uint3() {
        val v = UInt4(UInt3(1u, 2u, 3u), 4u)
        assertEquals(4u, v.w)
    }

    @Test fun uint4_from_uint2_uint2() {
        val v = UInt4(UInt2(1u, 2u), UInt2(3u, 4u))
        assertEquals(1u, v.x)
        assertEquals(4u, v.w)
    }

    @Test fun uint3_arithmetic() {
        val r = UInt3(6u, 4u, 2u) / UInt3(2u, 2u, 2u)
        assertEquals(3u, r.x)
        assertEquals(1u, r.z)
    }
}
