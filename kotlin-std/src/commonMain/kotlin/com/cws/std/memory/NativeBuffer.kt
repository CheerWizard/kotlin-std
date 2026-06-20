package com.cws.std.memory

enum class Endian {
    LITTLE,
    BIG,
}

expect open class NativeBuffer(
    capacity: Int,
    memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
    endian: Endian = Endian.LITTLE,
) {

    constructor(
        address: Long,
        capacity: Int,
        memoryLayout: MemoryLayout = MemoryLayout.KOTLIN,
        endian: Endian = Endian.LITTLE
    )

    var position: Int
    val capacity: Int
    val address: Long
    val memoryLayout: MemoryLayout
    val endian: Endian

    fun view(): Any

    fun release()

    fun resize(newCapacity: Int)

    fun setByteArray(index: Int, array: ByteArray)
    fun setCharArray(index: Int, array: CharArray)
    fun setShortArray(index: Int, array: ShortArray)
    fun setIntArray(index: Int, array: IntArray)
    fun setFloatArray(index: Int, array: FloatArray)
    fun setLongArray(index: Int, array: LongArray)
    fun setDoubleArray(index: Int, array: DoubleArray)

    fun clone(): NativeBuffer

    fun copyTo(
        dest: NativeBuffer,
        srcIndex: Int,
        destIndex: Int,
        size: Int,
    )

    fun copyToByteArray(
        array: ByteArray,
        offset: Int,
        size: Int,
    ): ByteArray

    fun copyToCharArray(
        array: CharArray,
        offset: Int,
        size: Int,
    ): CharArray

    fun copyToShortArray(
        array: ShortArray,
        offset: Int,
        size: Int,
    ): ShortArray

    fun copyToIntArray(
        array: IntArray,
        offset: Int,
        size: Int,
    ): IntArray

    fun copyToFloatArray(
        array: FloatArray,
        offset: Int,
        size: Int,
    ): FloatArray

    fun copyToLongArray(
        array: LongArray,
        offset: Int,
        size: Int,
    ): LongArray

    fun copyToDoubleArray(
        array: DoubleArray,
        offset: Int,
        size: Int,
    ): DoubleArray

    fun setTo(value: Byte, destIndex: Int, size: Int)
    fun setByte(index: Int, value: Byte)
    fun getByte(index: Int): Byte

}

fun NativeBuffer.clear() {
    position = 0
    setTo(0, 0, capacity)
}

fun NativeBuffer.flip() {
    position = 0
}

fun NativeBuffer.setBoolean(index: Int, value: Boolean) = setByte(index, if (value) 1 else 0)
fun NativeBuffer.getBoolean(index: Int): Boolean = getByte(index) == 1.toByte()

fun NativeBuffer.setShort(index: Int, value: Short) = packShort(index, value)
fun NativeBuffer.getShort(index: Int): Short = unpackShort(index)

fun NativeBuffer.setUShort(index: Int, value: UShort) = packUShort(index, value)
fun NativeBuffer.getUShort(index: Int): UShort = unpackUShort(index)

fun NativeBuffer.setChar(index: Int, value: Char) = packChar(index, value)
fun NativeBuffer.getChar(index: Int): Char = unpackChar(index)

fun NativeBuffer.setInt(index: Int, value: Int) = packInt(index, value)
fun NativeBuffer.getInt(index: Int): Int = unpackInt(index)

fun NativeBuffer.setUInt(index: Int, value: UInt) = packUInt(index, value)
fun NativeBuffer.getUInt(index: Int): UInt = unpackUInt(index)

fun NativeBuffer.setFloat(index: Int, value: Float) = packFloat(index, value)
fun NativeBuffer.getFloat(index: Int): Float = unpackFloat(index)

fun NativeBuffer.setLong(index: Int, value: Long) = packLong(index, value)
fun NativeBuffer.getLong(index: Int): Long = unpackLong(index)

fun NativeBuffer.setULong(index: Int, value: ULong) = packULong(index, value)
fun NativeBuffer.getULong(index: Int): ULong = unpackULong(index)

fun NativeBuffer.setDouble(index: Int, value: Double) = packDouble(index, value)

