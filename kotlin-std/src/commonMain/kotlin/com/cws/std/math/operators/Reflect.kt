package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4

fun reflect(i: Float2, n: Float2) = i - n * (2f * dot(n, i))
fun reflect(i: Float3, n: Float3) = i - n * (2f * dot(n, i))
fun reflect(i: Float4, n: Float4) = i - n * (2f * dot(n, i))