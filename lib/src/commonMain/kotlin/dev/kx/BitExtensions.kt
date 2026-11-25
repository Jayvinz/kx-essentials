package dev.kx

/**
 * Returns `true` when every bit of [flag] is set in this value.
 *
 * A multi-bit mask checks all of its bits; use [hasAnyFlag] when only one bit
 * needs to be set. A zero [flag] is trivially considered set.
 *
 * ```kotlin
 * val permissions = 0b0101
 * permissions hasFlag 0b0100 // true
 * permissions hasFlag 0b0010 // false
 * ```
 *
 * @param flag the bit or mask to test.
 */
public infix fun Int.hasFlag(flag: Int): Boolean = (this and flag) == flag

/**
 * Returns `true` when at least one bit of [flags] is set in this value.
 *
 * ```kotlin
 * val permissions = 0b0101
 * permissions hasAnyFlag 0b0110 // true
 * permissions hasAnyFlag 0b0010 // false
 * ```
 *
 * @param flags the bits or mask to test.
 */
public infix fun Int.hasAnyFlag(flags: Int): Boolean = (this and flags) != 0

/**
 * Returns this value with every bit of [flag] set.
 *
 * ```kotlin
 * 0b0001 withFlag 0b0010 // 0b0011
 * ```
 *
 * @param flag the bit or mask to set.
 */
public infix fun Int.withFlag(flag: Int): Int = this or flag

/**
 * Returns this value with every bit of [flag] cleared.
 *
 * ```kotlin
 * 0b0111 withoutFlag 0b0010 // 0b0101
 * ```
 *
 * @param flag the bit or mask to clear.
 */
public infix fun Int.withoutFlag(flag: Int): Int = this and flag.inv()

/**
 * Returns this value with every bit of [flag] toggled.
 *
 * ```kotlin
 * 0b0101 toggleFlag 0b0011 // 0b0110
 * ```
 *
 * @param flag the bit or mask to toggle.
 */
public infix fun Int.toggleFlag(flag: Int): Int = this xor flag

/**
 * Returns `true` when every bit of [flag] is set in this value.
 *
 * A multi-bit mask checks all of its bits; use [hasAnyFlag] when only one bit
 * needs to be set. A zero [flag] is trivially considered set.
 *
 * @param flag the bit or mask to test.
 */
public infix fun Long.hasFlag(flag: Long): Boolean = (this and flag) == flag

/**
 * Returns `true` when at least one bit of [flags] is set in this value.
 *
 * @param flags the bits or mask to test.
 */
public infix fun Long.hasAnyFlag(flags: Long): Boolean = (this and flags) != 0L

/**
 * Returns this value with every bit of [flag] set.
 *
 * @param flag the bit or mask to set.
 */
public infix fun Long.withFlag(flag: Long): Long = this or flag

/**
 * Returns this value with every bit of [flag] cleared.
 *
 * @param flag the bit or mask to clear.
 */
public infix fun Long.withoutFlag(flag: Long): Long = this and flag.inv()

/**
 * Returns this value with every bit of [flag] toggled.
 *
 * @param flag the bit or mask to toggle.
 */
public infix fun Long.toggleFlag(flag: Long): Long = this xor flag

/**
 * Returns the unsigned binary representation of this `Int`.
 *
 * Negative values use the 32-bit two's-complement representation, matching
 * Java's `Integer.toBinaryString`. There is no prefix and no leading zero.
 *
 * ```kotlin
 * 1.toBinaryString()      // "1"
 * (-1).toBinaryString()   // "11111111111111111111111111111111"
 * ```
 */
public fun Int.toBinaryString(): String = toUInt().toString(2)

/**
 * Returns the unsigned hexadecimal representation of this `Int`.
 *
 * Negative values use the 32-bit two's-complement representation, matching
 * Java's `Integer.toHexString`. The result is lowercase and has no prefix or
 * leading zero.
 *
 * ```kotlin
 * 0x12345678.toHexString()  // "12345678"
 * (-1).toHexString()        // "ffffffff"
 * ```
 */
public fun Int.toHexString(): String = toUInt().toString(16)

/**
 * Returns the unsigned octal representation of this `Int`.
 *
 * Negative values use the 32-bit two's-complement representation, matching
 * Java's `Integer.toOctalString`. The result has no prefix or leading zero.
 */
public fun Int.toOctalString(): String = toUInt().toString(8)

/**
 * Returns the unsigned binary representation of this `Long`.
 *
 * Negative values use the 64-bit two's-complement representation, matching
 * Java's `Long.toBinaryString`. There is no prefix and no leading zero.
 */
public fun Long.toBinaryString(): String = toULong().toString(2)

/**
 * Returns the unsigned hexadecimal representation of this `Long`.
 *
 * Negative values use the 64-bit two's-complement representation, matching
 * Java's `Long.toHexString`. The result is lowercase and has no prefix or
 * leading zero.
 */
public fun Long.toHexString(): String = toULong().toString(16)

/**
 * Returns the unsigned octal representation of this `Long`.
 *
 * Negative values use the 64-bit two's-complement representation, matching
 * Java's `Long.toOctalString`. The result has no prefix or leading zero.
 */
public fun Long.toOctalString(): String = toULong().toString(8)