package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runExpressionTimesTests() {
    // Term
    var e1 = Expression()
    var t = Term.ZERO
    var expected = Expression()
    assertEquals(expected, e1 * t)

    e1 = Expression.ZERO
    t = Term.ZERO
    expected = Expression.ZERO
    assertEquals(expected, e1 * t)

    e1 = Expression.ONE
    t = Term.ZERO
    expected = Expression.ZERO
    assertEquals(expected, e1 * t)

    e1 = Expression.ONE
    t = Term(1)
    expected = Expression.ONE
    assertEquals(expected, e1 * t)

    e1 = Expression(listOf(Term(2)))
    t = Term(ExactFraction.HALF)
    expected = Expression.ONE
    assertEquals(expected, e1 * t)

    e1 = Expression(listOf(
        Term(4, 2),
        Term(ExactFraction(-4, 7), 1),
        Term(3, 3),
        Term(-9, -3)
    ))
    t = Term(2, 2)
    expected = Expression(listOf(
        Term(8, 4),
        Term(ExactFraction(-8, 7), 3),
        Term(6, 5),
        Term(-18, -1)
    ))
    assertEquals(expected, e1 * t)

    e1 = Expression(listOf(
        Term(4, 2),
        Term(ExactFraction(-4, 7), 1),
        Term(3, 3),
        Term(-9, -3)
    ))
    t = Term(-2, 0)
    expected = Expression(listOf(
        Term(-8, 2),
        Term(ExactFraction(8, 7), 1),
        Term(-6, 3),
        Term(18, -3)
    ))
    assertEquals(expected, e1 * t)

    // Expression
}

fun runExpressionPlusTests() {
    var e1 = Expression()
    var e2 = Expression()
    var expected = Expression()
    assertEquals(expected, e1 + e2)

    e1 = Expression(Term.ZERO)
    e2 = Expression()
    expected = Expression(Term.ZERO)
    assertEquals(expected, e1 + e2)

    e1 = Expression()
    e2 = Expression(Term(8, 2))
    expected = Expression(Term(8, 2))
    assertEquals(expected, e1 + e2)

    e1 = Expression(listOf(
        Term(3, -2),
        Term(-4, 1),
        Term(ExactFraction.HALF, 5)
    ))
    e2 = Expression(listOf(
        Term(1),
        Term(ExactFraction(19, 17), -4),
        Term(ExactFraction(-12, 97), 5)
    ))
    expected = Expression(listOf(
        Term(3, -2),
        Term(-4, 1),
        Term(ExactFraction.HALF, 5),
        Term(1),
        Term(ExactFraction(19, 17), -4),
        Term(ExactFraction(-12, 97), 5)
    ))
    assertEquals(expected, e1 + e2)
}

