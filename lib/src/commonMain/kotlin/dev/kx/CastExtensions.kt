package dev.kx

/**
 * Safely casts this value to [T], returning `null` when the cast is not
 * possible, just like the `as?` operator.
 *
 * This is the default cast helper: the nullable result makes the failure case
 * explicit and keeps chains safe without an extra `as?` expression.
 *
 * ```kotlin
 * val name = response.body
 *     .asType<JsonObject>()
 *     ?.get("user")
 *     ?.asType<JsonObject>()
 *     ?.get("name")
 *     ?.asType<String>()
 * ```
 *
 * @return this value as [T], or `null` if it is not an instance of [T].
 */
public inline fun <reified T> Any?.asType(): T? = this as? T

/**
 * Casts this value to [T], just like the `as` operator.
 *
 * Unlike [asType], a failed cast throws instead of returning `null`. Use it
 * when the type is guaranteed by the surrounding code and you want to keep
 * the result non-null:
 *
 * ```kotlin
 * val user = response.body
 *     .asTypeNonNull<JsonObject>()
 *     .get("user")
 *     .asTypeNonNull<JsonObject>()
 * ```
 *
 * Mirroring `as`, a `null` receiver can only be cast to a nullable [T] when
 * [T] is declared nullable; when [T] is non-null this fails exactly like the
 * platform's `as` operator.
 *
 * @throws ClassCastException if this value is not an instance of [T].
 * @throws NullPointerException if this value is `null` and [T] is non-null on
 *   platforms where `as` reports that case as an NPE (for example the JVM).
 */
public inline fun <reified T> Any?.asTypeNonNull(): T = this as T