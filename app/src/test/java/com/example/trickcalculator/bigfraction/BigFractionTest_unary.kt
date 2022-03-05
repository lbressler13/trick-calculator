package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import org.junit.Test

// tests for unary operators
class BigFractionTest_unary {
    @Test
    fun testUnaryMinus() {
        var bf = BigFraction(0)
        var expected = BigFraction(0)
        assertEquals(expected, -bf)

        bf = BigFraction(3)
        expected = BigFraction(-3)
        assertEquals(expected, -bf)

        bf = BigFraction(-3)
        expected = BigFraction(3)
        assertEquals(expected, -bf)

        bf = BigFraction(5, 2)
        expected = BigFraction(-5, 2)
        assertEquals(expected, -bf)

        bf = BigFraction(-2, 5)
        expected = BigFraction(2, 5)
        assertEquals(expected, -bf)
    }

    @Test
    fun testUnaryPlus() {
        var bf = BigFraction(0)
        var expected = BigFraction(0, 1)
        assertEquals(expected, +bf)

        bf = BigFraction(3)
        expected = BigFraction(3, 1)
        assertEquals(expected, +bf)

        bf = BigFraction(-3)
        expected = BigFraction(-3, 1)
        assertEquals(expected, +bf)

        bf = BigFraction(5, 2)
        expected = BigFraction(5, 2)
        assertEquals(expected, +bf)

        bf = BigFraction(-2, 5)
        expected = BigFraction(-2, 5)
        assertEquals(expected, +bf)
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
