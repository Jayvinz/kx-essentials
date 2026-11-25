package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ThrowExtensionsTest {

    @Test
    fun orThrow_returnsTheValueWhenPresent() {
        val value: String? = "kx"
        assertEquals("kx", value.orThrow { IllegalStateException("missing") })
    }

    @Test
    fun orThrow_throwsTheProvidedExceptionWhenNull() {
        val value: String? = null

        val exception = assertFailsWith<IllegalStateException> {
            value.orThrow { IllegalStateException("missing") }
        }

        assertEquals("missing", exception.message)
    }

    @Test
    fun orThrow_doesNotCreateTheExceptionWhenPresent() {
        var calls = 0
        val value: String? = "kx"

        assertEquals("kx", value.orThrow { calls++; IllegalStateException("missing") })
        assertEquals(0, calls)
    }

    @Test
    fun orThrow_keepsChainsLinear() {
        val value: String? = "kx"

        val result = value.orThrow { IllegalStateException("missing") }.uppercase()

        assertEquals("KX", result)
    }

    @Test
    fun orThrow_worksWithFunctionTypedReceivers() {
        val value: (() -> Int)? = { 42 }

        assertEquals(42, value.orThrow { IllegalStateException("missing") }())
    }
}