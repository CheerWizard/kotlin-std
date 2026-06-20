package com.cws.std.math

import kotlin.math.roundToInt

fun Float.roundToUInt(): UInt = roundToInt().toUInt()
fun Double.roundToUInt(): UInt = roundToInt().toUInt()