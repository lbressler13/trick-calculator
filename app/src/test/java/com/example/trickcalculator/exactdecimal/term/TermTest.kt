package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactdecimal.Term
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Test
import org.junit.Assert.*

class TermTest {
    @Test fun testConstructor() = runTermConstructorTests()

    @Test fun testTimes() = runTermTimesTests()
    @Test fun testPlus() = runTermPlusTests()
    @Test fun testEquals() = runTermEqualsTests()
    @Test fun testCompareTo() = runTermCompareToTests()

    @Test fun testUnaryMinus() = runTermUnaryMinusTests()
    @Test fun testIsZero() = runTermIsZeroTests()
    @Test fun testIsNotZero() = runTermIsNotZeroTests()
    @Test fun testIsNegative() = runTermIsNegativeTests()

    @Test
    fun testToString() {
        var ef = ExactFraction.ZERO
        var t = Term(ef)
        var expected = "${ef}pi^0"
        assertEquals(expected, t.toString())

        ef = -ExactFraction.THREE
        t = Term(ef)
        expected = "${ef}pi^0"
        assertEquals(expected, t.toString())

        ef = ExactFraction.ONE
        t = Term(ef, 3)
        expected = "${ef}pi^3"
        assertEquals(expected, t.toString())

        ef = ExactFraction(-4, 17)
        t = Term(ef, -3)
        expected = "${ef}pi^-3"
        assertEquals(expected, t.toString())

        ef = ExactFraction(102, 35)
        t = Term(ef, 26)
        expected = "${ef}pi^26"
        assertEquals(expected, t.toString())
    }
}
