package com.cws.std.soa

import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.TimeSource

class DataLayoutBenchmarkTest {

    companion object {
        private const val ENTITY_COUNT = 100_000
        private const val ITERATIONS = 10
        private const val WARMUP = 5
        private const val RUNS = 10
    }

    // -------------------------------------------------------------------------
    // AoS (Nested Heap Objects)
    // -------------------------------------------------------------------------

    class Vec3(
        var x: Float,
        var y: Float,
        var z: Float,
    )

    class HeroNested(
        val position: Vec3,
        val velocity: Vec3,
        var health: Int,
    )

    // -------------------------------------------------------------------------
    // AoS (Flat Object)
    // -------------------------------------------------------------------------

    class HeroFlat(
        var px: Float,
        var py: Float,
        var pz: Float,

        var vx: Float,
        var vy: Float,
        var vz: Float,

        var health: Int,
    )

    // -------------------------------------------------------------------------
    // SoA
    // -------------------------------------------------------------------------

    class HeroSoA(size: Int) {

        val px = FloatArray(size)
        val py = FloatArray(size)
        val pz = FloatArray(size)

        val vx = FloatArray(size)
        val vy = FloatArray(size)
        val vz = FloatArray(size)

        val health = IntArray(size)

        val size = size
    }

    @Test
    fun benchmarkLayouts() {
        val random = Random(1234)

        val nested = Array(ENTITY_COUNT) {
            HeroNested(
                position = Vec3(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat()
                ),
                velocity = Vec3(
                    random.nextFloat(),
                    random.nextFloat(),
                    random.nextFloat()
                ),
                health = random.nextInt()
            )
        }

        val flat = Array(ENTITY_COUNT) {
            HeroFlat(
                px = random.nextFloat(),
                py = random.nextFloat(),
                pz = random.nextFloat(),

                vx = random.nextFloat(),
                vy = random.nextFloat(),
                vz = random.nextFloat(),

                health = random.nextInt()
            )
        }

        val soa = HeroSoA(ENTITY_COUNT)

        repeat(ENTITY_COUNT) { i ->
            soa.px[i] = random.nextFloat()
            soa.py[i] = random.nextFloat()
            soa.pz[i] = random.nextFloat()

            soa.vx[i] = random.nextFloat()
            soa.vy[i] = random.nextFloat()
            soa.vz[i] = random.nextFloat()

            soa.health[i] = random.nextInt()
        }

        benchmark("AoS Nested") {
            var checksum = 0f
            repeat(ITERATIONS) {
                for (hero in nested) {

                    hero.position.x += hero.velocity.x
                    hero.position.y += hero.velocity.y
                    hero.position.z += hero.velocity.z

                    hero.health++

                    checksum += hero.position.x
                }
            }
            checksum
        }

        benchmark("AoS Flat") {
            var checksum = 0f
            repeat(ITERATIONS) {
                for (hero in flat) {

                    hero.px += hero.vx
                    hero.py += hero.vy
                    hero.pz += hero.vz

                    hero.health++

                    checksum += hero.px
                }
            }
            checksum
        }

        benchmark("SoA") {
            var checksum = 0f
            repeat(ITERATIONS) {
                for (i in 0 until soa.size) {

                    soa.px[i] += soa.vx[i]
                    soa.py[i] += soa.vy[i]
                    soa.pz[i] += soa.vz[i]

                    soa.health[i]++

                    checksum += soa.px[i]
                }
            }
            checksum
        }
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