package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.createExprList
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

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // 4
    )

    expected = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // -4
    )

    assertEquals(expected, -ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // ExactFraction(12, 19)
    )

    expected = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // ExactFraction(-12, 19)
    )

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

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // 4
    )

    expected = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // 4
    )

    assertEquals(expected, +ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // ExactFraction(12, 19)
    )

    expected = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // ExactFraction(12, 19)
    )

    assertEquals(expected, +ed)
}

fun runNotTests() {
    var ed = ExactDecimal(0)
    assert(!ed)

    ed = ExactDecimal(
        createExprList(listOf("6")),
        createExprList(listOf("1p1", "5")),
        // ExactFraction.ZERO
    )
    assert(!ed)

    ed = ExactDecimal(2)
    assert(!!ed)

    ed = ExactDecimal(ExactFraction(-7, 11))
    assert(!!ed)


    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // 4
    )
    assert(!!ed)


    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
        // ExactFraction(12, 19)
    )
    assert(!!ed)
}