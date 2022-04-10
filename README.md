Benchmarking thread-safe ways to generate a single random normalized gaussian value in Kotlin/JVM.

Comparing:
- `java.util.Random().nextGaussian()` (creating new instance each time)
- `java.util.concurrent.ThreadLocalRandom.current().nextGaussian()` 
- getting  `org.apache.commons.rng.simple.ThreadLocalRandomSource.current(...)` and creating a new `ZigguratSampler` each time
- reusing `org.apache.commons.math3.random.SynchronizedRandomGenerator`

# Results

(on Intel© Core™ i7-8700K CPU @ 3.70GHz × 6, Linux 5.13.0-27-generic)

```text
================================================================================
RESULTS (2022-04-10T21:17:38.582922Z)
================================================================================
JVM 11.0.14 Kotlin 1.6.20

9017 ms threadLocalZigguratSampler(MSWS).sample()
9993 ms threadLocalZigguratSampler(XO_RO_SHI_RO_128_PP).sample()
9998 ms threadLocalZigguratSampler(SFC_64).sample()
10056 ms threadLocalZigguratSampler(SPLIT_MIX_64).sample()
10096 ms threadLocalZigguratSampler(PCG_RXS_M_XS_64_OS).sample()
10172 ms threadLocalZigguratSampler(XO_SHI_RO_256_PP).sample()
10176 ms threadLocalZigguratSampler(XO_RO_SHI_RO_1024_PP).sample()
10413 ms threadLocalZigguratSampler(XO_RO_SHI_RO_128_SS).sample()
10454 ms threadLocalZigguratSampler(XO_RO_SHI_RO_1024_S).sample()
10457 ms threadLocalZigguratSampler(XO_SHI_RO_512_PP).sample()
10461 ms threadLocalZigguratSampler(XO_RO_SHI_RO_1024_SS).sample()
10642 ms threadLocalZigguratSampler(JSF_64).sample()
10744 ms threadLocalZigguratSampler(XO_SHI_RO_512_PLUS).sample()
11192 ms threadLocalZigguratSampler(PCG_XSH_RS_32_OS).sample()
11214 ms threadLocalZigguratSampler(PCG_XSH_RR_32_OS).sample()
11226 ms threadLocalZigguratSampler(JSF_32).sample()
11236 ms threadLocalZigguratSampler(XO_SHI_RO_256_PLUS).sample()
11295 ms threadLocalZigguratSampler(PCG_MCG_XSH_RR_32).sample()
11322 ms threadLocalZigguratSampler(PCG_MCG_XSH_RS_32).sample()
11419 ms threadLocalZigguratSampler(SFC_32).sample()
11444 ms threadLocalZigguratSampler(XO_SHI_RO_128_PP).sample()
11522 ms threadLocalZigguratSampler(XO_RO_SHI_RO_128_PLUS).sample()
11580 ms threadLocalZigguratSampler(PCG_RXS_M_XS_64).sample()
11639 ms threadLocalZigguratSampler(XO_RO_SHI_RO_64_S).sample()
11721 ms threadLocalZigguratSampler(TWO_CMRES).sample()
11833 ms threadLocalZigguratSampler(XO_SHI_RO_256_SS).sample()
11906 ms threadLocalZigguratSampler(PCG_XSH_RR_32).sample()
11972 ms threadLocalZigguratSampler(XOR_SHIFT_1024_S).sample()
11985 ms threadLocalZigguratSampler(MT_64).sample()
12174 ms threadLocalZigguratSampler(XO_SHI_RO_128_SS).sample()
12192 ms threadLocalZigguratSampler(XO_RO_SHI_RO_64_SS).sample()
12248 ms threadLocalZigguratSampler(PCG_XSH_RS_32).sample()
12465 ms threadLocalZigguratSampler(XO_SHI_RO_512_SS).sample()
12559 ms threadLocalZigguratSampler(XOR_SHIFT_1024_S_PHI).sample()
12740 ms threadLocalZigguratSampler(MT).sample()
13012 ms threadLocalZigguratSampler(KISS).sample()
13498 ms threadLocalZigguratSampler(XO_SHI_RO_128_PLUS).sample()
14844 ms threadLocalZigguratSampler(MWC_256).sample()
15160 ms threadLocalZigguratSampler(WELL_512_A).sample()
15166 ms threadLocalZigguratSampler(JDK).sample()
15752 ms threadLocalZigguratSampler(WELL_1024_A).sample()
17015 ms threadLocalZigguratSampler(ISAAC).sample()
17827 ms threadLocalZigguratSampler(WELL_19937_C).sample()
17864 ms threadLocalZigguratSampler(WELL_19937_A).sample()
18085 ms ThreadLocalRandom.current().nextGaussian()
18332 ms threadLocalZigguratSampler(WELL_44497_A).sample()
19204 ms threadLocalZigguratSampler(WELL_44497_B).sample()
25089 ms reuse SynchronizedRandomGenerator(MersenneTwister())
26643 ms synchronized reuse of java.util.Random and calling .nextGaussian()
28741 ms reuse SynchronizedRandomGenerator(Well19937c())
58953 ms creating java.util.Random() and calling .nextGaussian()
```
 