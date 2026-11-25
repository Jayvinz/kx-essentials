# Kx Essentials

**Kotlin extensions that feel native.**

English · [中文](README.zh-CN.md) · `0.1.0` · Kotlin 2.0+ · JDK 17

Kx Essentials is a zero-dependency Kotlin Multiplatform library of focused extensions for strings, collections, maps, numbers, results, and bits. It follows the naming, argument order, and exception semantics of the Kotlin standard library, adds no new type system, and is not a framework.

- **Multiplatform**: one `commonMain` source set for Android, JVM, Kotlin/JS, and iOS.
- **Zero runtime dependencies**: only the Kotlin standard library.
- **Explicit API**: public declarations have explicit types and KDoc.
- **Chain-friendly**: nullable fallbacks, results, and updates stay inside the expression.

## Platform support

| Target | Artifact |
| --- | --- |
| Android | AAR, `minSdk 21` |
| JVM | JAR, JVM 17 |
| Kotlin/JS | klib, Node and browser configurations |
| iOS arm64 / simulator arm64 / x64 | klib; `Kx.framework` for Swift integration |

Apple targets are built on macOS. Consumers of the published iOS klibs do not need a Mac, but building the source locally on iOS does require macOS and Xcode.

## Installation

The library is published to Maven Central under:

```kotlin
top.jayvin.dev:kx-essentials:0.1.0
```

### Kotlin Multiplatform

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// shared/build.gradle.kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("top.jayvin.dev:kx-essentials:0.1.0")
        }
    }
}
```

Gradle resolves the correct AAR, JAR, JS klib, or iOS klib for each target.

### Android or JVM only

```kotlin
dependencies {
    implementation("top.jayvin.dev:kx-essentials:0.1.0")
}
```

## Quick start

The examples below are based on the demo app (`demo/src/main/kotlin/dev/kx/demo/Demo.kt`) and cover every extension family.

### Nullable values

```kotlin
import dev.kx.*

val retries: Int? = null
val ratio: Double? = null
retries.orZero() // 0
ratio.orZero()   // 0.0

val enabled: Boolean? = null
enabled.orFalse() // false
enabled.orTrue()  // true

val pixels: IntArray? = null
pixels.orEmpty().size // 0
```

`orZero` exists for every numeric receiver (`Byte?`, `Short?`, `Int?`, `Long?`,
`Float?`, `Double?`, `UByte?`, `UShort?`, `UInt?`, `ULong?`). `orEmpty` covers
the eight primitive array types because the standard `Array.orEmpty` does not
apply to `IntArray`, `BooleanArray`, and their peers. `orFalse` / `orTrue` are
the boolean counterparts. These are small, but they remove `?:` from the middle
of an expression and keep the receiver's type.

### Defaults and failures

```kotlin
val name: String? = null
name.orDefault("anonymous")    // "anonymous"
name.fallback { "anonymous" }  // "anonymous"

val present: String? = "Ada"
present.orThrow { IllegalStateException("missing") }.uppercase() // "ADA"
```

`orDefault` is the eager fallback: its argument is evaluated even when the
receiver is non-null. `fallback` takes a lambda and evaluates it only on the
null path. `orThrow` is the fail-fast counterpart: it returns a non-null value
or throws, without forcing `?: throw ...` into the chain. Use it when a missing
value is a programming error; use `toResultOr` when it is expected data.

### Lists and indices

```kotlin
val numbers = listOf(10, 20, 30, 40)
numbers.second()           // 20
numbers.secondOrNull()     // 20
numbers.secondLast()       // 30
numbers.secondLastOrNull() // 30
listOf(1).secondOrNull()   // null

numbers.at(-1)      // 40
numbers.atOrNull(99) // null

val mutable = mutableListOf("a", "b", "c")
mutable.swap(0, 2)  // [c, b, a]

