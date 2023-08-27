package xyz.lbres.exactnumbers.irrationals.sqrt

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import xyz.lbres.exactnumbers.irrationals.common.Memoize
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {
    @AfterTest
    fun resetMockk() {
        unmockkAll()
    }

    @Test
    fun testGetRootOf() {
        mockkObject(Memoize)
        every { Memoize.individualWholeNumber } answers { mutableMapOf() }

        // rational
        var num = BigInteger.ZERO
        var expected = BigDecimal.ZERO
        assertEquals(expected, getRootOf(num))

        num = BigInteger.ONE
        expected = BigDecimal.ONE
        assertEquals(expected, getRootOf(num))

        num = BigInteger("961")
        expected = BigDecimal("31")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("17424")
        expected = BigDecimal("132")
        assertEquals(expected, getRootOf(num))

        // irrational
        num = BigInteger.TWO
        expected = BigDecimal("1.4142135623730950488")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("8")
        expected = BigDecimal("2.8284271247461900976")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("1333333")
        expected = BigDecimal("1154.7003940416752105")
        assertEquals(expected, getRootOf(num))
    }

    @Test fun testExtractWholeOf() = runExtractWholeOfTests()
}
