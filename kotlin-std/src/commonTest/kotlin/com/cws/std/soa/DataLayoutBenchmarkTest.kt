package com.cws.std.soa

import com.cws.std.lists.FloatList
import com.cws.std.test.Hero
import com.cws.std.test.HeroClass
import com.cws.std.test.HeroList
import com.cws.std.test.Stats
import com.cws.std.test.Transform
import kotlin.random.Random
import kotlin.repeat
import kotlin.test.Test
import kotlin.time.TimeSource

class DataLayoutBenchmarkTest {

    companion object {
        private const val ENTITY_COUNT = 100_000
        private const val ITERATIONS = 10
        private const val WARMUP = 5
        private const val RUNS = 10
        private const val ROUNDS = 30
    }

    // -------------------------------------------------------------------------
    // Flat AoS
    // -------------------------------------------------------------------------

    class HeroFlat(
        val id: Int,
        var experience: Long,
        val speed: Float,
        val weight: Double,
        val level: Short,
        val prestige: Byte,
        val enabled: Boolean,
        val symbol: Char,

        val name: String,
        val tag: String,

        val heroClass: HeroClass,

        // Flattened Transform
        var x: Float,
        var y: Float,
        var z: Float,
        val rotation: Float,
        val scale: Float,

        // Flattened Stats
        var health: Int,
        val mana: Float,
        val stamina: Double,
        val statsLevel: Short,
        val alive: Boolean,

        val nickname: String?,
    )

    @Test
    fun benchmarkLayouts() {
        val random = Random(1234)

        // ---------------------------------------------------------------------
        // AoS Nested
        // ---------------------------------------------------------------------

        val nested = Array(ENTITY_COUNT) {
            Hero(
                id = it,
                experience = random.nextLong(),
                speed = random.nextFloat(),
                weight = random.nextDouble(),
                level = random.nextInt(100).toShort(),
                prestige = random.nextInt(10).toByte(),
                enabled = random.nextBoolean(),
                symbol = ('A'.code + random.nextInt(26)).toChar(),

                name = "Hero$it",
                tag = "TAG$it",

                heroClass = HeroClass.entries[
                    random.nextInt(HeroClass.entries.size)
                ],

                transform = Transform(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                ),

                stats = Stats(
                    health = random.nextInt(),
                    mana = random.nextFloat(),
                    stamina = random.nextDouble(),
                    level = random.nextInt().toShort(),
                    alive = true,
                ),

                nickname = if (it % 4 == 0) {
                    "Nick$it"
                } else {
                    null
                },
            )
        }

        // ---------------------------------------------------------------------
        // AoS Flat
        // ---------------------------------------------------------------------

        val flat = Array(ENTITY_COUNT) {
            HeroFlat(
                id = it,
                experience = random.nextLong(),
                speed = random.nextFloat(),
                weight = random.nextDouble(),
                level = random.nextInt(100).toShort(),
                prestige = random.nextInt(10).toByte(),
                enabled = random.nextBoolean(),
                symbol = ('A'.code + random.nextInt(26)).toChar(),

                name = "Hero$it",
                tag = "TAG$it",

                heroClass = HeroClass.entries[
                    random.nextInt(HeroClass.entries.size)
                ],

                x = random.nextFloat(),
                y = random.nextFloat(),
                z = random.nextFloat(),
                rotation = random.nextFloat(),
                scale = random.nextFloat(),

                health = random.nextInt(),
                mana = random.nextFloat(),
                stamina = random.nextDouble(),
                statsLevel = random.nextInt().toShort(),
                alive = true,

                nickname = if (it % 4 == 0) {
                    "Nick$it"
                } else {
                    null
                },
            )
        }

        // ---------------------------------------------------------------------
        // NativeList SoA
        // ---------------------------------------------------------------------

        val native = HeroList(ENTITY_COUNT)

        repeat(ENTITY_COUNT) { i ->
            native.id.add(i)
            native.experience.add(random.nextLong())
            native.speed.add(random.nextFloat())
            native.weight.add(random.nextDouble())
            native.level.add(random.nextInt(100).toShort())
            native.prestige.add(random.nextInt(10).toByte())
            native.enabled.add(random.nextBoolean())
            native.symbol.add(('A'.code + random.nextInt(26)).toChar())

            native.name.add("Hero$i")
            native.tag.add("TAG$i")

            native.heroClass.add(
                HeroClass.entries[
                    random.nextInt(HeroClass.entries.size)
                ]
            )

            native.transform.x.add(random.nextFloat())
            native.transform.y.add(random.nextFloat())
            native.transform.z.add(random.nextFloat())
            native.transform.rotation.add(random.nextFloat())
            native.transform.scale.add(random.nextFloat())

            native.stats.health.add(random.nextInt())
            native.stats.mana.add(random.nextFloat())
            native.stats.stamina.add(random.nextDouble())
            native.stats.level.add(random.nextInt().toShort())
            native.stats.alive.add(true)

            native.nickname.add(
                if (i % 4 == 0) {
                    "Nick$i"
                } else {
                    null
                }
            )
        }

        // Warm everything up before collecting rounds.
        repeat(WARMUP) {
            runNested(nested)
            runFlat(flat)
            runNative(native)
        }

        val nestedTimes = DoubleArray(ROUNDS)
        val flatTimes = DoubleArray(ROUNDS)
        val nativeTimes = DoubleArray(ROUNDS)

        var checksum = 0f

        repeat(ROUNDS) { round ->

            // Rotate order to reduce benchmark ordering effects.
            when (round % 3) {

                0 -> {
                    nestedTimes[round] =
                        measure {
                            checksum += runNested(nested)
                        }

                    flatTimes[round] =
                        measure {
                            checksum += runFlat(flat)
                        }

                    nativeTimes[round] =
                        measure {
                            checksum += runNative(native)
                        }
                }

                1 -> {
                    flatTimes[round] =
                        measure {
                            checksum += runFlat(flat)
                        }

                    nativeTimes[round] =
                        measure {
                            checksum += runNative(native)
                        }

                    nestedTimes[round] =
                        measure {
                            checksum += runNested(nested)
                        }
                }

                else -> {
                    nativeTimes[round] =
                        measure {
                            checksum += runNative(native)
                        }

                    nestedTimes[round] =
                        measure {
                            checksum += runNested(nested)
                        }

                    flatTimes[round] =
                        measure {
                            checksum += runFlat(flat)
                        }
                }
            }
        }

        repeat(ROUNDS) {
            println(
                "Round $it: " +
                        "Nested=${nestedTimes[it]} ms, " +
                        "Flat=${flatTimes[it]} ms, " +
                        "SoA=${nativeTimes[it]} ms"
            )
        }

        println("checksum=$checksum")
    }

