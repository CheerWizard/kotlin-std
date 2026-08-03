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

import com.cws.print.Print
import com.cws.std.io.File
import com.cws.std.io.flush
import com.cws.std.io.readText
import com.cws.std.io.write
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

internal expect fun getPreferencesFilepath(name: String): String

actual class Preferences(
    private val name: String,
) {
    companion object {
        private const val TAG = "Preferences"
    }

    private val file = File(getPreferencesFilepath(name))
    private var data: MutableMap<String, String> =
        runBlocking {
            try {
                Json.decodeFromString(file.readText())
            } catch (e: Exception) {
                Print.e(TAG, e) { "Failed to load json data from $name.json" }
                mutableMapOf()
            }
        }

    actual fun setByte(
        key: String,
        value: Byte,
    ) {
        data[key] = value.toString()
    }

    actual fun setBoolean(
        key: String,
        value: Boolean,
    ) {
        data[key] = value.toString()
    }

    actual fun setShort(
        key: String,
        value: Short,
    ) {
        data[key] = value.toString()
    }

    actual fun setInt(
        key: String,
        value: Int,
    ) {
        data[key] = value.toString()
    }

    actual fun setLong(
        key: String,
        value: Long,
    ) {
        data[key] = value.toString()
    }

    actual fun setFloat(
        key: String,
        value: Float,
    ) {
        data[key] = value.toString()
    }

    actual fun setDouble(
        key: String,
        value: Double,
    ) {
        data[key] = value.toString()
    }

    actual fun setString(
        key: String,
        value: String,
    ) {
        data[key] = value
    }

    actual fun getByte(
        key: String,
        default: Byte,
    ): Byte = data[key]?.toByteOrNull() ?: default

    actual fun getBoolean(
        key: String,
        default: Boolean,
    ): Boolean = data[key]?.toBooleanStrictOrNull() ?: default

    actual fun getShort(
        key: String,
        default: Short,
    ): Short = data[key]?.toShortOrNull() ?: default

    actual fun getInt(
        key: String,
        default: Int,
    ): Int = data[key]?.toIntOrNull() ?: default

    actual fun getLong(
        key: String,
        default: Long,
    ): Long = data[key]?.toLongOrNull() ?: default

    actual fun getFloat(
        key: String,
        default: Float,
    ): Float = data[key]?.toFloatOrNull() ?: default

    actual fun getDouble(
        key: String,
        default: Double,
    ): Double = data[key]?.toDoubleOrNull() ?: default

    actual fun getString(
        key: String,
        default: String,
    ): String = data[key] ?: default

    actual fun remove(key: String) {
        data.remove(key)
    }

    actual fun commit() {
        runBlocking {
            try {
                file.write(Json.encodeToString(data))
                file.flush()
            } catch (e: Exception) {
                Print.e(TAG, e) { "Failed to commit json data to $name.json" }
            }
        }
    }

    actual fun sync() {
        runBlocking {
            try {
                data.putAll(Json.decodeFromString(file.readText()))
            } catch (e: Exception) {
                Print.e(TAG, e) { "Failed to sync json data from $name.json" }
            }
        }
    }
}
