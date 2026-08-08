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

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.sequences.forEach

class NativeListProcessor(
    private val logger: KSPLogger,
    private val fileGenerator: FileGenerator,
) {

    companion object {
        private const val TAG = "NativeListProcessor"
        private const val LISTS_PACKAGE = "com.cws.std.lists"
    }

    private val packageMemory = "com.cws.std.memory"

    private val primitiveLists = mapOf(
        "Int" to "IntList",
        "UInt" to "UIntList",
        "Float" to "FloatList",
        "Double" to "DoubleList",
        "Long" to "LongList",
        "ULong" to "ULongList",
        "Short" to "ShortList",
        "UShort" to "UShortList",
        "Byte" to "ByteList",
        "UByte" to "YByteList",
        "Boolean" to "BooleanList",
        "Char" to "CharList",
    )

    fun process(resolver: Resolver) {
        logger.info("$TAG: Scanning for @NativeList...")

        resolver
            .getSymbolsWithAnnotation("$packageMemory.NativeList")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations.any { it.shortName.asString() == "NativeList" }
            }
            .forEach { declaration ->
                logger.info("$TAG: Generate from $declaration")
                generateForNativeList(declaration)
            }
    }

    private fun generateForNativeList(declaration: KSClassDeclaration) {
        val packageName = declaration.packageName.asString()
        val className = declaration.qualifiedName()
        val name = className.simpleName
        if (fileGenerator.contains(name)) return
        val fields = declaration
            .createFields()
            // TODO: collection types are not supported at the moment,
            //  since SoA idea is to avoid heap allocations, object references, etc.
            //  and keep data layout in CPU cache locality
            .filter { !it.type.isCollection }
        val file = buildNativeList(packageName, name, fields)
        fileGenerator.generateFile(packageName, "${name}List", file)
    }

    private fun getNativeListImport(field: Field): String {
        return when {
            primitiveLists.contains(field.type) -> "import $LISTS_PACKAGE.${field.type}List"
            field.type.isCollection ||
            field.type.isVariableLength ||
            field.isNativeEnum -> "import $LISTS_PACKAGE.GenericList"
            else -> "import ${field.packageName}.${field.type}List"
        }
    }

    private fun getNativeListType(field: Field): String {
        return when {
            primitiveLists.contains(field.type) -> primitiveLists[field.type].orEmpty()
            field.type.isCollection -> "GenericList<${field.type}?>"
            field.type.isVariableLength -> "GenericList<${field.type}?>"
            field.isNativeEnum -> "GenericList<${field.type}?>"
            else -> "${field.type}List"
        }
    }

    private fun buildNativeList(
        packageName: String,
        type: String,
        fields: List<Field>
    ): String {
        val firstField = fields.firstOrNull() ?: return ""

        val imports = fields.map {
            getNativeListImport(it)
        }.toSet().joinToString("\n") +
                "\nimport com.cws.std.memory.NativeData\n" +
                "\nimport kotlin.random.Random\n"

        val constructorArgs = fields.joinToString("\n") {
            val type = getNativeListType(it)
            "   val ${it.name}: $type = ${type}(capacity),"
        }

        val secondConstructor = buildString {
            appendLine()
            append("    constructor(")
            appendLine()
            fields.forEach { field ->
                appendLine("        ${field.name}: ${getNativeListType(field)},")
            }
            appendLine("        capacity: Int,")
            append("    ) : this(capacity, ")
            fields.forEachIndexed { i, field ->
                append(field.name)
                if (i != fields.lastIndex) {
                    append(", ")
                }
            }
            appendLine(")")
        }

        val reserveBody = fields.joinToString("\n") {
            "        ${it.name}.reserve(capacity)"
        }

        val ensureCapacityBody = fields.joinToString("\n") {
            "        ${it.name}.ensureCapacity(newCapacity)"
        }

        val trimBody = fields.joinToString("\n") {
            "        ${it.name}.trimToSize()"
        }

        val clearBody = fields.joinToString("\n") {
            "        ${it.name}.clear()"
        }

        val setBody = fields.joinToString("\n") {
            "        ${it.name}[i] = value.${it.name}"
        }

        val addBody = fields.joinToString("\n") {
            "        ${it.name}.add(value.${it.name})"
        }

        val addAllBody = fields.joinToString("\n") {
            "        ${it.name}.addAll(values.${it.name})"
        }

        val removeSwapBody = fields.joinToString("\n") {
            "        ${it.name}.removeAtSwap(index)"
        }

        val cloneArgs = fields.joinToString(",\n") {
            "            ${it.name}.clone()"
        }

        val shuffleBody = fields.joinToString("\n") {
            "            ${it.name}.shuffle(random)"
        }

        val addFromSetBody = fields.joinToString("\n") {
            "        ${it.name}.addFrom(source.${it.name}, index)"
        }

        return "" +
                "package $packageName\n" +
                "\n" +
                imports +
                "\n" +
                "\n@NativeData\n" +
                "class ${type}List(\n" +
                "    capacity: Int,\n" +
                "    $constructorArgs\n" +
                ") {\n" +

                "\n" +
                secondConstructor +

                "\n" +
                "    val capacity: Int get() = ${firstField.name}.capacity\n" +

                "\n" +
                "    val size: Int get() = ${firstField.name}.size\n" +

                "\n" +
                "    val isEmpty: Boolean\n" +
                "        get() = size == 0\n" +

                "\n" +

                "    val isNotEmpty: Boolean\n" +
                "        get() = size != 0\n" +

                "\n" +

                "    val indices: IntRange\n" +
                "        get() = 0 until size\n" +

                "\n" +
                "    val lastIndex: Int\n" +
                "        get() = size - 1\n" +

                "\n" +
                "    fun clear() {\n" +
                "        $clearBody\n" +
                "    }\n" +

                "\n" +
                "    operator fun set(i: Int, value: $type) {\n" +
                "        $setBody\n" +
                "    }\n" +

                "\n" +
                "    fun add(value: $type) {\n" +
                "        $addBody\n" +
                "    }\n" +

                "\n" +
                "    fun addAll(values: ${type}List) {\n" +
                "        $addAllBody\n" +
                "    }\n" +

                "\n" +
                "   fun addFrom(\n" +
                "            source: ${type}List,\n" +
                "            index: Int\n" +
                "        ) {\n" +
                "            ${addFromSetBody}\n" +
                "        }\n" +

                "\n" +
                "    fun push(value: $type) = add(value)\n" +
                "\n" +

                "\n" +
                "    fun trimToSize() {\n" +
                "        $trimBody\n" +
                "    }\n" +

                "\n" +
                "    fun reserve(capacity: Int) {\n" +
                "        $reserveBody\n" +
                "    }\n" +

                "\n" +
                "    fun ensureCapacity(newCapacity: Int) {\n" +
                "        $ensureCapacityBody\n" +
                "    }\n" +

                "\n" +
                "    fun removeAtSwap(index: Int) {\n" +
                "        $removeSwapBody\n" +
                "    }\n" +

                "\n" +
                "    fun clone(): ${type}List {\n" +
                "        val copy = ${type}List(capacity, $cloneArgs)\n" +
                "        return copy\n" +
                "    }\n" +

                "\n" +
                "    fun shuffle(random: Random = Random) {\n" +
                "        $shuffleBody\n" +
                "    }\n" +

                "\n" +
                "   inline fun forEach(block: ${type}List.(index: Int) -> Unit) {\n" +
                "        for (i in 0 until size) {\n" +
                "            block(i)\n" +
                "        }\n" +
                "    }\n" +

                "\n" +
                "   inline fun find(block: ${type}List.(index: Int) -> Boolean): Int {\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) {\n" +
                "                return i\n" +
                "            }\n" +
                "        }\n" +
                "        return -1\n" +
                "    }\n" +

                "\n" +
                "   inline fun any(block: ${type}List.(index: Int) -> Boolean): Boolean {\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) {\n" +
                "                return true\n" +
                "            }\n" +
                "        }\n" +
                "        return false\n" +
                "    }\n" +

                "\n" +
                "   inline fun all(block: ${type}List.(index: Int) -> Boolean): Boolean {\n" +
                "        var found = 0\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) found++\n" +
                "        }\n" +
                "        return found == size\n" +
                "    }\n" +

                "\n" +
                "   inline fun none(block: ${type}List.(index: Int) -> Boolean): Boolean {\n" +
                "        var found = 0\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) found++\n" +
                "        }\n" +
                "        return found == 0\n" +
                "    }\n" +

                "\n" +
                "   inline fun count(block: ${type}List.(index: Int) -> Boolean): Int {\n" +
                "        var found = 0\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) found++\n" +
                "        }\n" +
                "        return found\n" +
                "    }\n" +

                "\n" +
                "   inline fun removeIf(block: ${type}List.(index: Int) -> Boolean) {\n" +
                "        for (i in 0 until size) {\n" +
                "            if (block(i)) {\n" +
                "                removeAtSwap(i)\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +

                "\n" +
                "   inline fun copyTo(\n" +
                "        destination: ${type}List,\n" +
                "        predicate: ${type}List.(Int) -> Boolean\n" +
                "    ) {\n" +
                "        for (i in indices) {\n" +
                "            if (predicate(i)) {\n" +
                "                destination.addFrom(this, i)\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +

                "}"
    }

}