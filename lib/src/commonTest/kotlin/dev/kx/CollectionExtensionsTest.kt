package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CollectionExtensionsTest {

    @Test
    fun ordinalAccessors_returnTheExpectedElements() {
        val list = listOf("a", "b", "c", "d")
        assertEquals("b", list.second())
        assertEquals("c", list.secondLast())
    }

    @Test
    fun ordinalAccessors_returnNullWhenTheListIsTooShort() {
        assertNull(emptyList<String>().secondOrNull())
        assertNull(listOf("a").secondOrNull())
        assertNull(emptyList<String>().secondLastOrNull())
        assertNull(listOf("a").secondLastOrNull())
    }

    @Test
    fun ordinalAccessors_throwWhenTheListIsTooShort() {
        assertFailsWith<NoSuchElementException> { emptyList<String>().second() }
        assertFailsWith<NoSuchElementException> { listOf("a").secondLast() }
    }

    @Test
    fun swap_exchangesElementsInPlace() {
        val letters = mutableListOf("a", "b", "c")
        letters.swap(0, 2)
        assertEquals(listOf("c", "b", "a"), letters)

        letters.swap(1, 1)
        assertEquals(listOf("c", "b", "a"), letters)
    }

    @Test
    fun swap_rejectsOutOfBoundsIndices() {
        assertFailsWith<IndexOutOfBoundsException> { mutableListOf(1, 2).swap(0, 2) }
        assertFailsWith<IndexOutOfBoundsException> { mutableListOf(1, 2).swap(-1, 0) }
    }

    @Test
    fun containsAny_findsAtLeastOneElement() {
        val values = listOf(1, 2, 3)
        assertTrue(values.containsAny(4, 2))
        assertFalse(values.containsAny(4, 5))
        assertFalse(values.containsAny())
    }

    @Test
    fun containsAll_requiresEveryElement() {
        val values = listOf(1, 2, 3)
        assertTrue(values.containsAll(1, 3))
        assertTrue(values.containsAll())
        assertFalse(values.containsAll(1, 4))
    }

    @Test
    fun containsAll_keepsWorkingForCollections() {
        assertTrue(listOf(1, 2, 3).containsAll(listOf(1, 3)))
    }
}

class ListAtTest {

    @Test
    fun at_readsFromTheEndForNegativeIndices() {
        val list = listOf("a", "b", "c")

        assertEquals("c", list.at(-1))
        assertEquals("a", list.at(-3))
        assertEquals("b", list.at(1))
    }

    @Test
    fun at_throwsForOutOfBoundsIndices() {
        assertFailsWith<IndexOutOfBoundsException> { listOf(1).at(1) }
        assertFailsWith<IndexOutOfBoundsException> { listOf(1).at(-2) }
        assertFailsWith<IndexOutOfBoundsException> { emptyList<Int>().at(0) }
        assertFailsWith<IndexOutOfBoundsException> { listOf(1).at(Int.MIN_VALUE) }
    }

    @Test
    fun atOrNull_returnsNullForOutOfBoundsIndices() {
        val list = listOf(1, 2, 3)

        assertNull(list.atOrNull(3))
        assertNull(list.atOrNull(-4))
        assertNull(emptyList<Int>().atOrNull(-1))
        assertNull(list.atOrNull(Int.MIN_VALUE))
    }

    @Test
    fun atOrNull_returnsNullForANullElement() {
        val list = listOf("a", null)

        assertNull(list.atOrNull(1))
        assertEquals("a", list.at(0))
    }
}

class OverlapsTest {

    @Test
    fun overlaps_detectsSharedElements() {
        assertTrue(setOf(1, 2, 3) overlaps setOf(3, 4))
        assertTrue(listOf(1, 2, 2) overlaps listOf(2, 3))
        assertTrue(listOf(1) overlaps setOf(1))
        assertEquals(false, setOf(1, 2) overlaps setOf(3, 4))
    }

    @Test
    fun overlaps_returnsFalseForEmptyInputs() {
        assertFalse(emptySet<Int>() overlaps setOf(1))
        assertFalse(listOf(1) overlaps emptyList())
        assertFalse(emptyList<Int>() overlaps emptySet())
    }

    @Test
    fun overlaps_handlesNullsAndDuplicates() {
        assertTrue(listOf(null, 1) overlaps listOf(null))
        assertTrue(listOf(1, 1, 2) overlaps listOf(2, 2))
    }
}
