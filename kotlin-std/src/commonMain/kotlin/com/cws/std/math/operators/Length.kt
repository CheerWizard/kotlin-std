package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun length(v: Float2) = kotlin.math.sqrt(v.x * v.x + v.y * v.y)
fun length(v: Float3) = kotlin.math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z)
fun length(v: Float4) = kotlin.math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z + v.w * v.w)