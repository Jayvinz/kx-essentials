package dev.kx

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 *
 * ```kotlin
 * val retries: Int? = null
 * retries.orZero() // 0
 * ```
 */
public fun Byte?.orZero(): Byte = this ?: 0

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun Short?.orZero(): Short = this ?: 0

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 *
 * ```kotlin
 * val retries: Int? = null
 * retries.orZero() // 0
 * ```
 */
public fun Int?.orZero(): Int = this ?: 0

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun Long?.orZero(): Long = this ?: 0L

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun Float?.orZero(): Float = this ?: 0f

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 *
 * ```kotlin
 * val offset: Double? = null
 * offset.orZero() // 0.0
 * ```
 */
public fun Double?.orZero(): Double = this ?: 0.0

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun UByte?.orZero(): UByte = this ?: 0u.toUByte()

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun UShort?.orZero(): UShort = this ?: 0u.toUShort()

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun UInt?.orZero(): UInt = this ?: 0u

/**
 * Returns this number, or zero if it is `null`.
 *
 * The numeric counterpart of [String.orEmpty].
 */
public fun ULong?.orZero(): ULong = this ?: 0uL
