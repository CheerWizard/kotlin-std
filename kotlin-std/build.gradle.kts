/*
 *
 *  * Copyright 2026 CheerWizard
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.nmcp)
    alias(libs.plugins.ksp)
    alias(libs.plugins.spotless)
    `maven-publish`
    signing
}

group = "io.github.cheerwizard"
version = "1.0.9"

spotless {
    kotlin {
        target("**/*.kt")
        licenseHeaderFile(
            rootProject.layout.projectDirectory.file("config/license.txt").asFile,
            "^(@file:|package )"
        )
    }
    format("cpp") {
        target(
            "**/*.c",
            "**/*.cpp",
            "**/*.h",
            "**/*.hpp"
        )

        targetExclude(
            "**/.cxx/**",
            "**/build/**",
            "**/cmake-build*/**",
            "**/out/**"
        )

        licenseHeaderFile(
            rootProject.layout.projectDirectory.file("config/license-cpp.txt").asFile,
            "^(#pragma once|#include|namespace)"
        )
    }
}

tasks.named("check") {
    dependsOn("spotlessCheck")
}

kotlin {
    android {
        namespace = "com.cws.std"
        compileSdk = 37
        minSdk = 26

        withHostTest {}
        withDeviceTest {}
    }

    jvm("desktop")

    js {
        browser()
    }

    wasmJs {
        browser()
    }

    mingwX64()
    linuxX64()
    macosArm64()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDirs(
                "$buildDir/generated/commonMain/kotlin",
                "build/generated/ksp/metadata/commonMain/kotlin",
            )

            dependencies {
                // Logger
                api("io.github.cheerwizard:print-lib:1.0.5")
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

        val webMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(libs.kotlin.web)
                api(libs.kotlin.browser)
                api(libs.kotlinx.browser)
            }
        }
        val jsMain by getting {
            dependsOn(webMain)
        }
        val wasmJsMain by getting {
            dependsOn(webMain)
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }
        val iosX64Main by getting { dependsOn(iosMain) }
        val iosArm64Main by getting { dependsOn(iosMain) }
        val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val mingwX64Main by getting { dependsOn(nativeMain) }
        val linuxX64Main by getting { dependsOn(nativeMain) }
        val macosArm64Main by getting { dependsOn(nativeMain) }

        // test source sets

        val androidHostTest by getting {
            dependencies {
                implementation(libs.robolectric)
                implementation(libs.test.core)
            }
        }

        val webTest by creating { dependsOn(commonTest) }
        val jsTest by getting { dependsOn(webTest) }
        val wasmJsTest by getting { dependsOn(webTest) }

        val iosTest by creating { dependsOn(commonTest) }
        val iosX64Test by getting { dependsOn(iosTest) }
        val iosArm64Test by getting { dependsOn(iosTest) }
        val iosSimulatorArm64Test by getting { dependsOn(iosTest) }

        val nativeTest by creating { dependsOn(commonTest) }
        val mingwX64Test by getting { dependsOn(nativeTest) }
        val linuxX64Test by getting { dependsOn(nativeTest) }
        val macosArm64Test by getting { dependsOn(nativeTest) }
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
                    name.set("Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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

sealed class JniTarget {
    abstract val name: String
    abstract val libraryName: (String) -> String
    abstract val outputResourcePath: String

    abstract fun configureArgs(javaHome: String): List<String>
}

data class DesktopJniTarget(
    override val name: String,
    val generator: String,
    val jniInclude: String,
    override val libraryName: (String) -> String,
) : JniTarget() {
    override val outputResourcePath = "src/desktopMain/resources/jni/$name"

    override fun configureArgs(javaHome: String) = listOf(
        "-G", generator,
        "-DCMAKE_BUILD_TYPE=Release",
        "-DJAVA_HOME=$javaHome",
        "-DCMAKE_INCLUDE_PATH=$javaHome/include;$javaHome/include/$jniInclude",
    )
}

data class AndroidJniTarget(
    val abi: String,
    val minSdk: Int = 26,
    override val libraryName: (String) -> String = { "lib$it.so" },
) : JniTarget() {
    override val name = "android-$abi"
    override val outputResourcePath = "src/androidMain/jniLibs/$abi"

    override fun configureArgs(javaHome: String): List<String> {
        val ndkHome = resolveNdkHome()

        val toolchainFile = File(ndkHome, "build/cmake/android.toolchain.cmake")
        if (!toolchainFile.exists()) {
            throw GradleException("Android NDK toolchain file not found at $toolchainFile")
        }

        val ninjaPath = resolveNinjaPath()

        return listOf(
            "-G", "Ninja",
            "-DCMAKE_MAKE_PROGRAM=$ninjaPath",
            "-DCMAKE_TOOLCHAIN_FILE=${toolchainFile.absolutePath}",
            "-DANDROID_ABI=$abi",
            "-DANDROID_PLATFORM=android-$minSdk",
            "-DCMAKE_BUILD_TYPE=Release",
        )
    }
}

fun registerCmakeTask(
    projectName: String,
    target: JniTarget,
): TaskProvider<Task> {
    val javaHome = System.getenv("JAVA_HOME")

    val cmakePath = resolveSystemCmakePath()

    val safeName = target.name.replaceFirstChar(Char::uppercase)
        .replace("-", "")
        .replace(Regex("[^A-Za-z0-9]"), "")

    val outDir = layout.buildDirectory.dir("jni/${target.name}")

    val configure = tasks.register<Exec>("configureJni$safeName") {
        group = "jni"
        doFirst { outDir.get().asFile.mkdirs() }
        workingDir(outDir.get().asFile)
        environment("JAVA_HOME", javaHome)
        commandLine(listOf(cmakePath)
                + target.configureArgs(javaHome)
                + listOf(layout.projectDirectory.dir("src/cpp/$projectName").asFile.absolutePath)
        )
    }

    val build = tasks.register<Exec>("buildNativeJni$safeName") {
        group = "jni"
        dependsOn(configure)
        workingDir(outDir.get().asFile)
        environment("JAVA_HOME", javaHome)
        commandLine(cmakePath, "--build", ".")
    }

    val copy = tasks.register<Copy>("copyJni$safeName") {
        group = "jni"
        dependsOn(build)
        from(outDir.map { it.file(target.libraryName(projectName)) })
        into(layout.projectDirectory.dir(target.outputResourcePath))
    }

    return tasks.register("buildJni$safeName") {
        group = "jni"
        dependsOn(copy)
    }
}

val desktopJniTargets = listOf(
    DesktopJniTarget(name = "linuxX64", generator = "Unix Makefiles", jniInclude = "linux", libraryName = { "lib$it.so" }),
    DesktopJniTarget(name = "macosArm64", generator = "Unix Makefiles", jniInclude = "darwin", libraryName = { "lib$it.dylib" }),
    DesktopJniTarget(name = "mingwX64", generator = "Visual Studio 17 2022", jniInclude = "win32", libraryName = { "$it.dll" }),
)

val androidJniTargets = listOf(
    AndroidJniTarget(abi = "arm64-v8a"),
    AndroidJniTarget(abi = "x86_64"),
)

val allJniTargets: List<JniTarget> = desktopJniTargets + androidJniTargets

val registeredTasks = allJniTargets.associateWith {
    registerCmakeTask(projectName = "cmemory", target = it)
}

val currentHost: String get() = when {
    org.gradle.internal.os.OperatingSystem.current().isLinux -> "linuxX64"
    org.gradle.internal.os.OperatingSystem.current().isMacOsX -> "macosArm64"
    org.gradle.internal.os.OperatingSystem.current().isWindows -> "mingwX64"
    else -> throw GradleException("Unsupported desktop OS")
}

tasks.register("buildJni") {
    group = "jni"
    dependsOn(registeredTasks.entries.first { it.key.name == currentHost }.value)
}

tasks.register("buildJniAndroid") {
    group = "jni"
    dependsOn(registeredTasks.filterKeys { it is AndroidJniTarget }.values)
}

fun Project.resolveNdkHome(): String {
    // Return explicitly set variable
    System.getenv("ANDROID_NDK_HOME")?.let { return it }

    // Fallback to Android SDK locally installed on machine
    val sdkDir = run {
        val localProps = Properties()
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            localProps.load(localPropsFile.inputStream())
        }
        localProps.getProperty("sdk.dir") ?: System.getenv("ANDROID_HOME")
    } ?: throw GradleException("Could not resolve Android SDK location (no local.properties sdk.dir or ANDROID_HOME)")

    val ndkRoot = File(sdkDir, "ndk")
    val ndkVersion = ndkRoot.listFiles()?.filter { it.isDirectory }?.maxByOrNull { it.name }
        ?: throw GradleException("No NDK version found under $ndkRoot. Install one via Android Studio's SDK Manager.")

    return ndkVersion.absolutePath
}

