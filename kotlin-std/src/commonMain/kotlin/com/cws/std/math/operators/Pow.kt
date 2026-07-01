package com.cws.std.math.operators

import com.cws.std.math.vectors.Float2
import com.cws.std.math.vectors.Float3
import com.cws.std.math.vectors.Float4
import kotlin.math.pow
import kotlin.math.roundToInt

fun pow(v: Float, exp: Float): Float = v.pow(exp)
fun pow(v: Float, exp: Int): Float = v.pow(exp)
fun pow(v: Double, exp: Double): Double = v.pow(exp)
fun pow(v: Double, exp: Int): Double = v.pow(exp)
fun pow(v: Int, exp: Int): Int = v.toFloat().pow(exp).roundToInt()

fun pow(v: Float2, exp: Float)  = Float2(pow(v.x, exp), pow(v.y, exp))
fun pow(v: Float2, exp: Float2) = Float2(pow(v.x, exp.x), pow(v.y, exp.y))

fun pow(v: Float3, exp: Float)  = Float3(pow(v.x, exp), pow(v.y, exp), pow(v.z, exp))
fun pow(v: Float3, exp: Float3) = Float3(pow(v.x, exp.x), pow(v.y, exp.y), pow(v.z, exp.z))

fun pow(v: Float4, exp: Float)  = Float4(pow(v.x, exp), pow(v.y, exp), pow(v.z, exp), pow(v.w, exp))
fun pow(v: Float4, exp: Float4) = Float4(pow(v.x, exp.x), pow(v.y, exp.y), pow(v.z, exp.z), pow(v.w, exp.w))