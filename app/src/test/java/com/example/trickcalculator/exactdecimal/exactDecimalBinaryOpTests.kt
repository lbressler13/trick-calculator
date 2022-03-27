package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.createExprList
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runPlusTests() {}

fun runMinusTests() {}

fun runTimesTests() {
    var ed1 = ExactDecimal(
        createExprList(listOf("1")),
        createExprList(listOf("2")),
        // ExactFraction(1, 5)
    )

    var ed2 = ExactDecimal(
        createExprList(listOf("1p1", "1p1 1")),
        createExprList(listOf("3")),
        // ExactFraction(2, 7)
    )

    var expected = ExactDecimal(
        createExprList(listOf("1", "1p1", "1p1 1")),
        createExprList(listOf("2", "3")),
        // ExactFraction(2, 35)
    )

    assertEquals(expected, ed1 * ed2)
}

fun runDivTests() {}

fun runEqualsTests() {}