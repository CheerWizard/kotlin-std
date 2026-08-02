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
package com.cws.std.memory

enum class Endian {
    LITTLE,
    BIG,
}

enum class MemoryBoundary {
    // memory will live in scope of Kotlin VM heap,
    // useful for data < KOTLIN_HEAP_MAX_CAPACITY or data that could be safely copied
    KOTLIN_HEAP,

    // memory will live in scope of external boundary like C/C++ or WASM buffer.
    // useful for data > KOTLIN_HEAP_MAX_CAPACITY, big chunks of data or data that should not be interrupted by Kotlin GC,
    // or if data is passed to C/C++ libraries
    EXTERNAL,
}

// the best limit is ~10 MB, to prevent GC pressure
const val KOTLIN_HEAP_MAX_CAPACITY = 10 * 1024 * 1024

internal fun NativeBuffer.isHeapBoundary() = isHeapBoundary(limit)

internal fun NativeBuffer.isHeapBoundary(capacity: Int) =
    memoryBoundary == MemoryBoundary.KOTLIN_HEAP && capacity < KOTLIN_HEAP_MAX_CAPACITY

expect class NativeBuffer(
    capacity: Int,
    memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
    endian: Endian = Endian.LITTLE,
    memoryBoundary: MemoryBoundary = MemoryBoundary.KOTLIN_HEAP,
) {
    // wraps this buffer into EXTERNAL memory boundary
    constructor(
        address: Long,
        capacity: Int,
        memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
        endian: Endian = Endian.LITTLE,
    )

    // wraps this buffer into KOTLIN_HEAP memory boundary
    constructor(
        buffer: ByteArray,
        memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
        endian: Endian = Endian.LITTLE,
    )

    var position: Int
        internal set
    var limit: Int
        private set
    val address: Long
    val memoryLayout: MemoryLayout
    var memoryBoundary: MemoryBoundary
        private set
    val endian: Endian

    fun view(): Any?

    fun release()

    fun resize(newCapacity: Int)

    fun setByteArray(
        index: Int,
        array: ByteArray,
    )

    fun setCharArray(
        index: Int,
        array: CharArray,
    )

    fun setShortArray(
        index: Int,
        array: ShortArray,
    )

    fun setIntArray(
        index: Int,
        array: IntArray,
    )

    fun setFloatArray(
        index: Int,
        array: FloatArray,
    )

    fun setLongArray(
        index: Int,
        array: LongArray,
    )

    fun setDoubleArray(
        index: Int,
        array: DoubleArray,
    )

    fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        sizeBytes: Int,
    )

    fun copyToByteArray(
        array: ByteArray,
        offset: Int,
        sizeBytes: Int,
    ): ByteArray

    fun copyToCharArray(
        array: CharArray,
        offset: Int,
        sizeBytes: Int,
    ): CharArray

    fun copyToShortArray(
        array: ShortArray,
        offset: Int,
        sizeBytes: Int,
    ): ShortArray

    fun copyToIntArray(
        array: IntArray,
        offset: Int,
        sizeBytes: Int,
    ): IntArray

    fun copyToFloatArray(
        array: FloatArray,
        offset: Int,
        sizeBytes: Int,
    ): FloatArray

    fun copyToLongArray(
        array: LongArray,
        offset: Int,
        sizeBytes: Int,
    ): LongArray

    fun copyToDoubleArray(
        array: DoubleArray,
        offset: Int,
        sizeBytes: Int,
    ): DoubleArray

    fun setTo(
        value: Byte,
        destIndex: Int,
        sizeBytes: Int,
    )

    fun setByte(
        index: Int,
        value: Byte,
    )

    fun getByte(index: Int): Byte
}

fun NativeBuffer.clear(): NativeBuffer {
    position = 0
    setTo(0, 0, limit)
    return this
}

fun NativeBuffer.flip(): NativeBuffer {
    position = 0
    return this
}

fun NativeBuffer.clone(): NativeBuffer = NativeBuffer(limit, memoryLayout, endian, memoryBoundary)

fun NativeBuffer.setUByte(
    index: Int,
    value: UByte,
) = setByte(index, value.toByte())

