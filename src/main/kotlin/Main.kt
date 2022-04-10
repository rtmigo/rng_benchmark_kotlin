import org.apache.commons.math3.random.*
import org.apache.commons.rng.sampling.distribution.*
import org.apache.commons.rng.simple.*
import java.time.Instant

import java.util.concurrent.ThreadLocalRandom
import kotlin.system.measureTimeMillis

private fun threadLocalZigguratSampler(
    source: RandomSource = RandomSource.WELL_19937_C,
): NormalizedGaussianSampler {
    val rng = ThreadLocalRandomSource.current(source)
    return ZigguratSampler.NormalizedGaussian.of(rng)
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

fun mtGaussianBench() {

    val results = mutableListOf<Pair<String, Long>>()

    fun Pair<String, Long>.rmbr() = results.add(this)

    for (trial in 0..5) {

        val N = 100000000

        println()
        println("-".repeat(80))
        println()

        measure("creating java.util.Random() and calling .nextGaussian()", N) {
            java.util.Random().nextGaussian()
        }.rmbr()

        val javaUtilRandom = java.util.Random()

        measure("synchronized reusing of java.util.Random and calling .nextGaussian()", N) {
            synchronized(javaUtilRandom) {
                javaUtilRandom.nextGaussian()
            }
        }.rmbr()

        measure("ThreadLocalRandom.current().nextGaussian()", N) {
            ThreadLocalRandom.current().nextGaussian()
        }.rmbr()

        for (src in RandomSource.values()) {
            try {
                measure("threadLocalZigguratSampler($src).sample()", N) {
                    threadLocalZigguratSampler(src).sample()
                }.rmbr()
            } catch (e: Throwable) {
                println("skipped (${e::class.simpleName})")
            }
        }

        val syncWell = SynchronizedRandomGenerator(Well19937c())

        measure("reusing SynchronizedRandomGenerator(Well19937c())", N) {
            syncWell.nextGaussian()
        }.rmbr()

        val syncMt = SynchronizedRandomGenerator(MersenneTwister())

        measure("reusing SynchronizedRandomGenerator(MersenneTwister())", N) {
            syncMt.nextGaussian()
        }.rmbr()

    }

    println()
    println("=".repeat(80))
    println(" RESULTS (${Instant.now()})")
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
