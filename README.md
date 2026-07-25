# kotlin-std

[![Maven Central](https://img.shields.io/maven-central/v/io.github.cheerwizard/kotlin-std)](https://search.maven.org/)
[![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-blue.svg)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)

**koltin-std** is a modern Kotlin Multiplatform utility library focused on low-level development, performance, and cross-platform consistency.

Unlike the Kotlin Standard Library, kotlin-std provides additional primitives that are commonly needed when building game engines, graphics applications, native libraries, and other performance-sensitive software.

The project is designed to work consistently across **JVM**, **Android**, **iOS**, **Kotlin/Native**, **JavaScript**, and **WebAssembly**.

---

## Features

### Memory

* Cross-platform `NativeBuffer`
* Sequential and random-access binary reading/writing
* Configurable endianness
* Multiple memory layouts
* Zero-copy operations where supported
* Copying, resizing, slicing and cloning
* Byte array interoperability

### Math

* Vector types
* Matrix types
* Common math utilities
* Engine-oriented APIs

### Profiling

* Lightweight profiling utilities
* Cross-platform timing
* Performance measurement helpers

### Concurrency

* Multiplatform synchronization primitives
* Extended concurrency utilities

### Utilities

* File utilities
* Platform abstractions
* Common helper APIs
* Binary serialization helpers

### Code Generation

The project also includes **kotlin-std-gen**, a KSP processor capable of generating high-performance binary serialization code for kotlin-std memory APIs.

---

## Supported Platforms

* JVM
* Android
* iOS
* Linux
* macOS
* Windows (MinGW)
* JavaScript
* WebAssembly (Wasm)

---

## Why kotlin-std?

Kotlin's standard library intentionally stays relatively small and platform-agnostic.

kotlin-std focuses on functionality frequently required by engine and systems development:

* deterministic binary memory layouts
* native-style buffers
* efficient serialization
* performance-oriented utilities
* consistent behaviour across every Kotlin Multiplatform target

The goal is to make writing low-level multiplatform code feel as natural as writing JVM applications.

---

## Installation

```kotlin
dependencies {
    implementation("io.github.cheerwizard:kotlin-std:<version>")
}
```

If you want to use automatic code generation:

```kotlin
plugins {
    alias(libs.plugins.ksp)
}

dependencies {
    implementation("io.github.cheerwizard:kotlin-std:<version>")
    ksp("io.github.cheerwizard:kotlin-std-gen:<version>")
}
```

---

## Design Goals

* Kotlin Multiplatform first
* Zero unnecessary allocations
* Predictable performance
* Native-friendly APIs
* Consistent behaviour across platforms
* Small, composable modules
* Modern Kotlin APIs

---

## Roadmap

Current development is focused on:

* additional memory utilities
* SIMD-friendly math
* binary serialization improvements
* profiling tools
* further KSP code generation
* improved multiplatform performance

---

## Contributing

Contributions, bug reports and feature requests are welcome.

If you discover a bug or have an idea for improving the library, please open an issue or submit a pull request.

---

## License

Licensed under the Apache License, Version 2.0.

See the [LICENSE](LICENSE) file for details.
