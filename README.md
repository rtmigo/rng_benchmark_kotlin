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
RESULTS SINGLE_THREAD @ 2022-04-10T23:31:01.621449Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

481 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
491 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
534 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
537 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
537 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
538 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
543 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
544 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
544 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
547 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
553 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
556 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
561 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
564 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
564 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
567 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
567 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
568 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
570 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
570 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
578 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
589 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
591 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
592 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
600 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
601 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
601 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
602 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
602 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
603 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
603 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
603 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
607 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
613 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
619 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
649 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
651 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
669 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
746 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
761 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
778 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
840 ms kotlin.random.Random.boxMullerGaussian()
850 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
927 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
939 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
950 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
953 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
955 ms ThreadLocalRandom.current().nextGaussian()
1311 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
1385 ms synchronized reuse of java.util.Random and call .nextGaussian()
1470 ms reuse SynchronizedRandomGenerator( Well19937c() )
2985 ms create java.util.Random() and call .nextGaussian()


================================================================================
RESULTS SIX_LARGE_COROUTINES @ 2022-04-10T23:29:11.098844Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

284 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
294 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
298 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
298 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
302 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
302 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
304 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
305 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
307 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
307 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
308 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
310 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
311 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
312 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
316 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
318 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
320 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
321 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
321 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
322 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
327 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
342 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
343 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
344 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
345 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
346 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
348 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
350 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
351 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
353 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
354 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
354 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
355 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
358 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
363 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
376 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
384 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
438 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
465 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
472 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
486 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
497 ms kotlin.random.Random.boxMullerGaussian()
518 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
556 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
579 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
590 ms ThreadLocalRandom.current().nextGaussian()
594 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
660 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
805 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
837 ms synchronized reuse of java.util.Random and call .nextGaussian()
899 ms reuse SynchronizedRandomGenerator( Well19937c() )
1883 ms create java.util.Random() and call .nextGaussian()


================================================================================
RESULTS SIX_SMALL_COROUTINES @ 2022-04-10T23:24:03.666073Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

680 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
681 ms ZigguratSampler.sample() for ThreadLocalRandomSource SPLIT_MIX_64
682 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
682 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
683 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64
683 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
684 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_64
685 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_SS
685 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_32
686 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
686 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PLUS
686 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PP
686 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32_OS
687 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_S
688 ms ZigguratSampler.sample() for ThreadLocalRandomSource MSWS
688 ms ZigguratSampler.sample() for ThreadLocalRandomSource SFC_32
688 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
689 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RR_32
689 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_256_PP
690 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PLUS
690 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_SS
690 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_512_PP
690 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32_OS
691 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_PLUS
691 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
692 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_SHI_RO_128_SS
692 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_XSH_RS_32
692 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RS_32
692 ms ZigguratSampler.sample() for ThreadLocalRandomSource JSF_64
693 ms ZigguratSampler.sample() for ThreadLocalRandomSource PCG_MCG_XSH_RR_32
693 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
694 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT
694 ms ZigguratSampler.sample() for ThreadLocalRandomSource XOR_SHIFT_1024_S
694 ms ZigguratSampler.sample() for ThreadLocalRandomSource MWC_256
695 ms ZigguratSampler.sample() for ThreadLocalRandomSource TWO_CMRES
696 ms ZigguratSampler.sample() for ThreadLocalRandomSource ISAAC
696 ms ZigguratSampler.sample() for ThreadLocalRandomSource KISS
696 ms ZigguratSampler.sample() for ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
701 ms kotlin.random.Random.boxMullerGaussian()
708 ms ThreadLocalRandom.current().nextGaussian()
710 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_C
711 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_19937_A
712 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_B
713 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_44497_A
721 ms ZigguratSampler.sample() for ThreadLocalRandomSource JDK
742 ms reuse SynchronizedRandomGenerator( MersenneTwister() )
744 ms ZigguratSampler.sample() for ThreadLocalRandomSource MT_64
746 ms synchronized reuse of java.util.Random and call .nextGaussian()
754 ms reuse SynchronizedRandomGenerator( Well19937c() )
759 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_1024_A
762 ms ZigguratSampler.sample() for ThreadLocalRandomSource WELL_512_A
1023 ms create java.util.Random() and call .nextGaussian()
```