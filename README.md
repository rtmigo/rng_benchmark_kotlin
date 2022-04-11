Benchmarking thread-safe ways to generate a single random normalized gaussian value in Kotlin/JVM.

Comparing:
- `java.util.Random().nextGaussian()`
  - creating new `Random` object instance for each call
  - reusing `Random` in `synchronous` block
- `java.util.concurrent.ThreadLocalRandom.current().nextGaussian()` 
- `kotlin.math.Random.nextDouble(...)` with [Box–Muller transform](http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html) extension 
- `org.apache.commons.rng.simple.ThreadLocalRandomSource.current(...)`
  - with new `ZigguratSampler` instance for each call
  - with Box–Muller transform extension 
- reusing `org.apache.commons.math3.random.SynchronizedRandomGenerator`

See source for details.

# Results

(on Intel© Core™ i7-8700K CPU @ 3.70GHz × 6, Linux 5.13.0-27-generic)

```text
================================================================================
RESULTS SINGLE_THREAD @ 2022-04-10T23:58:04.436786Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

795 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
875 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
876 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
880 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
886 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
895 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
895 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
896 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
897 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
898 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
901 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
903 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
904 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
907 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
915 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
917 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
921 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
937 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
978 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
979 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
980 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
981 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
990 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
991 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
993 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
993 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1008 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
1018 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
1066 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1093 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
1097 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
1127 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
1146 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1169 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
1182 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1219 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
1287 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
1295 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1340 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
1433 ms kotlin.random.Random.boxMullerGaussian()
1661 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
1749 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
1769 ms ThreadLocalRandom.current().nextGaussian()
1848 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
1875 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
1930 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
2094 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
2110 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
2124 ms .boxMullerGaussian() for ThreadLocalRandomSource SPLIT_MIX_64
2127 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
2134 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_64
2141 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
2182 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
2186 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_64
2187 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
2208 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
2213 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_SS
2219 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PP
2236 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PP
2246 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64
2260 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
2286 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
2307 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_SS
2309 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
2317 ms synchronized reuse of java.util.Random and call .nextGaussian()
2335 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
2354 ms .boxMullerGaussian() for ThreadLocalRandomSource TWO_CMRES
2402 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
2415 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S
2432 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
2472 ms .boxMullerGaussian() for ThreadLocalRandomSource MSWS
2475 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
2515 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
2520 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
2534 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32
2542 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_32
2554 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32
2555 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
2556 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
2560 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
2577 ms .boxMullerGaussian() for ThreadLocalRandomSource MT_64
2583 ms reuse SynchronizedRandomGenerator( Well19937c() )
2597 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
2610 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_32
2615 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PP
2655 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_SS
2724 ms .boxMullerGaussian() for ThreadLocalRandomSource MWC_256
2839 ms .boxMullerGaussian() for ThreadLocalRandomSource KISS
2991 ms .boxMullerGaussian() for ThreadLocalRandomSource MT
3103 ms .boxMullerGaussian() for ThreadLocalRandomSource ISAAC
3327 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_512_A
3454 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_1024_A
3864 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_A
3920 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_A
3922 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_C
4010 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_B
4067 ms .boxMullerGaussian() for ThreadLocalRandomSource JDK
5057 ms create java.util.Random() and call .nextGaussian()


================================================================================
RESULTS SIX_LARGE_COROUTINES @ 2022-04-11T00:02:07.737756Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

478 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
521 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
523 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
528 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
531 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
539 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
542 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
548 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
554 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
560 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
570 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
574 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
574 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
578 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
579 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
584 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
588 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
592 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
594 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
595 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
599 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
599 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
604 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
609 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
613 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
614 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
625 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
636 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
647 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
661 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
665 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
668 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
682 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
694 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
703 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
719 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
741 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
748 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
824 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
853 ms kotlin.random.Random.boxMullerGaussian()
955 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
957 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
968 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
1015 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
1016 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
1022 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
1090 ms ThreadLocalRandom.current().nextGaussian()
1126 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
1295 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1311 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1318 ms .boxMullerGaussian() for ThreadLocalRandomSource SPLIT_MIX_64
1325 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_64
1348 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_SS
1349 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1350 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1356 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
1359 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PP
1361 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1362 ms synchronized reuse of java.util.Random and call .nextGaussian()
1370 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PP
1374 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64
1380 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_64
1399 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_SS
1401 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1411 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1419 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1425 ms .boxMullerGaussian() for ThreadLocalRandomSource TWO_CMRES
1457 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1473 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1480 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S
1502 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1511 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1511 ms .boxMullerGaussian() for ThreadLocalRandomSource MSWS
1527 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32
1536 ms reuse SynchronizedRandomGenerator( Well19937c() )
1542 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
1549 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1551 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1565 ms .boxMullerGaussian() for ThreadLocalRandomSource MT_64
1576 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
1581 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1585 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_32
1586 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32
1586 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_32
1610 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_SS
1655 ms .boxMullerGaussian() for ThreadLocalRandomSource MWC_256
1660 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PP
1735 ms .boxMullerGaussian() for ThreadLocalRandomSource KISS
1821 ms .boxMullerGaussian() for ThreadLocalRandomSource MT
1887 ms .boxMullerGaussian() for ThreadLocalRandomSource ISAAC
2241 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_1024_A
2346 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_512_A
2441 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_B
2454 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_A
2463 ms .boxMullerGaussian() for ThreadLocalRandomSource JDK
2586 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_A
2675 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_C
3061 ms create java.util.Random() and call .nextGaussian()


================================================================================
RESULTS SIX_SMALL_COROUTINES @ 2022-04-11T00:00:12.251806Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

1188 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1193 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1194 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
1194 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1195 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1196 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1198 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1198 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
1198 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1200 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
1202 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1205 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
1205 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
1206 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
1207 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
1208 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
1210 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
1214 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1215 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1227 ms kotlin.random.Random.boxMullerGaussian()
1235 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
1237 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
1238 ms ThreadLocalRandom.current().nextGaussian()
1243 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
1244 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
1244 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1245 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
1245 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
1249 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
1253 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
1256 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
1258 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1258 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
1265 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1266 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
1266 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
1274 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
1280 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
1282 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_SS
1284 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1287 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1288 ms .boxMullerGaussian() for ThreadLocalRandomSource SPLIT_MIX_64
1288 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S
1288 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1292 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1292 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64
1294 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
1297 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1297 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_SS
1300 ms .boxMullerGaussian() for ThreadLocalRandomSource MT_64
1301 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_32
1301 ms .boxMullerGaussian() for ThreadLocalRandomSource SFC_64
1304 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
1304 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_64
1306 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1306 ms .boxMullerGaussian() for ThreadLocalRandomSource JSF_32
1307 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
1307 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
1308 ms .boxMullerGaussian() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1308 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1309 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32
1310 ms .boxMullerGaussian() for ThreadLocalRandomSource MWC_256
1311 ms .boxMullerGaussian() for ThreadLocalRandomSource KISS
1313 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_256_PP
1314 ms .boxMullerGaussian() for ThreadLocalRandomSource TWO_CMRES
1315 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1319 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_512_PP
1320 ms .boxMullerGaussian() for ThreadLocalRandomSource MSWS
1322 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
1325 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_PP
1326 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1326 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32
1327 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
1331 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1333 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
1334 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1337 ms synchronized reuse of java.util.Random and call .nextGaussian()
1337 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
1342 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_SHI_RO_128_SS
1357 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1365 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1381 ms .boxMullerGaussian() for ThreadLocalRandomSource ISAAC
1384 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1386 ms .boxMullerGaussian() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1401 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
1418 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_1024_A
1420 ms reuse SynchronizedRandomGenerator( Well19937c() )
1426 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_B
1428 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
1430 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
1437 ms .boxMullerGaussian() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1440 ms .boxMullerGaussian() for ThreadLocalRandomSource MT
1454 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_44497_A
1478 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_A
1507 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_512_A
1509 ms .boxMullerGaussian() for ThreadLocalRandomSource JDK
1541 ms .boxMullerGaussian() for ThreadLocalRandomSource WELL_19937_C
1716 ms create java.util.Random() and call .nextGaussian()
```