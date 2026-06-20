package com.cws.std.storage

class DesktopPreferences(name: String) : Preferences {

    private val preferences = java.util.prefs.Preferences.userRoot().node(name)

    override fun setByte(key: String, value: Byte) {
        preferences.putInt(key, value.toInt())
    }

    override fun setBoolean(key: String, value: Boolean) {
        preferences.putBoolean(key, value)
    }

    override fun setShort(key: String, value: Short) {
        preferences.putInt(key, value.toInt())
    }

    override fun setInt(key: String, value: Int) {
        preferences.putInt(key, value)
    }

    override fun setLong(key: String, value: Long) {
        preferences.putLong(key, value)
    }

    override fun setFloat(key: String, value: Float) {
        preferences.putFloat(key, value)
    }

    override fun setDouble(key: String, value: Double) {
        preferences.putDouble(key, value)
    }

    override fun setString(key: String, value: String) {
        preferences.put(key, value)
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
        return preferences.getDouble(key, default)
    }

    override fun getString(key: String, default: String): String {
        return preferences.get(key, default)
    }

    override fun remove(key: String) {
        preferences.remove(key)
    }

    override fun commit() {
        preferences.flush()
    }

    override fun sync() {
        preferences.sync()
    }

}