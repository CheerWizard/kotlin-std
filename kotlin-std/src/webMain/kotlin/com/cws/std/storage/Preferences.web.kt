/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.storage

import kotlinx.browser.window
import org.w3c.dom.get
import org.w3c.dom.set

actual class Preferences(
    name: String,
) {
    actual fun setByte(
        key: String,
        value: Byte,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setBoolean(
        key: String,
        value: Boolean,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setShort(
        key: String,
        value: Short,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setInt(
        key: String,
        value: Int,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setLong(
        key: String,
        value: Long,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setFloat(
        key: String,
        value: Float,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setDouble(
        key: String,
        value: Double,
    ) {
        window.localStorage[key] = value.toString()
    }

    actual fun setString(
        key: String,
        value: String,
    ) {
        window.localStorage[key] = value
    }

    actual fun getByte(
        key: String,
        default: Byte,
    ): Byte = window.localStorage[key]?.toByteOrNull() ?: default

    actual fun getBoolean(
        key: String,
        default: Boolean,
    ): Boolean = window.localStorage[key]?.toBooleanStrictOrNull() ?: default

    actual fun getShort(
        key: String,
        default: Short,
    ): Short = window.localStorage[key]?.toShortOrNull() ?: default

    actual fun getInt(
        key: String,
        default: Int,
    ): Int = window.localStorage[key]?.toIntOrNull() ?: default

    actual fun getLong(
        key: String,
        default: Long,
    ): Long = window.localStorage[key]?.toLongOrNull() ?: default

    actual fun getFloat(
        key: String,
        default: Float,
    ): Float = window.localStorage[key]?.toFloatOrNull() ?: default

    actual fun getDouble(
        key: String,
        default: Double,
    ): Double = window.localStorage[key]?.toDoubleOrNull() ?: default

    actual fun getString(
        key: String,
        default: String,
    ): String = window.localStorage[key] ?: default

    actual fun remove(key: String) {
        window.localStorage.removeItem(key)
    }

    actual fun commit() {
        // do nothing
    }

    actual fun sync() {
        // do nothing
    }
}