fun NativeBuffer.getUByte(index: Int): UByte = getByte(index).toUByte()

fun NativeBuffer.setBoolean(
    index: Int,
    value: Boolean,
) = setByte(index, if (value) 1 else 0)

fun NativeBuffer.getBoolean(index: Int): Boolean = getByte(index) == 1.toByte()

fun NativeBuffer.setShort(
    index: Int,
    value: Short,
) = packShort(index, value)

fun NativeBuffer.getShort(index: Int): Short = unpackShort(index)

fun NativeBuffer.setUShort(
    index: Int,
    value: UShort,
) = packUShort(index, value)

fun NativeBuffer.getUShort(index: Int): UShort = unpackUShort(index)

fun NativeBuffer.setChar(
    index: Int,
    value: Char,
) = packChar(index, value)

fun NativeBuffer.getChar(index: Int): Char = unpackChar(index)

fun NativeBuffer.setInt(
    index: Int,
    value: Int,
) = packInt(index, value)

fun NativeBuffer.getInt(index: Int): Int = unpackInt(index)

fun NativeBuffer.setUInt(
    index: Int,
    value: UInt,
) = packUInt(index, value)

fun NativeBuffer.getUInt(index: Int): UInt = unpackUInt(index)

fun NativeBuffer.setFloat(
    index: Int,
    value: Float,
) = packFloat(index, value)

fun NativeBuffer.getFloat(index: Int): Float = unpackFloat(index)

fun NativeBuffer.setLong(
    index: Int,
    value: Long,
) = packLong(index, value)

fun NativeBuffer.getLong(index: Int): Long = unpackLong(index)

fun NativeBuffer.setULong(
    index: Int,
    value: ULong,
) = packULong(index, value)

fun NativeBuffer.getULong(index: Int): ULong = unpackULong(index)

fun NativeBuffer.setDouble(
    index: Int,
    value: Double,
) = packDouble(index, value)

