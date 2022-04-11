import kotlinx.coroutines.*
import org.apache.commons.math3.random.*
import org.apache.commons.rng.UniformRandomProvider
import org.apache.commons.rng.sampling.distribution.*
import org.apache.commons.rng.simple.*
import java.time.Instant

import java.util.concurrent.ThreadLocalRandom
import kotlin.math.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

private fun threadLocalZigguratSampler(
    source: RandomSource = RandomSource.WELL_19937_C,
): NormalizedGaussianSampler {
    return ZigguratSampler.NormalizedGaussian.of(ThreadLocalRandomSource.current(source))
}

enum class Mode {
    SIX_SMALL_COROUTINES,
    SIX_LARGE_COROUTINES,
    SINGLE_THREAD
}

//val mode = Mode.SINGLE_THREAD

fun measure(name: String, mode: Mode, block: () -> Unit): Pair<String, Long> {
    print("$name... ")
    val elapsed = measureTimeMillis {
        when (mode) {
            Mode.SIX_SMALL_COROUTINES ->
                runBlocking {
                    val repeat =  100_000
                    for (i in 1..repeat) {
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

            Mode.SIX_LARGE_COROUTINES ->
                runBlocking {
                    val repeat = 1_000_000
                    fun runme() {
                        for (i in 1..repeat)
                            block()
                    }

                    val a = launch { runme() }
                    val b = launch { runme() }
                    val c = launch { runme() }
                    val d = launch { runme() }
                    val e = launch { runme() }
                    val f = launch { runme() }

                    a.join()
                    b.join()
                    c.join()
                    d.join()
                    e.join()
                    f.join()
                }

            Mode.SINGLE_THREAD -> {
                val repeat = 10_000_000
                for (i in 1..repeat) {
                    block()
                }
            }
        }
    }
    println("$elapsed ms")
    return Pair(name, elapsed)
}

fun Random.boxMullerGaussian(): Double {
    // made from
    // http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html
    // use the polar form of the Box-Muller transform
    //
    // (not tested, made for benchmark only)

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

fun UniformRandomProvider.boxMullerGaussian(): Double {
    // made from
    // http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html
    // use the polar form of the Box-Muller transform
    //
    // (not tested, made for benchmark only)

    var r: Double
    var x: Double
    var y: Double
    do {
        x = this.nextDouble()*2.0-1.0
        y = this.nextDouble()*2.0-1.0
        r = x * x + y * y
    } while (r >= 1 || r == 0.0)
    return x * sqrt(-2 * ln(r) / r)

    // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
    // is an independent random gaussian
}

fun mtGaussianBench(mode: Mode): String {

    val results = mutableListOf<Pair<String, Long>>()

    fun Pair<String, Long>.rmbr() = results.add(this)

    for (trial in 1..7) {

        println()
        println("-".repeat(80))
        println("$mode / $trial")
        println("-".repeat(80))
        println()

        measure("[A1] java.util.Random: create for each call", mode) {
            java.util.Random().nextGaussian()
        }.rmbr()

        val javaUtilRandom = java.util.Random()

        measure("[A2] java.util.Random: synchronized reuse", mode) {
            synchronized(javaUtilRandom) {
                javaUtilRandom.nextGaussian()
            }
        }.rmbr()

        measure("[B] ThreadLocalRandom.current().nextGaussian()", mode) {
            java.util.concurrent.ThreadLocalRandom.current().nextGaussian()
        }.rmbr()


        measure("[C] kotlin.random.Random.boxMullerGaussian()", mode) {
            kotlin.random.Random.boxMullerGaussian()
        }.rmbr()



        for (src in RandomSource.values()) {
            try {
                measure("[D1] ZigguratSampler ThreadLocalRandomSource $src", mode) {
                    threadLocalZigguratSampler(src).sample()
                }.rmbr()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }

        for (src in RandomSource.values()) {
            try {
                measure("[D2] boxMullerGaussian() ThreadLocalRandomSource $src", mode) {
                    ThreadLocalRandomSource.current(src).boxMullerGaussian()
                }.rmbr()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }


        val syncWell = SynchronizedRandomGenerator(Well19937c())

        measure("[E] SynchronizedRandomGenerator Well19937c", mode) {
            syncWell.nextGaussian()
        }.rmbr()

        val syncMt = SynchronizedRandomGenerator(MersenneTwister())

        measure("[E] SynchronizedRandomGenerator MersenneTwister", mode) {
            syncMt.nextGaussian()
        }.rmbr()

    }

    val result = mutableListOf<String>()

    result.add("=".repeat(80))
    result.add("RESULTS $mode @ ${Instant.now()}")
    result.add("=".repeat(80))
    result.add("JVM ${System.getProperty("java.version")} Kotlin ${KotlinVersion.CURRENT}")
    result.add("")

    result.addAll(results
                .groupBy { it.first }
                .map { it.key to it.value.sumOf { it.second } }
                .sortedBy { it.second }
                .map { "${it.second} ms ${it.first}" }
    )

    val s = result.joinToString("\n")
    println(s)
    return s
}

fun main(args: Array<String>) {
    val results = listOf<String>(
        mtGaussianBench(Mode.SINGLE_THREAD),
        mtGaussianBench(Mode.SIX_LARGE_COROUTINES),
        mtGaussianBench(Mode.SIX_SMALL_COROUTINES),
    )
    println("=".repeat(80))
    println("=".repeat(80))
    println()
    println(results.joinToString("\n\n"))
}
