package com.cws.std.gen

import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toTypeName

fun KSPropertyDeclaration.createField(offset: String): Field {
    val resolvedType = type.resolve()
    val simpleType = resolvedType.declaration.simpleName.asString()

    val typeName: TypeName = when {
        simpleType.isPrimitive || simpleType.isCollection -> resolvedType.toTypeName()
        else -> resolvedType.declaration.qualifiedName()
    }

    // also handle nullable types - strip nullability for type arg resolution
    val nonNullTypeName = when (typeName) {
        is ParameterizedTypeName -> typeName.copy(nullable = false)
        is ClassName -> typeName.copy(nullable = false)
        else -> typeName
    }

    return Field(
        name = simpleName.asString(),
        offset = offset,
        type = simpleType,
        typeName = nonNullTypeName,
        defaultValue = typesWithDefaults.getOrDefault(simpleType, "$simpleType()"),
        fixedSize = nativeFixedSize(),
        isStringUtf16 = nativeStringUtf16(),
    )
}