listOf(1, 2, 3).containsAny(4, 2) // true
listOf(1, 2, 3).containsAll(1, 3) // true
```

`second` / `secondLast` complete the `first` / `last` family; the `OrNull`
variants avoid exceptions when the list is short. `at` gives negative indexing
one explicit contract: `-1` is the last element, `-size` the first, and
out-of-range values do not wrap. `containsAny` / `containsAll` accept elements
directly instead of requiring a temporary collection. `swap` is the missing
in-place primitive for `MutableList`.

### Arrays

```kotlin
arrayOf("a", "b").at(-1)            // "b"
intArrayOf(1, 2, 3).at(-2)          // 2
doubleArrayOf(1.0, 2.0).atOrNull(5) // null
```

`at` / `atOrNull` are overloaded for `Array<out T>` and all eight primitive
array types: `BooleanArray`, `ByteArray`, `CharArray`, `ShortArray`, `IntArray`,
`LongArray`, `FloatArray`, and `DoubleArray`. They share the list contract;
primitive arrays need their own overloads because they are not `Array`
subtypes.

### Sets and maps

```kotlin
setOf(1, 2, 3) overlaps setOf(3, 4) // true: both contain 3
setOf(1, 2) overlaps emptySet<Int>() // false

setOf(1, 2).diff(setOf(2, 3))       // added=[3], removed=[1]

mapOf("a" to 1, "b" to 2).inverse() // {1=a, 2=b}
mapOf("a" to 1, "b" to null).filterValuesNotNull() // {a=1}

val config: Map<String, Int?> = mapOf("port" to 8080, "timeout" to null)
config.requireValue("port") // 8080

mapOf("a" to 1)
    .mergeWith(mapOf("a" to 2, "b" to 3)) { old, new -> old + new } // {a=3, b=3}
```

`overlaps` answers the relationship question without constructing an
intersection set. `diff` returns both directions (`added`, `removed`), while
the standard `union` / `subtract` calls express only one side. `inverse` has no
standard equivalent; the last entry wins when values collide. `filterValuesNotNull`
removes null values and smart-casts the remaining values, avoiding `!!`.
`requireValue` distinguishes a missing key from a key whose value is `null`,
which `Map.getValue` cannot do for `Map<K, V?>`. `mergeWith` makes the
duplicate-key policy explicit; `Map.plus` silently overwrites.

### Strings

```kotlin
"hello   kotlin WORLD".toTitleCase() // "Hello   Kotlin World"
"Kotlin extensions are fun".truncate(18) // "Kotlin extensions…"
"   ".nullIfBlank() // null

"key=value".splitAtFirst('=')  // ("key", "value")
"a::b::c".splitAtFirst("::")   // ("a", "b::c")
"a.b.c".splitAtLast('.')       // ("a.b", "c")
"a::b::c".splitAtLast("::")    // ("a::b", "c")
```

`toTitleCase` is the common replacement for the deprecated `capitalize`, but it
preserves internal whitespace. `truncate` is safe in common code and never
returns a string longer than `maxLength`. `nullIfBlank` is the nullable
counterpart of `ifBlank`. `splitAtFirst` / `splitAtLast` return `Pair?` so the
missing-separator case is represented by the type instead of an empty list or
an index check.

### Casting and type checks

```kotlin
val payload: Any = listOf("a", "b")

payload.asType<List<String>>()?.second() // "b"
(1 as Any).asType<String>()              // null
payload.asTypeNonNull<List<String>>().second() // "b"
payload.isType<List<String>>()           // true
```

`asType` is the chain-friendly counterpart of `as`: a failed cast returns
`null` instead of throwing. `asTypeNonNull` keeps the non-null result when the
type is already guaranteed. `isType` uses a reified type parameter, so it can
spell `List<String>` even though `is List<String>` is impossible because of
erasure; the runtime check still sees only the erased `List`. Its contract also
smart-casts the receiver in the `true` branch.

### Numbers, angles, and rounding

```kotlin
PI.toDegrees()           // 180.0
(PI / 2).toDegrees()     // 90.0
180.0.toRadians()        // PI
PI.toFloat().toDegrees() // 180.0f
90f.toRadians()          // 1.5707964f

