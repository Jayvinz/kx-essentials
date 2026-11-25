package dev.kx.demo

import dev.kx.asType
import dev.kx.asTypeNonNull
import dev.kx.at
import dev.kx.atOrNull
import dev.kx.combine
import dev.kx.diff
import dev.kx.containsAll
import dev.kx.containsAny
import dev.kx.fallback
import dev.kx.filterValuesNotNull
import dev.kx.hasAnyFlag
import dev.kx.hasFlag
import dev.kx.inverse
import dev.kx.mergeWith
import dev.kx.isType
import dev.kx.nullIfBlank
import dev.kx.orDefault
import dev.kx.overlaps
import dev.kx.orThrow
import dev.kx.orEmpty
import dev.kx.orFalse
import dev.kx.orZero
import dev.kx.partitionResults
import dev.kx.requireValue
import dev.kx.RoundingMode
import dev.kx.roundTo
import dev.kx.roundToString
import dev.kx.splitAtFirst
import dev.kx.splitAtLast
import dev.kx.second
import dev.kx.secondLast
import dev.kx.swap
import dev.kx.toggleFlag
import dev.kx.withFlag
import dev.kx.withoutFlag
import dev.kx.toBinaryString
import dev.kx.toDegrees
import dev.kx.toHexString
import dev.kx.toOctalString
import dev.kx.toRadians
import dev.kx.toResultOr
import dev.kx.toTitleCase
import dev.kx.fastLazy
import dev.kx.safeLazy
import dev.kx.truncate
import kotlin.math.PI

/**
 * Collects the examples shown by the demo app. Everything is evaluated at
 * runtime so the screen reflects the actual library output.
 */
internal object Demo {

