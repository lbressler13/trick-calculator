package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.createExprList
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*
import kotlin.math.exp

fun runInverseTests() {
    var ed = ExactDecimal(0)
    var error = assertThrows(ArithmeticException::class.java) { ed.inverse() }
    val expectedError = "divide by zero"
    assertEquals(expectedError, error.message)

    ed = ExactDecimal(-8)
    var expected = ExactDecimal(ExactFraction(-1, 8))
    assertEquals(expected, ed.inverse())

    ed = ExactDecimal(
        createExprList(listOf("5 1p1", "2 -2p1")),
        createExprList(listOf("6", "7"))
    )
    expected = ExactDecimal(
        createExprList(listOf("6", "7")),
        createExprList(listOf("5 1p1", "2 -2p1"))
    )
    assertEquals(expected, ed.inverse())

    ed = ExactDecimal(
        createExprList(listOf("5 1p1", "2 -1p1", "-7p-4")),
        createExprList(listOf("6", "7"))
    )
    expected = ExactDecimal(
        createExprList(listOf("6", "7")),
        createExprList(listOf("5 1p1", "2 -1p1", "-7p-4"))
    )
    assertEquals(expected, ed.inverse())

    ed = ExactDecimal(
        createExprList(listOf("1p1 -1p1", "1p1")),
        createExprList(listOf("5 1p1", "2 -1p1", "-7p-4"))
    )
    error = assertThrows(ArithmeticException::class.java) { ed.inverse() }
    assertEquals(expectedError, error.message)
}

fun runIsZeroTests() {
    var ed = ExactDecimal(0)
    assert(ed.isZero())

    ed = ExactDecimal(
        createExprList(listOf("0", "0 3")),
        createExprList(listOf("1"))
    )
    assert(ed.isZero())

    ed = ExactDecimal(
        createExprList(listOf("6")),
        createExprList(listOf("1p1", "5"))
    )
    assertFalse(ed.isZero())

    ed = ExactDecimal(2)
    assertFalse(ed.isZero())

    ed = ExactDecimal(ExactFraction(-7, 11))
    assertFalse(ed.isZero())

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1"))
    )
    assertFalse(ed.isZero())
}
