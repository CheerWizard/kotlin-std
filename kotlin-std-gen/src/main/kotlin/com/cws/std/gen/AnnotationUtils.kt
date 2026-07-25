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

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.TypeName

fun KSAnnotated.nativeCommand(): Boolean = hasAnnotation("NativeCommand")

fun KSAnnotated.nativeEvent(): Boolean = hasAnnotation("NativeEvent")

fun KSAnnotated.nativeSnapshot(): Boolean = hasAnnotation("NativeSnapshot")

fun KSAnnotated.nativeMessage(): Boolean = nativeCommand() || nativeEvent() || nativeSnapshot()

fun KSClassDeclaration.nativeMessageId(): Int {
    // ID is always stable, because it relies on hashcode of full class name
    return qualifiedName!!.asString().hashCode() and 0x7FFFFFFF
}

fun KSAnnotated.nativeFixedSize(): Int? = findAnnotationInt("NativeFixedSize")

fun KSAnnotated.nativeStringUtf16(): Boolean = hasAnnotation("NativeStringUtf16")

fun TypeName.nativeStringUtf16(): Boolean = hasAnnotation("NativeStringUtf16")

private fun KSAnnotated.hasAnnotation(name: String): Boolean {
    return annotations.any { it.shortName.asString() == name }
}

private fun TypeName.hasAnnotation(name: String): Boolean {
    return annotations.any { it.typeName.toString() == name }
}

private fun KSAnnotated.findAnnotationInt(name: String): Int? {
    return annotations
        .find { it.shortName.asString() == name }
        ?.arguments
        ?.firstOrNull()
        ?.value as? Int
}

fun TypeName.nativeFixedSize(): Int? {
    val annotation = annotations.find {
        it.typeName.toString().endsWith("NativeFixedSize")
    } ?: return null

    return annotation.members
        .firstOrNull()
        ?.toString()
        ?.trim()
        ?.toIntOrNull()
}
