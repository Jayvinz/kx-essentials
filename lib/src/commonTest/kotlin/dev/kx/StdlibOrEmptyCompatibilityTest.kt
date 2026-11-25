package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Guards against this library accidentally shadowing the standard library's
 * more specific `orEmpty()` overloads for `List`, `Set`, `Map`, `String`,
 * `Array` and `Sequence`.
 */
class StdlibOrEmptyCompatibilityTest {

    @Test
    fun standardLibraryOrEmptyOverloadsStillResolve() {
        val list: List<Int>? = null
        val set: Set<Int>? = null
        val map: Map<String, Int>? = null
        val text: String? = null
        val array: Array<Int>? = null
        val sequence: Sequence<Int>? = null

        assertEquals(emptyList(), list.orEmpty())
        assertEquals(emptySet(), set.orEmpty())
        assertEquals(emptyMap(), map.orEmpty())
        assertEquals("", text.orEmpty())
        assertEquals(0, array.orEmpty().size)
        assertEquals(0, sequence.orEmpty().count())
    }

    @Test
    fun standardLibraryResultRecoverIsStillAvailable() {
        val recovered = Result.failure<Int>(IllegalStateException("boom")).recover { 1 }

        assertEquals(1, recovered.getOrNull())
    }
}
