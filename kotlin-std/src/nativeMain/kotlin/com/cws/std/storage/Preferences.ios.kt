package com.cws.std.storage

import platform.Foundation.NSUserDefaults

class IOSPreferences(name: String) : Preferences {

    private val defaults = NSUserDefaults.standardUserDefaults()

    override fun setByte(key: String, value: Byte) {
        defaults.setInteger(value.toLong(), key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        defaults.setBool(value, key)
    }

    override fun setShort(key: String, value: Short) {
        defaults.setInteger(value.toLong(), key)
    }

    override fun setInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), key)
    }

    override fun setLong(key: String, value: Long) {
        defaults.setInteger(value, key)
    }

    override fun setFloat(key: String, value: Float) {
        defaults.setFloat(value, key)
    }

    override fun setDouble(key: String, value: Double) {
        defaults.setDouble(value, key)
    }

    override fun setString(key: String, value: String) {
        defaults.setObject(value, key)
    }

    override fun getByte(key: String, default: Byte): Byte {
        return defaults.integerForKey(key).toByte()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return defaults.boolForKey(key)
    }

    override fun getShort(key: String, default: Short): Short {
        return defaults.integerForKey(key).toShort()
    }

    override fun getInt(key: String, default: Int): Int {
        return defaults.integerForKey(key).toInt()
    }

    override fun getLong(key: String, default: Long): Long {
        return defaults.integerForKey(key)
    }

    override fun getFloat(key: String, default: Float): Float {
        return defaults.floatForKey(key)
    }

    override fun getDouble(key: String, default: Double): Double {
        return defaults.doubleForKey(key)
    }

    override fun getString(key: String, default: String): String {
        return defaults.stringForKey(key) ?: default
    }

    override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    override fun commit() {
        // no-op
    }

    override fun sync() {
        defaults.synchronize()
    }

}

