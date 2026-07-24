package com.cws.std.storage

actual class Preferences(name: String) {

    private val preferences = java.util.prefs.Preferences.userRoot().node(name)

    actual fun setByte(key: String, value: Byte) {
        preferences.putInt(key, value.toInt())
    }

    actual fun setBoolean(key: String, value: Boolean) {
        preferences.putBoolean(key, value)
    }

    actual fun setShort(key: String, value: Short) {
        preferences.putInt(key, value.toInt())
    }

    actual fun setInt(key: String, value: Int) {
        preferences.putInt(key, value)
    }

    actual fun setLong(key: String, value: Long) {
        preferences.putLong(key, value)
    }

    actual fun setFloat(key: String, value: Float) {
        preferences.putFloat(key, value)
    }

    actual fun setDouble(key: String, value: Double) {
        preferences.putDouble(key, value)
    }

    actual fun setString(key: String, value: String) {
        preferences.put(key, value)
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
        return preferences.getDouble(key, default)
    }

    actual fun getString(key: String, default: String): String {
        return preferences.get(key, default)
    }

    actual fun remove(key: String) {
        preferences.remove(key)
    }

    actual fun commit() {
        preferences.flush()
    }

    actual fun sync() {
        preferences.sync()
    }

}