package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ArrayExtensionsTest {

    @Test
    fun orEmpty_returnsEmptyArraysForNullPrimitiveArrays() {
        assertTrue((null as BooleanArray?).orEmpty().isEmpty())
        assertTrue((null as ByteArray?).orEmpty().isEmpty())
        assertTrue((null as CharArray?).orEmpty().isEmpty())
        assertTrue((null as ShortArray?).orEmpty().isEmpty())
        assertTrue((null as IntArray?).orEmpty().isEmpty())
        assertTrue((null as LongArray?).orEmpty().isEmpty())
        assertTrue((null as FloatArray?).orEmpty().isEmpty())
        assertTrue((null as DoubleArray?).orEmpty().isEmpty())
    }

    @Test
    fun orEmpty_keepsNonNullArraysUnchanged() {
        val ints = intArrayOf(1, 2, 3)
        val chars = charArrayOf('k', 'x')

        assertSame(ints, ints.orEmpty())
        assertSame(chars, chars.orEmpty())
        assertEquals(listOf(1, 2, 3), ints.orEmpty().toList())
        assertEquals("kx", chars.orEmpty().concatToString())
    }

    @Test
    fun orEmpty_doesNotShareEmptyInstancesBetweenCalls() {
        val first = (null as IntArray?).orEmpty()
        val second = (null as IntArray?).orEmpty()

        assertTrue(first.isEmpty())
        assertTrue(second.isEmpty())
    }
}

class ArrayAtTest {

    @Test
    fun arrayAt_readsFromTheEndForNegativeIndices() {
        val array = arrayOf("a", "b", "c")

        assertEquals("c", array.at(-1))
        assertEquals("a", array.at(-3))
        assertEquals("b", array.at(1))
    }

    @Test
    fun arrayAt_throwsForOutOfBoundsIndices() {
        val array = arrayOf(1, 2)

        assertFailsWith<IndexOutOfBoundsException> { array.at(2) }
        assertFailsWith<IndexOutOfBoundsException> { array.at(-3) }
        assertFailsWith<IndexOutOfBoundsException> { arrayOf<Int>().at(0) }
    }

    @Test
    fun arrayAtOrNull_returnsNullForOutOfBoundsIndices() {
        val array = arrayOf(1, 2, 3)

        assertNull(array.atOrNull(3))
        assertNull(array.atOrNull(-4))
        assertNull(arrayOf<Int>().atOrNull(0))
    }

    @Test
    fun primitiveArrays_supportNegativeIndices() {
        assertEquals(3, intArrayOf(1, 2, 3).at(-1))
        assertEquals(3L, longArrayOf(1L, 2L, 3L).at(-1))
        assertEquals(3.toShort(), shortArrayOf(1, 2, 3).at(-1))
        assertEquals(3.toByte(), byteArrayOf(1, 2, 3).at(-1))
        assertEquals('c', charArrayOf('a', 'b', 'c').at(-1))
        assertEquals(true, booleanArrayOf(true).at(-1))
        assertEquals(3f, floatArrayOf(1f, 2f, 3f).at(-1))
        assertEquals(3.0, doubleArrayOf(1.0, 2.0, 3.0).at(-1))
    }

    @Test
    fun primitiveArrays_atOrNullHandlesBounds() {
        assertNull(intArrayOf(1).atOrNull(1))
        assertNull(intArrayOf(1).atOrNull(-2))
        assertNull(booleanArrayOf().atOrNull(0))
        assertEquals(1, intArrayOf(1).atOrNull(-1))
    }

    @Test
    fun primitiveArrays_throwForOutOfBoundsIndices() {
        assertFailsWith<IndexOutOfBoundsException> { intArrayOf(1).at(1) }
        assertFailsWith<IndexOutOfBoundsException> { charArrayOf('a').at(-2) }
    }
}
