/*
 * Copyright 2026 CheerWizard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cws.std.storage

expect class Preferences {
    fun setByte(
        key: String,
        value: Byte,
    )

    fun setBoolean(
        key: String,
        value: Boolean,
    )

    fun setShort(
        key: String,
        value: Short,
    )

    fun setInt(
        key: String,
        value: Int,
    )

    fun setLong(
        key: String,
        value: Long,
    )

    fun setFloat(
        key: String,
        value: Float,
    )

    fun setDouble(
        key: String,
        value: Double,
    )

    fun setString(
        key: String,
        value: String,
    )

    fun getByte(
        key: String,
        default: Byte = 0,
    ): Byte

    fun getBoolean(
        key: String,
        default: Boolean = false,
    ): Boolean

    fun getShort(
        key: String,
        default: Short = 0,
    ): Short

    fun getInt(
        key: String,
        default: Int = 0,
    ): Int

    fun getLong(
        key: String,
        default: Long = 0L,
    ): Long

    fun getFloat(
        key: String,
        default: Float = 0.0f,
    ): Float

    fun getDouble(
        key: String,
        default: Double = 0.0,
    ): Double

    fun getString(
        key: String,
        default: String,
    ): String

    fun remove(key: String)

    fun commit()

    fun sync()
}
