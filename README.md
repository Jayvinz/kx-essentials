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

### Imports

The package is `dev.kx`. Import functions explicitly or use the package wildcard:

```kotlin
import dev.kx.second
import dev.kx.toTitleCase

println(listOf("a", "b").second()) // b
```

## Quick start

```kotlin
"hello   kotlin WORLD".toTitleCase()        // "Hello   Kotlin World"
"Kotlin extensions are fun".truncate(18)    // "Kotlin extensions…"
listOf(1, 2, 3, 4).second()                 // 2
listOf("a", "b", "c").at(-1)                // "c"
mapOf("a" to 1, "b" to 2).inverse()         // {1=a, 2=b}
3.14159.roundTo(2)                          // 3.14
(-1).toHexString()                          // "ffffffff"
```

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
