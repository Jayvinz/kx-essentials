package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TypeCheckExtensionsTest {

    @Test
    fun isType_returnsTrueAndAllowsSmartCastWhenTheTypeMatches() {
        val value: Any? = "kx"

        if (value.isType<String>()) {
            // The contract makes this smart cast possible.
            assertEquals(2, value.length)
        } else {
            throw AssertionError("expected String")
        }
    }

    @Test
    fun isType_returnsFalseWhenTheTypeDoesNotMatch() {
        val value: Any? = 1
        assertFalse(value.isType<String>())
        assertTrue((1 as Any).isType<Int>())
    }

    @Test
    fun isType_returnsFalseForNull() {
        val value: Any? = null
        assertFalse(value.isType<String>())
    }

    @Test
    fun isType_checksTheErasedClassForGenericTypes() {
        val value: Any = listOf("a", "b")

        assertTrue(value.isType<List<String>>())
        assertTrue(value.isType<List<Int>>())
        assertFalse(value.isType<Set<String>>())
    }

    @Test
    fun isType_worksInsideCollectionPipelines() {
        val values = listOf<Any>(1, "a", 2, "b")

        assertEquals(listOf("a", "b"), values.filter { it.isType<String>() })
    }
}