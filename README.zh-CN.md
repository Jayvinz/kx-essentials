# Kx Essentials

**像 Kotlin 原生 API 一样顺手的扩展库。**

English: [README.md](README.md)

Kx Essentials 是一个零依赖的 Kotlin Multiplatform 扩展库，覆盖字符串、集合、Map、数字、Result 和位操作。命名与异常语义对齐标准库，不引入新的类型体系，也不是框架。

- **多平台**：`lib` 是 Kotlin Multiplatform 模块，同一份 `commonMain` 代码编译到 **Android / JVM / Kotlin-JS / iOS**。
- **零依赖**：只用标准库，不引入 AndroidX 或第三方库。
- **显式 API**：开启 `explicitApi()`，所有公开声明都有明确类型和 KDoc。

## 依赖接入

发布坐标：

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

Gradle 会根据消费方的 target 自动选择 AAR、JAR、JS klib 或 iOS klib。

### 纯 Android / JVM

```kotlin
dependencies {
    implementation("top.jayvin.dev:kx-essentials:0.1.0")
}
```

### 平台说明

| 平台 | 说明 |
| --- | --- |
| Android | AAR，要求 minSdk 21 |
| JVM | JAR，JVM 17 |
| Kotlin/JS | klib，支持 Node 和 browser 配置 |
| iOS | iosArm64、iosSimulatorArm64、iosX64；KMP 消费方直接走 Maven |

iOS target 只在 macOS 上构建；消费 iOS 产物的 KMP 项目不需要本机是 macOS，但如果要自行编译源码则需要 macOS 和 Xcode。

产物使用 Kotlin 2.0.21 编译，Kotlin 2.0 及以上的消费方均可使用；更新的 Kotlin 编译器可以读取旧版本产物。

## 快速开始

以下示例取自 demo（`demo/src/main/kotlin/dev/kx/demo/Demo.kt`）。

### 可空值

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

`orZero` / `orDefault` 的价值不是替代单个 `?:`，而是让后续调用继续留在链上；否则一旦
fallback 后面还要接方法，就得多加括号。`orDefault` 的参数会立即求值；默认值昂贵或只应
在 null 时计算时用 `fallback { ... }`。`orThrow` 则表达相反的契约：快速失败，不需要写
`?: throw ...`。

### 负索引与类型转换

```kotlin
val names = listOf("a", "b", "c")
names.at(-1)          // "c"
names.atOrNull(5)     // null

val payload: Any = listOf("a", "b")
payload.asType<List<String>>()?.second() // "b"
(1 as Any).asType<String>()              // null
payload.isType<List<String>>()           // true
```

`at` 把负索引收敛成一条明确契约：`-1` 是最后一个、`-size` 是第一个，越界不回绕。
`asType` 是 `as` 的安全版本：失败返回 `null`，不会打断链。`isType` 使用 reified 类型
参数，因此可以写 `List<String>`；`is List<String>` 因类型擦除无法写出来，而运行时也只
能检查擦除后的 `List`。类型已经确定时再用 `asTypeNonNull`。

### 小数

```kotlin
3.14159.roundTo(2)                       // 3.14
3.1.roundToString(2)                     // "3.10"
(-0.001).roundToString(2)                // "-0.00"
2.341.roundTo(2, RoundingMode.CEILING)   // 2.35
2.341.roundTo(2, RoundingMode.FLOOR)     // 2.34
```

`kotlin.math.round` 只能取整，`String.format` 在 common 代码里不可用。`roundTo` 返回数值；
`roundToString` 保留末尾零和固定小数位，适合展示与序列化。显式 `RoundingMode` 避免依赖
平台默认行为。

### Lazy

```kotlin
val config = fastLazy { "loaded" }
val session = safeLazy { "safe" }
```

`lazy` 默认是带同步的实现。`fastLazy` 把“不需要同步”的选择写在调用处，适合单线程或已有
外部锁的初始化；并发首次访问时 initializer 可能执行多次。值可能跨线程共享时用
`safeLazy`，让线程安全的选择在声明处就可见。

