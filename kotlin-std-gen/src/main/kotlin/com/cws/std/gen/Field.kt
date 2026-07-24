package com.cws.std.gen

import com.squareup.kotlinpoet.TypeName

data class Field(
    val name: String,
    val offset: String,
    val typeName: TypeName,
    val type: String,
    val defaultValue: String,
    val fixedSize: Int?,
    val isStringUtf16: Boolean,
)

fun Field.sizeExpression(): String = when {
    isPrimitive -> "$type.SIZE_BYTES"
    isVariableLength -> "Int.SIZE_BYTES"
    isCollection -> "Int.SIZE_BYTES"
    else                         -> "$type.SIZE_BYTES"
}
