package com.cws.std.memory

expect object NativeLibrary {
    inline fun <reified T> load(libName: String)
}