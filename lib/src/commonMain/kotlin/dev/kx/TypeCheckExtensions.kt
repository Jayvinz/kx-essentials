package dev.kx

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Returns `true` if this value is an instance of [T].
 *
 * The check is reified and carries a contract, so the receiver is smart-cast
 * inside the `true` branch:
 *
 * ```kotlin
 * if (payload.isType<JsonObject>()) {
 *     println(payload["id"]) // payload: JsonObject
 * }
 * ```
 *
 * For generic types only the erased runtime class can be checked, so
 * `isType<List<String>>()` and `isType<List<Int>>()` both test for `List`.
 * Use [asType] when you need the cast value itself.
 *
 * @return `true` if this value is an instance of [T], `false` otherwise.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <reified T> Any?.isType(): Boolean {
    contract { returns(true) implies (this@isType is T) }
    return this is T
}
