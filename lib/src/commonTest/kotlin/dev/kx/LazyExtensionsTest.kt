package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LazyExtensionsTest {

    @Test
    fun fastLazy_doesNotInitializeBeforeFirstAccess() {
        var calls = 0
        val value = fastLazy {
            calls++
            42
        }

        assertEquals(0, calls)
        assertFalse(value.isInitialized())
    }

    @Test
    fun fastLazy_initializesOnlyOnce() {
        var calls = 0
        val value = fastLazy {
            calls++
            "kx"
        }

        assertEquals("kx", value.value)
        assertEquals("kx", value.value)
        assertEquals(1, calls)
        assertTrue(value.isInitialized())
    }

    @Test
    fun fastLazy_supportsPropertyDelegation() {
        var calls = 0
        val text by fastLazy {
            calls++
            "delegated"
        }

        assertEquals("delegated", text)
        assertEquals("delegated", text)
        assertEquals(1, calls)
    }

    @Test
    fun fastLazy_retriesAfterAFailedInitializer() {
        var calls = 0
        val value = fastLazy {
            calls++
            if (calls == 1) error("boom")
            "recovered"
        }

        assertFailsWith<IllegalStateException> { value.value }
        assertEquals("recovered", value.value)
        assertEquals(2, calls)
    }

    @Test
    fun safeLazy_initializesOnlyOnceAndSupportsDelegation() {
        var calls = 0
        val text by safeLazy {
            calls++
            "safe"
        }

        assertEquals("safe", text)
        assertEquals("safe", text)
        assertEquals(1, calls)
    }

    @Test
    fun safeLazy_keepsTheStandardLazySemantics() {
        var calls = 0
        val value = safeLazy {
            calls++
            7
        }

        assertFalse(value.isInitialized())
        assertEquals(7, value.value)
        assertEquals(1, calls)
        assertTrue(value.isInitialized())
    }
}