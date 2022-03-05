package com.example.trickcalculator

import org.junit.Test
import org.junit.Assert.*

class BigFractionTest {
    @Test
    fun testConstructor() {
        var bf = BigFraction(0)
        assertEquals(0, bf.numerator)
        assertEquals(1, bf.denominator)

        bf = BigFraction(3)
        assertEquals(3, bf.numerator)
        assertEquals(1, bf.denominator)

        bf = BigFraction(-3)
        assertEquals(-3, bf.numerator)
        assertEquals(1, bf.denominator)

        bf = BigFraction(3, 4)
        assertEquals(3, bf.numerator)
        assertEquals(4, bf.denominator)

        bf = BigFraction(3, -2)
        assertEquals(-3, bf.numerator)
        assertEquals(2, bf.denominator)

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
        var expected = BigFraction(0, 1)
        assertEquals(expected, bf)

        bf = BigFraction(0, -6)
        expected = BigFraction(0, 1)
        assertEquals(expected, bf)

        // simplify exact division
        bf = BigFraction(4, 2)
        expected = BigFraction(2, 1)
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
        bf = BigFraction(0, 1)
        expected = BigFraction(0, 1)
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
        expected = "-0.11111"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(5, 9)
        expected = "0.55556"
        assertEquals(expected, bf.toDecimalString())

        bf = BigFraction(-4, 19)
        expected = "-0.21053"
        assertEquals(expected, bf.toDecimalString())
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
        var expected = Pair(0, 1)
        assertEquals(expected, bf.toPair())

        bf = BigFraction(1)
        expected = Pair(1, 1)
        assertEquals(expected, bf.toPair())

        bf = BigFraction(2, 7)
        expected = Pair(2, 7)
        assertEquals(expected, bf.toPair())

        bf = BigFraction(-1)
        expected = Pair(-1, 1)
        assertEquals(expected, bf.toPair())

        bf = BigFraction(-2, 7)
        expected = Pair(-2, 7)
        assertEquals(expected, bf.toPair())
    }

    @Test
    fun testParse() {}
}