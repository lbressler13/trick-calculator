package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.bigfraction.BigFraction
import org.junit.Assert.*

fun runInverseTests() {
    var ed = ExactDecimal(0)
    val error = assertThrows(ArithmeticException::class.java) { ed.inverse() }
    val expectedError = "divide by zero"
    assertEquals(expectedError, error.message)

    ed = ExactDecimal(-8)
    var expected = ExactDecimal(BigFraction(-1, 8))
    assertEquals(expected, ed.inverse())

    ed = ExactDecimal(listOf("5 + pi", "2 - pi"), listOf("6", "7"), BigFraction(14, 3))
    expected = ExactDecimal(listOf("6", "7"), listOf("5 + pi", "2 - pi"), BigFraction(3, 14))
    assertEquals(expected, ed.inverse())
}

fun runIsZeroTests() {
    var ed = ExactDecimal(0)
    assert(ed.isZero())

    ed = ExactDecimal(listOf("6"), listOf("pi + 5"), BigFraction.ZERO)
    assert(ed.isZero())

    ed = ExactDecimal(2)
    assert(!ed.isZero())

    ed = ExactDecimal(BigFraction(-7, 11))
    assert(!ed.isZero())

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), 4)
    assert(!ed.isZero())

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), BigFraction(12, 19))
    assert(!ed.isZero())
}
