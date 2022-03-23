package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.createExprList
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runInverseTests() {
    var ed = ExactDecimal(0)
    val error = assertThrows(ArithmeticException::class.java) { ed.inverse() }
    val expectedError = "divide by zero"
    assertEquals(expectedError, error.message)

    ed = ExactDecimal(-8)
    var expected = ExactDecimal(ExactFraction(-1, 8))
    assertEquals(expected, ed.inverse())


    ed = ExactDecimal(
        createExprList(listOf("5 1p1", "2 -1p1")),
        createExprList(listOf("6", "7")),
        ExactFraction(14, 3)
    )
    expected = ExactDecimal(
        createExprList(listOf("6", "7")),
        createExprList(listOf("5 1p1", "2 -1p1")),
        ExactFraction(3, 14)
    )

    assertEquals(expected, ed.inverse())
}

fun runIsZeroTests() {
    var ed = ExactDecimal(0)
    assert(ed.isZero())

    ed = ExactDecimal(
        createExprList(listOf("6")),
        createExprList(listOf("1p1", "5")),
        ExactFraction.ZERO
    )
    assert(ed.isZero())

    ed = ExactDecimal(2)
    assert(!ed.isZero())

    ed = ExactDecimal(ExactFraction(-7, 11))
    assert(!ed.isZero())

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        4
    )
    assert(!ed.isZero())


    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        ExactFraction(12, 19)
    )
    assert(!ed.isZero())
}