    // -------------------------------------------------------------------------
    // Nested AoS
    // -------------------------------------------------------------------------

    private fun runNested(
        heroes: Array<Hero>,
    ): Float {
        var checksum = 0f

        repeat(ITERATIONS) {
            for (hero in heroes) {
                hero.transform.x += hero.speed
                hero.transform.y += hero.speed
                hero.transform.z += hero.speed

                hero.stats.health++

                hero.experience++

                if (hero.enabled) {
                    checksum += hero.transform.x
                }

                checksum += hero.speed
                checksum += hero.weight.toFloat()
                checksum += hero.level
                checksum += hero.prestige
            }
        }

        return checksum
    }

    // -------------------------------------------------------------------------
    // Flat AoS
    // -------------------------------------------------------------------------

    private fun runFlat(
        heroes: Array<HeroFlat>,
    ): Float {
        var checksum = 0f

        repeat(ITERATIONS) {
            for (hero in heroes) {
                hero.x += hero.speed
                hero.y += hero.speed
                hero.z += hero.speed

                hero.health++

                hero.experience++

                if (hero.enabled) {
                    checksum += hero.x
                }

                checksum += hero.speed
                checksum += hero.weight.toFloat()
                checksum += hero.level
                checksum += hero.prestige
            }
        }

        return checksum
    }

    // -------------------------------------------------------------------------
    // NativeList SoA
    // -------------------------------------------------------------------------

    private fun runNative(
        native: HeroList,
    ): Float {
        var checksum = 0f

        repeat(ITERATIONS) {
            for (i in 0 until ENTITY_COUNT) {
                native.transform.x[i] += native.speed[i]
                native.transform.y[i] += native.speed[i]
                native.transform.z[i] += native.speed[i]

                native.stats.health[i]++

                native.experience[i]++

                if (native.enabled[i]) {
                    checksum += native.transform.x[i]
                }

                checksum += native.speed[i]
                checksum += native.weight[i].toFloat()
                checksum += native.level[i]
                checksum += native.prestige[i]
            }
        }

        return checksum
    }

    private inline fun measure(
        block: () -> Unit,
    ): Double {
        val mark = TimeSource.Monotonic.markNow()

        block()

        return mark.elapsedNow().inWholeNanoseconds / 1_000_000.0
    }
    @Test
    fun benchmarkFloatLayout() {
        val random = Random(1234)
        val floats = FloatArray(ENTITY_COUNT)
        val list = FloatList(ENTITY_COUNT)

        repeat(ENTITY_COUNT) {
            floats[it] = random.nextFloat()
            list.add(random.nextFloat())
        }

        benchmark("FloatList") {
            var checksum = 0f

            repeat(ITERATIONS) {
                for (i in 0 until ENTITY_COUNT) {
                    list[i] += 1f
                    checksum += list[i]
                }
            }

            checksum
        }

        benchmark("FloatArray") {
            var checksum = 0f

            repeat(ITERATIONS) {
                for (i in 0 until ENTITY_COUNT) {
                    floats[i] += 1f
                    checksum += floats[i]
                }
            }

            checksum
        }
    }

