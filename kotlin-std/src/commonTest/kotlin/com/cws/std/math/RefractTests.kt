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

import com.cws.std.math.operators.normalize
import com.cws.std.math.operators.refract
import com.cws.std.math.vectors.Float3
import kotlin.test.Test

class RefractTests {
    @Test fun refract_same_medium_eta_1() {
        val i = normalize(Float3(1f, -1f, 0f))
        val n = Float3(0f, 1f, 0f)
        val r = refract(i, n, 1f)
        assertNear(i.x, r.x)
        assertNear(i.y, r.y)
    }

    @Test fun refract_total_internal_reflection_returns_zero() {
        // High eta + grazing angle → total internal reflection → zero vector
        val i = normalize(Float3(1f, -0.1f, 0f))
        val n = Float3(0f, 1f, 0f)
        val r = refract(i, n, 2f)
        assertNear(0f, r.x)
        assertNear(0f, r.y)
        assertNear(0f, r.z)
    }

    @Test fun refract_perpendicular_incidence() {
        // Straight-on incidence — no bending regardless of eta
        val i = Float3(0f, -1f, 0f)
        val n = Float3(0f, 1f, 0f)
        val r = refract(i, n, 1.5f)
        assertNear(0f, r.x)
        assertNear(-1f, r.y)
        assertNear(0f, r.z)
    }
}
