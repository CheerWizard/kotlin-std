package com.cws.std.storage

import platform.Foundation.NSNumber
import platform.Foundation.NSUserDefaults

actual class Preferences(name: String) {

    private val defaults = NSUserDefaults.standardUserDefaults()

    actual fun setByte(key: String, value: Byte) {
        defaults.setObject(value, key)
    }

    actual fun setBoolean(key: String, value: Boolean) {
        defaults.setObject(value, key)
    }

    actual fun setShort(key: String, value: Short) {
        defaults.setObject(value, key)
    }

    actual fun setInt(key: String, value: Int) {
        defaults.setObject(value, key)
    }

    actual fun setLong(key: String, value: Long) {
        defaults.setObject(value, key)
    }

    actual fun setFloat(key: String, value: Float) {
        defaults.setObject(value, key)
    }

    actual fun setDouble(key: String, value: Double) {
        defaults.setObject(value, key)
    }

    actual fun setString(key: String, value: String) {
        defaults.setObject(value, key)
    }

    actual fun getByte(key: String, default: Byte): Byte {
        return (defaults.objectForKey(key) as? NSNumber)?.charValue ?: default
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return (defaults.objectForKey(key) as? NSNumber)?.boolValue ?: default
    }

    actual fun getShort(key: String, default: Short): Short {
        return (defaults.objectForKey(key) as? NSNumber)?.shortValue ?: default
    }

    actual fun getInt(key: String, default: Int): Int {
        return (defaults.objectForKey(key) as? NSNumber)?.intValue ?: default
    }

    actual fun getLong(key: String, default: Long): Long {
        return (defaults.objectForKey(key) as? NSNumber)?.longLongValue ?: default
    }

    actual fun getFloat(key: String, default: Float): Float {
        return (defaults.objectForKey(key) as? NSNumber)?.floatValue ?: default
    }

    actual fun getDouble(key: String, default: Double): Double {
        return (defaults.objectForKey(key) as? NSNumber)?.doubleValue ?: default
    }

    actual fun getString(key: String, default: String): String {
        return defaults.stringForKey(key) ?: default
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    actual fun commit() {
        // no-op
    }

    actual fun sync() {
        defaults.synchronize()
    }

}

