package xyz.lbres.exactnumbers.irrationals.sqrt

import assertDivByZero
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Memoize
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SqrtTest {
    @BeforeTest
    fun setupMockk() {
        mockkObject(Memoize)
        every { Memoize.individualWholeNumber } answers { mutableMapOf() }
    }

    @AfterTest
    fun resetMockk() {
        unmockkAll()
    }

    @Test
    fun testConstructor() {
        // errors
        val expectedMessage = "Cannot calculate root of a negative number"
        assertFailsWith<ArithmeticException>(expectedMessage) { Sqrt(-ExactFraction.EIGHT) }
        assertFailsWith<ArithmeticException>(expectedMessage) { Sqrt(-8) }
        assertFailsWith<ArithmeticException>(expectedMessage) { Sqrt(-8L) }
        assertFailsWith<ArithmeticException>(expectedMessage) { Sqrt(BigInteger("-8")) }
        assertFailsWith<ArithmeticException>(expectedMessage) { Sqrt(-ExactFraction.HALF) }

        // no error
        var sqrts = listOf(Sqrt(ExactFraction.ZERO), Sqrt(0), Sqrt(0L), Sqrt(BigInteger.ZERO))
        var expectedRadicand = ExactFraction.ZERO
        sqrts.forEach {
            assertEquals(expectedRadicand, it.radicand)
        }

        sqrts = listOf(Sqrt(ExactFraction.EIGHT), Sqrt(8), Sqrt(8L), Sqrt(BigInteger("8")))
        expectedRadicand = ExactFraction.EIGHT
        sqrts.forEach {
            assertEquals(expectedRadicand, it.radicand)
        }

        var sqrt = Sqrt(ExactFraction(1000, 99))
        expectedRadicand = ExactFraction(1000, 99)
        assertEquals(expectedRadicand, sqrt.radicand)

        sqrt = Sqrt(ExactFraction(103, 782))
        expectedRadicand = ExactFraction(103, 782)
        assertEquals(expectedRadicand, sqrt.radicand)
    }

    @Test
    fun testIsZero() {
        // zero
        var sqrt = Sqrt.ZERO
        assertTrue(sqrt.isZero())

        // not zero
        sqrt = Sqrt.ONE
        assertFalse(sqrt.isZero())

        sqrt = Sqrt(ExactFraction(15, 1234))
        assertFalse(sqrt.isZero())

        sqrt = Sqrt(ExactFraction(12000, 179))
        assertFalse(sqrt.isZero())

        sqrt = Sqrt(2)
        assertFalse(sqrt.isZero())
    }

    @Test
    fun testSwapDivided() {
        // error
        assertDivByZero { Sqrt.ZERO.swapDivided() }

        // no error
        var sqrt = Sqrt(8)
        var expected = Sqrt(ExactFraction(1, 8))
        assertEquals(expected, sqrt.swapDivided())

        sqrt = Sqrt(ExactFraction.HALF)
        expected = Sqrt(2)
        assertEquals(expected, sqrt.swapDivided())

        sqrt = Sqrt(ExactFraction(100, 49))
        expected = Sqrt(ExactFraction(49, 100))
        assertEquals(expected, sqrt.swapDivided())

        sqrt = Sqrt(ExactFraction(1, 8))
        expected = Sqrt(8)
        assertEquals(expected, sqrt.swapDivided())
    }

    @Test
    fun testIsRational() {
        // rational
        var sqrt = Sqrt.ZERO
        assertTrue(sqrt.isRational())

        sqrt = Sqrt.ONE
        assertTrue(sqrt.isRational())

        sqrt = Sqrt(961)
        assertTrue(sqrt.isRational())

        sqrt = Sqrt(ExactFraction(1, 64))
        assertTrue(sqrt.isRational())

        sqrt = Sqrt(ExactFraction(81, 49))
        assertTrue(sqrt.isRational())

        // irrational
        sqrt = Sqrt(2)
        assertFalse(sqrt.isRational())

        sqrt = Sqrt(ExactFraction(1, 35))
        assertFalse(sqrt.isRational())

        sqrt = Sqrt(ExactFraction(49, 10))
        assertFalse(sqrt.isRational())
    }

    @Test
    fun testGetRationalValue() {
        // irrational
        var sqrt = Sqrt(2)
        assertNull(sqrt.getRationalValue())

        sqrt = Sqrt(ExactFraction(64, 15))
        assertNull(sqrt.getRationalValue())

        sqrt = Sqrt(155)
        assertNull(sqrt.getRationalValue())

        // rational
        sqrt = Sqrt.ZERO
        var expected = ExactFraction.ZERO
        assertEquals(expected, sqrt.getRationalValue())

        sqrt = Sqrt.ONE
        expected = ExactFraction.ONE
        assertEquals(expected, sqrt.getRationalValue())

        sqrt = Sqrt(2209)
        expected = ExactFraction(47)
        assertEquals(expected, sqrt.getRationalValue())

        sqrt = Sqrt(ExactFraction(1, 4))
        expected = ExactFraction.HALF
        assertEquals(expected, sqrt.getRationalValue())

        sqrt = Sqrt(ExactFraction(1, 100))
        expected = ExactFraction(1, 10)
        assertEquals(expected, sqrt.getRationalValue())

        sqrt = Sqrt(ExactFraction(81, 64))
        expected = ExactFraction(9, 8)
        assertEquals(expected, sqrt.getRationalValue())
    }

    @Test
    fun getValue() {
        var sqrt = Sqrt.ZERO
        var expected = BigDecimal.ZERO
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt.ONE
        expected = BigDecimal.ONE
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(144)
        expected = BigDecimal("12")
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(ExactFraction(1, 36))
        expected = BigDecimal("0.16666666666666666667")
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(ExactFraction(25, 16))
        expected = BigDecimal("1.25")
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(7)
        expected = BigDecimal("2.6457513110645905905")
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(32)
        expected = BigDecimal("5.6568542494923801952")
        assertEquals(expected, sqrt.getValue())

        sqrt = Sqrt(ExactFraction(17, 49))
        expected = BigDecimal("0.58901508937395150711")
        assertEquals(expected, sqrt.getValue())
    }

    @Test
    fun testEquals() {
        // equal
        var sqrt1 = Sqrt.ZERO
        assertEquals(sqrt1, sqrt1)

        sqrt1 = Sqrt(6)
        assertEquals(sqrt1, sqrt1)

        sqrt1 = Sqrt(ExactFraction(9, 400))
        assertEquals(sqrt1, sqrt1)

        // not equal
        sqrt1 = Sqrt.ZERO
        var sqrt2 = Sqrt.ONE
        assertNotEquals(sqrt1, sqrt2)
        assertNotEquals(sqrt2, sqrt1)

        sqrt1 = Sqrt(2)
        sqrt2 = Sqrt(ExactFraction.HALF)
        assertNotEquals(sqrt1, sqrt2)
        assertNotEquals(sqrt2, sqrt1)

        sqrt1 = Sqrt(ExactFraction(9, 25))
        sqrt2 = Sqrt(ExactFraction.NINE)
        assertNotEquals(sqrt1, sqrt2)
        assertNotEquals(sqrt2, sqrt1)

        sqrt1 = Sqrt(ExactFraction(103, 422))
        sqrt2 = Sqrt(ExactFraction(90, 37))
        assertNotEquals(sqrt1, sqrt2)
        assertNotEquals(sqrt2, sqrt1)
    }

    @Test
    fun testGetSimplified() {
        val one = ExactFraction.ONE

        // rational
        var sqrt = Sqrt.ZERO
        var expected = Pair(one, Sqrt.ZERO)
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt.ONE
        expected = Pair(one, Sqrt.ONE)
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(1024)
        expected = Pair(ExactFraction(32), Sqrt.ONE)
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(ExactFraction(9, 25))
        expected = Pair(ExactFraction(3, 5), Sqrt.ONE)
        assertEquals(expected, sqrt.getSimplified())

        // rational w/ whole
        sqrt = Sqrt(50)
        expected = Pair(ExactFraction.FIVE, Sqrt(ExactFraction.TWO))
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(3000)
        expected = Pair(ExactFraction.TEN, Sqrt(ExactFraction(30)))
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(ExactFraction(50, 27))
        expected = Pair(ExactFraction(5, 3), Sqrt(ExactFraction(2, 3)))
        assertEquals(expected, sqrt.getSimplified())

        // no whole
        sqrt = Sqrt(15)
        expected = Pair(one, sqrt)
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(107)
        expected = Pair(one, sqrt)
        assertEquals(expected, sqrt.getSimplified())

        sqrt = Sqrt(ExactFraction(94, 46))
        expected = Pair(one, sqrt)
        assertEquals(expected, sqrt.getSimplified())
    }

    @Test
    fun testCompareTo() {
        // equal
        var sqrt1 = Sqrt.ZERO
        assertEquals(sqrt1, sqrt1)

        sqrt1 = Sqrt(ExactFraction(9, 11))
        assertEquals(sqrt1, sqrt1)

        // not equal
        sqrt1 = Sqrt.ZERO
        var sqrt2 = Sqrt.ONE
        assertTrue(sqrt1 < sqrt2)
        assertTrue(sqrt2 > sqrt1)

        sqrt1 = Sqrt.ZERO
        sqrt2 = Sqrt.ONE
        assertTrue(sqrt1 < sqrt2)
        assertTrue(sqrt2 > sqrt1)

        sqrt1 = Sqrt(3)
        sqrt2 = Sqrt(9)
        assertTrue(sqrt1 < sqrt2)
        assertTrue(sqrt2 > sqrt1)

        sqrt1 = Sqrt(ExactFraction(15, 26))
        sqrt2 = Sqrt(ExactFraction(26, 15))
        assertTrue(sqrt1 < sqrt2)
        assertTrue(sqrt2 > sqrt1)
    }

    @Test
    fun testToString() {
        val symbol = "√"

        // whole number
        var sqrt = Sqrt(ExactFraction.ZERO)
        var expected = "[$symbol(0)]"
        assertEquals(expected, sqrt.toString())

        sqrt = Sqrt(10)
        expected = "[$symbol(10)]"
        assertEquals(expected, sqrt.toString())

        sqrt = Sqrt(1234567)
        expected = "[$symbol(1234567)]"
        assertEquals(expected, sqrt.toString())

        // fraction
        sqrt = Sqrt(ExactFraction.HALF)
        expected = "[$symbol(1/2)]"
        assertEquals(expected, sqrt.toString())

        sqrt = Sqrt(ExactFraction(12, 35))
        expected = "[$symbol(12/35)]"
        assertEquals(expected, sqrt.toString())
    }

    @Test fun testSimplifyList() = runSimplifyListTests()
}
