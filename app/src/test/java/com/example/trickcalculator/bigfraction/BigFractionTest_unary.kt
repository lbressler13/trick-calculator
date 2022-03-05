package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import org.junit.Test

// tests for unary operators
class BigFractionTest_unary {
    @Test
    fun testUnaryMinus() {
        var bf = BigFraction(0)
        var result = -bf
        assertEquals(0, result.numerator)
        assertEquals(1, result.denominator)

        bf = BigFraction(3)
        result = -bf
        assertEquals(-3, result.numerator)
        assertEquals(1, result.denominator)

        bf = BigFraction(-3)
        result = -bf
        assertEquals(3, result.numerator)
        assertEquals(1, result.denominator)

        bf = BigFraction(5, 2)
        result = -bf
        assertEquals(-5, result.numerator)
        assertEquals(2, result.denominator)

        bf = BigFraction(-2, 5)
        result = -bf
        assertEquals(2, result.numerator)
        assertEquals(5, result.denominator)
    }

    @Test
    fun testUnaryPlus() {
        var bf = BigFraction(0)
        var result = +bf
        var expected = BigFraction(0, 1)
        assertEquals(expected, result)

        bf = BigFraction(3)
        result = +bf
        expected = BigFraction(3, 1)
        assertEquals(expected, result)

        bf = BigFraction(-3)
        result = +bf
        expected = BigFraction(-3, 1)
        assertEquals(expected, result)

        bf = BigFraction(5, 2)
        result = +bf
        expected = BigFraction(5, 2)
        assertEquals(expected, result)

        bf = BigFraction(-2, 5)
        result = +bf
        expected = BigFraction(-2, 5)
        assertEquals(expected, result)
    }

    @Test
    fun testNot() {
        assert(!BigFraction(0))
        assert(!BigFraction(0, -3))
        assertEquals(false, !BigFraction(1))
        assertEquals(false, !BigFraction(-1))
        assertEquals(false, !BigFraction(1, 3))
    }

    @Test
    fun testInc() {
        var bf = BigFraction(3)
        bf++
        var expected = BigFraction(4, 1)
        assertEquals(expected, bf)

        bf = BigFraction(-3)
        bf++
        expected = BigFraction(-2, 1)
        assertEquals(expected, bf)

        bf = BigFraction(0)
        bf++
        expected = BigFraction(1, 1)
        assertEquals(expected, bf)

        bf = BigFraction(-1)
        bf++
        expected = BigFraction(0, 1)
        assertEquals(expected, bf)

        bf = BigFraction(6, 7)
        bf++
        expected = BigFraction(13, 7)
        assertEquals(expected, bf)

        bf = BigFraction(-7, 9)
        bf++
        expected = BigFraction(2, 9)
        assertEquals(expected, bf)
    }

    @Test
    fun testDec() {
        var bf = BigFraction(3)
        bf--
        var expected = BigFraction(2, 1)
        assertEquals(expected, bf)

        bf = BigFraction(-3)
        bf--
        expected = BigFraction(-4, 1)
        assertEquals(expected, bf)

        bf = BigFraction(0)
        bf--
        expected = BigFraction(-1, 1)
        assertEquals(expected, bf)

        bf = BigFraction(1)
        bf--
        expected = BigFraction(0, 1)
        assertEquals(expected, bf)

        bf = BigFraction(6, 7)
        bf--
        expected = BigFraction(-1, 7)
        assertEquals(expected, bf)

        bf = BigFraction(15, 9)
        bf--
        expected = BigFraction(6, 9)
        assertEquals(expected, bf)
    }
}