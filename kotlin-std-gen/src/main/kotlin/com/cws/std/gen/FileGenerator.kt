package com.cws.std.gen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

class FileGenerator(
    private val logger: KSPLogger,
    private val generator: CodeGenerator,
) {

    private val generatedFiles = mutableSetOf<String>()

    fun contains(name: String) = generatedFiles.contains(name)

    fun generateFile(pkg: String, name: String, code: String) {
        logger.warn("generateFile: $pkg.$name")
        generator
            .createNewFile(Dependencies.ALL_FILES, pkg, name)
            .bufferedWriter()
            .use {
                it.write(code)
                generatedFiles.add(name)
            }
    }

}