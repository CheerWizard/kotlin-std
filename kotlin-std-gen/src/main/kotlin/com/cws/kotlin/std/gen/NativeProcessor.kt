package com.cws.kotlin.std.gen

import com.google.devtools.ksp.getDeclaredProperties
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
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.ksp.toTypeName

class NativeProcessor(
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
        logger.warn("Scanning for @NativeObject...")

        val classes = resolver
            .getSymbolsWithAnnotation("com.cws.std.memory.NativeObject")
            .filterIsInstance<KSClassDeclaration>()
            .filter { declaration ->
                declaration.annotations
                    .find { it.shortName.asString() == "NativeObject" } != null
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
        val memoryHandleType = ClassName("com.cws.std.memory", "MemoryHandle")

        val fields = mutableListOf<Field>()
        var offset = "handle"

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
                    generatedType = type,
                    generatedTypeName = typeName,
                    type = type,
                    typeName = typeName,
                    nested = nested,
                    defaultValue = defaultValue
                )

                val typeSize = if (type in smallTypes) "Int.SIZE_BYTES" else "$type.SIZE_BYTES"

                offset += " + $typeSize"
            }

        val fileSpec = FileSpec.builder(pkg, generatedName)

        val size = fields.joinToString(" + ") {
            if (it.type in smallTypes) {
                "Int.SIZE_BYTES"
            } else {
                "${it.generatedType}.SIZE_BYTES"
            }
        }

        val constructorBuilder = FunSpec.constructorBuilder()

        constructorBuilder.addParameters(fields.map { field ->
            ParameterSpec
                .builder(field.name, field.generatedTypeName)
                .defaultValue(field.defaultValue)
                .build()
        })

        constructorBuilder.addParameter(
            ParameterSpec
                .builder("handle", memoryHandleType)
                .defaultValue("create().handle")
                .build()
        )

        constructorBuilder.callThisConstructor("handle")

        fields.forEach { field ->
            constructorBuilder.addStatement("this.${field.name} = ${field.name}")
        }

        val constructor = constructorBuilder.build()

        val generatedClass = TypeSpec.classBuilder(generatedName)
            .addModifiers(KModifier.VALUE)
            .addAnnotation(JvmInline::class)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("handle", memoryHandleType)
                    .build()
            )
            .addFunction(constructor)
            .addProperty(
                PropertySpec.builder("handle", memoryHandleType)
                    .initializer("handle")
                    .build()
            )
            .addType(
                TypeSpec.companionObjectBuilder()
                    .addProperty(
                        PropertySpec.builder("SIZE_BYTES", INT)
                            .addModifiers(KModifier.CONST)
                            .initializer(size)
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("create")
                            .returns(generatedClassName)
                            .addStatement("return %T(Heap.allocate(SIZE_BYTES))", generatedClassName)
                            .build()
                    ).apply {
                        val companions = declaration.declarations
                            .filterIsInstance<KSClassDeclaration>()
                            .filter { it.isCompanionObject }
                            .asIterable()

                        companions.forEach { companion ->
                            addProperties(
                                companion
                                    .getDeclaredProperties()
                                    .asIterable()
                                    .map { it.toPropertySpec() }
                            )
                            // TODO: function are always with empty body,
                            //  because KotlinPoet can only see declarations, not definitions
//                            addFunctions(
//                                companion
//                                    .getDeclaredFunctions()
//                                    .asIterable()
//                                    .map { it.com.cws.kotlin.std.gen.toFunSpec() }
//                                    .filterFunctions()
//                            )
                        }
                    }
                    .build()
            )
            .addFunction(
                FunSpec.builder("free")
                    .addStatement("Heap.free(handle, SIZE_BYTES)")
                    .addStatement("return %T(NULL)", generatedClassName)
                    .returns(generatedClassName)
                    .build()
            )

        // TODO: function are always with empty body,
        //  because KotlinPoet can only see declarations, not definitions

