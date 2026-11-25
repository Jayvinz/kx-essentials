package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultExtensionsTest {

    private data class Profile(val name: String?)
    private data class User(val profile: Profile?)

    @Test
    fun orDefault_returnsTheValueWhenPresent() {
        val value: String? = "kx"
        assertEquals("kx", value.orDefault("fallback"))
    }

    @Test
    fun orDefault_returnsTheDefaultWhenNull() {
        val value: String? = null
        assertEquals("fallback", value.orDefault("fallback"))
    }

    @Test
    fun orDefault_keepsLongChainsLinear() {
        val user: User? = User(Profile(name = null))

        val displayName = user?.profile?.name.orDefault("anonymous").uppercase()

        assertEquals("ANONYMOUS", displayName)
    }

    @Test
    fun orDefault_infersNullableReturnForNullableDefaults() {
        val value: String? = null
        val fallback: String? = null

        val fromNull: String? = value.orDefault(fallback)
        assertNull(fromNull)

        val present: String = "kx"
        val fromPresent: String? = present.orDefault(fallback)
        assertEquals("kx", fromPresent)

        val literalDefault: String? = value.orDefault(null)
        assertNull(literalDefault)
    }

    @Test
    fun fallback_infersNullableReturnForNullableResults() {
        val value: String? = null
        val present: String = "kx"

        val fromNull: String? = value.fallback { null }
        assertNull(fromNull)

        val fromPresent: String? = present.fallback { null }
        assertEquals("kx", fromPresent)
    }

    @Test
    fun orDefault_worksWithFunctionTypedReceivers() {
        val missing: (() -> Int)? = null
        val default: () -> Int = { 42 }
        val result: () -> Int = missing.orDefault(default)

        assertEquals(42, result())
    }

    @Test
    fun fallback_computesTheValueOnlyWhenNull() {
        var calls = 0
        val present: Int? = 1
        assertEquals(1, present.fallback { calls++; 0 })
        assertEquals(0, calls)

        val missing: Int? = null
        assertEquals(0, missing.fallback { calls++; 0 })
        assertEquals(1, calls)
    }

    @Test
    fun fallback_keepsLongChainsLinear() {
        val user: User? = User(Profile(name = null))

        val displayName = user?.profile?.name.fallback { "anonymous" }.uppercase()

        assertEquals("ANONYMOUS", displayName)
    }

    @Test
    fun fallback_worksWithFunctionTypedReceivers() {
        val missing: (() -> Int)? = null
        val result: () -> Int = missing.fallback { { 42 } }

        assertEquals(42, result())

        val present: (() -> Int)? = { 7 }
        assertEquals(7, present.fallback { { 42 } }())
    }
}