fun NativeBuffer.getDouble(index: Int): Double = unpackDouble(index)

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUByteArray(index: Int, array: UByteArray) {
    setByteArray(index, array.asByteArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUShortArray(index: Int, array: UShortArray) {
    setShortArray(index, array.asShortArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setUIntArray(index: Int, array: UIntArray) {
    setIntArray(index, array.asIntArray())
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.setULongArray(index: Int, array: ULongArray) {
    setLongArray(index, array.asLongArray())
}

fun NativeBuffer.nextByte(): Byte {
    val value = getByte(position)
    position += Byte.sizeBytes(memoryLayout)
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
    position += Short.sizeBytes(memoryLayout)
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
    position += Short.sizeBytes(memoryLayout)
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
    position += Short.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextDouble(): Double {
    val value = getDouble(position)
    position += Double.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextByteArray(): ByteArray = nextByteArray(nextInt())

fun NativeBuffer.nextByteArray(size: Int): ByteArray {
    val value = copyToByteArray(ByteArray(size), position, position + size * Byte.sizeBytes(memoryLayout))
    position += size * Byte.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUByteArray(): UByteArray = nextUByteArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUByteArray(size: Int): UByteArray {
    return nextByteArray(size).asUByteArray()
}

fun NativeBuffer.nextShortArray(): ShortArray = nextShortArray(nextInt())

fun NativeBuffer.nextShortArray(size: Int): ShortArray {
    val value = copyToShortArray(ShortArray(size), position, position + size * Short.sizeBytes(memoryLayout))
    position += size * Short.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUShortArray(): UShortArray = nextUShortArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUShortArray(size: Int): UShortArray {
    return nextShortArray(size).asUShortArray()
}

fun NativeBuffer.nextCharArray(): CharArray = nextCharArray(nextInt())

fun NativeBuffer.nextCharArray(size: Int): CharArray {
    val value = copyToCharArray(CharArray(size), position, position + size * Char.sizeBytes(memoryLayout))
    position += size * Char.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextIntArray(): IntArray = nextIntArray(nextInt())

fun NativeBuffer.nextIntArray(size: Int): IntArray {
    val value = copyToIntArray(IntArray(size), position, position + size * Int.sizeBytes(memoryLayout))
    position += size * Int.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUIntArray(): UIntArray = nextUIntArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextUIntArray(size: Int): UIntArray {
    return nextIntArray(size).asUIntArray()
}

fun NativeBuffer.nextLongArray(): LongArray = nextLongArray(nextInt())

fun NativeBuffer.nextLongArray(size: Int): LongArray {
    val value = copyToLongArray(LongArray(size), position, position + size * Long.sizeBytes(memoryLayout))
    position += size * Long.sizeBytes(memoryLayout)
    return value
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextULongArray(): ULongArray = nextULongArray(nextInt())

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.nextULongArray(size: Int): ULongArray {
    return nextLongArray(size).asULongArray()
}

fun NativeBuffer.nextFloatArray(): FloatArray = nextFloatArray(nextInt())

fun NativeBuffer.nextFloatArray(size: Int): FloatArray {
    val value = copyToFloatArray(FloatArray(size), position, position + size * Float.sizeBytes(memoryLayout))
    position += size * Float.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextDoubleArray(): DoubleArray = nextDoubleArray(nextInt())

fun NativeBuffer.nextDoubleArray(size: Int): DoubleArray {
    val value = copyToDoubleArray(DoubleArray(size), position, position + size * Double.sizeBytes(memoryLayout))
    position += size * Double.sizeBytes(memoryLayout)
    return value
}

fun NativeBuffer.nextStringUtf8(): String {
    return nextByteArray().decodeToString()
}

fun NativeBuffer.nextStringUtf8(size: Int): String {
    return nextByteArray(size).decodeToString().trimEnd('\u0000')
}

fun NativeBuffer.nextStringUtf16(): String {
    return nextCharArray().concatToString()
}

fun NativeBuffer.nextStringUtf16(size: Int): String {
    return nextCharArray(size).concatToString().trimEnd('\u0000')
}

fun <T> NativeBuffer.nextList(decode: () -> T): List<T> {
    val size = nextInt()
    return List(size) { decode() }
}

fun <T> NativeBuffer.nextSet(decode: () -> T): Set<T> {
    val size = nextInt()
    return LinkedHashSet<T>(size).apply {
        repeat(size) { add(decode()) }
    }
}

fun <K, V> NativeBuffer.nextMap(decodeKey: () -> K, decodeValue: () -> V): Map<K, V> {
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
    val value = value ?: ByteArray(0)
    pushInt(value.size)
    setByteArray(position, value)
    position += value.size
}

fun NativeBuffer.pushFixedByteArray(value: ByteArray?, size: Int) {
    val value = value ?: ByteArray(size)
    setByteArray(position, value)
    position += size
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUByteArray(value: UByteArray?) {
    val value = value ?: UByteArray(0)
    pushInt(value.size)
    setUByteArray(position, value)
    position += value.size
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUByteArray(value: UByteArray?, size: Int) {
    val value = value ?: UByteArray(size)
    setUByteArray(position, value)
    position += size
}

fun NativeBuffer.pushShortArray(value: ShortArray?) {
    val value = value ?: ShortArray(0)
    pushInt(value.size)
    setShortArray(position, value)
    position += value.size * Short.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedShortArray(value: ShortArray?, size: Int) {
    val value = value ?: ShortArray(size)
    setShortArray(position, value)
    position += size * Short.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUShortArray(value: UShortArray?) {
    val value = value ?: UShortArray(0)
    pushInt(value.size)
    setUShortArray(position, value)
    position += value.size * UShort.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUShortArray(value: UShortArray?, size: Int) {
    val value = value ?: UShortArray(size)
    setUShortArray(position, value)
    position += size * UShort.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushCharArray(value: CharArray?) {
    val value = value ?: CharArray(0)
    pushInt(value.size)
    setCharArray(position, value)
    position += value.size * Char.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedCharArray(value: CharArray?, size: Int) {
    val value = value ?: CharArray(size)
    setCharArray(position, value)
    position += size * Char.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushIntArray(value: IntArray?) {
    val value = value ?: IntArray(0)
    pushInt(value.size)
    setIntArray(position, value)
    position += value.size * Int.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedIntArray(value: IntArray?, size: Int) {
    val value = value ?: IntArray(size)
    setIntArray(position, value)
    position += size * Int.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushUIntArray(value: UIntArray?) {
    val value = value ?: UIntArray(0)
    pushInt(value.size)
    setUIntArray(position, value)
    position += value.size * UInt.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedUIntArray(value: UIntArray?, size: Int) {
    val value = value ?: UIntArray(size)
    setUIntArray(position, value)
    position += size * UInt.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushLongArray(value: LongArray?) {
    val value = value ?: LongArray(0)
    pushInt(value.size)
    setLongArray(position, value)
    position += value.size * Long.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedLongArray(value: LongArray?, size: Int) {
    val value = value ?: LongArray(size)
    setLongArray(position, value)
    position += size * Long.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushULongArray(value: ULongArray?) {
    val value = value ?: ULongArray(0)
    pushInt(value.size)
    setULongArray(position, value)
    position += value.size * ULong.sizeBytes(memoryLayout)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun NativeBuffer.pushFixedULongArray(value: ULongArray?, size: Int) {
    val value = value ?: ULongArray(size)
    setULongArray(position, value)
    position += size * ULong.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFloatArray(value: FloatArray?) {
    val value = value ?: FloatArray(0)
    pushInt(value.size)
    setFloatArray(position, value)
    position += value.size * Float.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedFloatArray(value: FloatArray?, size: Int) {
    val value = value ?: FloatArray(size)
    setFloatArray(position, value)
    position += size * Float.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushDoubleArray(value: DoubleArray?) {
    val value = value ?: DoubleArray(0)
    pushInt(value.size)
    setDoubleArray(position, value)
    position += value.size * Double.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushFixedDoubleArray(value: DoubleArray?, size: Int) {
    val value = value ?: DoubleArray(size)
    setDoubleArray(position, value)
    position += size * Double.sizeBytes(memoryLayout)
}

fun NativeBuffer.pushStringUtf8(value: String?) {
    pushByteArray(value.orEmpty().encodeToByteArray())
}

fun NativeBuffer.pushFixedStringUtf8(value: String?, size: Int) {
    val bytes = value.orEmpty()
        .take(size)
        .padEnd(size, '\u0000')
        .encodeToByteArray()
    pushFixedByteArray(bytes, size)
}

fun NativeBuffer.pushStringUtf16(value: String?) {
    pushCharArray(value.orEmpty().toCharArray())
}

fun NativeBuffer.pushFixedStringUtf16(value: String?, size: Int) {
    val chars = value.orEmpty()
        .take(size)
        .padEnd(size, '\u0000')
        .toCharArray()
    pushFixedCharArray(chars, size)
}

fun <T> NativeBuffer.pushCollection(items: Collection<T>, encode: (T) -> Unit) {
    pushInt(items.size)
    items.forEach { encode(it) }
}

fun <K, V> NativeBuffer.pushMap(map: Map<K, V>, encodeKey: (K) -> Unit, encodeValue: (V) -> Unit) {
    pushInt(map.size)
    map.forEach { (k, v) ->
        encodeKey(k)
        encodeValue(v)
    }
}

private fun NativeBuffer.packShort(index: Int, value: Short) {
    assertCapacity(index)
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

private fun NativeBuffer.unpackShort(index: Int): Short {
    assertCapacity(index)
    val b0 = getByte(index).toInt() and 0xFF
    val b1 = getByte(index + 1).toInt() and 0xFF
    return when (endian) {
        Endian.LITTLE -> (b0 or (b1 shl 8)).toShort()
        Endian.BIG -> ((b0 shl 8) or b1).toShort()
    }
}


private fun NativeBuffer.packUShort(index: Int, value: UShort) = packShort(index, value.toShort())

private fun NativeBuffer.unpackUShort(index: Int) = unpackShort(index).toUShort()

private fun NativeBuffer.packChar(index: Int, value: Char) {
    assertCapacity(index)
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

private fun NativeBuffer.unpackChar(index: Int): Char {
    assertCapacity(index)
    val b0 = getByte(index).toInt() and 0xFF
    val b1 = getByte(index + 1).toInt() and 0xFF
    return when (endian) {
        Endian.LITTLE -> (b0 or (b1 shl 8)).toChar()
        Endian.BIG -> ((b0 shl 8) or b1).toChar()
    }
}

private fun NativeBuffer.packInt(index: Int, value: Int) {
    assertCapacity(index)
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

private fun NativeBuffer.unpackInt(index: Int): Int {
    assertCapacity(index)
    val low = getShort(index).toInt() and 0xFFFF
    val high = getShort(index + 2).toInt() and 0xFFFF
    return when (endian) {
        Endian.LITTLE -> low or (high shl 16)
        Endian.BIG -> (low shl 16) or high
    }
}

private fun NativeBuffer.packUInt(index: Int, value: UInt) = packInt(index, value.toInt())

private fun NativeBuffer.unpackUInt(index: Int) = unpackInt(index).toUInt()

private fun NativeBuffer.packLong(index: Int, value: Long) {
    assertCapacity(index)
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

private fun NativeBuffer.unpackLong(index: Int): Long {
    assertCapacity(index)
    val low = getInt(index).toLong() and 0xFFFFFFFF
    val high = getInt(index + 4).toLong() and 0xFFFFFFFF
    return when (endian) {
        Endian.LITTLE -> low or (high shl 32)
        Endian.BIG -> (low shl 32) or high
    }
}

private fun NativeBuffer.packULong(index: Int, value: ULong) = packLong(index, value.toLong())

private fun NativeBuffer.unpackULong(index: Int) = unpackLong(index).toULong()


private fun NativeBuffer.packFloat(index: Int, value: Float) {
    assertCapacity(index)
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

private fun NativeBuffer.unpackFloat(index: Int): Float {
    assertCapacity(index)
    val low = getShort(index).toInt() and 0xFFFF
    val high = getShort(index + 2).toInt() and 0xFFFF
    val bits = when (endian) {
        Endian.LITTLE -> low or (high shl 16)
        Endian.BIG -> (low shl 16) or high
    }
    return Float.fromBits(bits)
}

private fun NativeBuffer.packDouble(index: Int, value: Double) {
    assertCapacity(index)
    val bits = value.toBits()
    packLong(index, bits)
}

private fun NativeBuffer.unpackDouble(index: Int): Double {
    assertCapacity(index)
    return Double.fromBits(unpackLong(index))
}

private fun NativeBuffer.assertPosition() {
    if (position > capacity) {
        throw IndexOutOfBoundsException("NativeBuffer: Position is out of bounds! position=$position capacity=${capacity}")
    }
}

private fun NativeBuffer.assertCapacity(i: Int) {
    if (i !in 0..<capacity) {
        throw IndexOutOfBoundsException("NativeBuffer: Index is out of bounds! i=$i capacity=${capacity}")
    }
}
