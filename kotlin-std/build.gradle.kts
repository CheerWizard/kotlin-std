plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.nmcp)
    `maven-publish`
    signing
}

group = "io.github.cheerwizard"
version = "1.0.2"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    js(IR) {
        browser {
            binaries.library()
        }
        nodejs {
            binaries.library()
        }
        compilerOptions {
            // enable support of BigInt for Long
            freeCompilerArgs.add("-Xir-per-module")
            freeCompilerArgs.add("-Xuse-js-bigint-for-long")
        }
    }
    jvm("desktop")
    iosArm64()
    iosX64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Logger
                api("io.github.cheerwizard:print-lib:1.0.0")
                // Standard
                api(kotlin("stdlib-common"))
                api(libs.kotlinx.atomicfu)
                api(libs.kotlinx.coroutines.core)
                api(libs.kotlinx.serialization.core)
                api(libs.kotlinx.serialization.json)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val jniMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(jniMain)
        }

        val desktopMain by getting {
            dependsOn(jniMain)
        }

        val jsMain by getting {
            dependsOn(commonMain)
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting { dependsOn(nativeMain) }
        val iosArm64Main by getting { dependsOn(nativeMain) }
        val iosSimulatorArm64Main by getting { dependsOn(nativeMain) }
    }
}

// TODO will need to find out how to fix it
//project.extra["jniProject"] = "cmemory"
//apply(from = "$rootDir/scripts/jni.gradle.kts")

android {
    namespace = "com.cws.std"
    compileSdk = 34
    ndkVersion = "27.3.13750724"

    defaultConfig {
        minSdk = 26
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/cpp/cmemory/CMakeLists.txt")
        }
    }
}

// Publishing
publishing {
    publications.withType<MavenPublication> {
        val pubName = name
        val javadocJar = tasks.register("${pubName}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(pubName)
        }
        artifact(javadocJar)
        pom {
            name.set("kotlid-std")
            description.set("A standard Kotlin library that includes custom memory managment, profiling, vector math, extended concurrency and other utilities.")
            url.set("https://github.com/CheerWizard/kotlin-std")

            licenses {
                license {
                    name.set("MIT License")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }

            developers {
                developer {
                    id.set("cheerwizard")
                    name.set("Cheer Wizard")
                    email.set("mechanik2442@gmail.com")
                }
            }

            scm {
                connection.set("scm:git:github.com/CheerWizard/kotlin-std.git")
                developerConnection.set("scm:git:ssh://github.com/CheerWizard/kotlin-std.git")
                url.set("https://github.com/CheerWizard/kotlin-std")
            }
        }
    }
}

// Signing
signing {
    val signingKey = System.getenv("GPG_SIGNING_KEY")
    val signingPassword = System.getenv("GPG_SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

// Auto publish to Maven Central portal
nmcp {
    publishAllPublicationsToCentralPortal {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
        publishingType = "AUTOMATIC"
    }
}

// TODO: need to fix build here
//val jniBuildDir = file("$buildDir/jni")
//
//fun cmakeTask(project: String): TaskProvider<Task?> {
//    // Determine platform once during configuration
//    val osName = System.getProperty("os.name").lowercase()
//    val osArch = System.getProperty("os.arch").lowercase()
//
//    println("OS: $osName, Arch: $osArch")
//
//    val generator = when {
//        osName.contains("windows") -> "Visual Studio 18 2026"
//        osName.contains("linux") -> "Unix Makefiles"
//        osName.contains("mac") -> "Unix Makefiles"
//        else -> throw GradleException("Unsupported OS: $osName")
//    }
//
//    val platform = when {
//        osName.contains("win") && (osArch == "amd64" || osArch == "x86_64") -> "windows-x86_64"
//        osName.contains("linux") && (osArch == "amd64" || osArch == "x86_64") -> "linux-x86_64"
//        osName.contains("linux") && (osArch == "aarch64" || osArch == "arm64") -> "linux-arm64"
//        osName.contains("mac") && (osArch == "x86_64") -> "macos-x86_64"
//        osName.contains("mac") && (osArch == "aarch64" || osArch == "arm64") -> "macos-arm64"
//        else -> throw GradleException("Unsupported platform: $osName / $osArch")
//    }
//
//    return tasks.register("buildJni_$platform") {
//        group = "jni"
//        doLast {
//            println("Running cmakeTask for platform:$platform project:$project")
//
//            val outDir = file( "$jniBuildDir/$platform")
//            outDir.mkdirs()
//
//            val javaHome = System.getenv("JAVA_HOME") ?: "/usr/lib/jvm/java-21-openjdk-amd64"
//
//            val jniPlatformInclude = when(platform) {
//                "linux-x86_64" -> "linux"
//                "windows-x86_64" -> "win32"
//                "macos-x86_64" -> "darwin"
//                else -> throw GradleException("Unknown platform")
//            }
//
//            val jniIncludeArgs = listOf(
//                "-DCMAKE_BUILD_TYPE=Release",
//                "-DJAVA_HOME=$javaHome",
//                "-DCMAKE_INCLUDE_PATH=$javaHome/include;$javaHome/include/$jniPlatformInclude"
//            )
//
//            exec {
//                workingDir = outDir
//                environment("JAVA_HOME", javaHome)
//                println("Running cmake -G $generator")
//                commandLine("cmake", "-G", generator, *jniIncludeArgs.toTypedArray(), "../../../src/cpp/$project")
//            }
//
//            exec {
//                workingDir = outDir
//                environment("JAVA_HOME", javaHome)
//                println("Running cmake --build .")
//                commandLine("cmake", "--build", ".")
//            }
//
//            val libName = when(platform) {
//                "linux-x86_64" -> "lib$project.so"
//                "windows-x86_64" -> "$project.dll"
//                "macos-x86_64" -> "lib$project.dylib"
//                else -> throw GradleException("Unknown platform")
//            }
//
//            copy {
//                val fromDir = if (osName.contains("win")) {
//                    "$outDir/Debug/$libName"
//                } else {
//                    "$outDir/$libName"
//                }
//                val toDir = "src/desktopMain/resources/jni/$platform"
//                println("Copying $fromDir -> $toDir")
//                from(fromDir)
//                into(toDir)
//            }
//        }
//    }
//}
//
//val cmakeBuild = cmakeTask("cmemory")
//
//tasks.register("kotlin-std_buildJni") {
//    dependsOn(cmakeBuild)
//}