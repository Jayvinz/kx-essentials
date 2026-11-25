package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitFlagTest {

    @Test
    fun hasFlag_checksAllBitsInTheMask() {
        val permissions = 0b0101



        assertTrue(permissions hasFlag 0b0100)
        assertTrue(permissions hasFlag 0b0001)
        assertTrue(permissions hasFlag 0b0101)
        assertFalse(permissions hasFlag 0b0010)
        assertFalse(permissions hasFlag 0b0110)
    }

    @Test
    fun hasAnyFlag_checksAtLeastOneBit() {
        val permissions = 0b0101

        assertTrue(permissions hasAnyFlag 0b0110)
        assertTrue(permissions hasAnyFlag 0b0100)
        assertFalse(permissions hasAnyFlag 0b0010)
    }

    @Test
    fun zeroMaskIsTriviallySetButHasNoAnyBit() {
        val value = 0b1010

        assertTrue(value hasFlag 0)
        assertFalse(value hasAnyFlag 0)
        assertEquals(value, value.withFlag(0))
        assertEquals(value, value.withoutFlag(0))
        assertEquals(value, value.toggleFlag(0))
    }

    @Test
    fun withWithoutAndToggle_updateTheMask() {
        val read = 0b0001
        val write = 0b0010

        assertEquals(0b0011, read withFlag write)
        assertEquals(0b0001, 0b0011 withoutFlag write)
        assertEquals(0b0010, 0b0011 toggleFlag read)
        assertEquals(0b0011, 0b0000 toggleFlag 0b0011)
    }

    @Test
    fun longFlagsUseTheSameSemantics() {
        val read = 0b0001L
        val write = 0b0010L
        val readWrite = 0b0011L

        assertTrue(readWrite hasFlag read)
        assertFalse(read hasFlag readWrite)
        assertTrue(readWrite hasAnyFlag read)
        assertFalse(read hasAnyFlag write)
        assertEquals(readWrite, read withFlag write)
        assertEquals(write, readWrite withoutFlag read)
        assertEquals(read, readWrite toggleFlag write)
    }
}

class UnsignedStringTest {

    @Test
    fun intBinaryStringMatchesJava() {
        assertEquals("0", 0.toBinaryString())
        assertEquals("1", 1.toBinaryString())
        assertEquals("11111111111111111111111111111111", (-1).toBinaryString())
        assertEquals("1" + "0".repeat(31), Int.MIN_VALUE.toBinaryString())
    }

    @Test
    fun intHexStringMatchesJava() {
        assertEquals("0", 0.toHexString())
        assertEquals("12345678", 0x12345678.toHexString())
        assertEquals("ffffffff", (-1).toHexString())
        assertEquals("80000000", Int.MIN_VALUE.toHexString())
    }

    @Test
    fun intOctalStringMatchesJava() {
        assertEquals("0", 0.toOctalString())
        assertEquals("37777777777", (-1).toOctalString())
        assertEquals("20000000000", Int.MIN_VALUE.toOctalString())
    }

    @Test
    fun longBinaryStringMatchesJava() {
        assertEquals("0", 0L.toBinaryString())
        assertEquals("1", 1L.toBinaryString())
        assertEquals("1".repeat(64), (-1L).toBinaryString())
        assertEquals("1" + "0".repeat(63), Long.MIN_VALUE.toBinaryString())
    }

    @Test
    fun longHexStringMatchesJava() {
        assertEquals("0", 0L.toHexString())
        assertEquals("ffffffffffffffff", (-1L).toHexString())
        assertEquals("8000000000000000", Long.MIN_VALUE.toHexString())
    }

    @Test
    fun longOctalStringMatchesJava() {
        assertEquals("0", 0L.toOctalString())
        assertEquals("1777777777777777777777", (-1L).toOctalString())
        assertEquals("1000000000000000000000", Long.MIN_VALUE.toOctalString())
    }
}
