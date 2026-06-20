package com.cws.std.storage

interface Preferences {

    fun setByte(key: String, value: Byte)
    fun setBoolean(key: String, value: Boolean)
    fun setShort(key: String, value: Short)
    fun setInt(key: String, value: Int)
    fun setLong(key: String, value: Long)
    fun setFloat(key: String, value: Float)
    fun setDouble(key: String, value: Double)
    fun setString(key: String, value: String)

    fun getByte(key: String, default: Byte = 0): Byte
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun getShort(key: String, default: Short = 0): Short
    fun getInt(key: String, default: Int = 0): Int
    fun getLong(key: String, default: Long = 0L): Long
    fun getFloat(key: String, default: Float = 0.0f): Float
    fun getDouble(key: String, default: Double = 0.0): Double
    fun getString(key: String, default: String): String

    fun remove(key: String)

    fun commit()

    fun sync()

}