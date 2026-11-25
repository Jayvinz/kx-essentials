package dev.kx

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty], which only covers
 * object arrays; `IntArray`, `BooleanArray` and the other primitive arrays
 * are not `Array` subtypes.
 */
public fun BooleanArray?.orEmpty(): BooleanArray = this ?: BooleanArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun ByteArray?.orEmpty(): ByteArray = this ?: ByteArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun CharArray?.orEmpty(): CharArray = this ?: CharArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun ShortArray?.orEmpty(): ShortArray = this ?: ShortArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 *
 * ```kotlin
 * val pixels: IntArray? = null
 * pixels.orEmpty().size // 0
 * ```
 */
public fun IntArray?.orEmpty(): IntArray = this ?: IntArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun LongArray?.orEmpty(): LongArray = this ?: LongArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun FloatArray?.orEmpty(): FloatArray = this ?: FloatArray(0)

/**
 * Returns this array, or an empty array if it is `null`.
 *
 * The primitive array counterpart of [Array.orEmpty].
 */
public fun DoubleArray?.orEmpty(): DoubleArray = this ?: DoubleArray(0)
/**
 * Returns the element at [index], where negative indices count from the end of
 * the array.
 *
 * `at(-1)` is the last element and `at(-size)` is the first one. Indices do not
 * wrap around.
 *
 * ```kotlin
 * arrayOf("a", "b", "c").at(-1) // "c"
 * ```
 *
 * @param index the index, or a negative offset from the end.
 * @return the element at the resolved index.
 * @throws IndexOutOfBoundsException if [index] is outside `-size until size`.
 */
public fun <T> Array<out T>.at(index: Int): T = this[resolveIndex(index, size)]

/**
 * Returns the element at [index], where negative indices count from the end of
 * the array, or `null` if the resolved index is out of bounds.
 *
 * @param index the index, or a negative offset from the end.
 * @return the element at the resolved index, or `null` if out of bounds.
 */
public fun <T> Array<out T>.atOrNull(index: Int): T? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun BooleanArray.at(index: Int): Boolean = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun BooleanArray.atOrNull(index: Int): Boolean? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun ByteArray.at(index: Int): Byte = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun ByteArray.atOrNull(index: Int): Byte? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun CharArray.at(index: Int): Char = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun CharArray.atOrNull(index: Int): Char? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun ShortArray.at(index: Int): Short = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun ShortArray.atOrNull(index: Int): Short? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun IntArray.at(index: Int): Int = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun IntArray.atOrNull(index: Int): Int? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun LongArray.at(index: Int): Long = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun LongArray.atOrNull(index: Int): Long? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun FloatArray.at(index: Int): Float = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun FloatArray.atOrNull(index: Int): Float? =
    resolveIndexOrNull(index, size)?.let { this[it] }

/** Returns the element at [index], counting negative indices from the end. */
public fun DoubleArray.at(index: Int): Double = this[resolveIndex(index, size)]

/** Returns the element at [index] or `null` when the resolved index is out of bounds. */
public fun DoubleArray.atOrNull(index: Int): Double? =
    resolveIndexOrNull(index, size)?.let { this[it] }
