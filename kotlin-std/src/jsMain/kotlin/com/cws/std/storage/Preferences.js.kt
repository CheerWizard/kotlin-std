package com.cws.std.storage

import kotlinx.browser.window
import org.w3c.dom.get
import org.w3c.dom.set

class JsPreferences(name: String) : Preferences {

    override fun setByte(key: String, value: Byte) {
        window.localStorage[key] = value.toString()
    }

    override fun setBoolean(key: String, value: Boolean) {
        window.localStorage[key] = value.toString()
    }

    override fun setShort(key: String, value: Short) {
        window.localStorage[key] = value.toString()
    }

    override fun setInt(key: String, value: Int) {
        window.localStorage[key] = value.toString()
    }

    override fun setLong(key: String, value: Long) {
        window.localStorage[key] = value.toString()
    }

    override fun setFloat(key: String, value: Float) {
        window.localStorage[key] = value.toString()
    }

    override fun setDouble(key: String, value: Double) {
        window.localStorage[key] = value.toString()
    }

    override fun setString(key: String, value: String) {
        window.localStorage[key] = value
    }

    override fun getByte(key: String, default: Byte): Byte {
        return window.localStorage[key]?.toByteOrNull() ?: default
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return window.localStorage[key]?.toBooleanStrictOrNull() ?: default
    }

    override fun getShort(key: String, default: Short): Short {
        return window.localStorage[key]?.toShortOrNull() ?: default
    }

    override fun getInt(key: String, default: Int): Int {
        return window.localStorage[key]?.toIntOrNull() ?: default
    }

    override fun getLong(key: String, default: Long): Long {
        return window.localStorage[key]?.toLongOrNull() ?: default
    }

    override fun getFloat(key: String, default: Float): Float {
        return window.localStorage[key]?.toFloatOrNull() ?: default
    }

    override fun getDouble(key: String, default: Double): Double {
        return window.localStorage[key]?.toDoubleOrNull() ?: default
    }

    override fun getString(key: String, default: String): String {
        return window.localStorage[key] ?: default
    }

    override fun remove(key: String) {
        window.localStorage.removeItem(key)
    }

    override fun commit() {
        // do nothing
    }

    override fun sync() {
        // do nothing
    }

}