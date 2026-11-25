package dev.kx

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AngleConversionTest {

    @Test
    fun doubleRadiansToDegrees() {
        assertEquals(0.0, 0.0.toDegrees())
        assertEquals(180.0, PI.toDegrees(), 1e-12)
        assertEquals(90.0, (PI / 2).toDegrees(), 1e-12)
        assertEquals(-90.0, (-PI / 2).toDegrees(), 1e-12)
    }

    @Test
    fun doubleDegreesToRadians() {
        assertEquals(0.0, 0.0.toRadians())
        assertEquals(PI, 180.0.toRadians(), 1e-12)
        assertEquals(PI / 2, 90.0.toRadians(), 1e-12)
        assertEquals(-PI / 2, (-90.0).toRadians(), 1e-12)
    }

    @Test
    fun doubleRoundTrip() {
        val values = listOf(-720.0, -180.0, -1.0, 0.0, 1.0, 180.0, 720.0)

        values.forEach { value ->
            assertEquals(value, value.toRadians().toDegrees(), 1e-12)
        }
    }

    @Test
    fun doubleSpecialValuesPropagate() {
        assertTrue(Double.NaN.toDegrees().isNaN())
        assertTrue(Double.NaN.toRadians().isNaN())
        assertEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY.toDegrees())
        assertEquals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY.toRadians())
    }

    @Test
    fun doubleNegativeZeroKeepsItsSign() {
        assertEquals(Double.NEGATIVE_INFINITY, 1.0 / (-0.0).toDegrees())
        assertEquals(Double.NEGATIVE_INFINITY, 1.0 / (-0.0).toRadians())
    }

    @Test
    fun floatConversions() {
        assertEquals(0f, 0f.toDegrees())
        assertEquals(180f, PI.toFloat().toDegrees(), 1e-5f)
        assertEquals(PI.toFloat(), 180f.toRadians(), 1e-5f)
        assertEquals(PI.toFloat() / 2f, 90f.toRadians(), 1e-5f)
        assertEquals(-PI.toFloat() / 2f, (-90f).toRadians(), 1e-5f)
    }

    @Test
    fun floatSpecialValuesPropagate() {
        assertTrue(Float.NaN.toDegrees().isNaN())
        assertTrue(Float.NaN.toRadians().isNaN())
        assertEquals(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY.toDegrees())
        assertEquals(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY.toRadians())
    }
}

class RoundingTest {

    @Test
    fun roundTo_roundsToTheRequestedNumberOfDecimals() {
        assertEquals(3.14, 3.14159.roundTo(2))
        assertEquals(3.0, 2.5.roundTo(0))
        assertEquals(-3.0, (-2.5).roundTo(0))
        assertEquals(1.0, 0.999.roundTo(2))
        assertEquals(1.0, 1.0.roundTo(4))
    }

    @Test
    fun roundTo_supportsRoundingModes() {
        assertEquals(2.35, 2.341.roundTo(2, RoundingMode.CEILING))
        assertEquals(2.34, 2.341.roundTo(2, RoundingMode.FLOOR))
        assertEquals(2.35, 2.349.roundTo(2, RoundingMode.CEILING))
        assertEquals(2.34, 2.349.roundTo(2, RoundingMode.FLOOR))

        assertEquals(-2.34, (-2.341).roundTo(2, RoundingMode.CEILING))
        assertEquals(-2.35, (-2.341).roundTo(2, RoundingMode.FLOOR))

        assertEquals(2.35, 2.346.roundTo(2, RoundingMode.HALF_UP))
        assertEquals(-2.35, (-2.346).roundTo(2, RoundingMode.HALF_UP))
    }

    @Test
    fun roundTo_modeOverloadMatchesTheDefaultForHalfUp() {
        assertEquals(2.5.roundTo(0), 2.5.roundTo(0, RoundingMode.HALF_UP))
        assertEquals((-2.5).roundTo(0), (-2.5).roundTo(0, RoundingMode.HALF_UP))
    }

    @Test
    fun floatRoundTo_supportsRoundingModes() {
        assertEquals(2.35f, 2.341f.roundTo(2, RoundingMode.CEILING))
        assertEquals(2.34f, 2.341f.roundTo(2, RoundingMode.FLOOR))
    }

