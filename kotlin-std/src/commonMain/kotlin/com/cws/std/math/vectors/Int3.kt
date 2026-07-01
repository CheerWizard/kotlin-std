package com.cws.std.math.vectors

import com.cws.std.math.operators.sqrt
import com.cws.std.memory.STD140_SIZE_BYTES
import com.cws.std.memory.STD430_SIZE_BYTES

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
        return sqrt(x * x + y * y + z * z)
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

    constructor(v: Int)             : this(v, v, v)
    constructor(xy: Int2, z: Int)   : this(xy.x, xy.y, z)
    constructor(x: Int, yz: Int2)   : this(x, yz.x, yz.y)

    val xx get() = Int2(x, x);  val xy get() = Int2(x, y);  val xz get() = Int2(x, z)
    val yx get() = Int2(y, x);  val yy get() = Int2(y, y);  val yz get() = Int2(y, z)
    val zx get() = Int2(z, x);  val zy get() = Int2(z, y);  val zz get() = Int2(z, z)
    val xyz get() = Int3(x, y, z)
    val xyzw get() = Int4(x, y, z, 0)

}
