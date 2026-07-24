package com.cws.std.storage

import android.content.Context
import android.content.SharedPreferences

actual class Preferences(context: Context, name: String) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private var preferencesEdit: SharedPreferences.Editor? = null

    actual fun setByte(key: String, value: Byte) {
        edit {
            putInt(key, value.toInt())
        }
    }

    actual fun setBoolean(key: String, value: Boolean) {
        edit {
            putBoolean(key, value)
        }
    }

    actual fun setShort(key: String, value: Short) {
        edit {
            putInt(key, value.toInt())
        }
    }

    actual fun setInt(key: String, value: Int) {
        edit {
            putInt(key, value)
        }
    }

    actual fun setLong(key: String, value: Long) {
        edit {
            putLong(key, value)
        }
    }

    actual fun setFloat(key: String, value: Float) {
        edit {
            putFloat(key, value)
        }
    }

    actual fun setDouble(key: String, value: Double) {
        edit {
            putString(key, value.toString())
        }
    }

    actual fun setString(key: String, value: String) {
        edit {
            putString(key, value)
        }
    }

    actual fun getByte(key: String, default: Byte): Byte {
        return preferences.getInt(key, default.toInt()).toByte()
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }

    actual fun getShort(key: String, default: Short): Short {
        return preferences.getInt(key, default.toInt()).toShort()
    }

    actual fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }

    actual fun getLong(key: String, default: Long): Long {
        return preferences.getLong(key, default)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return preferences.getFloat(key, default)
    }

    actual fun getDouble(key: String, default: Double): Double {
        return preferences.getString(key, default.toString())?.toDoubleOrNull() ?: default
    }

    actual fun getString(key: String, default: String): String {
        return preferences.getString(key, default) ?: default
    }

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

    private inline fun edit(commit: Boolean = true, block: SharedPreferences.Editor.() -> Unit) {
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