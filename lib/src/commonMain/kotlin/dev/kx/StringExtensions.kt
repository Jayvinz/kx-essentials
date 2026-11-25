package dev.kx

/**
 * Returns a copy of this string with the first letter of every word
 * uppercased and the remaining letters lowercased.
 *
 * Whitespace between words, including repeated spaces and line breaks, is
 * preserved as-is. This replaces the deprecated `String.capitalize()` for
 * the common "title case" use case, which used to only touch the first
 * character of the whole string.
 *
 * ```kotlin
 * "hello   kotlin WORLD".toTitleCase() // "Hello   Kotlin World"
 * ```
 *
 * @return this string in title case.
 */
public fun String.toTitleCase(): String = wordRegex.replace(this) { match ->
    match.value.lowercase().replaceFirstChar { it.uppercaseChar() }
}

private val wordRegex = Regex("\\S+")

/**
 * Shortens this string to at most [maxLength] characters, replacing the cut
 * part with [ellipsis].
 *
 * If the string already fits, it is returned unchanged. If [ellipsis] is
 * longer than the space left, no ellipsis is appended and the result is
 * simply cut to [maxLength], so the returned string never exceeds
 * [maxLength] characters.
 *
 * ```kotlin
 * "Kotlin extensions are fun".truncate(18)          // "Kotlin extensions…"
 * "Kotlin extensions are fun".truncate(12, "...")   // "Kotlin ext..."
 * ```
 *
 * @param maxLength the maximum number of characters in the result; must be non-negative.
 * @param ellipsis the text appended in place of the removed characters, `"…"` by default.
 * @return a shortened copy of this string, or `this` if it fits.
 * @throws IllegalArgumentException if [maxLength] is negative.
 */
public fun String.truncate(maxLength: Int, ellipsis: String = "…"): String {
    require(maxLength >= 0) { "maxLength must be non-negative, was $maxLength." }
    if (length <= maxLength) return this
    if (ellipsis.length >= maxLength) return take(maxLength)
    return take(maxLength - ellipsis.length) + ellipsis
}

/**
 * Returns `null` if this string is empty or contains only whitespace, or this
 * string unchanged otherwise.
 *
 * This is the nullable counterpart of [ifBlank] and pairs naturally with
 * [isNullOrBlank]:
 *
 * ```kotlin
 * form.name.nullIfBlank()
 *     .orThrow { ValidationError("name required") }
 *     .trim()
 * ```
 *
 * The string itself is not trimmed.
 *
 * @return this string if it is not blank, `null` otherwise.
 */
public fun String.nullIfBlank(): String? = if (isBlank()) null else this

/**
 * Splits this string at the first occurrence of [separator].
 *
 * The separator itself is not included in the result. If [separator] is not
 * found, returns `null`; unlike [split], the `Pair` makes the presence of the
 * separator explicit.
 *
 * An empty separator is treated as occurring at the start, so the result is
 * `"" to this`, matching [substringBefore] and [substringAfter].
 *
 * ```kotlin
 * "key=value".splitAtFirst('=') // ("key", "value")
 * "key=value".splitAtFirst(';') // null
 * ```
 *
 * @param separator the character to split at.
 * @return the text before and after the first separator, or `null` if absent.
 */
public fun String.splitAtFirst(separator: Char): Pair<String, String>? {
    val index = indexOf(separator)
    return if (index < 0) null else substring(0, index) to substring(index + 1)
}

/**
 * Splits this string at the first occurrence of [separator].
 *
 * The separator itself is not included in the result. If [separator] is not
 * found, returns `null`; unlike [split], the `Pair` makes the presence of the
 * separator explicit.
 *
 * An empty separator is treated as occurring at the start, so the result is
 * `"" to this`, matching [substringBefore] and [substringAfter].
 *
 * ```kotlin
 * "a::b::c".splitAtFirst("::") // ("a", "b::c")
 * ```
 *
 * @param separator the string to split at.
 * @return the text before and after the first separator, or `null` if absent.
 */
public fun String.splitAtFirst(separator: String): Pair<String, String>? {
    if (separator.isEmpty()) return "" to this
    val index = indexOf(separator)
    return if (index < 0) null else substring(0, index) to substring(index + separator.length)
}

/**
 * Splits this string at the last occurrence of [separator].
 *
 * The separator itself is not included in the result. If [separator] is not
 * found, returns `null`.
 *
 * An empty separator is treated as occurring at the end, so the result is
 * `this to ""`, matching [substringBeforeLast] and [substringAfterLast].
 *
 * ```kotlin
 * "a.b.c".splitAtLast('.') // ("a.b", "c")
 * ```
 *
 * @param separator the character to split at.
 * @return the text before and after the last separator, or `null` if absent.
 */
public fun String.splitAtLast(separator: Char): Pair<String, String>? {
    val index = lastIndexOf(separator)
    return if (index < 0) null else substring(0, index) to substring(index + 1)
}

/**
 * Splits this string at the last occurrence of [separator].
 *
 * The separator itself is not included in the result. If [separator] is not
 * found, returns `null`.
 *
 * An empty separator is treated as occurring at the end, so the result is
 * `this to ""`, matching [substringBeforeLast] and [substringAfterLast].
 *
 * ```kotlin
 * "a::b::c".splitAtLast("::") // ("a::b", "c")
 * ```
 *
 * @param separator the string to split at.
 * @return the text before and after the last separator, or `null` if absent.
 */
public fun String.splitAtLast(separator: String): Pair<String, String>? {
    if (separator.isEmpty()) return this to ""
    val index = lastIndexOf(separator)
    return if (index < 0) null else substring(0, index) to substring(index + separator.length)
}