### 字符串与 Map

```kotlin
"key=value".splitAtFirst('=') // ("key", "value")
"key=value".splitAtFirst(';') // null

mapOf("a" to 1).mergeWith(mapOf("a" to 2, "b" to 3)) { old, new -> old + new } // {a=3, b=3}
```

`split` 即使只期望一个分隔符也返回 List；`splitAtFirst` 返回 `Pair` 或 `null`，让“分隔符
不存在”成为类型的一部分。`Map.plus` 会静默覆盖重复 key；`mergeWith` 把冲突策略显式写在
调用处。

## API 一览

### 命名约定

- `orX()`：null 时给出替代结果，例如 `orZero`、`orFalse`、`orDefault`、`orThrow`
- `xOrNull()`：把失败/缺失显式变成 `null`，例如 `secondOrNull`
- `requireX()`：失败立即抛异常，并给出可诊断的消息，例如 `requireValue`
- 负索引：`-1` 表示末尾、`-size` 表示首位，不回绕；越界时抛异常或返回 `null`

### 字符串

| API | 说明 |
| --- | --- |
| `String.toTitleCase()` | 每个单词首字母大写、其余小写，保留原始空白；替代已废弃的 `capitalize()` |
| `String.truncate(maxLength, ellipsis = "…")` | 截断到指定长度，用省略号替代被截掉的部分 |
| `String.nullIfBlank()` | 空串或全空白转 `null`，与 `isNullOrBlank` / `ifBlank` 对称 |
| `String.splitAtFirst(separator)` | 在第一次出现处分隔，返回 `(before, after)`；未找到返回 `null`，不包含分隔符 |
| `String.splitAtLast(separator)` | 在最后一次出现处分隔，返回 `(before, after)`；未找到返回 `null`，不包含分隔符 |

### 集合

| API | 说明 |
| --- | --- |
| `List<T>.second()` / `secondOrNull()` | 第二个元素，语义对齐 `first()` / `firstOrNull()` |
| `List<T>.secondLast()` / `secondLastOrNull()` | 倒数第二个元素 |
| `MutableList<T>.swap(indexA, indexB)` | 原地交换两个元素 |
| `Iterable<T>.containsAny(vararg elements)` | 是否包含任意一个给定元素 |
| `Iterable<T>.containsAll(vararg elements)` | 是否包含全部给定元素 |
| `List<T>.at(index)` / `atOrNull(index)` | 支持负索引：`-1` 为最后一个，`-size` 为第一个；不回绕 |
| `Iterable<T>.overlaps(other)` | 是否有交集，不构造交集 `Set`；空集合返回 `false` |
| `Set<T>.diff(other)` | 返回 `(added, removed)`：新增元素和移除元素，保持各自顺序 |

`splitAtFirst` / `splitAtLast` 的空分隔符行为与 `substringBefore/After` 一致：前者结果是 `("" to this)`，后者是 `(this to "")`。

标准库已经提供 `union` / `intersect` / `subtract`（infix，返回 `Set`），本库不重复实现；`overlaps` 只补“是否有交集”的关系判断，`diff` 补新增/移除差量。

### 数组

| API | 说明 |
| --- | --- |
| `Array<T>.at(index)` / `atOrNull(index)` | 对象数组的负索引访问 |
| `IntArray` / `LongArray` / `ShortArray` / `ByteArray` / `CharArray` / `BooleanArray` / `FloatArray` / `DoubleArray` 的 `at` / `atOrNull` | 基本类型数组的负索引访问 |

### Map

| API | 说明 |
| --- | --- |
| `Map<K, V>.inverse()` | 键值互换，重复 value 时保留最后一个 |
| `Map<K, V?>.filterValuesNotNull()` | 过滤掉 null value，结果类型为 `Map<K, V>` |
| `Map<K, V?>.requireValue(key)` | 取非空值，并区分「键缺失」与「值为 null」 |
| `Map<K, V>.mergeWith(other, combine)` | 合并两个 Map，重复 key 用 combine 决定结果；不修改输入 |

