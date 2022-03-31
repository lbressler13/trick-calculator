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
    )
    expected = ExactDecimal(
        createExprList(listOf("-2 -4", "-3")),
        createExprList(listOf("7p1")),
    )
    assertEquals(expected, -ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3")),
        createExprList(listOf("7p1"))
    )
    expected = ExactDecimal(
        createExprList(listOf("-2 -4p2", "3")),
        createExprList(listOf("7p1"))
    )
    assertEquals(expected, -ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")),
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    expected = ExactDecimal(
        createExprList(listOf("-2 -4p2", "3", "6p-2 -4p1")),
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    assertEquals(expected, -ed)

    val efExpr = Expression(listOf(Term(ExactFraction.HALF, -8)))
    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")) + efExpr,
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    expected = ExactDecimal(
        createExprList(listOf("-2 -4p2", "3", "6p-2 -4p1")) + (-efExpr),
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
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
    )
    expected = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1")),
    )
    assertEquals(expected, +ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3")),
        createExprList(listOf("7p1"))
    )
    expected = ExactDecimal(
        createExprList(listOf("2 4p2", "-3")),
        createExprList(listOf("7p1"))
    )
    assertEquals(expected, +ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")),
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    expected = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")),
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    assertEquals(expected, +ed)

    val efExpr = Expression(listOf(Term(ExactFraction.HALF, -8)))
    ed = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")) + efExpr,
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    expected = ExactDecimal(
        createExprList(listOf("2 4p2", "-3", "-6p-2 4p1")) + efExpr,
        createExprList(listOf("7p1", "-2", "-3p-3 1p0 2p0"))
    )
    assertEquals(expected, +ed)
}

fun runNotTests() {
    var ed = ExactDecimal(0)
    assert(!ed)

    ed = ExactDecimal(
        createExprList(listOf("0", "0 3")),
        createExprList(listOf("1"))
    )
    assert(!ed)

    ed = ExactDecimal(
        createExprList(listOf("6")),
        createExprList(listOf("1p1", "5"))
    )
    assertFalse(!ed)

    ed = ExactDecimal(2)
    assertFalse(!ed)

    ed = ExactDecimal(ExactFraction(-7, 11))
    assertFalse(!ed)

    ed = ExactDecimal(
        createExprList(listOf("2 4", "3")),
        createExprList(listOf("7p1"))
    )
    assertFalse(!ed)
}