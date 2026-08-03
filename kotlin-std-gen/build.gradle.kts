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

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.nmcp)
    alias(libs.plugins.ksp)
    alias(libs.plugins.spotless)
    `maven-publish`
    signing
}

group = "io.github.cheerwizard"
version = "1.0.13"

java {
    withSourcesJar()
    withJavadocJar()
}

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

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}

// Publishing
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name.set("kotlid-std-gen")
                description.set("KSP code generator to kotlin-std library")
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