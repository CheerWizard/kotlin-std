package com.cws.std.math

import kotlin.math.abs
import kotlin.test.assertTrue

private val eps = 0.0001f

fun assertNear(expected: Float, actual: Float) = assertTrue(abs(expected - actual) < eps, "Expected $expected but was $actual")