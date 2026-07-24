package com.cws.std.gen

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.squareup.kotlinpoet.ClassName

fun KSClassDeclaration.createFields(): List<Field> {
    val fields = mutableListOf<Field>()
    var offset = "0"

    val constructorParamNames = primaryConstructor
        ?.parameters
        ?.map { it.name?.asString() }
        ?.toSet()
        ?: emptySet()

    getAllProperties()
        .filter { it.simpleName.asString() in constructorParamNames }
        .forEach { prop ->
            val field = prop.createField(offset)
            fields += field
            offset += " + ${field.sizeExpression()}"
        }

    return fields
}

fun KSDeclaration.qualifiedName(): ClassName {
    val packageName = packageName.asString()
    val names = mutableListOf<String>()
    var current: KSDeclaration = this
    while (current.parentDeclaration != null) {
        names.add(0, current.simpleName.asString())
        current = current.parentDeclaration!!
    }
    names.add(0, current.simpleName.asString())
    return ClassName(packageName, names.first(), *names.drop(1).toTypedArray())
}