fun NativeBuffer.getDouble(index: Int): Double = unpackDouble(index)

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUByteArray(
    index: Int,
    array: UByteArray,
) {
    setByteArray(index, array.asByteArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUShortArray(
    index: Int,
    array: UShortArray,
) {
    setShortArray(index, array.asShortArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUIntArray(
    index: Int,
    array: UIntArray,
) {
    setIntArray(index, array.asIntArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setULongArray(
    index: Int,
    array: ULongArray,
) {
    setLongArray(index, array.asLongArray())
}

fun NativeBuffer.nextByte(): Byte {
    val value = getByte(position)
    position += Byte.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextUByte(): UByte {
    val value = getUByte(position)
    position += UByte.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextBoolean(): Boolean {
    val value = getBoolean(position)
    position += Boolean.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextShort(): Short {
    val value = getShort(position)
    position += Short.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextUShort(): UShort {
    val value = getUShort(position)
    position += UShort.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextChar(): Char {
    val value = getChar(position)
    position += Char.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextInt(): Int {
    val value = getInt(position)
    position += Int.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextUInt(): UInt {
    val value = getUInt(position)
    position += UInt.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextFloat(): Float {
    val value = getFloat(position)
    position += Float.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextLong(): Long {
    val value = getLong(position)
    position += Long.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextULong(): ULong {
    val value = getULong(position)
    position += ULong.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextDouble(): Double {
    val value = getDouble(position)
    position += Double.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextByteArray(): ByteArray = nextByteArray(nextInt())

fun NativeBuffer.nextByteArray(size: Int): ByteArray {
    if (size <= 0) return ByteArray(0)
    val value = copyToByteArray(ByteArray(size), position, size)
    position += size * Byte.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUByteArray(): UByteArray = nextUByteArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUByteArray(size: Int): UByteArray = nextByteArray(size).asUByteArray()

fun NativeBuffer.nextShortArray(): ShortArray = nextShortArray(nextInt())

fun NativeBuffer.nextShortArray(size: Int): ShortArray {
    if (size <= 0) return ShortArray(0)
    val value = copyToShortArray(ShortArray(size), position, size)
    position += size * Short.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUShortArray(): UShortArray = nextUShortArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUShortArray(size: Int): UShortArray = nextShortArray(size).asUShortArray()

fun NativeBuffer.nextCharArray(): CharArray = nextCharArray(nextInt())

fun NativeBuffer.nextCharArray(size: Int): CharArray {
    if (size <= 0) return CharArray(0)
    val value = copyToCharArray(CharArray(size), position, size)
    position += size * Char.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextIntArray(): IntArray = nextIntArray(nextInt())

fun NativeBuffer.nextIntArray(size: Int): IntArray {
    if (size <= 0) return IntArray(0)
    val value = copyToIntArray(IntArray(size), position, size)
    position += size * Int.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUIntArray(): UIntArray = nextUIntArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUIntArray(size: Int): UIntArray = nextIntArray(size).asUIntArray()

fun NativeBuffer.nextLongArray(): LongArray = nextLongArray(nextInt())

fun NativeBuffer.nextLongArray(size: Int): LongArray {
    if (size <= 0) return LongArray(0)
    val value = copyToLongArray(LongArray(size), position, size)
    position += size * Long.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextULongArray(): ULongArray = nextULongArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextULongArray(size: Int): ULongArray = nextLongArray(size).asULongArray()

fun NativeBuffer.nextFloatArray(): FloatArray = nextFloatArray(nextInt())

fun NativeBuffer.nextFloatArray(size: Int): FloatArray {
    if (size <= 0) return FloatArray(0)
    val value = copyToFloatArray(FloatArray(size), position, size)
    position += size * Float.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextDoubleArray(): DoubleArray = nextDoubleArray(nextInt())

fun NativeBuffer.nextDoubleArray(size: Int): DoubleArray {
    if (size <= 0) return DoubleArray(0)
    val value = copyToDoubleArray(DoubleArray(size), position, size)
    position += size * Double.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextStringUtf8(): String = nextByteArray().decodeToString()

fun NativeBuffer.nextStringUtf8(size: Int): String = nextByteArray(size).decodeToString().trimEnd('\u0000')

fun NativeBuffer.nextStringUtf16(): String = nextCharArray().concatToString()

fun NativeBuffer.nextStringUtf16(size: Int): String = nextCharArray(size).concatToString().trimEnd('\u0000')

inline fun <reified T> NativeBuffer.nextArray(decode: () -> T): Array<T> {
    val size = nextInt()
    return Array(size) { decode() }
}

inline fun <T> NativeBuffer.nextList(decode: () -> T): List<T> {
    val size = nextInt()
    return List(size) { decode() }
}

inline fun <T> NativeBuffer.nextSet(decode: () -> T): Set<T> {
    val size = nextInt()
    return LinkedHashSet<T>(size).apply {
        repeat(size) { add(decode()) }
    }
}

inline fun <K, V> NativeBuffer.nextMap(
    decodeKey: () -> K,
    decodeValue: () -> V,
): Map<K, V> {
    val size = nextInt()
    return LinkedHashMap<K, V>(size).apply {
        repeat(size) { put(decodeKey(), decodeValue()) }
    }
}

fun NativeBuffer.pushByte(value: Byte?) {
    assertPosition()
    setByte(position, value ?: 0)
    position += Byte.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushUByte(value: UByte?) {
    assertPosition()
    setUByte(position, value ?: 0u)
    position += UByte.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushBoolean(value: Boolean?) {
    assertPosition()
    setBoolean(position, value ?: false)
    position += Boolean.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushShort(value: Short?) {
    assertPosition()
    setShort(position, value ?: 0)
    position += Short.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushUShort(value: UShort?) {
    assertPosition()
    setUShort(position, value ?: 0u)
    position += UShort.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushChar(value: Char?) {
    assertPosition()
    setChar(position, value ?: 0.toChar())
    position += Char.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushInt(value: Int?) {
    assertPosition()
    setInt(position, value ?: 0)
    position += Int.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushUInt(value: UInt?) {
    assertPosition()
    setUInt(position, value ?: 0u)
    position += UInt.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushLong(value: Long?) {
    assertPosition()
    setLong(position, value ?: 0L)
    position += Long.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushULong(value: ULong?) {
    assertPosition()
    setULong(position, value ?: 0u)
    position += ULong.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFloat(value: Float?) {
    assertPosition()
    setFloat(position, value ?: 0f)
    position += Float.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushDouble(value: Double?) {
    assertPosition()
    setDouble(position, value ?: 0.0)
    position += Double.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushByteArray(value: ByteArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setByteArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedByteArray(
    value: ByteArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedByteArray value == null or value.size != fixedSize"
    }
    setByteArray(position, value)
    position += size * Byte.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedByteArray(
    value: ByteArray?,
) {
    require(value != null) {
        "pushPackedByteArray value == null"
    }
    setByteArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUByteArray(value: UByteArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setUByteArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUByteArray(
    value: UByteArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedUByteArray value == null or value.size != fixedSize"
    }
    setUByteArray(position, value)
    position += size * UByte.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushPackedUByteArray(
    value: UByteArray?,
) {
    require(value != null) {
        "pushPackedUByteArray value == null"
    }
    setUByteArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushShortArray(value: ShortArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setShortArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedShortArray(
    value: ShortArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedShortArray value == null or value.size != fixedSize"
    }
    setShortArray(position, value)
    position += size * Short.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedShortArray(
    value: ShortArray?,
) {
    require(value != null) {
        "pushPackedShortArray value == null"
    }
    setShortArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUShortArray(value: UShortArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setUShortArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUShortArray(
    value: UShortArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedUShortArray value == null or value.size != fixedSize"
    }
    setUShortArray(position, value)
    position += size * UShort.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushPackedUShortArray(
    value: UShortArray?,
) {
    require(value != null) {
        "pushPackedUShortArray value == null"
    }
    setUShortArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushCharArray(value: CharArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setCharArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedCharArray(
    value: CharArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedCharArray value == null or value.size != fixedSize"
    }
    setCharArray(position, value)
    position += size * Char.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedCharArray(
    value: CharArray?,
) {
    require(value != null) {
        "pushPackedCharArray value == null"
    }
    setCharArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushIntArray(value: IntArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setIntArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedIntArray(
    value: IntArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedIntArray value == null or value.size != fixedSize"
    }
    setIntArray(position, value)
    position += size * Int.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedIntArray(
    value: IntArray?,
) {
    require(value != null) {
        "pushPackedIntArray value == null"
    }
    setIntArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUIntArray(value: UIntArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setUIntArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUIntArray(
    value: UIntArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedUIntArray value == null or value.size != fixedSize"
    }
    setUIntArray(position, value)
    position += size * UInt.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushPackedUIntArray(
    value: UIntArray?,
) {
    require(value != null) {
        "pushPackedUIntArray value == null"
    }
    setUIntArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushLongArray(value: LongArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setLongArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedLongArray(
    value: LongArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedLongArray value == null or value.size != fixedSize"
    }
    setLongArray(position, value)
    position += size * Long.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedLongArray(
    value: LongArray?,
) {
    require(value != null) {
        "pushPackedLongArray value == null"
    }
    setLongArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushULongArray(value: ULongArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setULongArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedULongArray(
    value: ULongArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedULongArray value == null or value.size != fixedSize"
    }
    setULongArray(position, value)
    position += size * ULong.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushPackedULongArray(
    value: ULongArray?,
) {
    require(value != null) {
        "pushPackedULongArray value == null"
    }
    setULongArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFloatArray(value: FloatArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setFloatArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedFloatArray(
    value: FloatArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedFloatArray value == null or value.size != fixedSize"
    }
    setFloatArray(position, value)
    position += size * Float.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedFloatArray(
    value: FloatArray?,
) {
    require(value != null) {
        "pushPackedFloatArray value == null"
    }
    setFloatArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushDoubleArray(value: DoubleArray?) {
    if (value == null || value.isEmpty()) {
        pushInt(0)
    } else {
        pushInt(value.size)
        setDoubleArray(position, value)
        position += value.sizeBytes(memoryLayout)
    }
}

fun NativeBuffer.pushFixedDoubleArray(
    value: DoubleArray?,
    size: Int,
) {
    require(value != null && value.size == size) {
        "pushFixedDoubleArray value == null or value.size != fixedSize"
    }
    setDoubleArray(position, value)
    position += size * Double.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushPackedDoubleArray(
    value: DoubleArray?,
) {
    require(value != null) {
        "pushFixedDoubleArray value == null or value.size != fixedSize"
    }
    setDoubleArray(position, value)
    position += value.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushStringUtf8(value: String?) {
    pushByteArray(value?.encodeToByteArray())
}

fun NativeBuffer.pushPackedStringUtf8(value: String?) {
    pushPackedByteArray(value?.encodeToByteArray())
}

fun NativeBuffer.pushFixedStringUtf8(
    value: String?,
    size: Int,
) {
    val bytes =
        value
            .orEmpty()
            .take(size)
            .padEnd(size, '\u0000')
            .encodeToByteArray()
    pushFixedByteArray(bytes, size)
}

fun NativeBuffer.pushStringUtf16(value: String?) {
    pushCharArray(value?.toCharArray())
}

fun NativeBuffer.pushPackedStringUtf16(value: String?) {
    pushPackedCharArray(value?.toCharArray())
}

fun NativeBuffer.pushFixedStringUtf16(
    value: String?,
    size: Int,
) {
    val chars =
        value
            .orEmpty()
            .take(size)
            .padEnd(size, '\u0000')
            .toCharArray()
    pushFixedCharArray(chars, size)
}

inline fun <T> NativeBuffer.pushCollection(
    items: Collection<T>,
    encode: (T) -> Unit,
) {
    pushInt(items.size)
    items.forEach { encode(it) }
}

inline fun <T> NativeBuffer.pushPackedCollection(
    items: Collection<T>,
    encode: (T) -> Unit,
) {
    items.forEach { encode(it) }
}

inline fun <K, V> NativeBuffer.pushMap(
    map: Map<K, V>,
    encodeKey: (K) -> Unit,
    encodeValue: (V) -> Unit,
) {
    pushInt(map.size)
    map.forEach { (k, v) ->
        encodeKey(k)
        encodeValue(v)
    }
}

inline fun <K, V> NativeBuffer.pushPackedMap(
    map: Map<K, V>,
    encodeKey: (K) -> Unit,
    encodeValue: (V) -> Unit,
) {
    map.forEach { (k, v) ->
        encodeKey(k)
        encodeValue(v)
    }
}

internal fun NativeBuffer.packShort(
    index: Int,
    value: Short,
) {
    assertLimit(index)
    when (endian) {
        Endian.LITTLE -> {
            setByte(index, value.toByte())
            setByte(index + 1, (value.toInt() shr 8).toByte())
        }

        Endian.BIG -> {
            setByte(index, (value.toInt() shr 8).toByte())
            setByte(index + 1, value.toByte())
        }
    }
}

internal fun NativeBuffer.unpackShort(index: Int): Short {
    assertLimit(index)
    val b0 = getByte(index).toInt() and 0xFF
    val b1 = getByte(index + 1).toInt() and 0xFF
    return when (endian) {
        Endian.LITTLE -> (b0 or (b1 shl 8)).toShort()
        Endian.BIG -> ((b0 shl 8) or b1).toShort()
    }
}

internal fun NativeBuffer.packUShort(
    index: Int,
    value: UShort,
) = packShort(index, value.toShort())

internal fun NativeBuffer.unpackUShort(index: Int) = unpackShort(index).toUShort()

internal fun NativeBuffer.packChar(
    index: Int,
    value: Char,
) {
    assertLimit(index)
    when (endian) {
        Endian.LITTLE -> {
            setByte(index, value.code.toByte())
            setByte(index + 1, (value.code shr 8).toByte())
        }

        Endian.BIG -> {
            setByte(index, (value.code shr 8).toByte())
            setByte(index + 1, value.code.toByte())
        }
    }
}

internal fun NativeBuffer.unpackChar(index: Int): Char {
    assertLimit(index)
    val b0 = getByte(index).toInt() and 0xFF
    val b1 = getByte(index + 1).toInt() and 0xFF
    return when (endian) {
        Endian.LITTLE -> (b0 or (b1 shl 8)).toChar()
        Endian.BIG -> ((b0 shl 8) or b1).toChar()
    }
}

internal fun NativeBuffer.packInt(
    index: Int,
    value: Int,
) {
    assertLimit(index)
    when (endian) {
        Endian.LITTLE -> {
            setShort(index, value.toShort())
            setShort(index + 2, (value shr 16).toShort())
        }

        Endian.BIG -> {
            setShort(index, (value shr 16).toShort())
            setShort(index + 2, value.toShort())
        }
    }
}

internal fun NativeBuffer.unpackInt(index: Int): Int {
    assertLimit(index)
    val low = getShort(index).toInt() and 0xFFFF
    val high = getShort(index + 2).toInt() and 0xFFFF
    return when (endian) {
        Endian.LITTLE -> low or (high shl 16)
        Endian.BIG -> (low shl 16) or high
    }
}

internal fun NativeBuffer.packUInt(
    index: Int,
    value: UInt,
) = packInt(index, value.toInt())

internal fun NativeBuffer.unpackUInt(index: Int) = unpackInt(index).toUInt()

internal fun NativeBuffer.packLong(
    index: Int,
    value: Long,
) {
    assertLimit(index)
    when (endian) {
        Endian.LITTLE -> {
            setInt(index, value.toInt())
            setInt(index + 4, (value shr 32).toInt())
        }

        Endian.BIG -> {
            setInt(index, (value shr 32).toInt())
            setInt(index + 4, value.toInt())
        }
    }
}

internal fun NativeBuffer.unpackLong(index: Int): Long {
    assertLimit(index)
    val low = getInt(index).toLong() and 0xFFFFFFFF
    val high = getInt(index + 4).toLong() and 0xFFFFFFFF
    return when (endian) {
        Endian.LITTLE -> low or (high shl 32)
        Endian.BIG -> (low shl 32) or high
    }
}

internal fun NativeBuffer.packULong(
    index: Int,
    value: ULong,
) = packLong(index, value.toLong())

internal fun NativeBuffer.unpackULong(index: Int) = unpackLong(index).toULong()

internal fun NativeBuffer.packFloat(
    index: Int,
    value: Float,
) {
    assertLimit(index)
    val bits = value.toBits()
    when (endian) {
        Endian.LITTLE -> {
            setShort(index, bits.toShort())
            setShort(index + 2, (bits shr 16).toShort())
        }

        Endian.BIG -> {
            setShort(index, (bits shr 16).toShort())
            setShort(index + 2, bits.toShort())
        }
    }
}

internal fun NativeBuffer.unpackFloat(index: Int): Float {
    assertLimit(index)
    val low = getShort(index).toInt() and 0xFFFF
    val high = getShort(index + 2).toInt() and 0xFFFF
    val bits =
        when (endian) {
            Endian.LITTLE -> low or (high shl 16)
            Endian.BIG -> (low shl 16) or high
        }
    return Float.fromBits(bits)
}

internal fun NativeBuffer.packDouble(
    index: Int,
    value: Double,
) {
    assertLimit(index)
    val bits = value.toBits()
    packLong(index, bits)
}

internal fun NativeBuffer.unpackDouble(index: Int): Double {
    assertLimit(index)
    return Double.fromBits(unpackLong(index))
}

private fun NativeBuffer.assertPosition() {
    if (position > limit) {
        throw IndexOutOfBoundsException("NativeBuffer: Position is out of bounds! position=$position limit=$limit")
    }
}

internal fun NativeBuffer.assertLimit(i: Int) {
    if (i !in 0..<limit) {
        throw IndexOutOfBoundsException("NativeBuffer: Index is out of bounds! i=$i limit=$limit")
    }
}
