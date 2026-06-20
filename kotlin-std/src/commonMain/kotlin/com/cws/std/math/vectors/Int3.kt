package com.cws.std.math.vectors

import com.cws.std.math.operators.normalize
import com.cws.std.memory.MemoryLayout
import com.cws.std.memory.NativeBuffer
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES
import com.cws.std.memory.nextInt
import com.cws.std.memory.pushInt

data class Int3(
    var x: Int = 0,
    var y: Int = 0,
    var z: Int = 0,
) {

    companion object {
        val SIZE_BYTES = Int.SIZE_BYTES * 3
        val STD140_SIZE_BYTES = Int.STD140_SIZE_BYTES * 3
        val STD430_SIZE_BYTES = Int.STD430_SIZE_BYTES * 3
    }

    operator fun get(i: Int): Int {
        return when (i) {
            0 -> x
            1 -> y
            2 -> z
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    operator fun set(i: Int, v: Int) {
        return when (i) {
            0 -> x = v
            1 -> y = v
            2 -> z = v
            else -> throw IndexOutOfBoundsException("i=$i out of range [0, 2]")
        }
    }

    val length: Int get() {
        val x = x
        val y = y
        val z = z
        return com.cws.std.math.sqrt(x * x + y * y + z * z)
    }

    fun normalize(): Int3 {
        return normalize(this, this)
    }

    operator fun plus(v: Int): Int3 {
        return Int3(x + v, y + v, z + v)
    }

    operator fun minus(v: Int): Int3 {
        return Int3(x - v, y - v, z - v)
    }

    operator fun times(v: Int): Int3 {
        return Int3(x * v, y * v, z * v)
    }

    operator fun div(v: Int): Int3 {
        return Int3(x / v, y / v, z / v)
    }

    operator fun plus(v: Int3): Int3 {
        return Int3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: Int3): Int3 {
        return Int3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(v: Int3): Int3 {
        return Int3(x * v.x, y * v.y, z * v.z)
    }

    operator fun div(v: Int3): Int3 {
        return Int3(x / v.x, y / v.y, z / v.z)
    }

    operator fun unaryMinus(): Int3 {
        return Int3(-x, -y, -z)
    }

}
