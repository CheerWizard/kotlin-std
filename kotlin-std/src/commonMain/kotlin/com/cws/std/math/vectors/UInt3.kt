package com.cws.std.math.vectors

import com.cws.std.math.operators.normalize
import com.cws.std.math.sqrt
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.NativeBuffer
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES
import com.cws.std.memory.nextUInt
import com.cws.std.memory.pushUInt

data class UInt3(
    var x: UInt = 0u,
    var y: UInt = 0u,
    var z: UInt = 0u,
) {

    operator fun get(i: Int): UInt {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    operator fun set(i: Int, v: UInt) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    val length: UInt get() {
        val x = x
        val y = y
        val z = z
        return sqrt(x * x + y * y + z * z)
    }

    fun normalize(): UInt3 {
        return normalize(this, this)
    }

    operator fun plus(v: UInt): UInt3 {
        return UInt3(x + v, y + v, z + v)
    }

    operator fun minus(v: UInt): UInt3 {
        return UInt3(x - v, y - v, z - v)
    }

    operator fun times(v: UInt): UInt3 {
        return UInt3(x * v, y * v, z * v)
    }

    operator fun div(v: UInt): UInt3 {
        return UInt3(x / v, y / v, z / v)
    }

    operator fun plus(v: UInt3): UInt3 {
        return UInt3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: UInt3): UInt3 {
        return UInt3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(v: UInt3): UInt3 {
        return UInt3(x * v.x, y * v.y, z * v.z)
    }

    operator fun div(v: UInt3): UInt3 {
        return UInt3(x / v.x, y / v.y, z / v.z)
    }

}