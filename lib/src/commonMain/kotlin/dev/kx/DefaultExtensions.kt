package dev.kx

/**
 * Returns this value if it is not `null`, or [defaultValue] otherwise.
 *
 * The fallback is evaluated eagerly. For a fallback that should only be
 * computed when needed, use [fallback].
 *
 * If [defaultValue] is nullable, `T` is inferred as that nullable type and the
 * result stays nullable as well.
 *
 * Unlike the elvis operator, this keeps long chains linear: the fallback stays
 * inside the chain and does not require wrapping the receiver in parentheses.
 *
 * ```kotlin
 * val displayName = user?.profile?.name.orDefault("anonymous").uppercase()
 * ```
 *
 * @param defaultValue the value returned when this value is `null`.
 * @return this value or [defaultValue].
 */
public fun <T> T?.orDefault(defaultValue: T): T = this ?: defaultValue

/**
 * Returns this value if it is not `null`, or the result of [defaultValue]
 * otherwise.
 *
 * [defaultValue] is only invoked when this value is `null`, which makes this
 * the right choice for expensive fallbacks:
 *
 * ```kotlin
 * val timeout = config?.timeout.fallback { computeTimeout() }
 * ```
 *
 * If the lambda returns a nullable value, `T` is inferred as nullable and the
 * result stays nullable as well.
 *
 * This has its own name instead of being an overload of [orDefault] because a
 * `T` parameter and a `() -> T` parameter become ambiguous when `T` is itself
 * a function type.
 *
 * @param defaultValue the function invoked to compute the fallback.
 * @return this value or the result of [defaultValue].
 */
public inline fun <T> T?.fallback(defaultValue: () -> T): T = this ?: defaultValue()