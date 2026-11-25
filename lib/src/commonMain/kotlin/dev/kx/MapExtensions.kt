package dev.kx

/**
 * Returns a new map with keys and values exchanged.
 *
 * If several entries share the same value, the last entry in iteration order
 * wins because the duplicate key overwrites earlier ones.
 *
 * ```kotlin
 * mapOf("a" to 1, "b" to 2).inverse() // {1=a, 2=b}
 * ```
 *
 * @return a map where every value of this map becomes a key pointing to the
 *   key it was associated with.
 */
public fun <K, V> Map<K, V>.inverse(): Map<V, K> {
    val result = LinkedHashMap<V, K>(size)
    for ((key, value) in this) {
        result[value] = key
    }
    return result
}

/**
 * Returns a map containing only the entries whose values are not `null`.
 *
 * The returned values are smart-cast to the non-null type, making this a
 * concise alternative to `filterValues { it != null }.mapValues { it.value!! }`.
 *
 * ```kotlin
 * mapOf("a" to 1, "b" to null).filterValuesNotNull() // {a=1}
 * ```
 *
 * @return a map with the same keys and the non-null values of this map.
 */
public fun <K, V : Any> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    val result = LinkedHashMap<K, V>(size)
    for ((key, value) in this) {
        if (value != null) result[key] = value
    }
    return result
}
/**
 * Returns the non-null value for [key], distinguishing a missing key from a key
 * whose value is `null`.
 *
 * The standard [Map.getValue] keeps the value type of the map, so a
 * `Map<K, V?>` still returns `V?` and cannot tell the two failure cases apart.
 * This function is intentionally limited to nullable-value maps; for
 * `Map<K, V>` use the standard `getValue`.
 *
 * ```kotlin
 * val config = mapOf("port" to 8080, "host" to null)
 * config.requireValue("port") // 8080
 * ```
 *
 * @param key the key to look up.
 * @return the non-null value associated with [key].
 * @throws NoSuchElementException if [key] is missing or its value is `null`.
 */
public fun <K, V : Any> Map<K, V?>.requireValue(key: K): V {
    if (!containsKey(key)) {
        throw NoSuchElementException("Key \"$key\" is missing.")
    }
    return get(key) ?: throw NoSuchElementException(
        "Key \"$key\" is present but its value is null.",
    )
}

/**
 * Returns a new map containing this map merged with [other].
 *
 * Keys only present in one map are copied as-is. Keys present in both maps are
 * combined with [combine], which receives the existing value from this map and
 * the incoming value from [other], in that order.
 *
 * The result preserves this map's iteration order, followed by keys that are
 * only present in [other]. Neither input map is modified. [combine] is invoked
 * once per duplicate key and is not invoked for keys that appear only once.
 *
 * ```kotlin
 * val counts = mapOf("a" to 1)
 *     .mergeWith(mapOf("a" to 2, "b" to 1)) { old, new -> old + new }
 * // {a=3, b=1}
 * ```
 *
 * @param other the map to merge into this one.
 * @param combine combines a value from this map with a value for the same key
 *   in [other].
 * @return a new map with merged entries.
 */
public fun <K, V> Map<K, V>.mergeWith(
    other: Map<K, V>,
    combine: (existing: V, incoming: V) -> V,
): Map<K, V> {
    val result = LinkedHashMap<K, V>(size + other.size)
    result.putAll(this)
    for ((key, value) in other) {
        if (result.containsKey(key)) {
            result[key] = combine(result.getValue(key), value)
        } else {
            result[key] = value
        }
    }
    return result
}
