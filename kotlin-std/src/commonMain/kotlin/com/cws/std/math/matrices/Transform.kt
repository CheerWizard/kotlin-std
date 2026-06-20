package com.cws.std.math.matrices

import com.cws.std.math.vectors.Float3

data class Transform(
    var position: Float3 = Float3(),
    var rotation: Float3 = Float3(),
    var scale: Float3 = Float3(),
) {
    fun toMat4(): Mat4 = ModelMatrix(position, rotation.x, rotation.y, rotation.z, scale)
}