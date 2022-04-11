Benchmarking thread-safe ways to generate a single random normalized gaussian value in Kotlin/JVM.

Comparing:
- `java.util.Random().nextGaussian()`
  - **[A1]** creating new `Random` object instance for each value
  - **[A2]** reusing `Random` in `synchronous` block
- **[B]** `java.util.concurrent.ThreadLocalRandom.current().nextGaussian()` 
- **[C]** `kotlin.math.Random.nextDouble(...)` with [Box–Muller transform](http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html) extension 
- `org.apache.commons.rng.simple.ThreadLocalRandomSource.current(...)`
  - **[D1]** with new `ZigguratSampler` instance for each value
  - **[D2]** with Box–Muller transform extension 
- **[E]** reusing `org.apache.commons.math3.random.SynchronizedRandomGenerator`

See [source](https://github.com/rtmigo/rng_benchmark_kt/blob/dev/src/main/kotlin/Main.kt) for details.

- SINGLE_THREAD: Accessing thread-safe generator from single thread

- LARGE_COROUTINES: Starting six coroutines in parallel and generating many
  samples in each of them
 
- SMALL_COROUTINES: Starting and stopping parallel coroutines multiple times to
  generate single samples in each of them

# Results

on Intel© Core™ i7-8700K CPU @ 3.70GHz × 6, Linux 5.13.0-27-generic

`ms` is milliseconds, lower time is better

```text
================================================================================
RESULTS SINGLE_THREAD @ 2022-04-11T03:44:44.816899Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

1088 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
1121 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
1196 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1207 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1209 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1209 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1211 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
1211 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
1221 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1224 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
1227 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
1238 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1239 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
1242 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1253 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
1254 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1255 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1262 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
1272 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
1284 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1296 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
1339 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1353 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
1356 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
1361 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
1361 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1362 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
1368 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
1368 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
1370 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1397 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
1399 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
1403 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
1412 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1472 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1473 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
1482 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
1541 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
1586 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
1669 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
1820 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
1897 ms [C] kotlin.random.Random.boxMullerGaussian()
1944 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
2131 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
2152 ms [B] ThreadLocalRandom.current().nextGaussian()
2163 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
2190 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
2246 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
2936 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
2947 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
2963 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
2978 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
2980 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
2997 ms [E] SynchronizedRandomGenerator MersenneTwister
3023 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
3058 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
3062 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
3064 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
3074 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
3077 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
3083 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
3100 ms [A2] java.util.Random: synchronized reuse
3129 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
3158 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
3171 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
3188 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
3192 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
3248 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
3289 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
3289 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
3353 ms [E] SynchronizedRandomGenerator Well19937c
3426 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
3432 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
3492 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
3493 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
3509 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
3533 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
3537 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
3541 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
3546 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
3548 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
3551 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
3552 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
3559 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
3626 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
3684 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
3805 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
3945 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
4140 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
4347 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
4635 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
4782 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
5137 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
5321 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
5421 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
5454 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
5552 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
6935 ms [A1] java.util.Random: create for each call

================================================================================
RESULTS SIX_LARGE_COROUTINES @ 2022-04-11T03:47:17.595702Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

644 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
655 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
700 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
712 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
718 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
719 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
719 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
722 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
725 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
728 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
734 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
738 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
739 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
739 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
745 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
746 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
755 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
756 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
759 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
762 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
764 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
798 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
800 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
807 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
810 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
819 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
820 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
826 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
829 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
832 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
840 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
840 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
850 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
861 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
863 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
877 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
900 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
902 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
939 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
1008 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
1128 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
1133 ms [C] kotlin.random.Random.boxMullerGaussian()
1163 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
1261 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
1274 ms [B] ThreadLocalRandom.current().nextGaussian()
1276 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
1310 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
1370 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
1771 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1782 ms [A2] java.util.Random: synchronized reuse
1794 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1794 ms [E] SynchronizedRandomGenerator MersenneTwister
1800 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1806 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
1814 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
1829 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
1852 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
1856 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1864 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1865 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
1866 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1887 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
1920 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
1927 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1928 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
1939 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1941 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
1988 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1990 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
1990 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
2016 ms [E] SynchronizedRandomGenerator Well19937c
2054 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
2075 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
2103 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
2107 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
2117 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
2133 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
2134 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
2143 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
2148 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
2156 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
2162 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
2177 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
2189 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
2206 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
2250 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
2292 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
2373 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
2497 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
2622 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
2886 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
2911 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
3162 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
3217 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
3252 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
3267 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
3329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
4144 ms [A1] java.util.Random: create for each call

================================================================================
RESULTS SIX_SMALL_COROUTINES @ 2022-04-11T03:50:16.235772Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

1722 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1722 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1726 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
1726 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1727 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1727 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
1728 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
1730 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1730 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
1732 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1732 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
1732 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1732 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1733 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
1734 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
1735 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
1737 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
1738 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1739 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
1740 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1741 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
1741 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1742 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
1743 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
1744 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
1744 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
1745 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
1745 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
1747 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1748 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
1748 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
1750 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
1750 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
1750 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
1753 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
1754 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1756 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
1757 ms [C] kotlin.random.Random.boxMullerGaussian()
1760 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1766 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1770 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
1772 ms [B] ThreadLocalRandom.current().nextGaussian()
1790 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
1798 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
1799 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
1806 ms [A2] java.util.Random: synchronized reuse
1806 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
1808 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
1811 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
1830 ms [E] SynchronizedRandomGenerator MersenneTwister
1841 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
1842 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
1843 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
1845 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
1846 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
1849 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
1849 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
1850 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
1851 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
1853 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
1858 ms [E] SynchronizedRandomGenerator Well19937c
1860 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
1860 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
1861 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
1862 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
1863 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
1863 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
1864 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
1866 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
1868 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
1871 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
1874 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
1874 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
1874 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
1876 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
1878 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
1880 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
1885 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
1887 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
1889 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
1889 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
1893 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
1895 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
1895 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
1895 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
1897 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
1900 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
1916 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
1930 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
1937 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
1959 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
1983 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
2002 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
2007 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
2016 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
2017 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
2029 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
2080 ms [A1] java.util.Random: create for each call
```