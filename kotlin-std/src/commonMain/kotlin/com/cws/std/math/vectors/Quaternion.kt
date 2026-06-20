package com.cws.std.math.vectors

import com.cws.std.math.operators.Radians
import com.cws.std.math.operators.dot
import com.cws.std.math.operators.radians
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.NativeBuffer
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES
import com.cws.std.memory.nextFloat
import com.cws.std.memory.pushFloat
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Quaternion(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f,
) {

    operator fun get(i: Int): Float {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            3 -> w
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    operator fun set(i: Int, v: Float) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            3 -> w = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 3]")
        }
    }

    val length: Float get() {
        val x = x
        val y = y
        val z = z
        val w = w
        return sqrt(x * x + y * y + z * z + w * w)
    }

    fun normalize(): Quaternion {
        return com.cws.std.math.operators.normalize(this, this)
    }

    operator fun plus(v: Float): Quaternion {
        return Quaternion(x + v, y + v, z + v, w + v)
    }

    operator fun minus(v: Float): Quaternion {
        return Quaternion(x - v, y - v, z - v, w - v)
    }

    operator fun times(v: Float): Quaternion {
        return Quaternion(x * v, y * v, z * v, w * v)
    }

    operator fun div(v: Float): Quaternion {
        return Quaternion(x / v, y / v, z / v, w / v)
    }

    operator fun plus(v: Quaternion): Quaternion {
        return Quaternion(x + v.x, y + v.y, z + v.z, w + v.w)
    }

    operator fun minus(v: Quaternion): Quaternion {
        return Quaternion(x - v.x, y - v.y, z - v.z, w - v.w)
    }

    operator fun times(v: Quaternion): Quaternion {
        return Quaternion(
            w * v.x + x * v.w + y * v.z - z * v.y,
            w * v.y - x * v.z + y * v.w + z * v.x,
            w * v.z + x * v.y - y * v.x + z * v.w,
            w * v.w - x * v.x - y * v.y - z * v.z
        )
    }

    operator fun div(v: Quaternion): Quaternion {
        return Quaternion(x / v.x, y / v.y, z / v.z, w / v.w)
    }

    operator fun unaryMinus(): Quaternion {
        return Quaternion(-x, -y, -z, w)
    }

    fun fromAngle(nx: Float, ny: Float, nz: Float, r: Radians): Quaternion {
        x = nx * sin(r * 0.5f)
        y = ny * sin(r * 0.5f)
        z = nz * sin(r * 0.5f)
        w = cos(r * 0.5f)
        return this
    }

    fun fromAngle(n: Float3, r: Radians): Quaternion = fromAngle(n.x, n.y, n.z, r)

    fun rotate(n: Float3): Quaternion {
        val q = this
        val r = Quaternion()
        return q * r.fromAngle(n, 0f.radians) * -q
    }

    fun slerp(q: Quaternion, t: Float): Quaternion {
        val q1 = this
        val q2 = q

        val dot = dot(q1, q2)
        var theta = acos(dot)
        if (theta < 0.0) {
            theta = -theta
        }

        val st = sin(theta)
        val sut = sin(t * theta)
        val sout = sin((1 - t) * theta)
        val coeff1 = sout / st
        val coeff2 = sut / st

        return Quaternion(
            coeff1 * q1.x + coeff2 * q2.x,
            coeff1 * q1.y + coeff2 * q2.y,
            coeff1 * q1.z + coeff2 * q2.z,
            coeff1 * q1.w + coeff2 * q2.w,
        ).normalize()
    }

}