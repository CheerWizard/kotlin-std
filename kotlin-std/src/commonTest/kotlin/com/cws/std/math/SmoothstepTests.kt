package com.cws.std.math

import com.cws.std.math.operators.smoothstep
import kotlin.test.Test
import kotlin.test.assertTrue

class SmoothstepTests {
 
    @Test fun smoothstep_at_edge0()     { assertNear(0f,   smoothstep(0f, 1f, 0f)) }
    @Test fun smoothstep_at_edge1()     { assertNear(1f,   smoothstep(0f, 1f, 1f)) }
    @Test fun smoothstep_at_half()      { assertNear(0.5f, smoothstep(0f, 1f, 0.5f)) }
    @Test fun smoothstep_below_range()  { assertNear(0f,   smoothstep(0f, 1f, -1f)) }
    @Test fun smoothstep_above_range()  { assertNear(1f,   smoothstep(0f, 1f,  2f)) }
 
    @Test fun smoothstep_monotonic() {
        val values = listOf(0f, 0.25f, 0.5f, 0.75f, 1f).map { smoothstep(0f, 1f, it) }
        for (i in 0 until values.size - 1)
            assertTrue(values[i] <= values[i + 1], "Not monotonic at index $i")
    }
 
    @Test fun smoothstep_flat_at_edges() {
        val eps = 0.001f
        val nearStart = smoothstep(0f, 1f, eps) - smoothstep(0f, 1f, 0f)
        val nearEnd   = smoothstep(0f, 1f, 1f)  - smoothstep(0f, 1f, 1f - eps)
        assertTrue(nearStart < eps)
        assertTrue(nearEnd   < eps)
    }
 
    @Test fun smoothstep_symmetric() {
        // smoothstep(0,1,t) + smoothstep(0,1,1-t) == 1
        listOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f).forEach { t ->
            assertNear(1f, smoothstep(0f, 1f, t) + smoothstep(0f, 1f, 1f - t))
        }
    }
}