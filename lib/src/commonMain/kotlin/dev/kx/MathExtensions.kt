package dev.kx

import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Rounding direction used by [Double.roundTo], [Float.roundTo],
 * [Double.roundToString], and [Float.roundToString].
 */
public enum class RoundingMode {
    /** Rounds toward positive infinity. */
    CEILING,

    /** Rounds toward negative infinity. */
    FLOOR,

    /** Rounds to the nearest value, with ties away from zero. */
    HALF_UP,
}

/**
 * Converts this angle from radians to degrees.
 *
 * The conversion is the standard `radians * 180 / PI`. `NaN` and infinities
 * propagate unchanged, and `-0.0` keeps its sign.
 *
 * ```kotlin
 * PI.toDegrees()          // 180.0
 * (PI / 2).toDegrees()    // 90.0
 * ```
 *
 * @return this angle in degrees.
 */
public fun Double.toDegrees(): Double = this * (180.0 / PI)

/**
 * Converts this angle from degrees to radians.
 *
 * The conversion is the standard `degrees * PI / 180`. `NaN` and infinities
 * propagate unchanged, and `-0.0` keeps its sign.
 *
 * ```kotlin
 * 180.0.toRadians() // PI
 * 90.0.toRadians()  // PI / 2
 * ```
 *
 * @return this angle in radians.
 */
public fun Double.toRadians(): Double = this * (PI / 180.0)

/**
 * Converts this angle from radians to degrees.
 *
 * The calculation is performed in `Double` and converted back to `Float`, so
 * the result has normal `Float` precision. `NaN` and infinities propagate.
 *
 * @return this angle in degrees.
 */
public fun Float.toDegrees(): Float = (toDouble() * (180.0 / PI)).toFloat()

/**
 * Converts this angle from degrees to radians.
 *
 * The calculation is performed in `Double` and converted back to `Float`, so
 * the result has normal `Float` precision. `NaN` and infinities propagate.
 *
 * @return this angle in radians.
 */
public fun Float.toRadians(): Float = (toDouble() * (PI / 180.0)).toFloat()

/**
 * Rounds this value to the given number of decimal places using
 * [RoundingMode.HALF_UP].
 *
 * Kotlin only ships [kotlin.math.round], which rounds to a whole number;
 * this rounds to an arbitrary precision while staying in common code.
 * Ties are rounded away from zero: `2.5.roundTo(0)` is `3.0` and
 * `(-2.5).roundTo(0)` is `-3.0`.
 *
 * ```kotlin
 * 3.14159.roundTo(2) // 3.14
 * 2.5.roundTo(0)     // 3.0
 * ```
 *
 * @param decimals the number of decimal places to keep; must be non-negative.
 * @return this value rounded to [decimals] decimal places.
 * @throws IllegalArgumentException if [decimals] is negative.
 */
public fun Double.roundTo(decimals: Int): Double =
    roundTo(decimals, RoundingMode.HALF_UP)

/**
 * Rounds this value to the given number of decimal places using [roundingMode].
 *
 * ```kotlin
 * 2.341.roundTo(2, RoundingMode.CEILING) // 2.35
 * 2.341.roundTo(2, RoundingMode.FLOOR)   // 2.34
 * ```
 *
 * If the intermediate scaling overflows, this value is returned unchanged.
 *
 * @param decimals the number of decimal places to keep; must be non-negative.
 * @param roundingMode the rounding direction.
 * @return this value rounded to [decimals] decimal places.
 * @throws IllegalArgumentException if [decimals] is negative.
 */
public fun Double.roundTo(decimals: Int, roundingMode: RoundingMode): Double {
    require(decimals >= 0) { "decimals must be non-negative, was $decimals." }
    var factor = 1.0
    repeat(decimals) { factor *= 10.0 }
    val scaled = this * factor
    if (!scaled.isFinite()) return this

    return when (roundingMode) {
        RoundingMode.CEILING -> ceil(scaled) / factor
        RoundingMode.FLOOR -> floor(scaled) / factor
        RoundingMode.HALF_UP ->
            // Round half away from zero, unlike kotlin.math.round which rounds half to even.
            if (scaled >= 0) floor(scaled + 0.5) / factor else ceil(scaled - 0.5) / factor
    }
}

/**
 * Rounds this value to the given number of decimal places using
 * [RoundingMode.HALF_UP].
 *
 * ```kotlin
 * 3.14159f.roundTo(2) // 3.14f
 * ```
 *
 * @param decimals the number of decimal places to keep; must be non-negative.
 * @return this value rounded to [decimals] decimal places.
 * @throws IllegalArgumentException if [decimals] is negative.
 */
public fun Float.roundTo(decimals: Int): Float =
    roundTo(decimals, RoundingMode.HALF_UP)

/**
 * Rounds this value to the given number of decimal places using [roundingMode].
 *
 * @param decimals the number of decimal places to keep; must be non-negative.
 * @param roundingMode the rounding direction.
 * @return this value rounded to [decimals] decimal places.
 * @throws IllegalArgumentException if [decimals] is negative.
 */
