import kotlinx.coroutines.*
import org.apache.commons.math3.random.*
import org.apache.commons.rng.UniformRandomProvider
import org.apache.commons.rng.sampling.distribution.*
import org.apache.commons.rng.simple.*
import java.time.Instant

import kotlin.math.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

private fun localZiggurat(source: RandomSource): NormalizedGaussianSampler {
    return ZigguratSampler.NormalizedGaussian.of(ThreadLocalRandomSource.current(source))
}

enum class Mode {
    SINGLE_THREAD,
    LARGE_COROUTINES,
    SMALL_COROUTINES,
}

const val DIVISOR = 1 // normally 1, larger values to debug

fun measure(name: String, mode: Mode, block: () -> Unit): Pair<String, Long> {
    print("$name... ")
    return measureTimeMillis {
        when (mode) {
            Mode.SMALL_COROUTINES ->
                runBlocking {
                    // starting/stopping many small tasks in parallel
                    val repeat: Int = 100_000 / DIVISOR
                    for (i in 1..repeat) {
                        // using local variables to avoid creating
                        // collections in benchmark code

                        val a = launch { block() }
                        val b = launch { block() }
                        val c = launch { block() }
                        val d = launch { block() }
                        val e = launch { block() }
                        val f = launch { block() }

                        a.join()
                        b.join()
                        c.join()
                        d.join()
                        e.join()
                        f.join()
                    }
                }

            Mode.LARGE_COROUTINES ->
                runBlocking {
                    // running large tasks in parallel

                    val repeat = 1_000_000 / DIVISOR
                    fun runMe() {
                        for (i in 1..repeat)
                            block()
                    }

                    // using local variables to avoid creating
                    // collections in benchmark code

                    val a = launch { runMe() }
                    val b = launch { runMe() }
                    val c = launch { runMe() }
                    val d = launch { runMe() }
                    val e = launch { runMe() }
                    val f = launch { runMe() }

                    a.join()
                    b.join()
                    c.join()
                    d.join()
                    e.join()
                    f.join()
                }

            Mode.SINGLE_THREAD -> {
                val repeat = 10_000_000 / DIVISOR
                for (i in 1..repeat) {
                    block()
                }
            }
        }
    }.let {
        println("$it ms")
        Pair(name, it)
    }
}

private fun Random.boxMullerGaussian(): Double {
    // made from http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html
    // use the polar form of the Box-Muller transform
    //
    // (not tested, ported for benchmark only)

    var r: Double
    var x: Double
    var y: Double
    do {
        x = this.nextDouble(-1.0, 1.0)
        y = this.nextDouble(-1.0, 1.0)
        r = x * x + y * y
    } while (r >= 1 || r == 0.0)
    return x * sqrt(-2 * ln(r) / r)

    // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
    // is an independent random gaussian
}

private fun UniformRandomProvider.boxMullerGaussian(): Double {
    // made from http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html
    // use the polar form of the Box-Muller transform
    //
    // (not tested, ported for benchmark only)

    var r: Double
    var x: Double
    var y: Double
    do {
        x = this.nextDouble() * 2.0 - 1.0
        y = this.nextDouble() * 2.0 - 1.0
        r = x * x + y * y
    } while (r >= 1 || r == 0.0)
    return x * sqrt(-2 * ln(r) / r)

    // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
    // is an independent random gaussian
}

internal fun threadsafeGaussianBenchmark(mode: Mode): String {

    val results = mutableListOf<Pair<String, Long>>()
    fun Pair<String, Long>.addToResults() = results.add(this)

    for (trial in 1..7) {

        println()
        println("-".repeat(80))
        println("$mode / $trial")
        println("-".repeat(80))
        println()

        ////////////////////////////////////////////////////////////////////////////////////////////

        measure("[A1] java.util.Random: create for each call", mode) {
            java.util.Random().nextGaussian()
        }.addToResults()

        ////////////////////////////////////////////////////////////////////////////////////////////

        val javaUtilRandom = java.util.Random()
        measure("[A2] java.util.Random: synchronized reuse", mode) {
            synchronized(javaUtilRandom) {
                javaUtilRandom.nextGaussian()
            }
        }.addToResults()

        ////////////////////////////////////////////////////////////////////////////////////////////

        measure("[B] ThreadLocalRandom.current().nextGaussian()", mode) {
            java.util.concurrent.ThreadLocalRandom.current().nextGaussian()
        }.addToResults()

        ////////////////////////////////////////////////////////////////////////////////////////////

        measure("[C] kotlin.random.Random.boxMullerGaussian()", mode) {
            kotlin.random.Random.boxMullerGaussian()
        }.addToResults()

        ////////////////////////////////////////////////////////////////////////////////////////////

        for (src in RandomSource.values()) {
            try {
                measure("[D1] ZigguratSampler ThreadLocalRandomSource $src", mode) {
                    localZiggurat(src).sample()
                }.addToResults()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        for (src in RandomSource.values()) {
            try {
                measure("[D2] boxMullerGaussian() ThreadLocalRandomSource $src", mode) {
                    ThreadLocalRandomSource.current(src).boxMullerGaussian()
                }.addToResults()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        val syncedWell = SynchronizedRandomGenerator(Well19937c())
        measure("[E] SynchronizedRandomGenerator Well19937c", mode) {
            syncedWell.nextGaussian()
        }.addToResults()

        ////////////////////////////////////////////////////////////////////////////////////////////

        val syncedTwister = SynchronizedRandomGenerator(MersenneTwister())
        measure("[E] SynchronizedRandomGenerator MersenneTwister", mode) {
            syncedTwister.nextGaussian()
        }.addToResults()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    val reportTextLines = mutableListOf<String>()

    reportTextLines.addAll(listOf(
        "=".repeat(80),
        "RESULTS $mode @ ${Instant.now()}",
        "=".repeat(80),
        "JVM ${System.getProperty("java.version")} Kotlin ${KotlinVersion.CURRENT}",
        ""))

    reportTextLines.addAll(
        results.groupBy { it.first }
            .map { it.key to it.value.sumOf { it.second } }
            .sortedBy { it.second }
            .map { "${it.second} ms ${it.first}" }
    )

    val reportText = reportTextLines.joinToString("\n")
    println(reportText)
    return reportText
}

fun main(args: Array<String>) {
    Mode.values().map(::threadsafeGaussianBenchmark).let {
        println("=".repeat(80))
        println("=".repeat(80))
        println()
        println(it.joinToString("\n\n"))
    }
}