    @Test
    fun benchmarkLayoutStability() {
        val random = Random(1234)

        val nested = Array(ENTITY_COUNT) {
            Hero(
                id = it,
                experience = random.nextLong(),
                speed = random.nextFloat(),
                weight = random.nextDouble(),
                level = random.nextInt(100).toShort(),
                prestige = random.nextInt(10).toByte(),
                enabled = random.nextBoolean(),
                symbol = ('A'.code + random.nextInt(26)).toChar(),
                name = "Hero$it",
                tag = "TAG$it",
                heroClass = HeroClass.entries[
                    random.nextInt(HeroClass.entries.size)
                ],
                transform = Transform(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat(),
                ),
                stats = Stats(
                    health = random.nextInt(),
                    mana = random.nextFloat(),
                    stamina = random.nextDouble(),
                    level = random.nextInt().toShort(),
                    alive = true,
                ),
                nickname = if (it % 4 == 0) "Nick$it" else null,
            )
        }

        val native = HeroList(ENTITY_COUNT)

        // populate native...

        // Warmup both independently first
        repeat(20) {
            runAoS(nested)
            runSoA(native)
        }

        val nestedTimes = DoubleArray(ROUNDS)
        val nativeTimes = DoubleArray(ROUNDS)

        var checksum = 0f

        repeat(ROUNDS) { round ->
            // Alternate order to reduce ordering/JIT effects.
            if (round % 2 == 0) {
                val nestedStart = TimeSource.Monotonic.markNow()
                checksum += runAoS(nested)
                nestedTimes[round] =
                    nestedStart.elapsedNow().inWholeNanoseconds / 1_000_000.0

                val nativeStart = TimeSource.Monotonic.markNow()
                checksum += runSoA(native)
                nativeTimes[round] =
                    nativeStart.elapsedNow().inWholeNanoseconds / 1_000_000.0
            } else {
                val nativeStart = TimeSource.Monotonic.markNow()
                checksum += runSoA(native)
                nativeTimes[round] =
                    nativeStart.elapsedNow().inWholeNanoseconds / 1_000_000.0

                val nestedStart = TimeSource.Monotonic.markNow()
                checksum += runAoS(nested)
                nestedTimes[round] =
                    nestedStart.elapsedNow().inWholeNanoseconds / 1_000_000.0
            }
        }

        println("Round | AoS | SoA")

        repeat(ROUNDS) {
            val aos = nestedTimes[it]
            val soa = nativeTimes[it]

            println(
                "Round $it: AoS=$aos ms, SoA=$soa ms"
            )
        }

        println("checksum=$checksum")
    }

    private fun runAoS(nested: Array<Hero>): Float {
        var checksum = 0f

        repeat(ITERATIONS) {
            for (hero in nested) {
                hero.transform.x += hero.speed
                hero.transform.y += hero.speed
                hero.transform.z += hero.speed

                hero.stats.health++
                hero.experience++

                if (hero.enabled) {
                    checksum += hero.transform.x
                }

                checksum += hero.speed
                checksum += hero.weight.toFloat()
                checksum += hero.level
                checksum += hero.prestige
            }
        }

        return checksum
    }

    private fun runSoA(native: HeroList): Float {
        var checksum = 0f

        repeat(ITERATIONS) {
            for (i in 0 until ENTITY_COUNT) {
                native.transform.x[i] += native.speed[i]
                native.transform.y[i] += native.speed[i]
                native.transform.z[i] += native.speed[i]

                native.stats.health[i]++
                native.experience[i]++

                if (native.enabled[i]) {
                    checksum += native.transform.x[i]
                }

                checksum += native.speed[i]
                checksum += native.weight[i].toFloat()
                checksum += native.level[i]
                checksum += native.prestige[i]
            }
        }

        return checksum
    }

    private inline fun benchmark(
        name: String,
        block: () -> Float,
    ) {
        repeat(WARMUP) {
            block()
        }

        var checksum = 0f

        val mark = TimeSource.Monotonic.markNow()

        repeat(RUNS) {
            checksum += block()
        }

        val elapsed = mark.elapsedNow().inWholeNanoseconds

        println(
            "$name: ${elapsed / RUNS / 1_000_000.0} ms, checksum=$checksum"
        )
    }
}