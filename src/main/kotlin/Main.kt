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

fun Collection<Double>.median(): Double {
    // https://gist.github.com/fikrirazzaq/c3818a1c34e2787aef63ed4da56045a6?permalink_comment_id=4090312#gistcomment-4090312
    val sorted = this.sorted()

    return if (sorted.size % 2 == 0) {
        ((sorted[sorted.size / 2] + sorted[sorted.size / 2 - 1]) / 2)
    } else {
        (sorted[sorted.size / 2])
    }
}

suspend fun runParallel(n: Int, block: () -> Unit) =
    coroutineScope {
        (1..n).map { launch(start = CoroutineStart.LAZY) { block() } }.joinAll()
    }


fun measure(name: String, mode: Mode, block: () -> Unit): Pair<String, Long> {
    print("$name... ")
    return measureTimeMillis {
        when (mode) {
            Mode.SMALL_COROUTINES ->
                runBlocking(Dispatchers.Default) {
                    // starting/stopping many small tasks in parallel
                    val repeat: Int = 10_000 / DIVISOR
                    for (i in 1..repeat) {
                        runParallel(32, block) // 32 threads
                    }
                }

            Mode.LARGE_COROUTINES ->
                runBlocking {
                    // running large tasks in parallel

                    val repeat = 250_000 / DIVISOR
                    fun runMeInThread() {
                        for (i in 1..repeat)
                            block()
                    }

                    runParallel(32, ::runMeInThread) // 32 threads
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

    for (trial in 1..5) {

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
            .map { it.key to it.value.map {pair -> pair.second.toDouble() }.median().roundToInt() }
            .sortedBy { it.second }
            .map { "${it.second} ms ${it.first}" }
    )

    val reportText = reportTextLines.joinToString("\n")
    println(reportText)
    return reportText
}

fun main(args: Array<String>) {

    //val modes = listOf(Mode.SINGLE_THREAD)
    val modes = Mode.values()

    modes.map(::threadsafeGaussianBenchmark).let {
        println("=".repeat(80))
        println("=".repeat(80))
        println()
        println(it.joinToString("\n\n"))
    }
}