    fun report(): String = buildString {
        append("Kx demo\n")
        append("Every value below is produced by dev.kx at runtime.\n\n")

        section("Strings")
        example(
            "\"hello   kotlin WORLD\".toTitleCase()",
            "hello   kotlin WORLD".toTitleCase(),
        )
        example(
            "\"Kotlin extensions are fun\".truncate(18)",
            "Kotlin extensions are fun".truncate(18),
        )
        val blankName = "   "
        example(
            "blankName.nullIfBlank().orDefault(\"anonymous\")",
            blankName.nullIfBlank().orDefault("anonymous"),
        )
        example(
            "\"key=value\".splitAtFirst('=')",
            "key=value".splitAtFirst('='),
        )
        example(
            "\"a.b.c\".splitAtLast('.')",
            "a.b.c".splitAtLast('.'),
        )
        example(
            "\"key=value\".splitAtFirst(';')",
            "key=value".splitAtFirst(';'),
        )

        section("Lists")
        example("listOf(1, 2, 3, 4).second()", listOf(1, 2, 3, 4).second())
        example("listOf(1, 2, 3, 4).secondLast()", listOf(1, 2, 3, 4).secondLast())
        example("listOf(1, 2, 3).containsAny(4, 2)", listOf(1, 2, 3).containsAny(4, 2))
        example("listOf(1, 2, 3).containsAll(1, 3)", listOf(1, 2, 3).containsAll(1, 3))
        example(
            "listOf(\"a\", \"b\", \"c\").at(-1)",
            listOf("a", "b", "c").at(-1),
        )
        example(
            "listOf(\"a\", \"b\").atOrNull(5)",
            listOf("a", "b").atOrNull(5),
        )
        example(
            "mutableListOf(1, 2, 3).apply { swap(0, 2) }",
            mutableListOf(1, 2, 3).apply { swap(0, 2) },
        )

        section("Arrays")
        example(
            "intArrayOf(1, 2, 3).at(-2)",
            intArrayOf(1, 2, 3).at(-2),
        )
        example(
            "arrayOf(\"a\", \"b\").atOrNull(5)",
            arrayOf("a", "b").atOrNull(5),
        )

        section("Sets")
        example(
            "setOf(1, 2, 3) overlaps setOf(3, 4)",
            setOf(1, 2, 3) overlaps setOf(3, 4),
        )
        example(
            "setOf(1, 2).diff(setOf(2, 3))",
            setOf(1, 2).diff(setOf(2, 3)),
        )

        section("Maps")
        example(
            "mapOf(\"a\" to 1, \"b\" to 2).inverse()",
            mapOf("a" to 1, "b" to 2).inverse(),
        )
        example(
            "mapOf(\"a\" to 1, \"b\" to null).filterValuesNotNull()",
            mapOf("a" to 1, "b" to null).filterValuesNotNull(),
        )
        val appConfig: Map<String, Int?> = mapOf("port" to 8080, "timeout" to null)
        example("appConfig.requireValue(\"port\")", appConfig.requireValue("port"))
        example(
            "mapOf(\"a\" to 1).mergeWith(mapOf(\"a\" to 2, \"b\" to 3)) { old, new -> old + new }",
            mapOf("a" to 1).mergeWith(mapOf("a" to 2, "b" to 3)) { old, new -> old + new },
        )

        section("Numbers")
        example("3.14159.roundTo(2)", 3.14159.roundTo(2))
        example("3.1.roundToString(2)", 3.1.roundToString(2))
        example("3.14159.roundToString(2)", 3.14159.roundToString(2))
        example("(-0.001).roundToString(2)", (-0.001).roundToString(2))
        example(
            "2.341.roundToString(2, RoundingMode.CEILING)",
            2.341.roundToString(2, RoundingMode.CEILING),
        )
        example(
            "2.341.roundToString(2, RoundingMode.FLOOR)",
            2.341.roundToString(2, RoundingMode.FLOOR),
        )
        example(
            "2.341.roundTo(2, RoundingMode.CEILING)",
            2.341.roundTo(2, RoundingMode.CEILING),
        )
        example(
            "2.341.roundTo(2, RoundingMode.FLOOR)",
            2.341.roundTo(2, RoundingMode.FLOOR),
        )

        section("Bit operations")
        val read = 0b0001
        val write = 0b0010
        example("read hasFlag 0b0001", read hasFlag 0b0001)
        example("read hasAnyFlag 0b0010", read hasAnyFlag 0b0010)
        example("read withFlag write", read withFlag write)
        example(
            "(read withFlag write) withoutFlag read",
            (read withFlag write) withoutFlag read,
        )
        example(
            "(read withFlag write) toggleFlag write",
            (read withFlag write) toggleFlag write,
        )
        example("(-1).toHexString()", (-1).toHexString())
        example(
            "Int.MIN_VALUE.toBinaryString()",
            Int.MIN_VALUE.toBinaryString(),
        )
        example("(-1L).toOctalString()", (-1L).toOctalString())
        example(
            "1.toBinaryString(withPrefix = true)",
            1.toBinaryString(withPrefix = true),
        )
        example(
            "(-1).toHexString(withPrefix = true)",
            (-1).toHexString(withPrefix = true),
        )
        example(
            "8.toOctalString(withPrefix = true)",
            8.toOctalString(withPrefix = true),
        )

        section("Angles")
        example("PI.toDegrees()", PI.toDegrees())
        example("(PI / 2).toDegrees()", (PI / 2).toDegrees())
        example("180.0.toRadians()", 180.0.toRadians())
        example("90f.toRadians()", 90f.toRadians())

        section("Pairs")
        example("(\"key\" to 1).swap()", ("key" to 1).swap())

        section("Nullable defaults")
        val missingCount: Int? = null
        val missingPixels: IntArray? = null
        val missingFlag: Boolean? = null
        example("missingCount.orZero()", missingCount.orZero())
        example("missingPixels.orEmpty().size", missingPixels.orEmpty().size)
        example("missingFlag.orFalse()", missingFlag.orFalse())
        val missingName: String? = null
        example(
            "missingName.orDefault(\"anonymous\")",
            missingName.orDefault("anonymous"),
        )
        example(
            "missingName.fallback { \"anonymous\" }",
            missingName.fallback { "anonymous" },
        )
        val presentName: String? = "Ada"
        example(
            "presentName.orThrow { IllegalStateException() }.uppercase()",
            presentName.orThrow { IllegalStateException() }.uppercase(),
        )
        example(
            "runCatching { missingName.orThrow { ... } }.exceptionOrNull()?.message",
            runCatching {
                missingName.orThrow { IllegalStateException("missing name") }
            }.exceptionOrNull()?.message,
        )

        section("Result")
        val parsedNumber: String? = "42"
        example(
            "parsedNumber.toResultOr { ... }.map(String::toInt)",
            parsedNumber.toResultOr { IllegalArgumentException("missing") }.map(String::toInt),
        )
        val missingNumber: String? = null
        example(
            "missingNumber.toResultOr { IllegalArgumentException(\"missing\") }",
            missingNumber.toResultOr { IllegalArgumentException("missing") },
        )
        example(
            "listOf(Result.success(1), Result.success(2)).combine()",
            listOf(Result.success(1), Result.success(2)).combine(),
        )
        val mixedResults = listOf(
            Result.success(1),
            Result.failure<Int>(IllegalStateException("bad")),
        )
        example("mixedResults.partitionResults()", mixedResults.partitionResults())

        section("Casting")
        val payload: Any = listOf("a", "b")
        example(
            "payload.asType<List<String>>()?.second()",
            payload.asType<List<String>>()?.second(),
        )
        example("(1 as Any).asType<String>()", (1 as Any).asType<String>())
        example(
            "payload.isType<List<String>>()",
            payload.isType<List<String>>(),
        )
        example(
            "payload.asTypeNonNull<List<String>>().second()",
            payload.asTypeNonNull<List<String>>().second(),
        )

        section("Lazy")
        var loads = 0
        val config = fastLazy {
            loads++
            "loaded"
        }
        val session = safeLazy { "safe" }
        example("config.isInitialized // before", config.isInitialized())
        example("config.value", config.value)
        example("config.isInitialized // after", config.isInitialized())
        example("loads", loads)
        example("session.value", session.value)

    }

    private fun StringBuilder.section(title: String) {
        append(title.uppercase()).append("\n\n")
    }

    private fun StringBuilder.example(expression: String, result: Any?) {
        append("  ").append(expression).append('\n')
        append("    → ").append(result.format()).append("\n\n")
    }

    private fun Any?.format(): String = when (this) {
        is String -> "\"$this\""
        else -> toString()
    }
}