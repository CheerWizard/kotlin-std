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
package com.cws.std.test

import com.cws.std.math.vectors.*
import com.cws.std.math.matrices.*
import com.cws.std.memory.NativeData
import com.cws.std.memory.NativeEnum
import com.cws.std.memory.NativeFixedSize
import com.cws.std.memory.NativeStringUtf16
import kotlin.math.PI

@NativeEnum
enum class TestEnumOrdinal {
    Ordinal_0,
    Ordinal_1,
    Ordinal_2,
    Ordinal_3,
    Ordinal_4,
    Ordinal_5,
}

@NativeEnum
enum class TestEnumRaw(val rawValue: Float) {
    Raw_0(0.125f),
    Raw_1(123.3f),
    Raw_2(1237.586f),
    Raw_3(0f),
    Raw_4(2442f),
    Raw_5(PI.toFloat()),
}

@NativeData
data class TestData(
    val id: Long,
    val timestamp: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val x: Float,
    val y: Float,
    val flag: Boolean,
    val age: Short,
    val ordinalEnum: TestEnumOrdinal,
    val rawEnum: TestEnumRaw,
    @NativeFixedSize(12)
    val fixedStringUtf8: String,
    val stringUtf8: String,
    @NativeFixedSize(18)
    @NativeStringUtf16
    val fixedStringUtf16: String,
    @NativeStringUtf16
    val stringUtf16: String,
    @NativeFixedSize(24)
    val fixedBytes: ByteArray,
    val bytes: ByteArray,
    @NativeFixedSize(48)
    val fixedShorts: ShortArray,
    val shorts: ShortArray,
    @NativeFixedSize(64)
    val fixedInts: IntArray,
    val ints: IntArray,
    @NativeFixedSize(36)
    val fixedLongs: LongArray,
    val longs: LongArray,
    @NativeFixedSize(36)
    val fixedFloats: FloatArray,
    val floats: FloatArray,
    @NativeFixedSize(36)
    val fixedDoubles: DoubleArray,
    val doubles: DoubleArray,
    val data: List<NestedData>,
) {

    @NativeData
    data class NestedData(
        val id: Long,
        val float2: Float2,
        val float3: Float3,
        val float4: Float4,
        val int2: Int2,
        val int3: Int3,
        val int4: Int4,
        val uint2: UInt2,
        val uInt3: UInt3,
        val uInt4: UInt4,
        val mat2: Mat2,
        val mat3: Mat3,
        val mat4: Mat4,
        val quaternion: Quaternion,
        val data: Map<String, String>,
        val subscribers: Set<String>,
    )

}