//            .addFunctions(
//                declaration
//                    .getAllFunctions()
//                    .asIterable()
//                    .map { it.com.cws.kotlin.std.gen.toFunSpec() }
//                    .filterFunctions()
//            )

        // properties
        fields.forEach { field ->
            val name = field.name
            val type = field.generatedTypeName as ClassName

            val property = PropertySpec.builder(name, type)
                .mutable(true)

            if (field.generatedType.isNotEmpty()) {
                if (field.nested) {
                    property.setter(
                        FunSpec.builder("set()")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("handle.checkNotNull()")
                            .addStatement("Heap.copyTo(Heap, value.handle, ${field.offset}, ${field.generatedType}.SIZE_BYTES)")
                            .build()
                    )
                    property.getter(
                        FunSpec.builder("get()")
                            .addStatement("handle.checkNotNull()")
                            .addStatement("return ${field.generatedType}(${field.offset})")
                            .build()
                    )
                } else {
                    property.setter(
                        FunSpec.builder("set()")
                            .addParameter("value", field.generatedTypeName)
                            .addStatement("handle.checkNotNull()")
                            .addStatement("Heap.set${field.generatedType}(${field.offset}, value)")
                            .build()
                    )
                    property.getter(
                        FunSpec.builder("get()")
                            .addStatement("handle.checkNotNull()")
                            .addStatement("return Heap.get${field.generatedType}(${field.offset})")
                            .build()
                    )
                }
            }

            generatedClass.addProperty(property.build())
        }

        val blockParam = ParameterSpec.builder(
            "block",
            LambdaTypeName.get(
                parameters = listOf(
                    ParameterSpec
                        .builder(
                            "value",
                            generatedClassName
                        ).build()
                ),
                returnType = TypeVariableName("T")
            )
        ).build()

        val useFun = FunSpec.builder("use")
            .receiver(generatedClassName)
            .addTypeVariable(TypeVariableName("T"))
            .addParameter(blockParam)
            .returns(TypeVariableName("T"))
            .addModifiers(KModifier.INLINE)
            .addCode(
                """
        try {
            return block(this)
        } finally {
            free()
        }
        """.trimIndent()
            )
            .build()

        val stackFunBuilder = FunSpec.builder(generatedName)
            .receiver(ClassName(pkg, "Stack"))
            .returns(generatedClassName)

        fields.forEach { field ->
            stackFunBuilder.addParameter(
                ParameterSpec
                    .builder(field.name, field.generatedTypeName)
                    .defaultValue(field.defaultValue)
                    .build()
            )
        }

        var constructorParams = fields.joinToString(",") { it.name }
        if (constructorParams.isNotEmpty()) {
            constructorParams += ","
        }

        stackFunBuilder.addCode(
            """
            return $generatedName($constructorParams push($generatedName.SIZE_BYTES))
            """.trimIndent()
        )

        val stackFun = stackFunBuilder.build()

        val copyParams = fields
            .map { "\n${it.name} = ${it.name}," }
            .joinToString("")

        val copyFun = FunSpec.builder("copy")
            .returns(generatedClassName)
            .addStatement("return $generatedName($copyParams)")
            .build()

        generatedClass.addFunction(copyFun)

        fileSpec.addImport("com.cws.std.memory", "NULL")
        fileSpec.addImport("com.cws.std.memory", "checkNotNull")
        fileSpec.addImport("com.cws.std.memory", "Heap")
        fileSpec.addImport("com.cws.std.memory", "Stack")
        fileSpec.addType(generatedClass.build())
        fileSpec.addFunction(stackFun)
        fileSpec.addFunction(useFun)

        val dep = declaration.containingFile?.let {
            Dependencies(false, it)
        } ?: Dependencies(false)

        generator.createNewFile(dep, pkg, "$generatedName.ksp")
            .bufferedWriter()
            .use {
                fileSpec.build().writeTo(it)
            }

        // only uncomment to generate new version of collections once!
        generatePrimitiveCollections()

        generateObjectCollections(pkg, generatedName)
    }

    private fun generatePrimitiveCollections() {
        primitiveTypes.forEachIndexed { i, type ->
            generatePrimitiveList("com.cws.std.collections", type, primitiveDefaultValues[i])
            generatePrimitiveSet("com.cws.std.collections", type)
        }
        primitiveTypes.forEach { key ->
            primitiveTypes.forEach { value ->
                generatePrimitiveMap("com.cws.std.collections", key, value)
            }
        }
    }

    private fun generateObjectCollections(pkg: String, name: String) {
        generateNativeList(pkg, name)
        generateNativeSet(pkg, name)
        primitiveTypes.forEach { key ->
            generateNativeMap(pkg, key, name)
        }
    }

    private fun Iterable<FunSpec>.filterFunctions(): Iterable<FunSpec> {
        return filter {
            !it.isConstructor &&
            !it.isAccessor &&
            !it.name.contains("component") &&
            it.name !in filterMethods
        }
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

    private val generatedFiles = mutableSetOf<String>()

    private fun generateFile(pkg: String, name: String, code: String) {
        logger.warn("generateFile: $pkg.$name")
        generator
            .createNewFile(Dependencies.ALL_FILES, pkg, name)
            .bufferedWriter()
            .use {
                it.write(code)
                generatedFiles.add(name)
            }
    }

    fun generateNativeList(pkg: String, name: String) {
        if (generatedFiles.contains("${name}List")) return

        var code = readTemplate("NativeList")
            .replace("#pkg", pkg)
            .replace("#T", name)

        code = if (name == "Boolean") {
            code.replace("#S", "Byte")
        } else {
            code.replace("#S", name)
        }

        code = if (name in primitiveTypes) {
            code
                .replace("#add", "add$name(value)")
                .replace("#set", "set$name(handle, value)")
                .replace("#get", "get$name(handle)")
        } else {
            code
                .replace("#add", "addNativeObject(value.handle)")
                .replace("#set", "setNativeObject(handle, value.handle)")
                .replace("#get", "$name(handle = handle)")
        }

        generateFile(pkg, "${name}List", code)
    }

    fun generateNativeSet(pkg: String, name: String) {
        if (generatedFiles.contains("${name}Set")) return

        val code = readTemplate("NativeSet")
            .replace("#pkg", pkg)
            .replace("#T", name)

        generateFile(pkg, "${name}Set", code)
    }

    fun generateNativeMap(pkg: String, key: String, value: String) {
        val name = "$key$value"
        if (generatedFiles.contains("${name}Map")) return

        val code = readTemplate("NativeMap")
            .replace("#pkg", pkg)
            .replace("#T", name)
            .replace("#K", key)
            .replace("#V", value)

        generateFile(pkg, "${name}Map", code)
    }

    fun generatePrimitiveList(pkg: String, name: String, defaultValue: String) {
        if (generatedFiles.contains("${name}List")) return

        val code = readTemplate("PrimitiveList")
            .replace("#pkg", pkg)
            .replace("#T", name)
            .replace("#DEFAULT_VALUE", defaultValue)

        generateFile(pkg, "${name}List", code)
    }

    fun generatePrimitiveSet(pkg: String, name: String) {
        if (generatedFiles.contains("${name}Set")) return

        val code = readTemplate("PrimitiveSet")
            .replace("#pkg", pkg)
            .replace("#T", name)

        generateFile(pkg, "${name}Set", code)
    }

    fun generatePrimitiveMap(pkg: String, key: String, value: String) {
        val name = "$key$value"
        if (generatedFiles.contains("${name}Map")) return

        val code = readTemplate("PrimitiveMap")
            .replace("#pkg", pkg)
            .replace("#T", name)
            .replace("#K", key)
            .replace("#V", value)

        generateFile(pkg, "${name}Map", code)
    }

}