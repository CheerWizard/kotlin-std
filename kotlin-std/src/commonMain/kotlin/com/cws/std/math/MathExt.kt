package com.cws.std.math

import kotlin.math.roundToInt
import kotlin.math.sqrt

fun sqrt(value: Int): Int = sqrt(value.toDouble()).roundToInt()
fun sqrt(value: UInt): UInt = sqrt(value.toDouble()).roundToUInt()