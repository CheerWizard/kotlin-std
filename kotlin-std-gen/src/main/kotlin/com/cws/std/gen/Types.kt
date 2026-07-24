package com.cws.std.gen

val primitiveTypes = setOf(
    "Boolean", "Byte",
    "Short", "Char",
    "Int", "Long",
    "Float", "Double"
)
val variableLengthTypes = setOf(
    "ByteArray",
    "ShortArray", "CharArray",
    "IntArray", "LongArray",
    "FloatArray", "DoubleArray",
    "String",
)

val String.isVariableLength get() = this in variableLengthTypes
val Field.isVariableLength get() = type.isVariableLength

val String.isString get() = this == "String"
val Field.isString get() = type.isString

val String.isPrimitive get() = this in primitiveTypes
val Field.isPrimitive get() = type.isPrimitive

val Field.isDynamic get() = (fixedSize == null) && isVariableLength

val Field.isNested get() = !isPrimitive && !isVariableLength

val String.isList get() = this == "List" || this == "MutableList" || this == "ArrayList"
val Field.isList get() = type.isList

val String.isSet get() = this == "Set" || this == "MutableSet" || this == "HashSet"
val Field.isSet get() = type.isSet

val String.isMap get() = this == "Map" || this == "MutableMap" || this == "HashMap"
val Field.isMap get() = type.isMap

val String.isCollection get() = isList || isSet || isMap
val Field.isCollection get() = type.isCollection

val typesWithDefaults = mapOf(
    "Boolean" to "false",
    "Byte" to "0",
    "Short" to "0",
    "Char" to "'\"0\"'",
    "Int" to "0",
    "Long" to "0L",
    "Float" to "0f",
    "Double" to "0.0",
    "ByteArray" to "ByteArray(0)",
    "ShortArray" to "ShortArray(0)",
    "CharArray" to "CharArray(0)",
    "IntArray" to "IntArray(0)",
    "LongArray" to "LongArray(0)",
    "FloatArray" to "FloatArray(0)",
    "DoubleArray" to "DoubleArray(0)",
    "String" to "\"\"",
    "List" to "emptyList()",
    "ArrayList" to "ArrayList()",
    "MutableList" to "mutableListOf()",
    "Set" to "emptySet()",
    "HashSet" to "HashSet()",
    "MutableSet" to "mutableSetOf()",
    "Map" to "emptyMap()",
    "HashMap" to "HashMap()",
    "MutableMap" to "mutableMapOf()",
)

fun String.sizeBytes(fixedSize: Int?, isStringUtf16: Boolean, ref: String): String? = when {
    isPrimitive -> "${this}.SIZE_BYTES"
    isString -> {
        if (fixedSize != null) {
            // fixed size - use compile time constant, no runtime length needed
            if (isStringUtf16) {
                "$fixedSize * Char.SIZE_BYTES"
            } else {
                "$fixedSize * Byte.SIZE_BYTES"
            }
        } else {
            // dynamic - runtime length + prefix
            val length = if (isStringUtf16) {
                "$ref.orEmpty().sizeBytesUtf16(memoryLayout)"
            } else {
                "$ref.orEmpty().sizeBytesUtf8(memoryLayout)"
            }
            "Int.SIZE_BYTES + $length"
        }
    }
    isVariableLength -> {
        if (fixedSize != null) {
            // fixed size - use compile time constant, no runtime length needed
            "$fixedSize * ${removeSuffix("Array")}.SIZE_BYTES"
        } else {
            // dynamic - runtime length + prefix
            val length = "$ref.sizeBytes(memoryLayout)"
            "Int.SIZE_BYTES + $length"
        }
    }
    else -> null
}