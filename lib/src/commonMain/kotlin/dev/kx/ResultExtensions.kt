package dev.kx

/**
 * Converts this nullable value into a [Result].
 *
 * A non-null value becomes [Result.success]; a `null` value becomes
 * [Result.failure] with the throwable produced by [error]. The lambda is only
 * invoked when this value is `null`, so no exception is created on the happy
 * path.
 *
 * ```kotlin
 * repository.find(id)
 *     .toResultOr { NotFoundException(id) }
 *     .map(::toDto)
 * ```
 *
 * This is the value-track counterpart of [orThrow] and deliberately returns
 * the standard [Result] instead of introducing a custom `Either` type.
 *
 * @param error produces the failure when this value is `null`.
 * @return a successful result containing this value, or a failure.
 */
public inline fun <T : Any> T?.toResultOr(error: () -> Throwable): Result<T> =
    if (this == null) Result.failure(error()) else Result.success(this)

/**
 * Combines an iterable of [Result] values into a single [Result] containing all
 * successful values.
 *
 * The first failure short-circuits the iteration and is returned as the result
 * failure; later elements are not consumed.
 *
 * ```kotlin
 * items.map(::parse).combine().onFailure(::log)
 * ```
 *
 * An empty iterable produces `Result.success(emptyList())`.
 *
 * @return a success with all values in iteration order, or the first failure.
 */
public fun <T> Iterable<Result<T>>.combine(): Result<List<T>> {
    val values = mutableListOf<T>()
    for (result in this) {
        val error = result.exceptionOrNull()
        if (error != null) return Result.failure(error)
        values += result.getOrThrow()
    }
    return Result.success(values)
}

/**
 * Splits an iterable of [Result] values into successful values and failures.
 *
 * Unlike [combine], this function consumes the whole iterable and keeps every
 * failure, which makes it suitable for batch validation and reporting.
 *
 * ```kotlin
 * val (valid, errors) = rows.map(::validate).partitionResults()
 * ```
 *
 * Iteration order is preserved inside both lists. An empty iterable produces
 * a pair of empty lists.
 *
 * @return a pair of the successful values and the failure throwables.
 */
public fun <T> Iterable<Result<T>>.partitionResults(): Pair<List<T>, List<Throwable>> {
    val values = mutableListOf<T>()
    val errors = mutableListOf<Throwable>()
    for (result in this) {
        val error = result.exceptionOrNull()
        if (error != null) {
            errors += error
        } else {
            values += result.getOrThrow()
        }
    }
    return values to errors
}
