package dev.kx

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BooleanExtensionsTest {

    @Test
    fun orFalse_replacesNullWithFalse() {
        val missing: Boolean? = null
        assertFalse(missing.orFalse())
        assertFalse(false.orFalse())
        assertTrue(true.orFalse())
    }

    @Test
    fun orTrue_replacesNullWithTrue() {
        val missing: Boolean? = null
        assertTrue(missing.orTrue())
        assertFalse(false.orTrue())
        assertTrue(true.orTrue())
    }
}
