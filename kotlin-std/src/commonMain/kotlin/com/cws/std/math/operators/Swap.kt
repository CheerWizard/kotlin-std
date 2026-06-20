package com.cws.std.math.operators

import com.cws.std.math.matrices.Mat2
import com.cws.std.math.matrices.Mat3
import com.cws.std.math.matrices.Mat4

fun Mat2.swap(i1: Int, j1: Int, i2: Int, j2: Int) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}

fun Mat3.swap(i1: Int, j1: Int, i2: Int, j2: Int) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}

fun Mat4.swap(i1: Int, j1: Int, i2: Int, j2: Int) {
    val temp = get(i1)[j1]
    get(i1)[j1] = get(i2)[j2]
    get(i2)[j2] = temp
}