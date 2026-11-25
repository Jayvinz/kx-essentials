package dev.kx

/**
 * Returns this boolean, or `false` if it is `null`.
 *
 * The boolean counterpart of [String.orEmpty].
 *
 * ```kotlin
 * val enabled: Boolean? = null
 * enabled.orFalse() // false
 * ```
 */
public fun Boolean?.orFalse(): Boolean = this ?: false

/**
 * Returns this boolean, or `true` if it is `null`.
 *
 * The boolean counterpart of [String.orEmpty].
 *
 * ```kotlin
 * val enabled: Boolean? = null
 * enabled.orTrue() // true
 * ```
 */
public fun Boolean?.orTrue(): Boolean = this ?: true