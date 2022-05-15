package com.example.trickcalculator.exactdecimal

import exactfraction.ExactFraction
import org.junit.Assert.*

fun runExpressionSimplifyExpressionTests() {
    testSimplified()
    testUnsimplified()
}

private fun testSimplified() {
    // single term
    var expr = Expression()
    var expected = Expression.ZERO
    assertEquals(expected, expr.simplifyExpression())

    expr = Expression.ZERO
    expected = Expression.ZERO
    assertEquals(expected, expr.simplifyExpression())

    expr = Expression.ONE
    expected = Expression.ONE
    assertEquals(expected, expr.simplifyExpression())

    expr = -Expression.ONE
    expected = -Expression.ONE
    assertEquals(expected, expr.simplifyExpression())

    expr = Expression(Term(4, 18))
    expected = Expression(Term(4, 18))
    assertEquals(expected, expr.simplifyExpression())

    expr = Expression(Term(10000, -30))
    expected = Expression(Term(10000, -30))
    assertEquals(expected, expr.simplifyExpression())

    // multiple terms
    expr = listOf(Term(3), Term(3, 4)).asExpression()
    expected = listOf(Term(3), Term(3, 4)).asExpression()
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(5), Term(5, -1), Term(5, 1)).asExpression()
    expected = listOf(Term(5), Term(5, -1), Term(5, 1)).asExpression()
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(7, 1), Term(-7, -1)).asExpression()
    expected = listOf(Term(7, 1), Term(-7, -1)).asExpression()
    assertEquals(expected, expr.simplifyExpression())
}

private fun testUnsimplified() {
    // single exponent
    var expr = listOf(Term(0), Term(1)).asExpression()
    var expected = Expression(Term(1))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(2), Term(1), Term(4)).asExpression()
    expected = Expression(Term(7))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(ExactFraction(1, 3)), Term(ExactFraction(-7, 2))).asExpression()
    expected = Expression(Term(ExactFraction(-19, 6)))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(2), Term(1), Term(-4)).asExpression()
    expected = Expression(Term(-1))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(-2), Term(-1), Term(-3), Term(-4)).asExpression()
    expected = Expression(Term(-10))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(2, 1), Term(1, 1), Term(4, 1)).asExpression()
    expected = Expression(Term(7, 1))
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(2, -15), Term(1, -15), Term(4, -15)).asExpression()
    expected = Expression(Term(7, -15))
    assertEquals(expected, expr.simplifyExpression())

    // multiple exponents
    expr = listOf(Term(3, 1), Term(3, -1), Term(2, 1)).asExpression()
    expected = listOf(Term(3, -1), Term(5, 1)).asExpression()
    assertEquals(expected, expr)

    expr = listOf(Term(0), Term(-4), Term(3, 1)).asExpression()
    expected = listOf(Term(-4), Term(3, 1)).asExpression()
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(-5, 1), Term(6, 2), Term(3, 0), Term(-2, 1), Term(7, 2)).asExpression()
    expected = listOf(Term(3), Term(-7, 1), Term(13, 2)).asExpression()
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(Term(-5, -1), Term(6, 2), Term(3, -1), Term(-2, 1), Term(7, 2)).asExpression()
    expected = listOf(Term(-2, -1), Term(-2, 1), Term(13, 2)).asExpression()
    assertEquals(expected, expr.simplifyExpression())

    expr = listOf(
        Term(4, 2),
        Term(3, 2),
        Term(-1, 2),
        Term(8, 2),
        Term(1, 1)
    ).asExpression()
    expected = listOf(
        Term(1, 1),
        Term(14, 2)
    ).asExpression()
    assertEquals(expected, expr.simplifyExpression())
}
