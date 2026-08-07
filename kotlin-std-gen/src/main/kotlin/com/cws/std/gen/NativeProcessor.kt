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

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName

class NativeProcessor(
    environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    companion object {
        private const val TAG = "NativeProcessor"
        private const val FUNCTION_SUFFIX_GPU = "Gpu"
    }

    private val generator: CodeGenerator = environment.codeGenerator
    private val logger: KSPLogger = environment.logger

    private val packageMemory = "com.cws.std.memory"
    private val nativeBufferClass = ClassName(packageMemory, "NativeBuffer")
    private val memoryLayoutClass = ClassName(packageMemory, "MemoryLayout")
    private val endianClass = ClassName(packageMemory, "Endian")
    private val memoryBoundaryClass = ClassName(packageMemory, "MemoryBoundary")
    private val byteArrayClass = ClassName("kotlin", "ByteArray")

    private val fileGenerator = FileGenerator(logger, generator)

    private val nativeListProcessor = NativeListProcessor(logger, fileGenerator)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        nativeListProcessor.process(resolver)
        scanNativeData(resolver)
        scanNativeEnum(resolver)

        // ATTENTION: only comment this out, when primitive lists need regeneration with new updates.
        // primitive lists only need to be generated once and copied into com.cws.std.lists package
//        generatePrimitiveLists()

        return emptyList()
    }

    override fun finish() {
    }

    private fun scanNativeData(resolver: Resolver) {
        logger.info("$TAG: Scanning for @NativeData...")

        resolver
            .getSymbolsWithAnnotation("$packageMemory.NativeData")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations.any { it.shortName.asString() == "NativeData" }
            }
            .forEach { declaration ->
                logger.info("$TAG: Generate from $declaration")
                generateForNativeData(declaration)
            }
    }

    private fun generateForNativeData(declaration: KSClassDeclaration) {
        val packageName = declaration.packageName.asString()
        val className = declaration.qualifiedName()
        val fields = declaration.createFields()

        val fileSpec = FileSpec.builder(packageName, className.simpleName)

        fileSpec.addFunction(buildSizeBytesFunction(fileSpec, className, fields))
        fileSpec.addFunction(buildSizeBytesPackedFunction(fileSpec, className, fields))

        fileSpec.addFunction(buildEncodeToNewBuffer(fileSpec, className, fields, ""))
        fileSpec.addFunction(buildEncodeToNewBuffer(fileSpec, className, fields, FUNCTION_SUFFIX_GPU))

        fileSpec.addFunction(buildEncodePackedToNewBuffer(fileSpec, className, fields))

        fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, ""))
        fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, FUNCTION_SUFFIX_GPU))

        fileSpec.addFunction(buildEncodePackedToBuffer(fileSpec, className, fields))

        fileSpec.addFunction(buildDecodeFromByteArray(className))
        fileSpec.addFunction(buildDecodeFromBuffer(fileSpec, className, fields, ""))
        fileSpec.addFunction(buildDecodeFromBuffer(fileSpec, className, fields, FUNCTION_SUFFIX_GPU))

        // generate ID constant
        if (declaration.nativeMessage()) {
            fileSpec.addProperty(buildIdProperty(className, declaration.nativeMessageId()))
        }

        fileSpec.writeTo(declaration)
    }

    private fun scanNativeEnum(resolver: Resolver) {
        logger.info("$TAG: Scanning for @NativeEnum...")

        resolver
            .getSymbolsWithAnnotation("$packageMemory.NativeEnum")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations.any { it.shortName.asString() == "NativeEnum" }
            }
            .forEach { declaration ->
                logger.info("$TAG: Generate from $declaration")
                generateForNativeEnum(declaration)
            }
    }

    private fun generateForNativeEnum(declaration: KSClassDeclaration) {
        if (declaration.classKind != ClassKind.ENUM_CLASS) {
            logger.error(
                "$TAG: @NativeEnum can only be applied to enums, but found ${declaration.classKind} '${declaration.simpleName.asString()}'",
                declaration,
            )
            return
        }

        val packageName = declaration.packageName.asString()
        val className = declaration.qualifiedName()

        val rawValueProp = declaration.getAllProperties()
            .find { it.simpleName.asString() == "rawValue" }
        val ordinalProp = declaration.getAllProperties()
            .find { it.simpleName.asString() == "ordinal" }

        val fileSpec = FileSpec.builder(packageName, className.simpleName)

        when {
            rawValueProp != null -> {
                // has rawValue, decode rawValue field
                val field = rawValueProp.createField(offset = "0")
                val fields = listOf(field)
                fileSpec.addFunction(buildSizeBytesFunction(fileSpec, className, fields))
                fileSpec.addFunction(buildSizeBytesPackedFunction(fileSpec, className, fields))
                fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, ""))
                fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, FUNCTION_SUFFIX_GPU))
                fileSpec.addFunction(buildEncodePackedToBuffer(fileSpec, className, fields))
                fileSpec.addFunction(buildDecodeFromByteArray(className))
                fileSpec.addFunction(buildEnumDecodeFromBuffer(className, field, ""))
                fileSpec.addFunction(buildEnumDecodeFromBuffer(className, field, FUNCTION_SUFFIX_GPU))
                fileSpec.addProperty(buildEnumValueProperty(className, field, useRawValue = true))
            }
            ordinalProp != null -> {
                // no rawValue, decode ordinal field
                logger.info("$TAG: @NativeEnum ${className.simpleName} has no 'rawValue', falling back to ordinal encoding")
                val field = ordinalProp.createField(offset = "0")
                val fields = listOf(field)
                fileSpec.addFunction(buildSizeBytesFunction(fileSpec, className, fields))
                fileSpec.addFunction(buildSizeBytesPackedFunction(fileSpec, className, fields))
                fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, ""))
                fileSpec.addFunction(buildEncodeToBuffer(fileSpec, className, fields, FUNCTION_SUFFIX_GPU))
                fileSpec.addFunction(buildEncodePackedToBuffer(fileSpec, className, fields))
                fileSpec.addFunction(buildDecodeFromByteArray(className))
                fileSpec.addFunction(buildEnumOrdinalDecodeFromBuffer(className, ""))
                fileSpec.addFunction(buildEnumOrdinalDecodeFromBuffer(className, FUNCTION_SUFFIX_GPU))
                fileSpec.addProperty(buildEnumValueProperty(className, field, useRawValue = false))
            }
            else -> {
                logger.error("$TAG: @NativeEnum ${className.simpleName} has no 'rawValue' and 'ordinal'! Unable to generate it")
            }
        }

        fileSpec.writeTo(declaration)
    }

    private fun buildIdProperty(className: ClassName, id: Int): PropertySpec =
        PropertySpec.builder("ID", Int::class)
            .receiver(className.nestedClass("Companion"))
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return ${String.format("0x%04X", id)}")
                    .build()
            )
            .build()

    private fun buildSizeBytesFunction(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
    ): FunSpec {
        return FunSpec.builder("sizeBytes")
            .addParameter(ParameterSpec("memoryLayout", memoryLayoutClass))
            .receiver(className.copy(nullable = true))
            .returns(INT)
            .apply {
                val parts = fields.map { field ->
                    val sizeBytes = field.type.sizeBytes(
                        field.fixedSize,
                        field.isStringUtf16,
                        field.name
                    )

                    sizeBytes ?: when {
                        field.isCollection -> {
                            when {
                                field.type.isListCollection -> {
                                    val elementSize = collectionElementSizeExpr(fileSpec, field)
                                    "Int.SIZE_BYTES + ${field.name}.sumOf { $elementSize }"
                                }

                                field.type.isMap -> {
                                    val keySize = collectionElementSizeExpr(fileSpec, field, key = true)
                                    val valueSize = collectionElementSizeExpr(fileSpec, field, key = false)
                                    "Int.SIZE_BYTES + ${field.name}.entries.sumOf { $keySize + $valueSize }"
                                }

                                else -> "0"
                            }
                        }

                        field.isNested -> {
                            val fieldClassName = field.typeName as ClassName
                            fileSpec.addImport(fieldClassName.packageName, "sizeBytes")
                            "${field.name}.sizeBytes(memoryLayout)"
                        }

                        else -> "0"
                    }
                }.ifEmpty { listOf("0") }

                val expr = parts.joinToString(" + ")
                addStatement("return if (this == null) 0 else $expr", className)
            }
            .build()
    }

    private fun buildSizeBytesPackedFunction(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
    ): FunSpec {
        return FunSpec.builder("sizeBytesPacked")
            .addParameter(ParameterSpec("memoryLayout", memoryLayoutClass))
            .receiver(className.copy(nullable = true))
            .returns(INT)
            .apply {
                val parts = fields.map { field ->
                    val sizeBytes = field.type.sizeBytesPacked(
                        field.isStringUtf16,
                        field.name
                    )

                    sizeBytes ?: when {
                        field.isCollection -> {
                            when {
                                field.type.isListCollection -> {
                                    val elementSize = collectionElementSizePackedExpr(fileSpec, field)
                                    "${field.name}.sumOf { $elementSize }"
                                }

                                field.type.isMap -> {
                                    val keySize = collectionElementSizePackedExpr(fileSpec, field, key = true)
                                    val valueSize = collectionElementSizePackedExpr(fileSpec, field, key = false)
                                    "${field.name}.entries.sumOf { $keySize + $valueSize }"
                                }

                                else -> "0"
                            }
                        }

                        field.isNested -> {
                            val fieldClassName = field.typeName as ClassName
                            fileSpec.addImport(fieldClassName.packageName, "sizeBytesPacked")
                            "${field.name}.sizeBytesPacked(memoryLayout)"
                        }

                        else -> "0"
                    }
                }.ifEmpty { listOf("0") }

                val expr = parts.joinToString(" + ")
                addStatement("return if (this == null) 0 else $expr", className)
            }
            .build()
    }

    private fun buildEncodeToNewBuffer(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
        functionSuffix: String,
    ): FunSpec {
        return FunSpec.builder("encode$functionSuffix")
            .addParameter(ParameterSpec("memoryLayout", memoryLayoutClass)
                .toBuilder()
                .defaultValue("MemoryLayout.KOTLIN")
                .build()
            )
            .addParameter(ParameterSpec("endian", endianClass)
                .toBuilder()
                .defaultValue("Endian.LITTLE")
                .build())
            .addParameter(ParameterSpec("memoryBoundary", memoryBoundaryClass)
                .toBuilder()
                .defaultValue("MemoryBoundary.KOTLIN_HEAP")
                .build())
            .receiver(className.copy(nullable = true))
            .returns(nativeBufferClass)
            .addStatement("if (this == null) return NativeBuffer(0)")
            .addStatement("val buffer = %T(capacity = sizeBytes(memoryLayout), memoryLayout = memoryLayout, endian = endian, memoryBoundary = memoryBoundary)", nativeBufferClass)
            .addStatement("encode$functionSuffix(buffer)")
            .addStatement("return buffer")
            .build()
    }

    private fun buildEncodePackedToNewBuffer(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>
    ): FunSpec {
        return FunSpec.builder("encodePacked")
            .addParameter(ParameterSpec("memoryLayout", memoryLayoutClass)
                .toBuilder()
                .defaultValue("MemoryLayout.KOTLIN")
                .build()
            )
            .addParameter(ParameterSpec("endian", endianClass)
                .toBuilder()
                .defaultValue("Endian.LITTLE")
                .build())
            .addParameter(ParameterSpec("memoryBoundary", memoryBoundaryClass)
                .toBuilder()
                .defaultValue("MemoryBoundary.KOTLIN_HEAP")
                .build())
            .receiver(className.copy(nullable = true))
            .returns(nativeBufferClass)
            .addStatement("if (this == null) return NativeBuffer(0)")
            .addStatement("val buffer = %T(capacity = sizeBytesPacked(memoryLayout), memoryLayout = memoryLayout, endian = endian, memoryBoundary = memoryBoundary)", nativeBufferClass)
            .addStatement("encodePacked(buffer)")
            .addStatement("return buffer")
            .build()
    }

    private fun collectionElementSizeExpr(
        fileSpec: FileSpec.Builder,
        field: Field,
        key: Boolean = true,
    ): String {
        val typeName = field.typeName

        if (typeName !is ParameterizedTypeName) {
            logger.warn("$TAG: Expected ParameterizedTypeName for collection field '${field.name}' but got ${typeName::class.simpleName}")
            return "0"
        }

        val args = typeName.typeArguments
        val elementType = if (field.isMap) {
            if (key) args[0] else args[1]
        } else {
            args[0]
        }

        val ref = if (field.isMap) (if (key) "it.key" else "it.value") else "it"

        return elementSizeExpr(fileSpec, elementType, ref)
    }

    private fun collectionElementSizePackedExpr(
        fileSpec: FileSpec.Builder,
        field: Field,
        key: Boolean = true,
    ): String {
        val typeName = field.typeName

        if (typeName !is ParameterizedTypeName) {
            logger.warn("$TAG: Expected ParameterizedTypeName for collection field '${field.name}' but got ${typeName::class.simpleName}")
            return "0"
        }

        val args = typeName.typeArguments
        val elementType = if (field.isMap) {
            if (key) args[0] else args[1]
        } else {
            args[0]
        }

        val ref = if (field.isMap) (if (key) "it.key" else "it.value") else "it"

        return elementSizePackedExpr(fileSpec, elementType, ref)
    }

    private fun elementSizeExpr(
        fileSpec: FileSpec.Builder,
        typeName: TypeName,
        ref: String,
    ): String {
        val nonNull = when (typeName) {
            is ParameterizedTypeName -> typeName.copy(nullable = false)
            is ClassName             -> typeName.copy(nullable = false)
            else                     -> typeName
        }

        return when (nonNull) {
            is ClassName -> {
                val sizeBytes = nonNull.simpleName.sizeBytes(
                    nonNull.nativeFixedSize(),
                    nonNull.nativeStringUtf16(),
                    ref
                )

                if (sizeBytes == null) {
                    fileSpec.addImport(nonNull.packageName, "sizeBytes")
                    "$ref.sizeBytes(memoryLayout)"
                } else {
                    sizeBytes
                }
            }

            is ParameterizedTypeName -> when {
                nonNull.rawType.simpleName.isListCollection -> {
                    val inner = elementSizeExpr(fileSpec, nonNull.typeArguments.first(), "it")
                    "Int.SIZE_BYTES + $ref.sumOf { $inner }"
                }

                nonNull.rawType.simpleName.isMap -> {
                    val keyExpr = elementSizeExpr(fileSpec, nonNull.typeArguments[0], "it.key")
                    val valueExpr = elementSizeExpr(fileSpec, nonNull.typeArguments[1], "it.value")
                    "Int.SIZE_BYTES + $ref.entries.sumOf { $keyExpr + $valueExpr }"
                }

                else -> error("Unsupported: ${nonNull.rawType.simpleName}")
            }

            else -> error("Unsupported type: $typeName")
        }
    }

    private fun elementSizePackedExpr(
        fileSpec: FileSpec.Builder,
        typeName: TypeName,
        ref: String,
    ): String {
        val nonNull = when (typeName) {
            is ParameterizedTypeName -> typeName.copy(nullable = false)
            is ClassName             -> typeName.copy(nullable = false)
            else                     -> typeName
        }

        return when (nonNull) {
            is ClassName -> {
                val sizeBytes = nonNull.simpleName.sizeBytes(
                    nonNull.nativeFixedSize(),
                    nonNull.nativeStringUtf16(),
                    ref
                )

                if (sizeBytes == null) {
                    fileSpec.addImport(nonNull.packageName, "sizeBytesPacked")
                    "$ref.sizeBytesPacked(memoryLayout)"
                } else {
                    sizeBytes
                }
            }

            is ParameterizedTypeName -> when {
                nonNull.rawType.simpleName.isListCollection -> {
                    val inner = elementSizePackedExpr(fileSpec, nonNull.typeArguments.first(), "it")
                    "$ref.sumOf { $inner }"
                }

                nonNull.rawType.simpleName.isMap -> {
                    val keyExpr = elementSizePackedExpr(fileSpec, nonNull.typeArguments[0], "it.key")
                    val valueExpr = elementSizePackedExpr(fileSpec, nonNull.typeArguments[1], "it.value")
                    "$ref.entries.sumOf { $keyExpr + $valueExpr }"
                }

                else -> error("Unsupported: ${nonNull.rawType.simpleName}")
            }

            else -> error("Unsupported type: $typeName")
        }
    }

    private fun buildEncodeToBuffer(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
        functionSuffix: String,
    ): FunSpec {
        return FunSpec.builder("encode$functionSuffix")
            .receiver(className.copy(nullable = true))
            .addParameter("buffer", nativeBufferClass)
            .addStatement("if (this == null) return")
            .apply {
                fields.forEach { field ->
                    when {
                        field.isPrimitive -> addStatement("buffer.push${field.type}(${field.name})")

                        field.isMatrix -> {
                            val majorSuffix = if (functionSuffix == FUNCTION_SUFFIX_GPU) {
                                "ColumnMajor"
                            } else {
                                "RowMajor"
                            }
                            addStatement("buffer.push${field.type}$majorSuffix(${field.name})")
                        }

                        field.isVariableLength -> {
                            val fieldType = if (field.isString) {
                                if (field.isStringUtf16) "StringUtf16" else "StringUtf8"
                            } else {
                                field.type
                            }

                            if (field.fixedSize == null) {
                                addStatement("buffer.push${fieldType}(${field.name})")
                            } else {
                                addStatement("buffer.pushFixed${fieldType}(${field.name}, ${field.fixedSize})")
                            }
                        }

                        field.isCollection -> {
                            val parameterized = field.typeName as? ParameterizedTypeName
                                ?: error("Collection field '${field.name}' typeName is not ParameterizedTypeName: ${field.typeName::class.simpleName}")

                            when {
                                field.type.isListCollection -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("Collection field '${field.name}' has no type argument")
                                    val encodeElement = encodeExprFor(elementType, "buffer", fileSpec, functionSuffix)
                                    addStatement("buffer.pushCollection(${field.name}) { $encodeElement }")
                                }

                                field.type.isMap -> {
                                    val keyType = parameterized.typeArguments.getOrNull(0)
                                        ?: error("Map field '${field.name}' has no key type")
                                    val valueType = parameterized.typeArguments.getOrNull(1)
                                        ?: error("Map field '${field.name}' has no value type")
                                    val encodeKey = encodeExprFor(keyType, "buffer", fileSpec, functionSuffix)
                                    val encodeValue = encodeExprFor(valueType, "buffer", fileSpec, functionSuffix)
                                    addStatement("buffer.pushMap(${field.name}, { $encodeKey }, { $encodeValue })")
                                }
                            }
                        }

                        field.isNested -> {
                            val fieldClassName = field.typeName as ClassName
                            fileSpec.addImport(fieldClassName.packageName, "encode$functionSuffix")
                            addStatement("${field.name}.encode$functionSuffix(buffer)")
                        }
                    }
                }
            }
            .build()
    }

    private fun buildEncodePackedToBuffer(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
    ): FunSpec {
        val functionSuffix = FUNCTION_SUFFIX_GPU // For now "packed encoding" encodes matrices as column-major
        return FunSpec.builder("encodePacked")
            .receiver(className.copy(nullable = true))
            .addParameter("buffer", nativeBufferClass)
            .addStatement("if (this == null) return")
            .apply {
                fields.forEach { field ->
                    when {
                        field.isPrimitive -> addStatement("buffer.push${field.type}(${field.name})")

                        field.isMatrix -> {
                            val majorSuffix = if (functionSuffix == FUNCTION_SUFFIX_GPU) {
                                "ColumnMajor"
                            } else {
                                "RowMajor"
                            }
                            addStatement("buffer.push${field.type}$majorSuffix(${field.name})")
                        }

                        field.isVariableLength -> {
                            val fieldType = if (field.isString) {
                                if (field.isStringUtf16) "StringUtf16" else "StringUtf8"
                            } else {
                                field.type
                            }
                            addStatement("buffer.pushPacked${fieldType}(${field.name})")
                        }

                        field.isCollection -> {
                            val parameterized = field.typeName as? ParameterizedTypeName
                                ?: error("Collection field '${field.name}' typeName is not ParameterizedTypeName: ${field.typeName::class.simpleName}")

                            when {
                                field.type.isListCollection -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("Collection field '${field.name}' has no type argument")
                                    val encodeElement = encodeExprFor(elementType, "buffer", fileSpec, functionSuffix)
                                    addStatement("buffer.pushPackedCollection(${field.name}) { $encodeElement }")
                                }

                                field.type.isMap -> {
                                    val keyType = parameterized.typeArguments.getOrNull(0)
                                        ?: error("Map field '${field.name}' has no key type")
                                    val valueType = parameterized.typeArguments.getOrNull(1)
                                        ?: error("Map field '${field.name}' has no value type")
                                    val encodeKey = encodeExprFor(keyType, "buffer", fileSpec, functionSuffix)
                                    val encodeValue = encodeExprFor(valueType, "buffer", fileSpec, functionSuffix)
                                    addStatement("buffer.pushPackedMap(${field.name}, { $encodeKey }, { $encodeValue })")
                                }
                            }
                        }

                        field.isNested -> {
                            val fieldClassName = field.typeName as ClassName
                            fileSpec.addImport(fieldClassName.packageName, "encodePacked")
                            addStatement("${field.name}.encodePacked(buffer)")
                        }
                    }
                }
            }
            .build()
    }

    private fun buildDecodeFromByteArray(className: ClassName): FunSpec {
        return FunSpec.builder("decode${className.simpleName}")
            .receiver(byteArrayClass)
            .returns(className)
            .addStatement("return %T(this).decode${className.simpleName}()", nativeBufferClass)
            .build()
    }

    private fun buildDecodeFromBuffer(
        fileSpec: FileSpec.Builder,
        className: ClassName,
        fields: List<Field>,
        functionSuffix: String,
    ): FunSpec {
        return FunSpec.builder("decode$functionSuffix${className.simpleName}")
            .receiver(nativeBufferClass)
            .returns(className)
            .apply {
                addCode("return %T(\n", className)
                fields.forEach { field ->
                    when {
                        field.isPrimitive -> addStatement("  next${field.type}(),")

                        field.isMatrix -> {
                            val majorSuffix = if (functionSuffix == FUNCTION_SUFFIX_GPU) {
                                "ColumnMajor"
                            } else {
                                "RowMajor"
                            }
                            addStatement("  next${field.type}$majorSuffix(),")
                        }

                        field.isVariableLength -> {
                            val fieldType = if (field.isString) {
                                if (field.isStringUtf16) "StringUtf16" else "StringUtf8"
                            } else {
                                field.type
                            }

                            if (field.fixedSize == null) {
                                addStatement("  next${fieldType}(),")
                            } else {
                                addStatement("  next${fieldType}(${field.fixedSize}),")
                            }
                        }

                        field.isCollection -> {
                            val parameterized = field.typeName as? ParameterizedTypeName
                                ?: error("Collection field '${field.name}' typeName is not ParameterizedTypeName: ${field.typeName::class.simpleName}")

                            when {
                                field.type.isArray -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("Array field '${field.name}' has no type argument")
                                    val decodeElement = decodeExprFor(elementType, "this", fileSpec, functionSuffix)
                                    addStatement("  nextArray { $decodeElement },")
                                }

                                field.type.isList -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("List field '${field.name}' has no type argument")
                                    val decodeElement = decodeExprFor(elementType, "this", fileSpec, functionSuffix)
                                    addStatement("  nextList { $decodeElement },")
                                }

                                field.type.isSet -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("Set field '${field.name}' has no type argument")
                                    val decodeElement = decodeExprFor(elementType, "this", fileSpec, functionSuffix)
                                    addStatement("  nextSet { $decodeElement },")
                                }

                                field.type.isGenericList -> {
                                    val elementType = parameterized.typeArguments.firstOrNull()
                                        ?: error("GenericList field '${field.name}' has no type argument")
                                    val decodeElement = decodeExprFor(elementType, "this", fileSpec, functionSuffix)
                                    addStatement("  nextGenericList { $decodeElement },")
                                }

                                field.type.isMap -> {
                                    val keyType = parameterized.typeArguments.getOrNull(0)
                                        ?: error("Map field '${field.name}' has no key type")
                                    val valueType = parameterized.typeArguments.getOrNull(1)
                                        ?: error("Map field '${field.name}' has no value type")
                                    val decodeKey = decodeExprFor(keyType, "this", fileSpec, functionSuffix)
                                    val decodeValue = decodeExprFor(valueType, "this", fileSpec, functionSuffix)
                                    addStatement("  nextMap({ $decodeKey }, { $decodeValue }),")
                                }
                            }
                        }

                        field.isNested -> {
                            val fieldClassName = field.typeName as ClassName
                            fileSpec.addImport(fieldClassName.packageName, "decode$functionSuffix${field.type}")
                            addStatement("  decode$functionSuffix${field.type}(),")
                        }
                    }
                }
                addCode(")\n")
            }
            .build()
    }

    private fun buildEnumDecodeFromBuffer(className: ClassName, rawField: Field, functionSuffix: String): FunSpec {
        return FunSpec.builder("decode$functionSuffix${className.simpleName}")
            .receiver(nativeBufferClass)
            .returns(className)
            .addStatement("val rawValue = next${rawField.type}()")
            .addStatement(
                "return %T.entries.find { it.rawValue == rawValue } ?: error(%S)",
                className,
                "Can't find rawValue inside of ${className.simpleName}",
            )
            .build()
    }

    private fun buildEnumOrdinalDecodeFromBuffer(className: ClassName, functionSuffix: String): FunSpec {
        return FunSpec.builder("decode$functionSuffix${className.simpleName}")
            .receiver(nativeBufferClass)
            .returns(className)
            .addStatement("val ordinal = nextInt()")
            .addStatement(
                "return %T.entries.getOrNull(ordinal) ?: error(%S)",
                className,
                "Can't find ordinal inside of ${className.simpleName}",
            )
            .build()
    }

    private fun buildEnumValueProperty(
        className: ClassName,
        field: Field,
        useRawValue: Boolean,
    ): PropertySpec {
        val value = if (useRawValue) "rawValue" else "ordinal"
        return PropertySpec.builder("value", field.typeName)
            .receiver(className)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $value")
                    .build()
            )
            .build()
    }

    private fun encodeExprFor(
        typeName: TypeName,
        bufferExpr: String,
        fileSpec: FileSpec.Builder,
        functionSuffix: String,
    ): String {
        // strip nullability before switching
        val nonNull = when (typeName) {
            is ParameterizedTypeName -> typeName.copy(nullable = false)
            is ClassName             -> typeName.copy(nullable = false)
            else                     -> typeName
        }

        return when (nonNull) {
            is ClassName -> {
                var simple = nonNull.simpleName
                when {
                    simple.isPrimitive -> "$bufferExpr.push$simple(it)"

                    simple.isMatrix -> {
                        val majorSuffix = if (functionSuffix == FUNCTION_SUFFIX_GPU) {
                            "ColumnMajor"
                        } else {
                            "RowMajor"
                        }
                        "$bufferExpr.push$simple$majorSuffix(it)"
                    }

                    simple.isVariableLength -> {
                        if (nonNull.simpleName.isString) {
                            simple = if (typeName.nativeStringUtf16()) "StringUtf16" else "StringUtf8"
                        }

                        val fixedSize = typeName.nativeFixedSize()
                        if (fixedSize == null) {
                            "$bufferExpr.push$simple(it)"
                        } else {
                            "$bufferExpr.pushFixed$simple(it, $fixedSize)"
                        }
                    }

                    else -> {
                        fileSpec.addImport(nonNull.packageName, "encode$functionSuffix")
                        "it.encode$functionSuffix($bufferExpr)"
                    }
                }
            }

            is ParameterizedTypeName -> {
                when {
                    nonNull.rawType.simpleName.isList || nonNull.rawType.simpleName.isSet -> {
                        val elementType = nonNull.typeArguments.first()
                        val innerEncode = encodeExprFor(elementType, bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.pushCollection(it) { $innerEncode }"
                    }
                    nonNull.rawType.simpleName.isMap -> {
                        val encodeKey = encodeExprFor(nonNull.typeArguments[0], bufferExpr, fileSpec, functionSuffix)
                        val encodeValue = encodeExprFor(nonNull.typeArguments[1], bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.pushMap(it, { $encodeKey }, { $encodeValue })"
                    }
                    else -> error("Unsupported parameterized type: ${nonNull.rawType.simpleName}")
                }
            }

            else -> error("Unsupported type: $typeName")
        }
    }

    private fun decodeExprFor(
        typeName: TypeName,
        bufferExpr: String,
        fileSpec: FileSpec.Builder,
        functionSuffix: String,
    ): String {
        val nonNull = when (typeName) {
            is ParameterizedTypeName -> typeName.copy(nullable = false)
            is ClassName             -> typeName.copy(nullable = false)
            else                     -> typeName
        }

        return when (nonNull) {
            is ClassName -> {
                var simple = nonNull.simpleName
                when {
                    simple.isPrimitive -> "$bufferExpr.next$simple()"
                    simple.isMatrix -> {
                        val majorSuffix = if (functionSuffix == FUNCTION_SUFFIX_GPU) {
                            "ColumnMajor"
                        } else {
                            "RowMajor"
                        }
                        "$bufferExpr.next$simple$majorSuffix()"
                    }
                    simple.isVariableLength -> {
                        if (nonNull.simpleName.isString) {
                            simple = if (typeName.nativeStringUtf16()) "StringUtf16" else "StringUtf8"
                        }

                        val fixedSize = typeName.nativeFixedSize()
                        if (fixedSize == null) {
                            "$bufferExpr.next$simple()"
                        } else {
                            "$bufferExpr.next$simple($fixedSize)"
                        }
                    }
                    else -> {
                        fileSpec.addImport(nonNull.packageName, "decode$functionSuffix$simple")
                        "$bufferExpr.decode$functionSuffix$simple()"
                    }
                }
            }

            is ParameterizedTypeName -> {
                when {
                    nonNull.rawType.simpleName.isArray -> {
                        val innerDecode = decodeExprFor(nonNull.typeArguments.first(), bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.nextArray { $innerDecode }"
                    }
                    nonNull.rawType.simpleName.isList -> {
                        val innerDecode = decodeExprFor(nonNull.typeArguments.first(), bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.nextList { $innerDecode }"
                    }
                    nonNull.rawType.simpleName.isSet -> {
                        val innerDecode = decodeExprFor(nonNull.typeArguments.first(), bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.nextSet { $innerDecode }"
                    }
                    nonNull.rawType.simpleName.isGenericList -> {
                        val innerDecode = decodeExprFor(nonNull.typeArguments.first(), bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.nextGenericList { $innerDecode }"
                    }
                    nonNull.rawType.simpleName.isMap -> {
                        val decodeKey = decodeExprFor(nonNull.typeArguments[0], bufferExpr, fileSpec, functionSuffix)
                        val decodeValue = decodeExprFor(nonNull.typeArguments[1], bufferExpr, fileSpec, functionSuffix)
                        "$bufferExpr.nextMap({ $decodeKey }, { $decodeValue })"
                    }
                    else -> error("Unsupported parameterized type: ${nonNull.rawType.simpleName}")
                }
            }

            else -> error("Unsupported type: $typeName")
        }
    }

    private fun FileSpec.Builder.writeTo(declaration: KSClassDeclaration) {
        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        val className = declaration.qualifiedName()

        generator.createNewFile(
            dep,
            declaration.packageName.asString(),
            "${className.simpleName}.gen",
        ).bufferedWriter().use {
            val output = build()
                .toString()
                .replace(
                    Regex("package\\s+[^\\n]+\\n"),
                    "$0\nimport com.cws.std.memory.*\n"
                )
                .replace(
                    Regex("sumOf\\s*\\n\\s*\\{"),
                    "sumOf {"
                )

            it.write(output)
        }
    }

    private fun generatePrimitiveLists() {
        val pkg = "com.cws.std.lists"
        primitiveTypes.forEach { type ->
            typesWithDefaults[type]?.let { default ->
                generatePrimitiveList(pkg, type, default)
            }
        }
    }

    private fun generatePrimitiveList(pkg: String, type: String, default: String) {
        if (fileGenerator.contains("${type}List")) return

        val code = readTemplate("PrimitiveList")
            .replace("#pkg", pkg)
            .replace("#T", type)
            .replace("#DEFAULT_VALUE", default)

        fileGenerator.generateFile(pkg, "${type}List", code)
    }

    private fun readTemplate(name: String): String {
        val file = "templates/$name.txt"
        logger.warn("readTemplate: $file")
        return NativeProcessor::class.java.classLoader
            .getResourceAsStream(file)
            ?.bufferedReader()
            ?.readText()
            ?: error("File not found $file")
    }

}