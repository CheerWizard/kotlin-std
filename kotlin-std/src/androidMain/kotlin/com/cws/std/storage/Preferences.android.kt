package com.cws.std.storage

import android.content.Context
import android.content.SharedPreferences

class AndroidPreferences(context: Context, name: String) : Preferences {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private var preferencesEdit: SharedPreferences.Editor? = null

    override fun setByte(key: String, value: Byte) {
        edit {
            putInt(key, value.toInt())
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        edit {
            putBoolean(key, value)
        }
    }

    override fun setShort(key: String, value: Short) {
        edit {
            putInt(key, value.toInt())
        }
    }

    override fun setInt(key: String, value: Int) {
        edit {
            putInt(key, value)
        }
    }

    override fun setLong(key: String, value: Long) {
        edit {
            putLong(key, value)
        }
    }

    override fun setFloat(key: String, value: Float) {
        edit {
            putFloat(key, value)
        }
    }

    override fun setDouble(key: String, value: Double) {
        edit {
            putString(key, value.toString())
        }
    }

    override fun setString(key: String, value: String) {
        edit {
            putString(key, value)
        }
    }

    override fun getByte(key: String, default: Byte): Byte {
        return preferences.getInt(key, default.toInt()).toByte()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }

    override fun getShort(key: String, default: Short): Short {
        return preferences.getInt(key, default.toInt()).toShort()
    }

    override fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return preferences.getLong(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return preferences.getFloat(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        return preferences.getString(key, default.toString())?.toDoubleOrNull() ?: default
    }

    override fun getString(key: String, default: String): String {
        return preferences.getString(key, default) ?: default
    }

    override fun remove(key: String) {
        edit {
            remove(key)
        }
    }

    override fun commit() {
        edit {}
    }

    override fun sync() {
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
            edit.commit()
        }
    }

}