### 数字

| API | 说明 |
| --- | --- |
| `Double/Float.roundTo(decimals)` | 保留指定小数位并返回数值，默认 `HALF_UP` |
| `Double/Float.roundTo(decimals, roundingMode)` | 指定舍入方向返回数值，支持 `CEILING` / `FLOOR` / `HALF_UP` |
| `Double/Float.roundToString(decimals, roundingMode = RoundingMode.HALF_UP)` | 保留指定小数位并返回固定小数位字符串，例如 `3.1.roundToString(2) == "3.10"` |

```kotlin
3.1.roundToString(2)     // "3.10"
3.14159.roundToString(2) // "3.14"
(-2.5).roundToString(0)  // "-3"

2.341.roundToString(2, RoundingMode.CEILING) // "2.35"
2.341.roundToString(2, RoundingMode.FLOOR)   // "2.34"

2.341.roundTo(2, RoundingMode.CEILING) // 2.35
2.341.roundTo(2, RoundingMode.FLOOR)   // 2.34
```

`RoundingMode` 的语义：

| 值 | 行为 |
| --- | --- |
| `CEILING` | 向正无穷方向舍入，即向上取整 |
| `FLOOR` | 向负无穷方向舍入，即向下取整 |
| `HALF_UP` | 四舍五入；恰好一半时远离零，默认值 |

`roundTo` 的单参数版本等价于 `roundTo(decimals, RoundingMode.HALF_UP)`；数值版本与字符串版本共用同一套舍入语义。

`roundToString` 是固定小数点格式，不是 locale 感知的格式化；`decimals` 范围为 `0..15`，超过会抛 `IllegalArgumentException`。它仍受二进制浮点表示影响（例如 `1.005.roundToString(2)` 可能得到 `"1.00"`）。

### 位运算

| API | 说明 |
| --- | --- |
| `Int/Long.hasFlag(flag)` | 指定 mask 的所有位都已设置；零 mask 视为已设置 |
| `Int/Long.hasAnyFlag(flags)` | 指定 mask 至少有一位已设置 |
| `Int/Long.withFlag(flag)` | 置位，返回新值 |
| `Int/Long.withoutFlag(flag)` | 清位，返回新值 |
| `Int/Long.toggleFlag(flag)` | 翻转位，返回新值 |
| `Int/Long.toBinaryString()` | 无符号二进制表示，负数按 32/64 位补码 |
| `Int/Long.toHexString()` | 无符号小写十六进制表示，与 Java 的 `Integer/Long.toHexString` 一致 |
| `Int/Long.toOctalString()` | 无符号八进制表示 |

```kotlin
val read = 0b0001
val write = 0b0010

read hasFlag read                  // true
read hasAnyFlag (read or write)    // true
read withFlag write                // 0b0011
(read withFlag write) withoutFlag read // 0b0010
(read withFlag write) toggleFlag write // 0b0001

(-1).toHexString()                 // "ffffffff"
Int.MIN_VALUE.toBinaryString()     // "1000...0000"
```

注意：`(-1).toString(16)` 的结果是 `"-1"`；需要 Java 风格的无符号表示时使用 `toHexString()`。

#### 从 Java 迁移

Kotlin 的位运算不能通过扩展新增 `&`、`|`、`^`、`~`、`>>>` 运算符，对应写法是标准库的 infix 函数（`Long` 使用同名函数）：

