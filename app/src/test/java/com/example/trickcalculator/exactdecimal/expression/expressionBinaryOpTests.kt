package com.example.trickcalculator.exactdecimal

import exactfraction.ExactFraction
import org.junit.Assert.*

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

fun runExpressionTimesTests() {
    // Term
    testWithTimes()

    // Expression
    testSpecialValues()
    testWithSingleTerms()
    testWithMultipleTerms()
}

private fun testWithTimes() {
    // Term
    var expr = Expression()
    var t = Term.ZERO
    var expected = Expression()
    assertEquals(expected, expr * t)

    expr = Expression.ZERO
    t = Term.ZERO
    expected = Expression.ZERO
    assertEquals(expected, expr * t)

    expr = Expression.ONE
    t = Term.ZERO
    expected = Expression.ZERO
    assertEquals(expected, expr * t)

    expr = Expression.ONE
    t = Term(1)
    expected = Expression.ONE
    assertEquals(expected, expr * t)

    expr = Expression(listOf(Term(2)))
    t = Term(ExactFraction.HALF)
    expected = Expression.ONE
    assertEquals(expected, expr * t)

    expr = Expression(listOf(
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
    assertEquals(expected, expr * t)

    expr = Expression(listOf(
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
    assertEquals(expected, expr * t)
}

private fun testSpecialValues() {
    // zero
    var e1 = Expression.ZERO
    var expected = Expression.ZERO

    var e2 = Expression.ZERO
    assertEquals(expected, e1 * e2)

    e2 = Expression.ONE
    assertEquals(expected, e1 * e2)

    e2 = listOf(Term(3), Term(1, -6), Term(-ExactFraction.HALF, 2)).asExpression()
    assertEquals(expected, e1 * e2)

    // one
    e1 = Expression.ONE

    e2 = Expression.ONE
    expected = Expression.ONE
    assertEquals(expected, e1 * e2)

    e2 = Expression(Term(1, 2))
    expected = Expression(Term(1, 2))
    assertEquals(expected, e1 * e2)

    e2 = Expression(Term(-11, 2))
    expected = Expression(Term(-11, 2))
    assertEquals(expected, e1 * e2)

    e2 = Expression(Term(-11, -2))
    expected = Expression(Term(-11, -2))
    assertEquals(expected, e1 * e2)

    e2 = listOf(Term(3), Term(1, -6), Term(-ExactFraction.HALF, 2)).asExpression()
    expected = listOf(Term(3), Term(1, -6), Term(-ExactFraction.HALF, 2)).asExpression()
    assertEquals(expected, e1 * e2)
}

private fun testWithSingleTerms() {
    // both single term
    var e1 = Expression(Term(3, 2))
    var e2 = Expression(Term(6, 3))
    var expected = Expression(Term(18, 5))
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(3, -2))
    e2 = Expression(Term(6, 3))
    expected = Expression(Term(18, 1))
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(3, 2))
    e2 = Expression(Term(6, -3))
    expected = Expression(Term(18, -1))
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(3, -2))
    e2 = Expression(Term(6, -3))
    expected = Expression(Term(18, -5))
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(3, 2))
    e2 = Expression(Term(6, -3))
    expected = Expression(Term(18, -1))
    assertEquals(expected, e1 * e2)

    // one single term
    e1 = Expression(Term(1, 3))
    e2 = listOf(Term(9, 1), Term(2, 3)).asExpression()
    expected = listOf(Term(9, 4), Term(2, 6)).asExpression()
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(ExactFraction(-3, 2), 3))
    e2 = listOf(
        Term(9, 0),
        Term(-2, 3),
        Term(ExactFraction(5, 7), -3)
    ).asExpression()
    expected = listOf(
        Term(ExactFraction(-27, 2), 3),
        Term(3, 6),
        Term(ExactFraction(-15, 14), 0)
    ).asExpression()
    assertEquals(expected, e1 * e2)

    e1 = Expression(Term(ExactFraction(-3, 2), -3))
    e2 = listOf(
        Term(9, 0),
        Term(-2, 3),
        Term(ExactFraction(5, 7), -3)
    ).asExpression()
    expected = listOf(
        Term(ExactFraction(-27, 2), -3),
        Term(3, 0),
        Term(ExactFraction(-15, 14), -6)
    ).asExpression()
    assertEquals(expected, e1 * e2)
}

private fun testWithMultipleTerms() {
    var e1 = listOf(Term(1), Term(2)).asExpression()
    var e2 = listOf(Term(3), Term(-2)).asExpression()
    var expected = listOf(
        Term(3),
        Term(-2),
        Term(6),
        Term(-4)
    ).asExpression()
    assertEquals(expected, e1 * e2)

    e1 = listOf(Term(2, 3), Term(-2, -3)).asExpression()
    e2 = listOf(Term(-2, 3), Term(2, -3)).asExpression()
    expected = listOf(
        Term(-4, 6),
        Term(4, 0),
        Term(4, 0),
        Term(-4, -6)
    ).asExpression()
    assertEquals(expected, e1 * e2)

    e1 = listOf(Term.ZERO, Term(9, 3)).asExpression()
    e2 = listOf(Term(3, 1), Term(-2, 3), Term(ExactFraction.HALF)).asExpression()
    expected = listOf(
        Term.ZERO, Term.ZERO, Term.ZERO,
        Term(27, 4),
        Term(-18, 6),
        Term(ExactFraction(9, 2), 3)
    ).asExpression()
    assertEquals(expected, e1 * e2)

    e1 = listOf(
        Term(ExactFraction(-9, 2), -3),
        Term(ExactFraction(3, 7), 1),
        Term(-2),
        Term(ExactFraction(1, 4)),
        Term(2, 1),
        Term(5, -1)
    ).asExpression()

    e2 = listOf(
        Term(1, -3),
        Term(ExactFraction(4, 3), 2),
        Term(ExactFraction(1, 4)),
        Term(11, 1),
        Term(-3),
        Term(8)
    ).asExpression()
    expected = listOf(
			// Term 1
			Term(ExactFraction(-9, 2), -6),
			Term(ExactFraction(-36, 6), -1),
			Term(ExactFraction(-9, 8), -3),
			Term(ExactFraction(-99, 2), -2),
			Term(ExactFraction(27, 2), -3),
			Term(ExactFraction(-72, 2), -3),
			// Term 2
			Term(ExactFraction(3, 7), -2),
			Term(ExactFraction(4, 7), 3),
			Term(ExactFraction(3, 28), 1),
			Term(ExactFraction(33, 7), 2),
			Term(ExactFraction(-9, 7), 1),
			Term(ExactFraction(24, 7), 1),
			// Term 3
      Term(-2, -3),
      Term(ExactFraction(-8, 3), 2),
      Term(ExactFraction(-1, 2)),
      Term(-22, 1),
      Term(6),
      Term(-16),
			// Term 4
      Term(ExactFraction(1, 4), -3),
      Term(ExactFraction(1, 3), 2),
      Term(ExactFraction(1, 16)),
      Term(ExactFraction(11, 4), 1),
      Term(ExactFraction(-3, 4)),
      Term(2),
			// Term 5
      Term(2, -2),
      Term(ExactFraction(8, 3), 3),
      Term(ExactFraction(1, 2), 1),
      Term(22, 2),
      Term(-6, 1),
      Term(16, 1),
			// Term 6
      Term(5, -4),
      Term(ExactFraction(20, 3), 1),
      Term(ExactFraction(5, 4), -1),
      Term(55),
      Term(-15, -1),
      Term(40, -1)
    ).asExpression()
    assertEquals(expected, e1 * e2)
}