    @Test
    fun roundTo_rejectsNegativeDecimals() {
        assertFailsWith<IllegalArgumentException> { 1.0.roundTo(-1) }
        assertFailsWith<IllegalArgumentException> { 1.0f.roundTo(-1) }
    }

    @Test
    fun roundTo_worksForFloats() {
        assertEquals(1.23f, 1.234f.roundTo(2))
        assertEquals(3f, 2.5f.roundTo(0))
    }

    @Test
    fun roundToString_keepsTrailingZeros() {
        assertEquals("3.10", 3.1.roundToString(2))
        assertEquals("3.14", 3.14159.roundToString(2))
        assertEquals("1.00", 1.0.roundToString(2))
        assertEquals("0.50", 0.5.roundToString(2))
        assertEquals("100.00", 100.0.roundToString(2))
        assertEquals("3", 3.1.roundToString(0))
        assertEquals("-3", (-2.5).roundToString(0))
    }

    @Test
    fun roundToString_supportsRoundingModes() {
        assertEquals("2.35", 2.341.roundToString(2, RoundingMode.CEILING))
        assertEquals("2.34", 2.341.roundToString(2, RoundingMode.FLOOR))
        assertEquals("2.35", 2.349.roundToString(2, RoundingMode.CEILING))
        assertEquals("2.34", 2.349.roundToString(2, RoundingMode.FLOOR))

        assertEquals("-2.34", (-2.341).roundToString(2, RoundingMode.CEILING))
        assertEquals("-2.35", (-2.341).roundToString(2, RoundingMode.FLOOR))

        assertEquals("2.35", 2.346.roundToString(2, RoundingMode.HALF_UP))
        assertEquals("-2.35", (-2.346).roundToString(2, RoundingMode.HALF_UP))
    }

    @Test
    fun roundToString_modesHandleSmallAndZeroValues() {
        assertEquals("0.01", 0.001.roundToString(2, RoundingMode.CEILING))
        assertEquals("0.00", 0.001.roundToString(2, RoundingMode.FLOOR))
        assertEquals("-0.00", (-0.001).roundToString(2, RoundingMode.CEILING))
        assertEquals("-0.01", (-0.001).roundToString(2, RoundingMode.FLOOR))
        assertEquals("-0.00", (-0.0).roundToString(2, RoundingMode.CEILING))
    }

    @Test
    fun roundToString_modesWorkForFloats() {
        assertEquals("2.35", 2.341f.roundToString(2, RoundingMode.CEILING))
        assertEquals("2.34", 2.341f.roundToString(2, RoundingMode.FLOOR))
    }

    @Test
    fun roundToString_handlesScientificNotation() {
        assertEquals("10000000.00", 1.0E7.roundToString(2))
        assertEquals("0.000100", 1.0E-4.roundToString(6))
        assertEquals("0.00", 1.0E-4.roundToString(2))
        assertEquals("100000000000000000000.00", 1.0E20.roundToString(2))
    }

    @Test
    fun roundToString_handlesZeroAndSpecialValues() {
        assertEquals("0.00", 0.0.roundToString(2))
        assertEquals("-0.00", (-0.0).roundToString(2))
        assertEquals("-0.00", (-0.001).roundToString(2))
        assertEquals("NaN", Double.NaN.roundToString(2))
        assertEquals("Infinity", Double.POSITIVE_INFINITY.roundToString(2))
        assertEquals("-Infinity", Double.NEGATIVE_INFINITY.roundToString(2))
    }

    @Test
    fun roundToString_worksForFloats() {
        assertEquals("3.10", 3.1f.roundToString(2))
        assertEquals("1.23", 1.234f.roundToString(2))
        assertEquals("NaN", Float.NaN.roundToString(2))
        assertEquals("Infinity", Float.POSITIVE_INFINITY.roundToString(2))
    }

    @Test
    fun roundToString_supportsTheMaximumPrecision() {
        assertEquals("1.000000000000000", 1.0.roundToString(15))
        assertEquals("-0.000100", (-1.0E-4).roundToString(6))
    }

    @Test
    fun roundToString_rejectsInvalidDecimals() {
        assertFailsWith<IllegalArgumentException> { 1.0.roundToString(-1) }
        assertFailsWith<IllegalArgumentException> { 1.0.roundToString(16) }
    }

}
