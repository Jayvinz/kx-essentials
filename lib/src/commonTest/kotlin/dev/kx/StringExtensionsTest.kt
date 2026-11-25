package dev.kx

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class StringExtensionsTest {

    @Test
    fun toTitleCase_uppercasesEachWordAndLowercasesTheRest() {
        assertEquals("Hello   Kotlin World", "hello   kotlin WORLD".toTitleCase())
        assertEquals("Hello World", "HELLO WORLD".toTitleCase())
        assertEquals("A", "a".toTitleCase())
    }

    @Test
    fun toTitleCase_preservesSurroundingAndLineBreaks() {
        assertEquals("  Hello  ", "  hELLO  ".toTitleCase())
        assertEquals("Hello\nWorld", "hello\nworld".toTitleCase())
        assertEquals("", "".toTitleCase())
    }

    @Test
    fun truncate_returnsShortStringsUnchanged() {
        assertEquals("kotlin", "kotlin".truncate(6))
        assertEquals("kotlin", "kotlin".truncate(64))
        assertEquals("", "".truncate(0))
    }

    @Test
    fun truncate_replacesTheCutPartWithTheEllipsis() {
        assertEquals("Kotlin…", "Kotlin extensions".truncate(7))
        assertEquals("Kotlin ex...", "Kotlin extensions are fun".truncate(12, "..."))
        assertEquals("a..", "abcdef".truncate(3, ".."))
    }

    @Test
    fun truncate_dropsTheEllipsisWhenItDoesNotFit() {
        assertEquals("ab", "abcdef".truncate(2, "..."))
        assertEquals("ab", "abcdef".truncate(2, ""))
        assertEquals("", "abcdef".truncate(0))
    }

    @Test
    fun truncate_rejectsNegativeLengths() {
        assertFailsWith<IllegalArgumentException> { "abcdef".truncate(-1) }
    }
}

class NullIfBlankTest {

    @Test
    fun nullIfBlank_returnsNullForEmptyAndWhitespaceOnlyStrings() {
        assertNull("".nullIfBlank())
        assertNull("   ".nullIfBlank())
        assertNull("\n\t ".nullIfBlank())
    }

    @Test
    fun nullIfBlank_returnsTheOriginalStringWhenNotBlank() {
        assertEquals("kx", "kx".nullIfBlank())
        assertEquals(" kx ", " kx ".nullIfBlank())
    }
}

class SplitAtTest {

    @Test
    fun splitAtFirst_splitsAtTheFirstOccurrence() {
        assertEquals("a" to "b-c", "a-b-c".splitAtFirst('-'))
        assertEquals("a" to "b::c", "a::b::c".splitAtFirst("::"))
    }

    @Test
    fun splitAtFirst_returnsNullWhenTheSeparatorIsMissing() {
        assertNull("abc".splitAtFirst('-'))
        assertNull("abc".splitAtFirst("::"))
    }

    @Test
    fun splitAtFirst_handlesAnEmptySeparator() {
        assertEquals("" to "abc", "abc".splitAtFirst(""))
    }

    @Test
    fun splitAtFirst_handlesSeparatorsAtTheEdges() {
        assertEquals("" to "abc", "-abc".splitAtFirst('-'))
        assertEquals("abc" to "", "abc-".splitAtFirst('-'))
    }

    @Test
    fun splitAtLast_splitsAtTheLastOccurrence() {
        assertEquals("a-b" to "c", "a-b-c".splitAtLast('-'))
        assertEquals("a::b" to "c", "a::b::c".splitAtLast("::"))
    }

    @Test
    fun splitAtLast_returnsNullWhenTheSeparatorIsMissing() {
        assertNull("abc".splitAtLast('-'))
        assertNull("abc".splitAtLast("::"))
    }

    @Test
    fun splitAtLast_handlesAnEmptySeparator() {
        assertEquals("abc" to "", "abc".splitAtLast(""))
    }

    @Test
    fun splitAtLast_handlesSeparatorsAtTheEdges() {
        assertEquals("" to "abc", "-abc".splitAtLast('-'))
        assertEquals("abc" to "", "abc-".splitAtLast('-'))
    }
}
