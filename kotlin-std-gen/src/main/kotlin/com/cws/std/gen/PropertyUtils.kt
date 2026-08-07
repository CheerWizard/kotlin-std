/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.gen

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
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
        packageName = packageName.asString(),
        name = simpleName.asString(),
        offset = offset,
        type = simpleType,
        typeName = nonNullTypeName,
        defaultValue = typesWithDefaults.getOrDefault(simpleType, "$simpleType()"),
        fixedSize = nativeFixedSize(),
        isStringUtf16 = nativeStringUtf16(),
        isNativeEnum = (resolvedType.declaration as? KSClassDeclaration)?.classKind == ClassKind.ENUM_CLASS,
    )
}