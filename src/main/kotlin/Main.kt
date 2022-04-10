import org.apache.commons.math3.random.*
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

fun measure(name: String, repeat: Int, block: () -> Unit): Pair<String, Long> {
    print("$name... ")
    val elapsed = measureTimeMillis {
        for (i in 1..repeat) {
            block()
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
        x =this.nextDouble(-1.0, 1.0)
        y =this.nextDouble(-1.0, 1.0)
        r = x * x + y * y
    } while (r >= 1 || r == 0.0)
    return x * sqrt(-2 * ln(r) / r)

    // Remark:  y * Math.sqrt(-2 * Math.log(r) / r)
    // is an independent random gaussian
}

fun mtGaussianBench() {

    val results = mutableListOf<Pair<String, Long>>()

    fun Pair<String, Long>.rmbr() = results.add(this)

    for (trial in 0..5) {

        val N = 10000000

        println()
        println("-".repeat(80))
        println()

        measure("create java.util.Random() and call .nextGaussian()", N) {
            java.util.Random().nextGaussian()
        }.rmbr()

        val javaUtilRandom = java.util.Random()

        measure("synchronized reuse of java.util.Random and call .nextGaussian()", N) {
            synchronized(javaUtilRandom) {
                javaUtilRandom.nextGaussian()
            }
        }.rmbr()

        measure("kotlin.random.Random.boxMullerGaussian()", N) {
            kotlin.random.Random.boxMullerGaussian()
        }.rmbr()

        measure("ThreadLocalRandom.current().nextGaussian()", N) {

            ThreadLocalRandom.current().nextGaussian()
        }.rmbr()


        for (src in RandomSource.values()) {
            try {
                measure("ZigguratSampler.sample() for ThreadLocalRandomSource $src", N) {
                    threadLocalZigguratSampler(src).sample()
                }.rmbr()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }

        val syncWell = SynchronizedRandomGenerator(Well19937c())

        measure("reuse SynchronizedRandomGenerator( Well19937c() )", N) {
            syncWell.nextGaussian()
        }.rmbr()

        val syncMt = SynchronizedRandomGenerator(MersenneTwister())

        measure("reuse SynchronizedRandomGenerator( MersenneTwister() )", N) {
            syncMt.nextGaussian()
        }.rmbr()

    }

    println()
    println("=".repeat(80))
    println("RESULTS (${Instant.now()})")
    println("=".repeat(80))
    println("JVM ${System.getProperty("java.version")} Kotlin ${KotlinVersion.CURRENT}")
    println()

    println(results
                .groupBy { it.first }
                .map { it.key to it.value.sumOf { it.second } }
                .sortedBy { it.second }
                .joinToString("\n") { "${it.second} ms ${it.first}" }
    )
}

fun main(args: Array<String>) {
    mtGaussianBench()
}
