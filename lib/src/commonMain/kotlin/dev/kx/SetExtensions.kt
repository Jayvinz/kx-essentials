package dev.kx

/**
 * Returns the elements that were added to and removed from this set to reach
 * [other].
 *
 * The result is `(added, removed)`, where:
 * - `added` contains elements in [other] that are not in this set
 * - `removed` contains elements in this set that are not in [other]
 *
 * Both sets preserve iteration order: `added` follows [other], `removed`
 * follows this set. This function does not modify either input.
 *
 * ```kotlin
 * val old = setOf("a", "b")
 * val new = setOf("b", "c")
 * old.diff(new) // (added = [c], removed = [a])
 * ```
 *
 * @param other the new set to compare against.
 * @return a pair of added and removed elements.
 */
public fun <T> Set<T>.diff(other: Set<T>): Pair<Set<T>, Set<T>> =
    (other - this) to (this - other)