fun Project.resolveNinjaPath(): String {
    val sdkDir = resolveSdkDir()
    val cmakeRoot = File(sdkDir, "cmake")
    val cmakeVersionDir = cmakeRoot.listFiles()?.filter { it.isDirectory }?.maxByOrNull { it.name }
        ?: throw GradleException("No SDK-bundled CMake/Ninja found under $cmakeRoot. Install 'CMake' via SDK Manager.")
    val ninja = File(cmakeVersionDir, "bin/ninja")
    if (!ninja.exists()) throw GradleException("ninja binary not found at $ninja")
    return ninja.absolutePath
}

fun Project.resolveSdkDir(): String {
    // 1. local.properties (standard Android project convention)
    val localPropsFile = rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        val localProps = Properties()
        localProps.load(localPropsFile.inputStream())
        localProps.getProperty("sdk.dir")?.let { return it }
    }

    // 2. Environment variable fallback
    System.getenv("ANDROID_HOME")?.let { return it }
    System.getenv("ANDROID_SDK_ROOT")?.let { return it } // older/alternate env var some setups still use

    throw GradleException(
        "Could not resolve Android SDK location. Set 'sdk.dir' in local.properties, " +
                "or export ANDROID_HOME / ANDROID_SDK_ROOT."
    )
}