| Java | Kotlin |
| --- | --- |
| `a & b` | `a and b` |
| `a \| b` | `a or b` |
| `a ^ b` | `a xor b` |
| `~a` | `a.inv()` |
| `a << n` | `a shl n` |
| `a >> n` | `a shr n` |
| `a >>> n` | `a ushr n` |
| `Integer.bitCount(a)` | `a.countOneBits()` |
| `Integer.highestOneBit(a)` | `a.takeHighestOneBit()` |
| `Integer.lowestOneBit(a)` | `a.takeLowestOneBit()` |
| `Integer.numberOfLeadingZeros(a)` | `a.countLeadingZeroBits()` |
| `Integer.numberOfTrailingZeros(a)` | `a.countTrailingZeroBits()` |
| `Integer.rotateLeft(a, n)` | `a.rotateLeft(n)` |
| `Integer.rotateRight(a, n)` | `a.rotateRight(n)` |
| `Integer.toBinaryString(a)` | `a.toBinaryString()` |
| `Integer.toHexString(a)` | `a.toHexString()` |
| `Integer.toOctalString(a)` | `a.toOctalString()` |

`Byte` / `Short` 的 `and`、`or`、`xor`、`inv` 位于 `kotlin.experimental`，需要显式导入，例如 `import kotlin.experimental.and`。本库不重复提供这些别名。

### 数学

| API | 说明 |
| --- | --- |
| `Double/Float.toDegrees()` | 弧度转角度 |
| `Double/Float.toRadians()` | 角度转弧度 |

```kotlin
PI.toDegrees()        // 180.0
(PI / 2).toDegrees()  // 90.0
180.0.toRadians()     // PI
90f.toRadians()       // PI / 2
```

`NaN` 和无穷大原样传播；`-0.0` 保留符号。`Float` 版本内部用 `Double` 计算后转回 `Float`。

### Pair

| API | 说明 |
| --- | --- |
| `Pair<A, B>.swap()` | 交换两个元素 |

### 可空默认值

标准库已提供 `String?`、`List?`、`Set?`、`Collection?`、`Map?`、`Sequence?`、`Array<T>?` 的 `orEmpty()`，这里只补真正缺失的部分：

| API | 说明 |
| --- | --- |
| `Byte?/Short?/Int?/Long?/Float?/Double?.orZero()` | null 时返回对应类型的零 |
| `UByte?/UShort?/UInt?/ULong?.orZero()` | 无符号数值的零值 |
| `BooleanArray?` … `DoubleArray?.orEmpty()` | 基本类型数组的空数组，标准库只覆盖了 `Array<T>?` |
| `T?.orDefault(value)` | null 时返回默认值，默认值立即求值 |
| `T?.fallback { value }` | null 时才调用 lambda 计算默认值 |
| `T?.orThrow { exception }` | null 时抛出 lambda 产生的异常，非空时直接返回，保持链式 |
| `Boolean?.orFalse()` / `orTrue()` | null 时返回 `false` / `true` |

`orDefault` / `fallback` 的 `T` 也可以是可空类型：当默认值或 lambda 返回可空值时，`T` 会推断为对应的可空类型，结果保持可空。

`?:` 在长链中继续调用时需要加括号，例如 `(user?.name ?: "anonymous").uppercase()`。`orDefault` / `fallback` 可以直接接在链尾：

```kotlin
val displayName = user?.profile?.name.orDefault("anonymous").uppercase()
val timeout = config?.timeout.fallback { computeTimeout() }
```

> `Iterable<T>?` 和 `CharSequence?` 的 `orEmpty()` 刻意没有加入：Kotlin 的导入优先级会让宽接收者扩展遮蔽标准库对 `List?`、`Set?`、`String?` 等更具体类型的重载，导致类型从 `List` 退化成 `Iterable`。无符号数组（`UIntArray?` 等）和 `Duration?.orZero()` 使用场景较少，暂不扩充。

### Result 互操作

只桥接标准库 `kotlin.Result`，不引入自有 `Result`/`Either` 类型。领域错误建模请使用 kotlin-result 或 Arrow；决策记录见 [ADR-0001](docs/adr/0001-result-strategy.md)。

