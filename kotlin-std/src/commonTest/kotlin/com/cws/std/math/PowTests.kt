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

import com.cws.std.math.operators.pow
import com.cws.std.math.vectors.Float3
import kotlin.test.Test
import kotlin.test.assertTrue

class PowTests {
    @Test
    fun pow_scalar_basic() {
        assertNear(8f, pow(2f, 3f))
    }

    @Test fun pow_to_one() {
        assertNear(5f, pow(5f, 1f))
    }

    @Test fun pow_to_zero() {
        assertNear(1f, pow(5f, 0f))
    }

    @Test fun pow_float3_scalar() {
        val r = pow(Float3(2f, 3f, 4f), 2f)
        assertNear(4f, r.x)
        assertNear(9f, r.y)
        assertNear(16f, r.z)
    }

    @Test fun pow_float3_vector() {
        val r = pow(Float3(2f, 3f, 4f), Float3(1f, 2f, 3f))
        assertNear(2f, r.x)
        assertNear(9f, r.y)
        assertNear(64f, r.z)
    }

    @Test fun pow_gamma_encode_brightens_midtones() {
        val c = Float3(0.5f)
        val g = pow(c, 1f / 2.2f)
        assertTrue(g.x > c.x)
    }

    @Test fun pow_gamma_decode_darkens_midtones() {
        val c = Float3(0.5f)
        val l = pow(c, 2.2f)
        assertTrue(l.x < c.x)
    }

    @Test fun pow_gamma_roundtrip() {
        val original = Float3(0.3f, 0.6f, 0.9f)
        val decoded = pow(pow(original, 2.2f), 1f / 2.2f)
        assertNear(original.x, decoded.x)
        assertNear(original.y, decoded.y)
        assertNear(original.z, decoded.z)
    }
}
