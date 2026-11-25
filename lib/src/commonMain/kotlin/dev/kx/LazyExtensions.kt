package dev.kx

/**
 * Creates a [Lazy] that initializes its value on first access without any
 * synchronization, equivalent to `lazy(LazyThreadSafetyMode.NONE, initializer)`.
 *
 * Use it when the property is only touched from one thread, or when its
 * initialization is already protected by an external lock:
 *
 * ```kotlin
 * val config by fastLazy { loadConfig() }
 * ```
 *
 * Initialization happens at most once for normal single-threaded access and
 * [Lazy.isInitialized] keeps working. This mode is not thread-safe: concurrent
 * first access may run the initializer more than once, so never use it for
 * state shared between threads without external synchronization. Use
 * [safeLazy] for those cases.
 *
 * @param initializer the function invoked on first access to compute the value.
 * @return a lazy delegate backed by [LazyThreadSafetyMode.NONE].
 */
public fun <T> fastLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Creates a thread-safe [Lazy] that takes a lock while initializing,
 * equivalent to the standard `lazy(initializer)`:
 * `lazy(LazyThreadSafetyMode.SYNCHRONIZED, initializer)`.
 *
 * This is the safe counterpart of [fastLazy]. Prefer it whenever the property
 * may be initialized from multiple threads:
 *
 * ```kotlin
 * val session by safeLazy { createSession() }
 * ```
 *
 * @param initializer the function invoked on first access to compute the value.
 * @return a lazy delegate backed by [LazyThreadSafetyMode.SYNCHRONIZED].
 */
public fun <T> safeLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.SYNCHRONIZED, initializer)