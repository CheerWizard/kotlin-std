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

import android.content.Context
import android.content.SharedPreferences

actual class Preferences(
    context: Context,
    name: String,
) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private var preferencesEdit: SharedPreferences.Editor? = null

    actual fun setByte(
        key: String,
        value: Byte,
    ) {
        edit {
            putInt(key, value.toInt())
        }
    }

    actual fun setBoolean(
        key: String,
        value: Boolean,
    ) {
        edit {
            putBoolean(key, value)
        }
    }

    actual fun setShort(
        key: String,
        value: Short,
    ) {
        edit {
            putInt(key, value.toInt())
        }
    }

    actual fun setInt(
        key: String,
        value: Int,
    ) {
        edit {
            putInt(key, value)
        }
    }

    actual fun setLong(
        key: String,
        value: Long,
    ) {
        edit {
            putLong(key, value)
        }
    }

    actual fun setFloat(
        key: String,
        value: Float,
    ) {
        edit {
            putFloat(key, value)
        }
    }

    actual fun setDouble(
        key: String,
        value: Double,
    ) {
        edit {
            putString(key, value.toString())
        }
    }

    actual fun setString(
        key: String,
        value: String,
    ) {
        edit {
            putString(key, value)
        }
    }

    actual fun getByte(
        key: String,
        default: Byte,
    ): Byte = preferences.getInt(key, default.toInt()).toByte()

    actual fun getBoolean(
        key: String,
        default: Boolean,
    ): Boolean = preferences.getBoolean(key, default)

    actual fun getShort(
        key: String,
        default: Short,
    ): Short = preferences.getInt(key, default.toInt()).toShort()

    actual fun getInt(
        key: String,
        default: Int,
    ): Int = preferences.getInt(key, default)

    actual fun getLong(
        key: String,
        default: Long,
    ): Long = preferences.getLong(key, default)

    actual fun getFloat(
        key: String,
        default: Float,
    ): Float = preferences.getFloat(key, default)

    actual fun getDouble(
        key: String,
        default: Double,
    ): Double = preferences.getString(key, default.toString())?.toDoubleOrNull() ?: default

    actual fun getString(
        key: String,
        default: String,
    ): String = preferences.getString(key, default) ?: default

    actual fun remove(key: String) {
        edit {
            remove(key)
        }
    }

    actual fun commit() {
        edit {}
    }

    actual fun sync() {
        // do nothing for now
    }

    private inline fun edit(
        commit: Boolean = true,
        block: SharedPreferences.Editor.() -> Unit,
    ) {
        var edit = preferencesEdit
        if (edit == null) {
            edit = preferences.edit()
            preferencesEdit = edit
        }
        edit.block()
        if (commit) {
            edit.apply()
        }
    }
}
