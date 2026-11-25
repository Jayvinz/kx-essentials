package dev.kx

/**
 * Returns the second element of this list.
 *
 * Mirrors [List.first] and [List.last] for the element that follows the first.
 *
 * @return the element at index `1`.
 * @throws NoSuchElementException if this list has fewer than two elements.
 */
public fun <T> List<T>.second(): T {
    if (size < 2) throw NoSuchElementException("List has no second element.")
    return this[1]
}

/**
 * Returns the second element of this list, or `null` if it has fewer than two elements.
 *
 * @return the element at index `1`, or `null`.
 */
public fun <T> List<T>.secondOrNull(): T? = getOrNull(1)

/**
 * Returns the element right before the last one, the counterpart of [List.last].
 *
 * @return the element at index `size - 2`.
 * @throws NoSuchElementException if this list has fewer than two elements.
 */
public fun <T> List<T>.secondLast(): T {
    if (size < 2) throw NoSuchElementException("List has no second-to-last element.")
    return this[size - 2]
}

/**
 * Returns the element right before the last one, or `null` if this list has
 * fewer than two elements.
 *
 * @return the element at index `size - 2`, or `null`.
 */
public fun <T> List<T>.secondLastOrNull(): T? = if (size < 2) null else this[size - 2]

/**
 * Swaps the elements at [indexA] and [indexB] in place.
 *
 * ```kotlin
 * val letters = mutableListOf("a", "b", "c")
 * letters.swap(0, 2) // ["c", "b", "a"]
 * ```
 *
 * @param indexA the index of the first element.
 * @param indexB the index of the second element.
 * @throws IndexOutOfBoundsException if either index is outside `0 until size`.
 */
public fun <T> MutableList<T>.swap(indexA: Int, indexB: Int) {
    val element = this[indexA]
    this[indexA] = this[indexB]
    this[indexB] = element
}

/**
 * Returns `true` if this iterable contains at least one of the given [elements].
 *
 * ```kotlin
 * listOf(1, 2, 3).containsAny(4, 2) // true
 * ```
 *
 * @param elements the elements to look for.
 * @return `true` if any of [elements] is contained in this iterable.
 */
public fun <T> Iterable<T>.containsAny(vararg elements: T): Boolean =
    elements.any { it in this }

/**
 * Returns `true` if this iterable contains all of the given [elements].
 *
 * Empty calls return `true`. Unlike [Collection.containsAll], this overload
 * accepts elements directly instead of another collection.
 *
 * ```kotlin
 * listOf(1, 2, 3).containsAll(1, 3) // true
 * ```
 *
 * @param elements the elements to look for.
 * @return `true` if every element is contained in this iterable.
 */
public fun <T> Iterable<T>.containsAll(vararg elements: T): Boolean =
    elements.all { it in this }

/**
 * Returns the element at [index], where negative indices count from the end of
 * the list.
 *
 * `at(-1)` is the last element and `at(-size)` is the first one. Indices do not
 * wrap around; anything outside `-size until size` is rejected.
 *
 * ```kotlin
 * listOf("a", "b", "c").at(-1) // "c"
 * ```
 *
 * @param index the index, or a negative offset from the end.
 * @return the element at the resolved index.
 * @throws IndexOutOfBoundsException if [index] is outside `-size until size`.
 */
public fun <T> List<T>.at(index: Int): T = this[resolveIndex(index, size)]

/**
 * Returns the element at [index], where negative indices count from the end of
 * the list, or `null` if the resolved index is out of bounds.
 *
 * ```kotlin
 * listOf("a", "b", "c").atOrNull(-1) // "c"
 * listOf("a", "b", "c").atOrNull(3)  // null
 * ```
 *
 * @param index the index, or a negative offset from the end.
 * @return the element at the resolved index, or `null` if out of bounds.
 */
public fun <T> List<T>.atOrNull(index: Int): T? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/**
 * Returns `true` if this iterable and [other] have at least one element in common.
 *
 * This is the non-allocating relationship check for the same idea as
 * [Iterable.intersect]: it stops at the first match and does not build the
 * intersection set.
 *
 * If either side is a [Set], membership checks are O(1). Otherwise a hash set
 * is built from [other], giving O(n + m) average time. Duplicate elements do
 * not affect the result. Empty iterables never overlap.
 *
 * ```kotlin
 * setOf(1, 2, 3) overlaps setOf(3, 4) // true
 * setOf(1, 2) overlaps setOf(3, 4)    // false
 * ```
 *
 * For infinite iterables this function may not terminate when there is no
 * overlap.
 *
 * @param other the elements to compare with.
 * @return `true` if the two iterables share at least one element.
 */
public infix fun <T> Iterable<T>.overlaps(other: Iterable<T>): Boolean {
    if (this is Collection && isEmpty()) return false
    if (other is Collection && other.isEmpty()) return false

    if (this is Set && other is Set) {
        return if (size <= other.size) {
            any { it in other }
        } else {
            other.any { it in this }
        }
    }
    if (this is Set) return other.any { it in this }
    if (other is Set) return any { it in other }

    val otherSet = other.toHashSet()
    return any { it in otherSet }
}
