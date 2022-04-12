Benchmarking thread-safe ways to generate a single random normalized gaussian value in Kotlin/JVM.

Comparing:
- `java.util.Random().nextGaussian()`
  - **[A1]** creating new `Random` object instance for each sample
  - **[A2]** reusing `Random` in `synchronous` block
- **[B]** `java.util.concurrent.ThreadLocalRandom.current().nextGaussian()` 
- **[C]** `kotlin.math.Random.nextDouble(...)` with [Box–Muller transform](http://www.java2s.com/example/java-utility-method/gaussian/gaussian-973fd.html) extension 
- `org.apache.commons.rng.simple.ThreadLocalRandomSource.current(...)`
  - **[D1]** with new `ZigguratSampler` instance for each sample
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

Percentage is time, lower values are better (faster).

```text
================================================================================
RESULTS SINGLE_THREAD @ 2022-04-12T22:08:51.133317Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

100.00% [D1] ZigguratSampler ThreadLocalRandomSource MSWS
100.83% [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
110.25% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
110.97% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
111.39% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
112.01% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
112.73% [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
112.84% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
112.84% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
113.77% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
113.87% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
115.84% [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
116.36% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
116.36% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
117.29% [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
118.12% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
118.43% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
119.05% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
119.25% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
121.22% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
124.43% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
124.74% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
124.84% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
126.40% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
126.92% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
127.02% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
127.12% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
127.23% [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
127.95% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
128.47% [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
130.02% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
130.95% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
131.47% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
133.13% [D1] ZigguratSampler ThreadLocalRandomSource MT_64
139.44% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
144.10% [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
145.96% [D1] ZigguratSampler ThreadLocalRandomSource MT
149.69% [D1] ZigguratSampler ThreadLocalRandomSource KISS
152.69% [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
159.01% [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
185.82% [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
188.41% [D1] ZigguratSampler ThreadLocalRandomSource JDK
198.03% [C] kotlin.random.Random.boxMullerGaussian()
207.45% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
207.87% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
212.53% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
216.25% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
223.60% [B] ThreadLocalRandom.current().nextGaussian()
242.13% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
247.93% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
249.17% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
249.69% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
254.24% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
254.45% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
257.04% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
260.46% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
261.80% [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
263.46% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
264.91% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
270.19% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
271.74% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
272.98% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
275.47% [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
277.43% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
277.54% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
278.67% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
279.92% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
284.89% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
295.76% [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
295.96% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
301.04% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
301.76% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
302.90% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
303.21% [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
304.24% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
304.55% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
304.55% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
304.97% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
305.69% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
310.97% [E] SynchronizedRandomGenerator MersenneTwister
311.18% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
315.94% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
317.18% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
318.32% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
319.77% [A2] java.util.Random: synchronized reuse
325.67% [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
349.90% [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
350.93% [E] SynchronizedRandomGenerator Well19937c
364.60% [D2] boxMullerGaussian() ThreadLocalRandomSource MT
386.34% [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
421.95% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
426.29% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
485.82% [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
487.47% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
490.99% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
492.24% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
509.83% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
712.22% [A1] java.util.Random: create for each call

================================================================================
RESULTS LARGE_COROUTINES @ 2022-04-12T22:12:07.696790Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

100.00% [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
105.99% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
107.68% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
109.15% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
109.49% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
110.06% [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
110.62% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
110.85% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
111.64% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
112.09% [D1] ZigguratSampler ThreadLocalRandomSource MSWS
113.11% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
113.90% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
114.35% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
114.80% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
115.48% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
115.82% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
116.61% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
117.97% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
118.64% [D1] ZigguratSampler ThreadLocalRandomSource MT_64
120.00% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
121.69% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
123.50% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
124.18% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
124.29% [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
125.42% [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
126.78% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
128.36% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
130.28% [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
131.98% [D1] ZigguratSampler ThreadLocalRandomSource KISS
132.32% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
136.61% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
137.40% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
139.66% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
140.56% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
141.36% [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
142.82% [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
147.68% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
148.59% [D1] ZigguratSampler ThreadLocalRandomSource MT
150.40% [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
158.76% [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
180.34% [C] kotlin.random.Random.boxMullerGaussian()
195.59% [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
201.69% [D1] ZigguratSampler ThreadLocalRandomSource JDK
208.25% [B] ThreadLocalRandom.current().nextGaussian()
223.05% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
224.97% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
229.04% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
229.72% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
229.94% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
234.12% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
234.92% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
237.40% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
238.08% [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
242.49% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
243.84% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
244.63% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
246.55% [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
246.89% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
247.12% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
252.77% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
255.59% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
258.53% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
260.00% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
262.15% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
265.76% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
266.55% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
268.59% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
269.15% [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
269.60% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
269.94% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
275.48% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
276.16% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
278.87% [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
280.23% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
281.24% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
282.37% [A2] java.util.Random: synchronized reuse
283.28% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
284.07% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
286.10% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
288.25% [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
289.94% [E] SynchronizedRandomGenerator MersenneTwister
291.75% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
294.35% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
298.19% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
307.12% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
318.08% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
320.90% [E] SynchronizedRandomGenerator Well19937c
323.50% [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
365.99% [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
367.01% [D2] boxMullerGaussian() ThreadLocalRandomSource MT
403.73% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
423.39% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
443.28% [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
510.73% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
522.37% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
529.38% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
536.95% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
642.60% [A1] java.util.Random: create for each call

================================================================================
RESULTS SMALL_COROUTINES @ 2022-04-12T22:15:50.452538Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

100.00% [D1] ZigguratSampler ThreadLocalRandomSource MSWS
100.19% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
100.23% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
100.32% [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
100.37% [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
100.37% [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
100.37% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
100.42% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
100.42% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
100.51% [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
100.51% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
100.65% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
100.69% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
100.69% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
100.79% [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
100.79% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
100.83% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
100.83% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
100.88% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
100.93% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
100.93% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
101.06% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
101.11% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
101.16% [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
101.20% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
101.25% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
101.44% [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
101.53% [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
101.67% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
101.67% [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
101.71% [D1] ZigguratSampler ThreadLocalRandomSource MT_64
101.81% [D1] ZigguratSampler ThreadLocalRandomSource MT
101.85% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
101.94% [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
102.04% [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
102.22% [D1] ZigguratSampler ThreadLocalRandomSource KISS
102.41% [D1] ZigguratSampler ThreadLocalRandomSource JDK
102.41% [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
102.45% [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
102.55% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
103.15% [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
103.89% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
104.44% [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
104.54% [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
104.63% [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
104.68% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
104.86% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
104.91% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
104.91% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
105.32% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
105.32% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
105.46% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
105.46% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
105.74% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
105.88% [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
105.88% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
105.97% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
106.16% [C] kotlin.random.Random.boxMullerGaussian()
106.16% [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
106.20% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
106.25% [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
106.39% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
106.39% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
106.44% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
106.57% [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
106.67% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
106.71% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
106.81% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
106.99% [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
106.99% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
107.08% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
107.13% [E] SynchronizedRandomGenerator MersenneTwister
107.18% [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
107.36% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
107.69% [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
107.82% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
108.06% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
108.10% [E] SynchronizedRandomGenerator Well19937c
108.15% [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
108.24% [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
108.24% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
108.38% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
108.47% [A2] java.util.Random: synchronized reuse
108.47% [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
108.80% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
109.12% [B] ThreadLocalRandom.current().nextGaussian()
109.54% [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
109.72% [D2] boxMullerGaussian() ThreadLocalRandomSource MT
110.09% [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
110.28% [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
112.64% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
113.70% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
115.00% [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
115.88% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
116.06% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
116.44% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
116.90% [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
121.85% [A1] java.util.Random: create for each call
```