| API | 说明 |
| --- | --- |
| `T?.toResultOr { error }` | 非空转 `Result.success`，null 转 `Result.failure`，lambda 只在 null 时调用 |
| `Iterable<Result<T>>.combine()` | 全部成功合并为 `Result<List<T>>`，首个失败短路 |
| `Iterable<Result<T>>.partitionResults()` | 消费全部结果，拆成成功列表与异常列表 |

```kotlin
val user = repo.find(id)
    .toResultOr { NotFoundException(id) }
    .map(::toDto)

val (values, errors) = rows.map(::validate).partitionResults()
```

标准库已有 `Result.recover` / `recoverCatching`，本库不重复提供。

### 类型转换

| API | 说明 |
| --- | --- |
| `Any?.asType<T>()` | 默认安全转换，等价于 `as? T`，失败时返回 `null` |
| `Any?.asTypeNonNull<T>()` | 等价于 `as T`，失败时抛异常，不额外引入可空性 |
| `Any?.isType<T>()` | 判断是否为 `T`，带 contract，可在 `if` 中触发智能转换 |

两者都是 `inline` + `reified`，类型检查在运行期真正执行。默认返回 `T?`，链式调用继续用 `?.`：

```kotlin
val name = payload
    .asType<Map<String, List<String>>>()
    ?.getValue("items")
    ?.asType<List<String>>()
    ?.second()
```

只有明确确定类型时才使用 `asTypeNonNull`。它的异常行为与 `as` 一致：类型不匹配抛 `ClassCastException`；`null` 转为非空类型时按平台行为失败（JVM 为 `NullPointerException`，Kotlin/JS 为 `ClassCastException`）。

`isType` 的 contract 让接收者在 true 分支自动智能转换：

```kotlin
if (payload.isType<Map<String, Any>>()) {
    payload["id"] // payload 被智能转换为 Map<String, Any>
}
```

泛型只检查擦除后的运行时类型：`isType<List<String>>()` 和 `isType<List<Int>>()` 都只检查 `List`。

### Lazy：fast 与 safe

| API | 说明 |
| --- | --- |
| `fastLazy(initializer)` | 无锁，等价于 `lazy(LazyThreadSafetyMode.NONE)`，仅用于单线程或已有外部同步的场景 |
| `safeLazy(initializer)` | 加锁，等价于默认的 `lazy`，多线程初始化安全 |

```kotlin
// 明确无竞态：去掉同步开销
val config by fastLazy { loadConfig() }

// 可能被多个线程首次访问：显式选择安全实现
val session by safeLazy { createSession() }
```

标准库已经占用了 `lazy` 这个名字，库内再定义同名的 `lazy` 会在 `import dev.kx.*` 时静默遮蔽标准库版本，把已有的 `by lazy {}` 变成无锁实现，属于破坏性行为（与之前 `orEmpty` 的问题同类）。因此这里使用正面命名：轻量版是 `fastLazy`，需要锁时用 `safeLazy`。

`fastLazy` 首次访问仍只初始化一次，`isInitialized` 语义不变；并发首次访问可能重复执行 initializer，仅在明确无竞态时使用。

## 示例 App

`demo` 是一个不依赖 AndroidX 的原生 Android Activity，把每个扩展函数的真实返回值渲染到屏幕上。

```bash
./gradlew :demo:installDebug
```

或只构建 APK：

```bash
./gradlew :demo:assembleDebug
# demo/build/outputs/apk/debug/demo-debug.apk
```

## 测试

```bash
./gradlew :lib:jvmTest   # JVM
./gradlew :lib:jsTest    # Node.js（首次运行会下载 Node 与 npm 依赖）
./gradlew :lib:allTests  # 所有平台
```

测试位于 `lib/src/commonTest`，同一套用例会在所有目标平台上运行。

## 环境

- Gradle 8.10.2（Wrapper 已包含）
- Kotlin 2.0.21（产物兼容 Kotlin 2.0+）
- AGP 8.7.3
- Android `compileSdk 35` / `minSdk 21`，示例 App `minSdk 24`
- JVM 17

## 许可证

Apache License 2.0，见 [LICENSE](LICENSE)。
