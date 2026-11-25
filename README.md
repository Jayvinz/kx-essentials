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

The examples below mirror the demo app (`demo/src/main/kotlin/dev/kx/demo/Demo.kt`).

### Nullable values

```kotlin
import dev.kx.*

val missingCount: Int? = null
missingCount.orZero()                       // 0
missingCount.orDefault(10)                  // 10

val missingName: String? = null
missingName.orDefault("anonymous")          // "anonymous"
missingName.fallback { "anonymous" }        // "anonymous"

val presentName: String? = "Ada"
presentName.orThrow { IllegalStateException() }.uppercase() // "ADA"
```

`orZero` and `orDefault` keep the value inside a chain; `?:` works, but quickly
adds parentheses when the fallback is followed by another call. `orDefault`
evaluates its argument eagerly. Use `fallback { ... }` when the default is
expensive or should only run on the null path. `orThrow` is for the opposite
contract: fail fast without writing `?: throw ...`.

### Indices and casts

```kotlin
val names = listOf("a", "b", "c")
names.at(-1)          // "c"
names.atOrNull(5)     // null

val payload: Any = listOf("a", "b")
payload.asType<List<String>>()?.second() // "b"
(1 as Any).asType<String>()              // null
payload.isType<List<String>>()           // true
```

`at` gives negative indexing one explicit contract: `-1` is the last element,
`-size` the first, and out-of-range values do not wrap. `asType` is the safe-cast
counterpart of `as`: it returns `null` and keeps the chain alive. `isType` uses a
reified type parameter, so it can express `List<String>` where `is List<String>`
is impossible because of erasure; only the erased `List` is checked at runtime.
Use `asTypeNonNull` only when the type is guaranteed.

### Decimal numbers

```kotlin
3.14159.roundTo(2)                       // 3.14
3.1.roundToString(2)                     // "3.10"
(-0.001).roundToString(2)                // "-0.00"
2.341.roundTo(2, RoundingMode.CEILING)   // 2.35
2.341.roundTo(2, RoundingMode.FLOOR)     // 2.34
```

`kotlin.math.round` only rounds to a whole number, and `String.format` is not
available in common code. `roundTo` keeps a numeric result; `roundToString`
keeps trailing zeros and a fixed scale, which is what display and serialization
code usually needs. The explicit `RoundingMode` avoids depending on platform
defaults.

### Lazy values

```kotlin
val config = fastLazy { "loaded" }
val session = safeLazy { "safe" }
```

`lazy` defaults to a synchronized implementation. `fastLazy` makes the
no-synchronization choice explicit for values initialized from one thread; it
can run the initializer more than once under a concurrent first access. Use
`safeLazy` when the value may be shared across threads, so the safety choice is
visible at the declaration.

### Text and maps

```kotlin
"key=value".splitAtFirst('=') // ("key", "value")
"key=value".splitAtFirst(';') // null

mapOf("a" to 1).mergeWith(mapOf("a" to 2, "b" to 3)) { old, new -> old + new } // {a=3, b=3}
```

`split` returns a list even when exactly one separator is expected;
`splitAtFirst` returns a `Pair` or `null`, so the missing-separator case is
handled by the type. `Map.plus` overwrites duplicate keys silently; `mergeWith`
makes the conflict policy explicit.

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
