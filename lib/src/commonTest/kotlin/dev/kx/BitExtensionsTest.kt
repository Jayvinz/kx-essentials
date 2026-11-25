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

class PrefixedRadixStringTest {

    @Test
    fun intPrefixedStringsUseTheExpectedMarkers() {
        assertEquals("0b0", 0.toBinaryString(withPrefix = true))
        assertEquals("0b1", 1.toBinaryString(withPrefix = true))
        assertEquals("0b" + "1".repeat(32), (-1).toBinaryString(withPrefix = true))

        assertEquals("0x0", 0.toHexString(withPrefix = true))
        assertEquals("0xff", 255.toHexString(withPrefix = true))
        assertEquals("0xffffffff", (-1).toHexString(withPrefix = true))

        assertEquals("0o0", 0.toOctalString(withPrefix = true))
        assertEquals("0o10", 8.toOctalString(withPrefix = true))
        assertEquals("0o37777777777", (-1).toOctalString(withPrefix = true))
    }

    @Test
    fun longPrefixedStringsUseTheExpectedMarkers() {
        assertEquals("0b" + "1".repeat(64), (-1L).toBinaryString(withPrefix = true))
        assertEquals("0xffffffffffffffff", (-1L).toHexString(withPrefix = true))
        assertEquals("0o1777777777777777777777", (-1L).toOctalString(withPrefix = true))
    }

    @Test
    fun falseKeepsTheJavaCompatibleResult() {
        assertEquals(1.toBinaryString(), 1.toBinaryString(withPrefix = false))
        assertEquals(255.toHexString(), 255.toHexString(withPrefix = false))
        assertEquals(8.toOctalString(), 8.toOctalString(withPrefix = false))
        assertEquals(1L.toBinaryString(), 1L.toBinaryString(withPrefix = false))
        assertEquals(255L.toHexString(), 255L.toHexString(withPrefix = false))
        assertEquals(8L.toOctalString(), 8L.toOctalString(withPrefix = false))
    }
}
