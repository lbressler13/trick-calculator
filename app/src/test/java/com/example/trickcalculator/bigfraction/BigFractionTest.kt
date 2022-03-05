package com.example.trickcalculator.bigfraction

import com.example.trickcalculator.ext.toBI
import org.junit.Test
import org.junit.Assert.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class BigFractionTest {
    @Test
    fun testConstructor() {
        var bf = BigFraction(0)
        assertEquals(0.toBI(), bf.numerator)
        assertEquals(1.toBI(), bf.denominator)

        bf = BigFraction(3)
        assertEquals(3.toBI(), bf.numerator)
        assertEquals(1.toBI(), bf.denominator)

        bf = BigFraction(-3)
        assertEquals((-3).toBI(), bf.numerator)
        assertEquals(1.toBI(), bf.denominator)

        bf = BigFraction(3, 4)
        assertEquals(3.toBI(), bf.numerator)
        assertEquals(4.toBI(), bf.denominator)

        bf = BigFraction(3, -2)
        assertEquals((-3).toBI(), bf.numerator)
        assertEquals(2.toBI(), bf.denominator)

        var error = ""
        try {
            BigFraction(1, 0)
        } catch (e: ArithmeticException) {
            error = e.message.toString()
        }
        assertEquals("divide by zero", error)
    }

    @Test
    fun testSimplify() {
        // simplify 0
        var bf = BigFraction(0, 2)
        var expected = BigFraction(0)
        assertEquals(expected, bf)

        bf = BigFraction(0, -6)
        expected = BigFraction(0)
        assertEquals(expected, bf)

        // simplify exact division
        bf = BigFraction(4, 2)
        expected = BigFraction(2)
        assertEquals(expected, bf)

        bf = BigFraction(2, 4)
        expected = BigFraction(1, 2)
        assertEquals(expected, bf)

        // simplify sign
        bf = BigFraction(-3, -4)
        expected = BigFraction(3, 4)
        assertEquals(expected, bf)

        bf = BigFraction(1, -3)
        expected = BigFraction(-1, 3)
        assertEquals(expected, bf)

        // multiple simplifications
        bf = BigFraction(3, -9)
        expected = BigFraction(-1, 3)
        assertEquals(expected, bf)

        // unchanged
        bf = BigFraction(0)
        expected = BigFraction(0)
        assertEquals(expected, bf)

        bf = BigFraction(-4, 3)
        expected = BigFraction(-4, 3)
        assertEquals(expected, bf)

        bf = BigFraction(5, 7)
        expected = BigFraction(5, 7)
        assertEquals(expected, bf)
    }

    @Test
    fun testSetSign() {
        var bf = BigFraction(-3, -4)
        var expected = BigFraction(3, 4)
        assertEquals(expected, bf)

        bf = BigFraction(1, -3)
        expected = BigFraction(-1, 3)
        assertEquals(expected, bf)

        bf = BigFraction(1, 3)
        expected = BigFraction(1, 3)
        assertEquals(expected, bf)

        bf = BigFraction(-5, 2)
        expected = BigFraction(-5, 2)
        assertEquals(expected, bf)
    }

    // OTHERS
    @Test
    fun testInverse() {
        var bf = BigFraction(1, 2)
        var expected = BigFraction(2, 1)
        assertEquals(expected, bf.inverse())

        bf = BigFraction(2, 1)
        expected = BigFraction(1, 2)
        assertEquals(expected, bf.inverse())

        bf = BigFraction(-3, 2)
        expected = BigFraction(-2, 3)
        assertEquals(expected, bf.inverse())

        bf = BigFraction(-3, -9)
        expected = BigFraction(3, 1)
        assertEquals(expected, bf.inverse())

        bf = BigFraction(19, 7)
        expected = BigFraction(7, 19)
        assertEquals(expected, bf.inverse())

        var error = ""
        try {
            BigFraction(0, 1).inverse()
        } catch (e: ArithmeticException) {
            error = e.message.toString()
        }
        assertEquals("divide by zero", error)
    }

    @Test
    fun testAbs() {
        var bf = BigFraction(0)
        var expected = BigFraction(0)
        assertEquals(expected, abs(bf))

        bf = BigFraction(3)
        expected = BigFraction(3)
        assertEquals(expected, abs(bf))

        bf = BigFraction(-3)
        expected = BigFraction(3)
        assertEquals(expected, abs(bf))

        bf = BigFraction(3, 5)
        expected = BigFraction(3, 5)
        assertEquals(expected, abs(bf))

        bf = BigFraction(-5, 3)
        expected = BigFraction(5, 3)
        assertEquals(expected, abs(bf))
    }

    @Test
    fun testIsNegative() {
        var bf = BigFraction(0)
        assert(!bf.isNegative())

        bf = BigFraction(1)
        assert(!bf.isNegative())

        bf = BigFraction(2, 7)
        assert(!bf.isNegative())

        bf = BigFraction(-1)
        assert(bf.isNegative())

        bf = BigFraction(-2, 7)
        assert(bf.isNegative())
    }

    @Test
    fun testIsZero() {
        var bf = BigFraction(0)
        assert(bf.isZero())

        bf = BigFraction(1)
        assert(!bf.isZero())

        bf = BigFraction(2, 7)
        assert(!bf.isZero())

        bf = BigFraction(-1)
        assert(!bf.isZero())

        bf = BigFraction(-2, 7)
        assert(!bf.isZero())
    }

    // TO STRING

    @Test
    fun toDecimalString() {
        var bf = BigFraction(0)
        var expected = "0"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(4)
        expected = "4"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(-3)
        expected = "-3"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(1, 2)
        expected = "0.5"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(3, 8)
        expected = "0.375"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(-1, 9)
        expected = "-0.11111111"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(5, 9)
        expected = "0.55555556"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(-4, 19)
        expected = "-0.21052632"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(3, 8)
        expected = "0.38"
        assertEquals(expected, bf.toDecimalString(2))

        bf = BigFraction(-1, 9)
        expected = "-0.111111111111"
        assertEquals(expected, bf.toDecimalString(12))

        bf = BigFraction(-4, 19)
        expected = "-0.21053"
        assertEquals(expected, bf.toDecimalString(5))
    }

    @Test
    fun toFractionString() {
        var bf = BigFraction(0)
        var expected = "0"
        assertEquals(expected, bf.toFractionString())

        bf = BigFraction(4)
        expected = "4"
        assertEquals(expected, bf.toFractionString())

        bf = BigFraction(-3)
        expected = "-3"
        assertEquals(expected, bf.toFractionString())

        bf = BigFraction(2, 7)
        expected = "2/7"
        assertEquals(expected, bf.toFractionString())

        bf = BigFraction(-7, 2)
        expected = "-7/2"
        assertEquals(expected, bf.toFractionString())
    }

    @Test
    fun testToPairString() {
        var bf = BigFraction(0)
        var expected = "(0, 1)"
        assertEquals(expected, bf.toPairString())

        bf = BigFraction(4)
        expected = "(4, 1)"
        assertEquals(expected, bf.toPairString())

        bf = BigFraction(-3)
        expected = "(-3, 1)"
        assertEquals(expected, bf.toPairString())

        bf = BigFraction(2, 7)
        expected = "(2, 7)"
        assertEquals(expected, bf.toPairString())

        bf = BigFraction(-7, 2)
        expected = "(-7, 2)"
        assertEquals(expected, bf.toPairString())
    }

    @Test
    fun testToPair() {
        var bf = BigFraction(0)
        var expected = Pair(0.toBI(), 1.toBI())
        assertEquals(expected, bf.toPair())

        bf = BigFraction(1)
            expected = Pair(1.toBI(), 1.toBI())
        assertEquals(expected, bf.toPair())

        bf = BigFraction(2.toBI(), 7.toBI())
        expected = Pair(2.toBI(), 7.toBI())
        assertEquals(expected, bf.toPair())

        bf = BigFraction(-1)
        expected = Pair((-1).toBI(), 1.toBI())
        assertEquals(expected, bf.toPair())

        bf = BigFraction(-2, 7)
        expected = Pair((-2).toBI(), 7.toBI())
        assertEquals(expected, bf.toPair())
    }

    @Test
    fun testToBigDecimal() {
        var bf = BigFraction(0)
        var bd = BigDecimal(0)
        assertEquals(bd, bf.toBigDecimal())

        bf = BigFraction(10)
        bd = BigDecimal(10)
        assertEquals(bd, bf.toBigDecimal())

        bf = BigFraction(-10)
        bd = BigDecimal(-10)
        assertEquals(bd, bf.toBigDecimal())

        bf = BigFraction(1, 2)
        bd = BigDecimal(0.5)
        assertEquals(bd, bf.toBigDecimal())

        bf = BigFraction(-5, 4)
        bd = BigDecimal(-1.25)
        assertEquals(bd, bf.toBigDecimal())

        var mc = MathContext(8, RoundingMode.HALF_UP)
        bf = BigFraction(-4, 19)
        bd = BigDecimal(-0.21052632, mc)
        assertEquals(bd, bf.toBigDecimal(8))

        mc = MathContext(4, RoundingMode.HALF_UP)
        bf = BigFraction(1, 9)
        bd = BigDecimal(0.1111, mc)
        assertEquals(bd, bf.toBigDecimal(4))

        mc = MathContext(20, RoundingMode.HALF_UP)
        bf = BigFraction(5, 3)
        bd = BigDecimal("1.66666666666666666667", mc)
        assertEquals(bd, bf.toBigDecimal())
    }
}
