package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun fract(x: Float)  = x - kotlin.math.floor(x)
fun fract(v: Float2) = Float2(fract(v.x), fract(v.y))
fun fract(v: Float3) = Float3(fract(v.x), fract(v.y), fract(v.z))
fun fract(v: Float4) = Float4(fract(v.x), fract(v.y), fract(v.z), fract(v.w))