3.14159.roundTo(2)      // 3.14
3.14159f.roundTo(2)     // 3.14f
2.341.roundTo(2, RoundingMode.CEILING) // 2.35
2.341.roundTo(2, RoundingMode.FLOOR)   // 2.34
2.341f.roundTo(2, RoundingMode.FLOOR)  // 2.34f

3.1.roundToString(2)    // "3.10"
3.1f.roundToString(2)   // "3.10"
2.341.roundToString(2, RoundingMode.CEILING) // "2.35"
2.341f.roundToString(2, RoundingMode.FLOOR)  // "2.34"
```

`toDegrees` / `toRadians` handle both `Double` and `Float` without duplicating
the conversion at call sites. `kotlin.math.round` only rounds to a whole
number, and `String.format` is not available in common code. `roundTo` returns
a numeric value and drops trailing zeros; `roundToString` keeps a fixed scale
and trailing zeros, which is what display and serialization code needs.
`RoundingMode` has `HALF_UP` (the default), `CEILING`, and `FLOOR`.

### Bit flags and formatting

```kotlin
val read = 0b0001
val write = 0b0010
read hasFlag 0b0001                 // true
read hasAnyFlag 0b0011              // true
read withFlag write                 // 0b0011
(read withFlag write) withoutFlag read // 0b0010
(read withFlag write) toggleFlag write // 0b0001

1L hasFlag 1L                       // true
1L hasAnyFlag 2L                    // false
1L withFlag 2L                      // 3L
3L withoutFlag 1L                   // 2L
3L toggleFlag 1L                    // 2L

1.toBinaryString()                  // "1" (no 0b prefix)
(-1).toHexString()                  // "ffffffff" (no 0x prefix)
8.toOctalString()                   // "10" (no octal prefix)
1L.toBinaryString()                 // "1"
(-1L).toHexString()                 // "ffffffffffffffff"
8L.toOctalString()                  // "10"
```

The `hasFlag` / `hasAnyFlag` / `withFlag` / `withoutFlag` / `toggleFlag` set
exists for both `Int` and `Long`. Kotlin cannot define custom `&`, `|`, or `^`
operators for these, so named operations make permission masks readable. The
formatting functions use unsigned two's-complement output, matching Java's
`Integer` / `Long` helpers; there is no common equivalent in Kotlin. They return
Java-style digits without a `0b`, `0x`, or octal prefix, because they mirror
`Integer.toBinaryString` / `toHexString` / `toOctalString`. If you need a
Kotlin-style literal, prepend the prefix at the call site; Kotlin has no octal
integer literal syntax.

### Results

```kotlin
val parsed: String? = "42"
parsed.toResultOr { IllegalArgumentException("missing") }
    .map(String::toInt) // Success(42)

val missing: String? = null
missing.toResultOr { IllegalArgumentException("missing") } // Failure(...)

listOf(Result.success(1), Result.success(2)).combine() // Success([1, 2])

