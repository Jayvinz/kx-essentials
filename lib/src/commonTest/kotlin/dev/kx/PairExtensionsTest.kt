package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals

class PairExtensionsTest {

    @Test
    fun swap_reversesTheElements() {
        assertEquals(1 to "a", ("a" to 1).swap())
    }
}
