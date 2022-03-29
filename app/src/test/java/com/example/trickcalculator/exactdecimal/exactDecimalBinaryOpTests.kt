package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.createExprList
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runPlusTests() {}

fun runMinusTests() {}

fun runTimesTests() {
    var ed1 = ExactDecimal(0)

    var ed2 = ExactDecimal(3)
    var expected = ExactDecimal(0)
    assertEquals(expected, ed1 * ed2)

    ed2 = ExactDecimal(
        createExprList(listOf("1p1", "1p1 1")),
        createExprList(listOf("3"))
    )
    assertEquals(expected, ed1 * ed2)

    ed1 = ExactDecimal(1)

    ed2 = ExactDecimal(3)
    expected = ExactDecimal(3)
    assertEquals(expected, ed1 * ed2)

    ed2 = ExactDecimal(
        createExprList(listOf("1p1", "1p1 1")),
        createExprList(listOf("3"))
    )
    expected = ExactDecimal(
        createExprList(listOf("1p1", "1p1 1")),
        createExprList(listOf("3"))
    )
    assertEquals(expected, ed1 * ed2)
}

fun runDivTests() {}

fun runEqualsTests() {}