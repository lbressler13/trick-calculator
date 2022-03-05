package com.example.trickcalculator.bigfraction

import org.junit.Test
import org.junit.Assert.*

// tests for binary operators
class BigFractionTest_binary {
    @Test
    fun testPlus() {
        // add zero
        var first = BigFraction(0)
        var second = BigFraction(0)
        var expected = BigFraction(0, 1)
        assertEquals(expected, first + second)

        first = BigFraction(4)
        second = BigFraction(0)
        expected = BigFraction(4, 1)
        assertEquals(expected, first + second)

        first = BigFraction(-4)
        second = BigFraction(0)
        expected = BigFraction(-4, 1)
        assertEquals(expected, first + second)

        first = BigFraction(0)
        second = BigFraction(-4)
        expected = BigFraction(-4, 1)
        assertEquals(expected, first + second)

        // whole numbers
        first = BigFraction(4)
        second = BigFraction(1)
        expected = BigFraction(5, 1)
        assertEquals(expected, first + second)

        first = BigFraction(-12)
        second = BigFraction(33)
        expected = BigFraction(21, 1)
        assertEquals(expected, first + second)

        first = BigFraction(-12)
        second = BigFraction(-6)
        expected = BigFraction(-18, 1)
        assertEquals(expected, first + second)

        // same denominator
        first = BigFraction(5, 3)
        second = BigFraction(-6, 3)
        expected = BigFraction(-1, 3)
        assertEquals(expected, first + second)

        first = BigFraction(5, 3)
        second = BigFraction(-2, 3)
        expected = BigFraction(1, 1)
        assertEquals(expected, first + second)

        first = BigFraction(5, 19)
        second = BigFraction(11, 19)
        expected = BigFraction(16, 19)
        assertEquals(expected, first + second)

        first = BigFraction(24, 19)
        second = BigFraction(32, 19)
        expected = BigFraction(56, 19)
        assertEquals(expected, first + second)

        // different denominator
        first = BigFraction(5, 1)
        second = BigFraction(-4, 3)
        expected = BigFraction(11, 3)
        assertEquals(expected, first + second)

        first = BigFraction(5, 2)
        second = BigFraction(7, 3)
        expected = BigFraction(29, 6)
        assertEquals(expected, first + second)

        first = BigFraction(5, 12)
        second = BigFraction(3, 11)
        expected = BigFraction(91, 132)
        assertEquals(expected, first + second)

        first = BigFraction(4, 8)
        second = BigFraction(-1, 3)
        expected = BigFraction(1, 6)
        assertEquals(expected, first + second)
    }

    @Test
    fun testMinus() {
        // zero
        var first = BigFraction(0)
        var second = BigFraction(0)
        var expected = BigFraction(0, 1)
        assertEquals(expected, first - second)

        first = BigFraction(4)
        second = BigFraction(0)
        expected = BigFraction(4, 1)
        assertEquals(expected, first - second)

        first = BigFraction(-4)
        second = BigFraction(0)
        expected = BigFraction(-4, 1)
        assertEquals(expected, first - second)

        first = BigFraction(0)
        second = BigFraction(4)
        expected = BigFraction(-4, 1)
        assertEquals(expected, first - second)

        first = BigFraction(0)
        second = BigFraction(-4)
        expected = BigFraction(4, 1)
        assertEquals(expected, first - second)

        // whole numbers
        first = BigFraction(4)
        second = BigFraction(1)
        expected = BigFraction(3, 1)
        assertEquals(expected, first - second)

        first = BigFraction(-12)
        second = BigFraction(33)
        expected = BigFraction(-45, 1)
        assertEquals(expected, first - second)

        first = BigFraction(12)
        second = BigFraction(-33)
        expected = BigFraction(45, 1)
        assertEquals(expected, first - second)

        first = BigFraction(-12)
        second = BigFraction(-6)
        expected = BigFraction(-6, 1)
        assertEquals(expected, first - second)

        // same denominator
        first = BigFraction(5, 3)
        second = BigFraction(-6, 3)
        expected = BigFraction(11, 3)
        assertEquals(expected, first - second)

        first = BigFraction(-5, 3)
        second = BigFraction(2, 3)
        expected = BigFraction(-7, 3)
        assertEquals(expected, first - second)

        first = BigFraction(5, 19)
        second = BigFraction(11, 19)
        expected = BigFraction(-6, 19)
        assertEquals(expected, first - second)

        first = BigFraction(-24, 19)
        second = BigFraction(-32, 19)
        expected = BigFraction(8, 19)
        assertEquals(expected, first - second)

        // different denominator
        first = BigFraction(5, 1)
        second = BigFraction(-4, 3)
        expected = BigFraction(19, 3)
        assertEquals(expected, first - second)

        first = BigFraction(5, 2)
        second = BigFraction(7, 3)
        expected = BigFraction(1, 6)
        assertEquals(expected, first - second)

        first = BigFraction(-5, 12)
        second = BigFraction(3, 11)
        expected = BigFraction(-91, 132)
        assertEquals(expected, first - second)

        first = BigFraction(-4, 8)
        second = BigFraction(-1, 3)
        expected = BigFraction(-4, 24)
        assertEquals(expected, first - second)
    }

