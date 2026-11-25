package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MapExtensionsTest {

    @Test
    fun inverse_swapsKeysAndValues() {
        assertEquals(
            mapOf(1 to "a", 2 to "b"),
            mapOf("a" to 1, "b" to 2).inverse(),
        )
        assertEquals(emptyMap<Int, String>(), emptyMap<String, Int>().inverse())
    }

    @Test
    fun inverse_keepsTheLastEntryForDuplicateValues() {
        assertEquals(
            mapOf(1 to "b"),
            mapOf("a" to 1, "b" to 1).inverse(),
        )
    }

    @Test
    fun filterValuesNotNull_dropsNullValues() {
        val source = mapOf("a" to 1, "b" to null, "c" to 3)
        assertEquals(mapOf("a" to 1, "c" to 3), source.filterValuesNotNull())
    }

    @Test
    fun filterValuesNotNull_handlesEmptyMaps() {
        assertEquals(emptyMap<String, Int>(), emptyMap<String, Int?>().filterValuesNotNull())
    }
}

class RequireValueTest {

    @Test
    fun requireValue_returnsTheNonNullValue() {
        val config = mapOf("port" to 8080, "host" to null)

        assertEquals(8080, config.requireValue("port"))
    }

    @Test
    fun requireValue_distinguishesAMissingKey() {
        val config = mapOf<String, Int?>("port" to 8080)

        val exception = assertFailsWith<NoSuchElementException> {
            config.requireValue("timeout")
        }

        assertEquals("Key \"timeout\" is missing.", exception.message)
    }

    @Test
    fun requireValue_distinguishesANullValue() {
        val config = mapOf("port" to 8080, "timeout" to null)

        val exception = assertFailsWith<NoSuchElementException> {
            config.requireValue("timeout")
        }

        assertEquals("Key \"timeout\" is present but its value is null.", exception.message)
    }
}

class MapMergeWithTest {

    @Test
    fun mergeWith_combinesDuplicateKeysAndAppendsNewKeys() {
        val left = linkedMapOf("a" to 1, "b" to 2)
        val right = linkedMapOf("b" to 3, "c" to 4)

        val result = left.mergeWith(right) { existing, incoming -> existing + incoming }

        assertEquals(mapOf("a" to 1, "b" to 5, "c" to 4), result)
        assertEquals(listOf("a", "b", "c"), result.keys.toList())
    }

    @Test
    fun mergeWith_onlyInvokesCombineForDuplicateKeys() {
        var calls = 0

        val result = mapOf("a" to 1).mergeWith(mapOf("b" to 2)) { _, _ ->
            calls++
            0
        }

        assertEquals(0, calls)
        assertEquals(mapOf("a" to 1, "b" to 2), result)
    }

    @Test
    fun mergeWith_handlesEmptyMaps() {
        val values = mapOf("a" to 1)

        assertEquals(values, values.mergeWith(emptyMap()) { _, _ -> 0 })
        assertEquals(values, emptyMap<String, Int>().mergeWith(values) { _, _ -> 0 })
        assertEquals(emptyMap<String, Int>(), emptyMap<String, Int>().mergeWith(emptyMap()) { _, _ -> 0 })
    }

    @Test
    fun mergeWith_supportsNullableValues() {
        val left = mapOf<String, Int?>("a" to null, "b" to 2)
        val right = mapOf<String, Int?>("a" to 1, "b" to null)

        val result = left.mergeWith(right) { existing, incoming ->
            (existing ?: 0) + (incoming ?: 0)
        }

        assertEquals(mapOf("a" to 1, "b" to 2), result)
    }

    @Test
    fun mergeWith_doesNotModifyItsInputs() {
        val left = mapOf("a" to 1)
        val right = mapOf("a" to 2)

        left.mergeWith(right) { existing, incoming -> existing + incoming }

        assertEquals(mapOf("a" to 1), left)
        assertEquals(mapOf("a" to 2), right)
    }
}