public fun Float.roundTo(decimals: Int, roundingMode: RoundingMode): Float =
    toDouble().roundTo(decimals, roundingMode).toFloat()

private const val MAX_ROUND_TO_STRING_DECIMALS: Int = 15

/**
 * Rounds this value to [decimals] decimal places and formats it with exactly
 * that many digits after the decimal point.
 *
 * Unlike [roundTo], which returns a numeric value and therefore drops trailing
 * zeros, this method preserves them:
 *
 * ```kotlin
 * 3.1.roundToString(2)     // "3.10"
 * 3.14159.roundToString(2) // "3.14"
 * (-2.5).roundToString(0)  // "-3"
 * ```
 *
 * Rounding direction is controlled by [roundingMode]:
 * [RoundingMode.CEILING] rounds toward positive infinity,
 * [RoundingMode.FLOOR] rounds toward negative infinity, and
 * [RoundingMode.HALF_UP] rounds to the nearest value with ties away from zero.
 *
 * Special values are formatted as `"NaN"`, `"Infinity"`, and `"-Infinity"`.
 * A negative value that rounds to zero keeps its sign, for example
 * `(-0.001).roundToString(2) == "-0.00"`.
 *
 * This is fixed-point formatting, not a locale-aware number formatter. The
 * binary representation of `Double` still applies: `1.005.roundToString(2)`
 * may produce `"1.00"` rather than `"1.01"`.
 *
 * @param decimals the number of digits after the decimal point, in `0..15`.
 * @param roundingMode the rounding direction; defaults to [RoundingMode.HALF_UP].
 * @return a fixed-point string with exactly [decimals] fractional digits.
 * @throws IllegalArgumentException if [decimals] is outside `0..15`.
 */
public fun Double.roundToString(
    decimals: Int,
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
): String {
    require(decimals in 0..MAX_ROUND_TO_STRING_DECIMALS) {
        "decimals must be between 0 and $MAX_ROUND_TO_STRING_DECIMALS, was $decimals."
    }
    if (isNaN()) return "NaN"
    if (isInfinite()) return if (this > 0) "Infinity" else "-Infinity"

    val rounded = roundTo(decimals, roundingMode)
    val safe = if (isFinite() && !rounded.isFinite()) this else rounded

    if (safe == 0.0) {
        val negative = this < 0.0 || (this == 0.0 && 1.0 / this < 0.0)
        return formatZero(decimals, negative)
    }
    return formatFixed(safe, decimals)
}

/**
 * Rounds this value to [decimals] decimal places and formats it with exactly
 * that many digits after the decimal point.
 *
 * ```kotlin
 * 3.1f.roundToString(2) // "3.10"
 * ```
 *
 * See [Double.roundToString] for rounding and special-value semantics.
 *
 * @param decimals the number of digits after the decimal point, in `0..15`.
 * @param roundingMode the rounding direction; defaults to [RoundingMode.HALF_UP].
 * @return a fixed-point string with exactly [decimals] fractional digits.
 * @throws IllegalArgumentException if [decimals] is outside `0..15`.
 */
public fun Float.roundToString(
    decimals: Int,
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
): String = toDouble().roundToString(decimals, roundingMode)

private fun formatZero(decimals: Int, negative: Boolean): String {
    val body = if (decimals == 0) "0" else "0." + "0".repeat(decimals)
    return if (negative) "-$body" else body
}

private fun formatFixed(value: Double, decimals: Int): String {
    val text = value.toString()
    val negative = text.startsWith('-')
    val unsigned = if (negative) text.substring(1) else text
    val exponentIndex = unsigned.indexOfFirst { it == 'e' || it == 'E' }
    val mantissa = if (exponentIndex >= 0) unsigned.substring(0, exponentIndex) else unsigned
    val exponent = if (exponentIndex >= 0) unsigned.substring(exponentIndex + 1).toInt() else 0
    val dotIndex = mantissa.indexOf('.')
    val digits = mantissa.replace(".", "")
    val decimalPosition = (if (dotIndex >= 0) dotIndex else mantissa.length) + exponent

    var integer: String
    var fraction: String
    when {
        decimalPosition <= 0 -> {
            integer = "0"
            fraction = "0".repeat(-decimalPosition) + digits
        }
        decimalPosition >= digits.length -> {
            integer = digits + "0".repeat(decimalPosition - digits.length)
            fraction = ""
        }
        else -> {
            integer = digits.substring(0, decimalPosition)
            fraction = digits.substring(decimalPosition)
        }
    }

    integer = integer.trimStart('0').ifEmpty { "0" }
    fraction = if (fraction.length >= decimals) {
        fraction.substring(0, decimals)
    } else {
        fraction + "0".repeat(decimals - fraction.length)
    }

    val body = if (decimals == 0) integer else "$integer.$fraction"
    return if (negative) "-$body" else body
}