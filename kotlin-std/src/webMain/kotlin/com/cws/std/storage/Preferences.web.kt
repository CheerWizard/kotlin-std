package com.cws.std.storage

import kotlinx.browser.window
import org.w3c.dom.get
import org.w3c.dom.set

actual class Preferences(name: String) {

    actual fun setByte(key: String, value: Byte) {
        window.localStorage[key] = value.toString()
    }

    actual fun setBoolean(key: String, value: Boolean) {
        window.localStorage[key] = value.toString()
    }

    actual fun setShort(key: String, value: Short) {
        window.localStorage[key] = value.toString()
    }

    actual fun setInt(key: String, value: Int) {
        window.localStorage[key] = value.toString()
    }

    actual fun setLong(key: String, value: Long) {
        window.localStorage[key] = value.toString()
    }

    actual fun setFloat(key: String, value: Float) {
        window.localStorage[key] = value.toString()
    }

    actual fun setDouble(key: String, value: Double) {
        window.localStorage[key] = value.toString()
    }

    actual fun setString(key: String, value: String) {
        window.localStorage[key] = value
    }

    actual fun getByte(key: String, default: Byte): Byte {
        return window.localStorage[key]?.toByteOrNull() ?: default
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return window.localStorage[key]?.toBooleanStrictOrNull() ?: default
    }

    actual fun getShort(key: String, default: Short): Short {
        return window.localStorage[key]?.toShortOrNull() ?: default
    }

    actual fun getInt(key: String, default: Int): Int {
        return window.localStorage[key]?.toIntOrNull() ?: default
    }

    actual fun getLong(key: String, default: Long): Long {
        return window.localStorage[key]?.toLongOrNull() ?: default
    }

    actual fun getFloat(key: String, default: Float): Float {
        return window.localStorage[key]?.toFloatOrNull() ?: default
    }

    actual fun getDouble(key: String, default: Double): Double {
        return window.localStorage[key]?.toDoubleOrNull() ?: default
    }

    actual fun getString(key: String, default: String): String {
        return window.localStorage[key] ?: default
    }

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