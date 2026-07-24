package com.cws.std.test

import com.cws.std.memory.NativeData
import com.cws.std.memory.NativeEnum
import com.cws.std.memory.NativeFixedSize
import com.cws.std.memory.NativeStringUtf16
import kotlin.math.PI
import kotlinx.serialization.Serializable

@Serializable
@NativeEnum
enum class TestEnumOrdinal {
    Ordinal_0,
    Ordinal_1,
    Ordinal_2,
    Ordinal_3,
    Ordinal_4,
    Ordinal_5,
}

@Serializable
@NativeEnum
enum class TestEnumRaw(val rawValue: Float) {
    Raw_0(0.125f),
    Raw_1(123.3f),
    Raw_2(1237.586f),
    Raw_3(0f),
    Raw_4(2442f),
    Raw_5(PI.toFloat()),
}

@Serializable
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

    @Serializable
    @NativeData
    data class NestedData(
        val id: Long,
        val data: Map<String, String>,
        val subscribers: Set<String>,
    )

}