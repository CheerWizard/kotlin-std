package com.cws.std.memory

import com.cws.print.Print
import java.io.File
import kotlin.use

actual object NativeLibrary {

    @Suppress("UnsafeDynamicallyLoadedCode")
    actual inline fun <reified T> load(libName: String) {
        val os = System.getProperty("os.name").lowercase()
        val arch = System.getProperty("os.arch").lowercase()

        val libFile = when {
            os.contains("win") && (arch == "amd64" || arch == "x86_64") -> "/jni/windows-x86_64/$libName.dll"
            os.contains("linux") && (arch == "amd64" || arch == "x86_64") -> "/jni/linux-x86_64/lib$libName.so"
            os.contains("linux") && (arch == "aarch64" || arch == "arm64") -> "/jni/linux-arm64/lib$libName.so"
            os.contains("mac") && (arch == "x86_64") -> "/jni/macos-x86_64/lib$libName.dylib"
            os.contains("mac") && (arch == "aarch64" || arch == "arm64") -> "/jni/macos-arm64/lib$libName.dylib"
            else -> error("Unsupported platform: $os / $arch")
        }

        val tmpFile = createTempFile(suffix = File(libFile).extension)

        Print.d("NativeLibrary", "Library path - $libFile")

        T::class.java.getResourceAsStream(libFile)!!.use { input ->
            tmpFile.outputStream().use { output ->
                output.write(input.readBytes())
            }
        }

        val outputPath = tmpFile.absolutePath
        Print.d("NativeLibrary", "Output path - $outputPath")
        System.load(outputPath)
    }

}