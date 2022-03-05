package com.example.trickcalculator

import org.junit.Test
import org.junit.Assert.*

class BigFractionTest {
    // CONSTRUCTORS
    @Test
    fun testConstructors() {
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
    fun testIsNegative() {}

    @Test
    fun testIsZero() {}

    // TO STRING

    @Test
    fun toDecimalString() {}

    @Test
    fun toFractionString() {}

    @Test
    fun testToPairString() {}

    @Test
    fun testHashCode() {}

    // CASTING

    @Test
    fun testToPair() {}

    // OBJECT FUNCTIONS

    @Test
    fun testParse() {}
}