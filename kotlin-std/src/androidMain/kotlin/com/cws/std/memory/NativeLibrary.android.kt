package com.cws.std.memory

actual object NativeLibrary {

    actual inline fun <reified T> load(libName: String) {
        System.loadLibrary(libName)
    }

}