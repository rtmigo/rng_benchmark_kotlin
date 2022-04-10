Benchmarking thread-safe ways to generate a single random normalized gaussian value in Kotlin/JVM.

Comparing:
- `java.util.Random().nextGaussian()` (creating new `Random` instance each time)
- `java.util.concurrent.ThreadLocalRandom.current().nextGaussian()` 
- using [Box–Muller transform](http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html) with `kotlin.math.Random.nextDouble(...)`
- getting  `org.apache.commons.rng.simple.ThreadLocalRandomSource.current(...)` and creating a new `ZigguratSampler` each time
- reusing `org.apache.commons.math3.random.SynchronizedRandomGenerator`

See source for details.

# Results

(on Intel© Core™ i7-8700K CPU @ 3.70GHz × 6, Linux 5.13.0-27-generic)

```text
================================================================================
RESULTS (2022-04-10T22:23:43.193350Z)
================================================================================
JVM 11.0.14 Kotlin 1.6.20

989 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
1012 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
1068 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1073 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1076 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1083 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
1083 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1084 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
1086 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
1086 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
1099 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
1102 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1111 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1112 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1113 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
1113 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1123 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
1130 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1133 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
1146 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1156 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
1167 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1189 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
1190 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1195 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
1200 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1211 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
1212 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
1213 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1213 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
1215 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1228 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
1240 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
1244 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
1250 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
1287 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
1307 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
1340 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
1370 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
1484 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
1607 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
1658 ms kotlin.random.Random.boxMullerGaussian()
1683 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
1835 ms ThreadLocalRandom.current().nextGaussian()
1870 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
1905 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
1915 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
1915 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
2611 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
2694 ms synchronized reuse of java.util.Random and call .nextGaussian()
2923 ms reuse SynchronizedRandomGenerator( Well19937c() )
6032 ms create java.util.Random() and call .nextGaussian()
```
 