package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals

class SetExtensionsTest {

    @Test
    fun diff_returnsAddedAndRemovedElements() {
        val old = linkedSetOf("a", "b")
        val new = linkedSetOf("b", "c")

        val (added, removed) = old.diff(new)

        assertEquals(setOf("c"), added)
        assertEquals(setOf("a"), removed)
    }

    @Test
    fun diff_preservesTheIterationOrderOfEachSide() {
        val old = linkedSetOf("a", "b", "c")
        val new = linkedSetOf("b", "d")

        val (added, removed) = old.diff(new)

        assertEquals(listOf("d"), added.toList())
        assertEquals(listOf("a", "c"), removed.toList())
    }

    @Test
    fun diff_handlesEmptyAndIdenticalSets() {
        val values = setOf(1, 2, 3)

        assertEquals(emptySet<Int>() to emptySet(), emptySet<Int>().diff(emptySet()))
        assertEquals(emptySet<Int>() to emptySet(), values.diff(values))
        assertEquals(setOf(1) to emptySet(), emptySet<Int>().diff(setOf(1)))
        assertEquals(emptySet<Int>() to values, values.diff(emptySet()))
    }

    @Test
    fun diff_doesNotModifyItsInputs() {
        val old = setOf("a", "b")
        val new = setOf("b", "c")

        old.diff(new)

        assertEquals(setOf("a", "b"), old)
        assertEquals(setOf("b", "c"), new)
    }
}
