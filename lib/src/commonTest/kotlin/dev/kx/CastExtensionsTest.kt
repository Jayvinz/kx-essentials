package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CastExtensionsTest {

    @Test
    fun asType_returnsTheValueWhenTheTypeMatches() {
        val value: Any = "kx"
        assertEquals("kx", value.asType<String>())
    }

    @Test
    fun asType_returnsNullOnMismatch() {
        assertNull((1 as Any).asType<String>())
        assertNull((null as Any?).asType<String>())
    }

    @Test
    fun asType_keepsWorkingInsideChains() {
        data class Wrapper(val payload: Any)

        val wrapped = Wrapper(mapOf("items" to listOf("a", "b")))
        val second = wrapped.payload
            .asType<Map<String, List<String>>>()
            ?.getValue("items")
            ?.asType<List<String>>()
            ?.second()

        assertEquals("b", second)
    }

    @Test
    fun asType_acceptsNullableTargetTypes() {
        val value: Any? = null
        assertNull(value.asType<String?>())
        assertEquals("kx", ("kx" as Any).asType<String?>())
    }

    @Test
    fun asTypeNonNull_returnsTheValueWhenTheTypeMatches() {
        assertEquals("kx", ("kx" as Any).asTypeNonNull<String>())
        assertEquals(1, (1 as Any).asTypeNonNull<Int>())
    }

    @Test
    fun asTypeNonNull_throwsClassCastExceptionOnMismatch() {
        assertFailsWith<ClassCastException> { (1 as Any).asTypeNonNull<String>() }
    }

    @Test
    fun asTypeNonNull_failsForNullWithNonNullTarget_likeTheAsOperator() {
        // JVM throws NullPointerException, Kotlin/JS throws ClassCastException;
        // both are RuntimeException and both match the platform's `as` behavior.
        assertFailsWith<RuntimeException> { (null as Any?).asTypeNonNull<String>() }
    }
}