package com.cws.kotlin.std.gen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

class NativeDataProcessor(
    environment: SymbolProcessorEnvironment
) : SymbolProcessor {

    private val generator: CodeGenerator = environment.codeGenerator
    private val logger: KSPLogger = environment.logger

    data class Class(
        val declaration: KSClassDeclaration,
    )

    data class Field(
        val name: String,
        val offset: String,
        val std140_offset: String,
        val std430_offset: String,
        val typeName: TypeName,
        val type: String,
        val generatedTypeName: TypeName,
        val generatedType: String,
        val nested: Boolean,
        val defaultValue: String,
    )

    private val primitiveTypes = arrayOf(
        "Int", "Double", "Float", "Long", "Boolean", "Byte", "Short"
    )

    private val primitiveDefaultValues = arrayOf(
        "0", "0.0", "0f", "0L", "false", "0", "0"
    )

    private val filterMethods = arrayOf(
        "equals", "toString", "copy", "hashCode"
    )

    private val smallTypes = arrayOf(
        "Boolean", "Byte", "Short", "UByte", "UShort"
    )

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("Scanning for @NativeData...")

        val classes = resolver
            .getSymbolsWithAnnotation("com.cws.std.memory.NativeData")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations
                    .find { it.shortName.asString() == "NativeData" } != null
            }
            .map { declaration -> Class(declaration) }

        classes.forEach { clazz ->
            logger.warn("Generate: $clazz")
            generate(clazz)
        }

        return emptyList()
    }

    private fun generate(clazz: Class) {
        val declaration = clazz.declaration
        val pkg = declaration.packageName.asString()
        val generatedName = declaration.simpleName.asString().removePrefix("_")
        val generatedClassName = ClassName(pkg, generatedName)
        val nativeDataInterface = ClassName("com.cws.std.memory", "INativeData")
        val nativeBuffer = ClassName("com.cws.std.memory", "NativeBuffer")
        val memoryLayout = ClassName("com.cws.std.memory", "MemoryLayout")
        val fields = mutableListOf<Field>()
        var offset = "0"
        var std140_offset = "0"
        var std430_offset = "0"

        declaration.getAllProperties()
            .asIterable()
            .forEachIndexed { i, prop ->
                var typeName = prop.type.resolve().toTypeName()
                var type = typeName.toString().split(".").last()
                val nested = type !in primitiveTypes

                type = if (nested) type.removePrefix("_") else type

                when (type) {
                    in smallTypes -> {
                        type = "Int"
                        typeName as ClassName
                        typeName = ClassName(typeName.packageName, "Int")
                    }
                    "Vec3" -> {
                        type = "Vec4"
                        typeName as ClassName
                        typeName = ClassName(typeName.packageName, "Vec4")
                    }
                    "Mat3" -> {
                        type = "Mat4"
                        typeName as ClassName
                        typeName = ClassName(typeName.packageName, "Mat4")
                    }
                }

                val defaultValue = when (type) {
                    "Boolean" -> "false"
                    "Byte", "Short", "Int" -> "0"
                    "Long" -> "0L"
                    "Float" -> "0f"
                    "Double" -> "0.0"
                    else -> "$type()"
                }

                fields += Field(
                    name = prop.simpleName.asString(),
                    offset = offset,
                    std140_offset = std140_offset,
                    std430_offset = std430_offset,
                    generatedType = type,
                    generatedTypeName = typeName,
                    type = type,
                    typeName = typeName,
                    nested = nested,
                    defaultValue = defaultValue
                )

                val typeSize = if (type in smallTypes) "Int.SIZE_BYTES" else "$type.SIZE_BYTES"
                val std140_typeSize = if (type in smallTypes) "Int.STD140_SIZE_BYTES" else "$type.STD140_SIZE_BYTES"
                val std430_typeSize = if (type in smallTypes) "Int.STD430_SIZE_BYTES" else "$type.STD430_SIZE_BYTES"

                offset += " + $typeSize"
                std140_offset += " + $std140_typeSize"
                std430_offset += " + $std430_typeSize"
            }

        val fileSpec = FileSpec.builder(pkg, generatedName)

        val size = fields.joinToString(" + ") {
            if (it.type in smallTypes) {
                "Int.SIZE_BYTES"
            } else {
                "${it.generatedType}.SIZE_BYTES"
            }
        }

        val std140_size = fields.joinToString(" + ") {
            if (it.type in smallTypes) {
                "Int.STD140_SIZE_BYTES"
            } else {
                "${it.generatedType}.STD140_SIZE_BYTES"
            }
        }

        val std430_size = fields.joinToString(" + ") {
            if (it.type in smallTypes) {
                "Int.STD430_SIZE_BYTES"
            } else {
                "${it.generatedType}.STD430_SIZE_BYTES"
            }
        }

        val constructor = FunSpec.constructorBuilder()
            .addParameters(fields.map { field ->
                ParameterSpec.builder(field.name, field.generatedTypeName)
                    .defaultValue(field.defaultValue)
                    .build()
            })
            .build()

        val generatedClass = TypeSpec.classBuilder(generatedName)
            .addModifiers(KModifier.DATA)
            .primaryConstructor(constructor)
            .addSuperinterface(nativeDataInterface)
            .addProperties(fields.map { field ->
                PropertySpec.builder(field.name, field.generatedTypeName)
                    .initializer(field.name)
                    .build()
            })
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder("SIZE_BYTES", INT)
                            .addModifiers(KModifier.CONST)
                            .initializer(size)
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder("STD140_SIZE_BYTES", INT)
                            .addModifiers(KModifier.CONST)
                            .initializer(std140_size)
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder("STD430_SIZE_BYTES", INT)
                            .addModifiers(KModifier.CONST)
                            .initializer(std430_size)
                            .build()
                    )
                    .build()
            )
            .addFunction(
                FunSpec.builder("sizeBytes")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        ParameterSpec
                            .builder("layout", memoryLayout)
                            .build()
                    )
                    .returns(INT)
                    .addCode("return when (layout) {\n" +
                            "MemoryLayout.KOTLIN -> SIZE_BYTES" +
                            "MemoryLayout.STD140 -> STD140_SIZE_BYTES" +
                            "MemoryLayout.STD430 -> STD430_SIZE_BYTES" +
                            "\n}")
                    .build()
            )
            .addFunction(
                FunSpec.builder("serialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        ParameterSpec
                            .builder("buffer", nativeBuffer)
                            .build()
                    )
                    .addCode(fields.joinForSerialize())
                    .build()
            )
            .addFunction(
                FunSpec.builder("deserialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter(
                        ParameterSpec
                            .builder("buffer", nativeBuffer)
                            .build()
                    )
                    .addCode("return $generatedName(${fields.joinForDeserialize()}\n)")
                    .returns(nativeDataInterface)
                    .build()
            )

        fileSpec.addImport("com.cws.std.memory", "INativeData")
        fileSpec.addImport("com.cws.std.memory", "NativeBuffer")
        fileSpec.addType(generatedClass.build())

        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        generator.createNewFile(dep, pkg, "$generatedName.ksp")
            .bufferedWriter()
            .use {
                fileSpec.build().writeTo(it)
            }
    }

    private fun List<Field>.joinForSerialize(): String {
        return joinToString("") {
            if (it.type in primitiveTypes) {
                "\nbuffer.push${it.type}(${it.name})"
            } else {
                "\nbuffer.push(${it.name})"
            }
        }
    }

    private fun List<Field>.joinForDeserialize(): String {
        return joinToString("") {
            if (it.type in primitiveTypes) {
                "\nbuffer.next${it.type}(),"
            } else {
                "\nbuffer.next(${it.name}),"
            }
        }
    }

}