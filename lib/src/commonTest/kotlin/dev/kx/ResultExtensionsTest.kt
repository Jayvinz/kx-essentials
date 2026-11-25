package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultExtensionsTest {

    @Test
    fun toResultOr_wrapsAPresentValueInSuccess() {
        val value: String? = "kx"

        val result = value.toResultOr { IllegalStateException("missing") }

        assertTrue(result.isSuccess)
        assertEquals("kx", result.getOrNull())
    }

    @Test
    fun toResultOr_wrapsNullInFailure() {
        val value: String? = null

        val result = value.toResultOr { IllegalStateException("missing") }

        assertTrue(result.isFailure)
        assertEquals("missing", result.exceptionOrNull()?.message)
    }

    @Test
    fun toResultOr_onlyInvokesTheLambdaForNull() {
        var calls = 0
        val present: String? = "kx"
        val missing: String? = null

        present.toResultOr { calls++; IllegalStateException("missing") }
        assertEquals(0, calls)

        missing.toResultOr { calls++; IllegalStateException("missing") }
        assertEquals(1, calls)
    }

    @Test
    fun combine_returnsAllSuccessfulValuesInOrder() {
        val result = listOf(Result.success(1), Result.success(2)).combine()

        assertEquals(listOf(1, 2), result.getOrNull())
    }

    @Test
    fun combine_returnsTheFirstFailureAndStops() {
        val results = object : Iterable<Result<Int>> {
            override fun iterator(): Iterator<Result<Int>> = object : Iterator<Result<Int>> {
                private var index = 0

                override fun hasNext(): Boolean = index < 3

                override fun next(): Result<Int> = when (index++) {
                    0 -> Result.success(1)
                    1 -> Result.failure(IllegalStateException("boom"))
                    else -> error("combine must not consume elements after the first failure")
                }
            }
        }

        val result = results.combine()

        assertTrue(result.isFailure)
        assertEquals("boom", result.exceptionOrNull()?.message)
    }

    @Test
    fun combine_handlesAnEmptyIterable() {
        val result = emptyList<Result<Int>>().combine()

        assertEquals(emptyList(), result.getOrNull())
    }

    @Test
    fun partitionResults_splitsValuesAndErrorsPreservingOrder() {
        val results = listOf(
            Result.success(1),
            Result.failure<Int>(IllegalStateException("first")),
            Result.success(2),
            Result.failure<Int>(IllegalStateException("second")),
        )

        val (values, errors) = results.partitionResults()

        assertEquals(listOf(1, 2), values)
        assertEquals(listOf("first", "second"), errors.map { it.message })
    }

    @Test
    fun partitionResults_handlesAnEmptyIterable() {
        val (values, errors) = emptyList<Result<Int>>().partitionResults()

        assertEquals(emptyList(), values)
        assertEquals(emptyList(), errors)
    }
}
