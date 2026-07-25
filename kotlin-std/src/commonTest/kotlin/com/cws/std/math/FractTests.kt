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

import com.cws.std.math.operators.fract
import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.test.Test
import kotlin.test.assertTrue

class FractTests {
    @Test fun fract_positive() {
        assertNear(0.5f, fract(1.5f))
    }

    @Test fun fract_negative() {
        assertNear(0.75f, fract(-0.25f))
    }

    @Test fun fract_whole_number() {
        assertNear(0f, fract(3f))
    }

    @Test fun fract_zero() {
        assertNear(0f, fract(0f))
    }

    @Test fun fract_always_in_0_1() {
        listOf(-2.7f, -1f, -0.1f, 0f, 0.5f, 1f, 1.9f, 3.14f).forEach { v ->
            val f = fract(v)
            assertTrue(f in 0f..<1f, "fract($v) = $f out of [0,1)")
        }
    }

    @Test fun fract_float2() {
        val r = fract(Float2(1.3f, 2.7f))
        assertNear(0.3f, r.x)
        assertNear(0.7f, r.y)
    }

    @Test fun fract_float3() {
        val r = fract(Float3(1.1f, 2.2f, 3.3f))
        assertNear(0.1f, r.x)
        assertNear(0.2f, r.y)
        assertNear(0.3f, r.z)
    }

    @Test fun fract_float4() {
        val r = fract(Float4(1.1f, 2.2f, 3.3f, 4.4f))
        assertNear(0.1f, r.x)
        assertNear(0.2f, r.y)
        assertNear(0.3f, r.z)
        assertNear(0.4f, r.w)
    }
}
