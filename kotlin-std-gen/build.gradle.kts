plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.nmcp)
    alias(libs.plugins.ksp)
    `maven-publish`
    signing
}

group = "io.github.cheerwizard"
version = "1.0.3"

dependencies {
    implementation(libs.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}

// Publishing
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = "kotlin-std-gen"

            pom {
                name.set("kotlin-std-gen")
                description.set("KSP code generator for kotlin-std.")
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