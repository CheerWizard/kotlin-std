@file:OptIn(ExperimentalForeignApi::class)

package com.cws.std.storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

internal actual fun getPreferencesFilepath(name: String): String {
    return getenv("XDG_CONFIG_HOME")?.toKString() ?: "${getenv("HOME")!!.toKString()}/.config/$name/preferences.json"
}