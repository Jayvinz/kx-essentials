package dev.kx

/**
 * Returns this value if it is not `null`, or throws the exception produced by
 * [exception].
 *
 * The lambda is only invoked when this value is `null`, so the exception is
 * not created on the happy path. This keeps null checks inside a chain:
 *
 * ```kotlin
 * val user = repository.find(id)
 *     .orThrow { NotFoundException(id) }
 *     .profile
 * ```
 *
 * @param exception produces the exception to throw when this value is `null`.
 * @return this value.
 * @throws Throwable the exception produced by [exception] if this value is `null`.
 */
public inline fun <T : Any> T?.orThrow(exception: () -> Throwable): T =
    this ?: throw exception()
