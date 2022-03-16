package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runUnaryMinusTests() {
    var ed = ExactDecimal(0)
    var expected = ExactDecimal(0)
    assertEquals(expected, -ed)

    ed = ExactDecimal(2)
    expected = ExactDecimal(-2)
    assertEquals(expected, -ed)

    ed = ExactDecimal(ExactFraction(-7, 11))
    expected = ExactDecimal(ExactFraction(7, 11))
    assertEquals(expected, -ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), 4)
    expected = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), -4)
    assertEquals(expected, -ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), ExactFraction(12, 19))
    expected = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), ExactFraction(-12, 19))
    assertEquals(expected, -ed)
}

fun runUnaryPlusTests() {
    var ed = ExactDecimal(0)
    var expected = ExactDecimal(0)
    assertEquals(expected, +ed)

    ed = ExactDecimal(2)
    expected = ExactDecimal(2)
    assertEquals(expected, +ed)

    ed = ExactDecimal(ExactFraction(-7, 11))
    expected = ExactDecimal(ExactFraction(-7, 11))
    assertEquals(expected, +ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), 4)
    expected = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), 4)
    assertEquals(expected, +ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), ExactFraction(12, 19))
    expected = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), ExactFraction(12, 19))
    assertEquals(expected, +ed)
}

fun runNotTests() {
    var ed = ExactDecimal(0)
    assert(!ed)

    ed = ExactDecimal(listOf("6"), listOf("pi + 5"), ExactFraction.ZERO)
    assert(!ed)

    ed = ExactDecimal(2)
    assert(!!ed)

    ed = ExactDecimal(ExactFraction(-7, 11))
    assert(!!ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), 4)
    assert(!!ed)

    ed = ExactDecimal(listOf("2 + 4", "3"), listOf("7pi"), ExactFraction(12, 19))
    assert(!!ed)
}