val mixed = listOf(
    Result.success(1),
    Result.failure<Int>(IllegalStateException("bad")),
)
mixed.combine()          // Failure(...)
mixed.partitionResults() // values=[1], errors=[IllegalStateException("bad")]
```

`toResultOr` is the value-track counterpart of `orThrow`: it keeps the standard
`Result` instead of introducing a custom `Either` type. `combine` short-circuits
on the first failure; `partitionResults` consumes everything and keeps every
error, which is useful for batch validation.

### Lazy values

```kotlin
val config by fastLazy { "loaded" }
val session by safeLazy { "safe" }
```

Use `by`, not `=`: both functions return a `Lazy<T>` delegate. `lazy` defaults
to a synchronized implementation. `fastLazy` exposes the no-synchronization
choice for values initialized from one thread; a concurrent first access may
run the initializer more than once. `safeLazy` keeps the synchronized
guarantee. The explicit names make the trade-off visible at the declaration.

### Pairs

```kotlin
("key" to 1).swap() // (1, "key")
```

`Pair` has no standard `swap`; this extension keeps the operation on the value
instead of forcing destructuring and reconstruction.

### Overload coverage

The examples above cover these families; the receiver list is complete:

| API family | Receivers / variants |
| --- | --- |
| `orZero` | `Byte?`, `Short?`, `Int?`, `Long?`, `Float?`, `Double?`, `UByte?`, `UShort?`, `UInt?`, `ULong?` |
| `orEmpty` | `BooleanArray?`, `ByteArray?`, `CharArray?`, `ShortArray?`, `IntArray?`, `LongArray?`, `FloatArray?`, `DoubleArray?` |
| `at` / `atOrNull` | `List<T>`, `Array<out T>`, `BooleanArray`, `ByteArray`, `CharArray`, `ShortArray`, `IntArray`, `LongArray`, `FloatArray`, `DoubleArray` |
| `hasFlag`, `hasAnyFlag`, `withFlag`, `withoutFlag`, `toggleFlag` | `Int`, `Long` |
| `toDegrees`, `toRadians` | `Double`, `Float` |
| `roundTo`, `roundToString` | `Double`, `Float` |
| `splitAtFirst`, `splitAtLast` | `Char`, `String` |
## API highlights

### Text

| API | Description |
| --- | --- |
| `String.toTitleCase()` | Title-case every word while preserving whitespace |
| `String.truncate(maxLength, ellipsis)` | Truncate with an ellipsis |
| `String.nullIfBlank()` | Turn blank strings into `null` |
| `String.splitAtFirst(separator)` / `splitAtLast(separator)` | Split at the first/last occurrence, returning `(before, after)` |

### Collections

| API | Description |
| --- | --- |
| `List.second()` / `secondLast()` | Ordinal access mirrored from `first()` / `last()` |
| `List.at(index)` / `atOrNull(index)` | Negative indexing without wraparound |
| `Iterable.overlaps(other)` | Check whether two iterables share an element |
| `Set.diff(other)` | Compute `(added, removed)` |
| `MutableList.swap(a, b)` | Swap two elements in place |

Kotlin already provides `union`, `intersect`, and `subtract`; Kx Essentials does not duplicate them.

### Maps and results

| API | Description |
| --- | --- |
| `Map.inverse()` | Swap keys and values |
| `Map.mergeWith(other, combine)` | Merge maps with a conflict resolver |
| `Map<K, V?>.requireValue(key)` | Distinguish a missing key from a `null` value |
| `T?.toResultOr { error }` | Convert nullable values to `Result` |
| `Iterable<Result<T>>.combine()` | Combine results, short-circuiting on the first failure |

### Numbers and bits

| API | Description |
| --- | --- |
| `Double/Float.roundTo(decimals, mode)` | Round to decimal places with `HALF_UP`, `CEILING`, or `FLOOR` |
| `Double/Float.roundToString(decimals, mode)` | Fixed-point string with trailing zeros |
| `Double/Float.toDegrees()` / `toRadians()` | Angle conversion |
| `Int/Long.hasFlag(flag)` / `withFlag` / `withoutFlag` | Readable bit masks |
| `Int/Long.toBinaryString()` / `toHexString()` | Java-compatible unsigned formatting |

## Compatibility

- Kotlin 2.0 and later 2.x releases
- Published with Kotlin 2.0.21 so JVM metadata and KLIB ABI remain consumable by Kotlin 2.0+
- JDK 17 for JVM and Android builds
- Android `minSdk 21`
- Gradle 8.10.2

## License

Apache License 2.0. See [LICENSE](LICENSE).