fun Project.resolveSystemCmakePath(): String {
    // 1. Check if an environment variable specifies it directly
    System.getenv("CMAKE_PATH")?.let { return it }

    // 2. Check standard installation locations (especially for macOS Homebrew)
    val commonPaths = listOf("/usr/local/bin/cmake", "/opt/homebrew/bin/cmake", "/usr/bin/cmake")
    for (path in commonPaths) {
        if (File(path).exists()) return path
    }

    // 3. Fallback to a shell evaluation query to find out where it is
    try {
        val process = ProcessBuilder("which", "cmake").start()
        val path = process.inputStream.bufferedReader().readText().trim()
        if (path.isNotEmpty() && File(path).exists()) return path
    } catch (_: Exception) {}

    // 4. Ultimate fallback if nothing else catches it
    return "cmake"
}

// Code generation configuration
dependencies {
    add("kspCommonMainMetadata", project(":kotlin-std-gen"))
}
afterEvaluate {
    listOf(
        "sourcesJar",
        // JVM Desktop
        "compileKotlinDesktop",
        "desktopSourcesJar",
        // Android
        "compileAndroidMain",
        "androidSourcesJar",
        // JS
        "compileKotlinJs",
        "jsSourcesJar",
        // WASM
        "compileKotlinWasmJs",
        "wasmJsSourcesJar",
        // Native Desktop
        "compileKotlinLinuxX64",
        "linuxX64SourcesJar",
        "compileKotlinMacosArm64",
        "macosArm64SourcesJar",
        "compileKotlinMingwX64",
        "mingwX64SourcesJar",
        // IOS
        "compileKotlinIosArm64",
        "compileKotlinIosSimulatorArm64",
        "compileKotlinIosX64",
        "kspKotlinIosArm64",
        "kspKotlinIosSimulatorArm64",
        "kspKotlinIosX64",
    ).forEach { taskName ->
        tasks.findByName(taskName)?.dependsOn("kspCommonMainKotlinMetadata")
    }
}