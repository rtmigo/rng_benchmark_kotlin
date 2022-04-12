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

`ms` is milliseconds, lower time is better

```text
================================================================================
RESULTS SINGLE_THREAD @ 2022-04-12T21:21:06.253573Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

161 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
165 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
174 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
175 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
175 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
176 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
176 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
176 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
177 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
177 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
177 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
177 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
178 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
179 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
181 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
181 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
182 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
182 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
184 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
189 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
190 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
191 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
191 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
192 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
195 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
195 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
195 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
196 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
196 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
196 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
197 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
197 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
200 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
201 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
211 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
214 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
215 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
219 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
238 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
263 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
270 ms [C] kotlin.random.Random.boxMullerGaussian()
270 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
276 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
306 ms [B] ThreadLocalRandom.current().nextGaussian()
306 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
310 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
313 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
317 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
326 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
332 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
335 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
339 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
341 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
343 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
344 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
345 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
348 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
350 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
352 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
358 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
358 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
359 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
363 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
369 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
376 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
377 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
387 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
400 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
400 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
405 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
407 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
407 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
409 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
409 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
409 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
410 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
410 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
411 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
411 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
414 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
419 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
423 ms [A2] java.util.Random: synchronized reuse
425 ms [E] SynchronizedRandomGenerator MersenneTwister
431 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
440 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
470 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
472 ms [E] SynchronizedRandomGenerator Well19937c
491 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
521 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
570 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
587 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
657 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
667 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
668 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
671 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
696 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
976 ms [A1] java.util.Random: create for each call

================================================================================
RESULTS LARGE_COROUTINES @ 2022-04-12T21:23:13.364483Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

124 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
127 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
135 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
136 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
136 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
136 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
136 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
137 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
137 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
138 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
138 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
138 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
138 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
139 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
140 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
141 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
141 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
142 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
144 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
145 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
145 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
149 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
149 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
149 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
152 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
152 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
152 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
152 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
152 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
153 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
153 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
154 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
156 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
157 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
159 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
166 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
171 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
174 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
186 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
211 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
214 ms [C] kotlin.random.Random.boxMullerGaussian()
216 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
221 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
242 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
244 ms [B] ThreadLocalRandom.current().nextGaussian()
245 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
247 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
248 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
260 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
263 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
267 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
269 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
270 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
273 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
276 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
276 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
276 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
278 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
279 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
281 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
287 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
288 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
292 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
292 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
295 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
302 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
305 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
307 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
317 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
318 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
324 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
325 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
325 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
326 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
327 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
328 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
328 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
332 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
336 ms [E] SynchronizedRandomGenerator MersenneTwister
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
339 ms [A2] java.util.Random: synchronized reuse
352 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
379 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
381 ms [E] SynchronizedRandomGenerator Well19937c
395 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
418 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
462 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
471 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
531 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
532 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
539 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
543 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
551 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
769 ms [A1] java.util.Random: create for each call

================================================================================
RESULTS SMALL_COROUTINES @ 2022-04-12T21:25:55.199400Z
================================================================================
JVM 11.0.14 Kotlin 1.6.20

315 ms [C] kotlin.random.Random.boxMullerGaussian()
315 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_SS
316 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
316 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PLUS
316 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32_OS
317 ms [D1] ZigguratSampler ThreadLocalRandomSource SPLIT_MIX_64
317 ms [D1] ZigguratSampler ThreadLocalRandomSource TWO_CMRES
317 ms [D1] ZigguratSampler ThreadLocalRandomSource MT_64
317 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
317 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_SS
317 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
317 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32
317 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64
317 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_PP
317 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
317 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
318 ms [D1] ZigguratSampler ThreadLocalRandomSource MWC_256
318 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PLUS
318 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RS_32
318 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RR_32
318 ms [D1] ZigguratSampler ThreadLocalRandomSource MSWS
318 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_32
318 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
319 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S
319 ms [D1] ZigguratSampler ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
319 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PLUS
319 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_512_SS
319 ms [D1] ZigguratSampler ThreadLocalRandomSource SFC_64
320 ms [D1] ZigguratSampler ThreadLocalRandomSource ISAAC
321 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_512_A
321 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_1024_A
321 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
322 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_B
322 ms [D1] ZigguratSampler ThreadLocalRandomSource KISS
322 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_32
322 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_128_PP
322 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
322 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_SHI_RO_256_PP
322 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_XSH_RR_32_OS
322 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_64
323 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_A
323 ms [D1] ZigguratSampler ThreadLocalRandomSource XO_RO_SHI_RO_64_S
323 ms [D1] ZigguratSampler ThreadLocalRandomSource JSF_64
324 ms [A2] java.util.Random: synchronized reuse
324 ms [D1] ZigguratSampler ThreadLocalRandomSource MT
324 ms [D1] ZigguratSampler ThreadLocalRandomSource PCG_MCG_XSH_RS_32
325 ms [D1] ZigguratSampler ThreadLocalRandomSource JDK
325 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64_OS
326 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_SS
328 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SPLIT_MIX_64
328 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_RXS_M_XS_64
329 ms [B] ThreadLocalRandom.current().nextGaussian()
329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PLUS
329 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_SS
330 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_44497_A
330 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PLUS
330 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_64
330 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_S
331 ms [D2] boxMullerGaussian() ThreadLocalRandomSource TWO_CMRES
331 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PLUS
331 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_SS
331 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_SS
331 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_512_PP
332 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MSWS
332 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_128_PP
332 ms [E] SynchronizedRandomGenerator MersenneTwister
333 ms [D1] ZigguratSampler ThreadLocalRandomSource WELL_19937_C
333 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RS_32
333 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_1024_PP
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_SS
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_MCG_XSH_RR_32
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource SFC_32
334 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RR_32_OS
335 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_S
335 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_RO_SHI_RO_64_SS
335 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PLUS
336 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S
336 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT_64
336 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32_OS
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MWC_256
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XOR_SHIFT_1024_S_PHI
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_128_PP
337 ms [D2] boxMullerGaussian() ThreadLocalRandomSource XO_SHI_RO_256_PP
338 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JSF_32
339 ms [E] SynchronizedRandomGenerator Well19937c
340 ms [D2] boxMullerGaussian() ThreadLocalRandomSource KISS
341 ms [D2] boxMullerGaussian() ThreadLocalRandomSource MT
341 ms [D2] boxMullerGaussian() ThreadLocalRandomSource PCG_XSH_RS_32
343 ms [D2] boxMullerGaussian() ThreadLocalRandomSource ISAAC
349 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_1024_A
353 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_512_A
355 ms [D2] boxMullerGaussian() ThreadLocalRandomSource JDK
365 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_A
366 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_A
367 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_19937_C
367 ms [D2] boxMullerGaussian() ThreadLocalRandomSource WELL_44497_B
373 ms [A1] java.util.Random: create for each call
```