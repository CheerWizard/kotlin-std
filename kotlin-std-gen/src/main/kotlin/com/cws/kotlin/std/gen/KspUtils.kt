package com.cws.kotlin.std.gen

import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.symbol.Nullability
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.toTypeVariableName

fun KSFunctionDeclaration.toFunSpec(): FunSpec {
    val builder = if (isConstructor()) {
        FunSpec.constructorBuilder()
    } else {
        FunSpec.builder(simpleName.asString())
            .returns(returnType?.toTypeName() ?: UNIT)
    }

    typeParameters.forEach { tp ->
        builder.addTypeVariable(tp.toTypeVariableName())
    }

    parameters.forEach { param ->
        builder.addParameter(
            param.name?.asString() ?: "_",
            param.type.toTypeName()
        )
    }

    modifiers.forEach { mod ->
        builder.addModifiers(mod.toKModifier())
    }

    return builder.build()
}

fun KSTypeReference.toTypeName(): TypeName = toTypeNameImpl(this.resolve())

private fun toTypeNameImpl(type: KSType): TypeName {
    val declaration = type.declaration
    val pkg = declaration.packageName.asString()
    val name = declaration.simpleName.asString()
    val className = ClassName(pkg, name)

    return if (type.arguments.isNotEmpty()) {
        className.parameterizedBy(type.arguments.map {
            it.type?.toTypeName() ?: STAR
        })
    } else {
        className
    }.copy(nullable = type.nullability == Nullability.NULLABLE)
}

fun Modifier.toKModifier(): KModifier =
    when (this) {
        Modifier.ABSTRACT -> KModifier.ABSTRACT
        Modifier.OVERRIDE -> KModifier.OVERRIDE
        Modifier.PRIVATE -> KModifier.PRIVATE
        Modifier.PROTECTED -> KModifier.PROTECTED
        Modifier.PUBLIC -> KModifier.PUBLIC
        else -> KModifier.PUBLIC
    }

fun KSPropertyDeclaration.toPropertySpec(): PropertySpec {
    val name = simpleName.asString()
    val typeName = type.toTypeName()

    val builder = PropertySpec.builder(name, typeName)
        .mutable(isMutable)

    return builder.build()
}