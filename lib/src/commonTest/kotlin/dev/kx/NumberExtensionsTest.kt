package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals

class NullableNumberExtensionsTest {

    @Test
    fun orZero_replacesNullWithZeroForSignedNumbers() {
        assertEquals(0.toByte(), (null as Byte?).orZero())
        assertEquals(0.toShort(), (null as Short?).orZero())
        assertEquals(0, (null as Int?).orZero())
        assertEquals(0L, (null as Long?).orZero())
        assertEquals(0f, (null as Float?).orZero())
        assertEquals(0.0, (null as Double?).orZero())
    }

    @Test
    fun orZero_replacesNullWithZeroForUnsignedNumbers() {
        assertEquals(0u.toUByte(), (null as UByte?).orZero())
        assertEquals(0u.toUShort(), (null as UShort?).orZero())
        assertEquals(0u, (null as UInt?).orZero())
        assertEquals(0uL, (null as ULong?).orZero())
    }

    @Test
    fun orZero_keepsNonNullValues() {
        val byte: Byte? = 7
        val short: Short? = 7
        val int: Int? = 7
        val long: Long? = 7L
        val float: Float? = 7f
        val double: Double? = 7.0
        val uint: UInt? = 7u

        assertEquals(7.toByte(), byte.orZero())
        assertEquals(7.toShort(), short.orZero())
        assertEquals(7, int.orZero())
        assertEquals(7L, long.orZero())
        assertEquals(7f, float.orZero())
        assertEquals(7.0, double.orZero())
        assertEquals(7u, uint.orZero())
    }
}