    @Test
    fun testTimes() {
        // zero
        var first = BigFraction(0)
        var second = BigFraction(0)
        var expected = BigFraction(0, 1)
        assertEquals(expected, first * second)

        first = BigFraction(1)
        second = BigFraction(0)
        expected = BigFraction(0, 1)
        assertEquals(expected, first * second)

        first = BigFraction(0)
        second = BigFraction(1)
        expected = BigFraction(0, 1)
        assertEquals(expected, first * second)

        // whole numbers
        first = BigFraction(1)
        second = BigFraction(8)
        expected = BigFraction(8, 1)
        assertEquals(expected, first * second)

        first = BigFraction(7)
        second = BigFraction(23)
        expected = BigFraction(161, 1)
        assertEquals(expected, first * second)

        first = BigFraction(-17)
        second = BigFraction(9)
        expected = BigFraction(-153, 1)
        assertEquals(expected, first * second)

        first = BigFraction(-5)
        second = BigFraction(-7)
        expected = BigFraction(35, 1)
        assertEquals(expected, first * second)

        first = BigFraction(-5)
        second = BigFraction(-7)
        expected = BigFraction(35, 1)
        assertEquals(expected, first * second)

        // fractions
        first = BigFraction(7, 11)
        second = BigFraction(3, 12)
        expected = BigFraction(21, 132)
        assertEquals(expected, first * second)

        first = BigFraction(-1, 3)
        second = BigFraction(-4, 12)
        expected = BigFraction(1, 9)
        assertEquals(expected, first * second)

        first = BigFraction(11, 3)
        second = BigFraction(4, 3)
        expected = BigFraction(44, 9)
        assertEquals(expected, first * second)

        first = BigFraction(6, 4)
        second = BigFraction(8, 3)
        expected = BigFraction(4, 1)
        assertEquals(expected, first * second)

        first = BigFraction(-6, 7)
        second = BigFraction(8, 3)
        expected = BigFraction(-48, 21)
        assertEquals(expected, first * second)

        first = BigFraction(6, 7)
        second = BigFraction(-8, 3)
        expected = BigFraction(-48, 21)
        assertEquals(expected, first * second)

        first = BigFraction(6, 7)
        second = BigFraction(7, 6)
        expected = BigFraction(1)
        assertEquals(expected, first * second)
    }

    @Test
    fun testDiv() {
        // 0
        var first = BigFraction(0)
        var second = BigFraction(2, 3)
        var expected = BigFraction(0)
        assertEquals(expected, first / second)

        first = BigFraction(1)
        second = BigFraction(0)
        var error = ""

        try {
            first / second
        } catch (e: ArithmeticException) {
            error = e.message.toString()
        }
        assertEquals("divide by zero", error)

        first = BigFraction(0)
        second = BigFraction(0)
        error = ""

        try {
            first / second
        } catch (e: ArithmeticException) {
            error = e.message.toString()
        }
        assertEquals("divide by zero", error)

        // whole numbers
        first = BigFraction(8)
        second = BigFraction(2)
        expected = BigFraction(4)
        assertEquals(expected, first / second)

        first = BigFraction(2)
        second = BigFraction(8)
        expected = BigFraction(1, 4)
        assertEquals(expected, first / second)

        first = BigFraction(-7)
        second = BigFraction(9)
        expected = BigFraction(-7, 9)
        assertEquals(expected, first / second)

        first = BigFraction(-7)
        second = BigFraction(-9)
        expected = BigFraction(7, 9)
        assertEquals(expected, first / second)

        first = BigFraction(9)
        second = BigFraction(-7)
        expected = BigFraction(-9, 7)
        assertEquals(expected, first / second)

        // fractions
        first = BigFraction(9, 2)
        second = BigFraction(3, 7)
        expected = BigFraction(63, 6)
        assertEquals(expected, first / second)

        first = BigFraction(3, 2)
        second = BigFraction(3, 2)
        expected = BigFraction(1)
        assertEquals(expected, first / second)

        first = BigFraction(3, 2)
        second = BigFraction(3, -2)
        expected = BigFraction(-1)
        assertEquals(expected, first / second)

        first = BigFraction(-2, 13)
        second = BigFraction(-4, 5)
        expected = BigFraction(10, 52)
        assertEquals(expected, first / second)

        first = BigFraction(-3, 10)
        second = BigFraction(3, 2)
        expected = BigFraction(-1, 5)
        assertEquals(expected, first / second)

        first = BigFraction(3, 10)
        second = BigFraction(-3, 2)
        expected = BigFraction(-1, 5)
        assertEquals(expected, first / second)
    }

    @Test
    fun testCompareTo() {
        // equal values
        var first = BigFraction(0)
        var second = BigFraction(0)
        assert(first <= second)
        assert(first >= second)

        first = BigFraction(100)
        second = BigFraction(100)
        assert(first <= second)
        assert(first >= second)

        // pos less than zero
        first = BigFraction(3)
        second = BigFraction(0)
        assert(first > second)
        assert(first >= second)
        assert(second < first)
        assert(second <= first)

        // neg less than zero
        first = BigFraction(-3)
        second = BigFraction(0)
        assert(first < second)
        assert(first <= second)
        assert(second > first)
        assert(second >= first)

        // pos less than neg
        first = BigFraction(-1)
        second = BigFraction(1)
        assert(first < second)
        assert(first <= second)
        assert(second > first)
        assert(second >= first)

        // neg order
        first = BigFraction(-3)
        second = BigFraction(-2)
        assert(first < second)
        assert(first <= second)
        assert(second > first)
        assert(second >= first)

        // pos order
        first = BigFraction(3)
        second = BigFraction(2)
        assert(first > second)
        assert(first >= second)
        assert(second < first)
        assert(second <= first)
    }

    @Test
    fun testEquals() {
        // BigFraction
        assertEquals(BigFraction(0, 1), BigFraction(0, 1))
        assertEquals(BigFraction(-1, 3), BigFraction(-1, 3))
        assertEquals(BigFraction(5, 2), BigFraction(5, 2))

        assertNotEquals(BigFraction(1, 3), BigFraction(-1, 3))
        assertNotEquals(BigFraction(2, 3), BigFraction(3, 2))

        // Long

        // Int

        // Short

        // Char

        // Byte

        // BigDecimal

        // Other
    }
}