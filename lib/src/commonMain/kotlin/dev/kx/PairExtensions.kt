package dev.kx

/**
 * Returns a new pair with the elements in reversed order.
 *
 * ```kotlin
 * ("key" to 1).swap() // (1, "key")
 * ```
 *
 * @return a pair of `second to first`.
 */
public fun <A, B> Pair<A, B>.swap(): Pair<B, A> = second to first
