package com.cws.std.memory

// use this annotation to generate encodings for annotated class
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeData

// use this annotation to generate encodings for annotated enum
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeEnum

// use this annotation to declare new message in Kotlin/C++ bridge protocol
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeMessage

// use this annotation for fixed size data types, like static arrays or static string
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeFixedSize(val size: Int)

// use this annotation to say generator to output String as UTF-16 encoded type
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class NativeStringUtf16