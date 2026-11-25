package dev.kx

/**
 * Resolves a possibly negative index against [size], or returns `null` when it
 * is out of bounds. Negative indices count from the end, without wrap-around.
 */
internal fun resolveIndexOrNull(index: Int, size: Int): Int? {
    val actual = if (index < 0) size + index else index
    return if (actual in 0 until size) actual else null
}

/**
 * Resolves a possibly negative index against [size].
 *
 * @throws IndexOutOfBoundsException if the resolved index is out of bounds.
 */
internal fun resolveIndex(index: Int, size: Int): Int {
    val actual = if (index < 0) size + index else index
    if (actual !in 0 until size) {
        throw IndexOutOfBoundsException("Index $index is out of bounds for size $size.")
    }
    return actual
}