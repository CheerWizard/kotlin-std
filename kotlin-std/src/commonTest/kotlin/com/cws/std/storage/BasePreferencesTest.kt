package com.cws.std.storage

import kotlin.random.Random
import kotlin.random.nextUInt
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class BasePreferencesTest {

    protected abstract val preferences: Preferences
    private val usedKeys = mutableListOf<String>()
    protected val name = "test-${Random.nextUInt().toString().removeSuffix("u")}"

    @BeforeTest
    fun setUp() {
        usedKeys.clear()
    }

    @AfterTest
    fun tearDown() {
        // Explicit cleanup since there's no clear()/keys() on this interface —
        // each test tracks what it touched and removes it, so runs don't
        // leak state into a real registry key / plist / file between runs.
        usedKeys.forEach { preferences.remove(it) }
    }

    private fun key(name: String): String {
        usedKeys += name
        return name
    }

    // --- round-trip: each type gives back exactly what was set ---

    @Test
    fun byte_roundTrip() {
        val k = key("byteKey")
        preferences.setByte(k, 42)
        assertEquals(42, preferences.getByte(k))
    }

    @Test
    fun boolean_roundTrip() {
        val k = key("boolKey")
        preferences.setBoolean(k, true)
        assertEquals(true, preferences.getBoolean(k))
    }

    @Test
    fun short_roundTrip() {
        val k = key("shortKey")
        preferences.setShort(k, 1234)
        assertEquals(1234.toShort(), preferences.getShort(k))
    }

    @Test
    fun int_roundTrip() {
        val k = key("intKey")
        preferences.setInt(k, 123456)
        assertEquals(123456, preferences.getInt(k))
    }

    @Test
    fun long_roundTrip() {
        val k = key("longKey")
        preferences.setLong(k, 9_000_000_000L)
        assertEquals(9_000_000_000L, preferences.getLong(k))
    }

    @Test
    fun float_roundTrip() {
        val k = key("floatKey")
        preferences.setFloat(k, 3.14f)
        assertEquals(3.14f, preferences.getFloat(k))
    }

    @Test
    fun double_roundTrip() {
        val k = key("doubleKey")
        preferences.setDouble(k, 3.14159265)
        assertEquals(3.14159265, preferences.getDouble(k))
    }

    @Test
    fun string_roundTrip() {
        val k = key("stringKey")
        preferences.setString(k, "hello world")
        assertEquals("hello world", preferences.getString(k, "default"))
    }

    // --- boundary values, since some backing stores (registry, plist) can be finicky here ---

    @Test
    fun byte_boundaries() {
        val k = key("byteBoundary")
        preferences.setByte(k, Byte.MIN_VALUE)
        assertEquals(Byte.MIN_VALUE, preferences.getByte(k))
        preferences.setByte(k, Byte.MAX_VALUE)
        assertEquals(Byte.MAX_VALUE, preferences.getByte(k))
    }

    @Test
    fun long_boundaries() {
        val k = key("longBoundary")
        preferences.setLong(k, Long.MIN_VALUE)
        assertEquals(Long.MIN_VALUE, preferences.getLong(k))
        preferences.setLong(k, Long.MAX_VALUE)
        assertEquals(Long.MAX_VALUE, preferences.getLong(k))
    }

    @Test
    fun string_empty() {
        val k = key("emptyString")
        preferences.setString(k, "")
        assertEquals("", preferences.getString(k, "default"))
    }

    // --- default values when key is absent ---

    @Test
    fun missingKey_returnsProvidedDefault_int() {
        assertEquals(-1, preferences.getInt("neverSet", -1))
    }

    @Test
    fun missingKey_returnsProvidedDefault_string() {
        assertEquals("fallback", preferences.getString("neverSet", "fallback"))
    }

    @Test
    fun missingKey_returnsBuiltInDefault_whenNoneProvided() {
        assertEquals(0, preferences.getInt("neverSet"))
        assertEquals(false, preferences.getBoolean("neverSet"))
        assertEquals(0L, preferences.getLong("neverSet"))
        assertEquals(0.0f, preferences.getFloat("neverSet"))
        assertEquals(0.0, preferences.getDouble("neverSet"))
    }

    // --- remove() ---

    @Test
    fun remove_clearsKey_backToDefault() {
        val k = key("removable")
        preferences.setInt(k, 99)
        assertEquals(99, preferences.getInt(k))

        preferences.remove(k)
        assertEquals(0, preferences.getInt(k))
    }

    @Test
    fun remove_onMissingKey_doesNotThrow() {
        preferences.remove("neverExisted") // should be a safe no-op
    }

    // --- overwrite behavior ---

    @Test
    fun set_overwritesPreviousValue() {
        val k = key("overwrite")
        preferences.setInt(k, 1)
        preferences.setInt(k, 2)
        assertEquals(2, preferences.getInt(k))
    }

    @Test
    fun differentTypes_sameKeyName_dontCollide() {
        // Some backing stores keep a single value-slot per key regardless of type;
        // if this fails on a given platform it's worth knowing about explicitly
        // rather than assuming type-per-key isolation.
        val k = key("sharedName")
        preferences.setInt(k, 5)
        preferences.setString(k, "five")
        assertEquals("five", preferences.getString(k, "default"))
    }

    // --- key independence ---

    @Test
    fun separateKeys_doNotAffectEachOther() {
        val k1 = key("independentA")
        val k2 = key("independentB")
        preferences.setInt(k1, 1)
        preferences.setInt(k2, 2)
        assertEquals(1, preferences.getInt(k1))
        assertEquals(2, preferences.getInt(k2))
    }

    // --- commit/sync should be safe to call, regardless of whether the
    // platform needs them (e.g. Registry/NSUserDefaults may no-op these) ---

    @Test
    fun commit_doesNotThrow() {
        preferences.setInt(key("commitTest"), 1)
        preferences.commit()
    }

    @Test
    fun sync_doesNotThrow() {
        preferences.setInt(key("syncTest"), 1)
        preferences.sync()
    }

    @Test
    fun valuePersists_afterCommitAndSync() {
        val k = key("persistTest")
        preferences.setInt(k, 77)
        preferences.commit()
        preferences.sync()
        assertEquals(77, preferences.getInt(k))
    }
}