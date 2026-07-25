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