package com.cws.std.storage

import com.cws.print.Print
import com.cws.std.io.File
import com.cws.std.io.readText
import com.cws.std.io.write
import kotlinx.serialization.json.Json

internal expect fun getPreferencesFilepath(name: String): String

actual class Preferences(private val name: String) {

    companion object {
        private const val TAG = "Preferences"
    }

    private val file = File(getPreferencesFilepath(name))
    private var data: MutableMap<String, String> = try {
        Json.decodeFromString(file.readText())
    } catch (e: Exception) {
        Print.e(TAG, "Failed to load json data from $name.json", e)
        mutableMapOf()
    }

    actual fun setByte(key: String, value: Byte) {
        data[key] = value.toString()
    }

    actual fun setBoolean(key: String, value: Boolean) {
        data[key] = value.toString()
    }

    actual fun setShort(key: String, value: Short) {
        data[key] = value.toString()
    }

    actual fun setInt(key: String, value: Int) {
        data[key] = value.toString()
    }

    actual fun setLong(key: String, value: Long) {
        data[key] = value.toString()
    }

    actual fun setFloat(key: String, value: Float) {
        data[key] = value.toString()
    }

    actual fun setDouble(key: String, value: Double) {
        data[key] = value.toString()
    }

    actual fun setString(key: String, value: String) {
        data[key] = value
    }

    actual fun getByte(key: String, default: Byte): Byte {
        return data[key]?.toByteOrNull() ?: default
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return data[key]?.toBooleanStrictOrNull() ?: default
    }

    actual fun getShort(key: String, default: Short): Short {
        return data[key]?.toShortOrNull() ?: default
    }

    actual fun getInt(key: String, default: Int): Int {
        return data[key]?.toIntOrNull() ?: default
    }

    actual fun getLong(key: String, default: Long): Long {
        return data[key]?.toLongOrNull() ?: default
    }

    actual fun getFloat(key: String, default: Float): Float {
        return data[key]?.toFloatOrNull() ?: default
    }

    actual fun getDouble(key: String, default: Double): Double {
        return data[key]?.toDoubleOrNull() ?: default
    }

    actual fun getString(key: String, default: String): String {
        return data[key] ?: default
    }

    actual fun remove(key: String) {
        data.remove(key)
    }

    actual fun commit() {
        try {
            file.write(Json.encodeToString(data))
            file.flush()
        } catch (e: Exception) {
            Print.e(TAG, "Failed to commit json data to $name.json", e)
        }
    }

    actual fun sync() {
        try {
            data.putAll(Json.decodeFromString(file.readText()))
        } catch (e: Exception) {
            Print.e(TAG, "Failed to sync json data from $name.json", e)
        }
    }

}