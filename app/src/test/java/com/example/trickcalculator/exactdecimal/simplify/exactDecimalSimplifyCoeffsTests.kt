package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprList
import org.junit.Assert.*

fun runSimplifyCoeffsSingleExprTests() {
    var e = Expression()
    var expected: Pair<Expression, ExactFraction> =
        Pair(Expression(), ExactFraction.ONE)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression.ONE
    expected = Pair(Expression.ONE, ExactFraction.ONE)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression(Term(ExactFraction.HALF, 3))
    expected = Pair(Expression(Term(1, 3)), ExactFraction.HALF)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression(Term(2))
    expected = Pair(Expression(Term.ONE), ExactFraction.TWO)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression(Term(-ExactFraction.THREE))
    expected = Pair(Expression.ONE, -ExactFraction.THREE)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression(Term(-ExactFraction.THREE, -1))
    expected = Pair(Expression(Term(1, -1)), -ExactFraction.THREE)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(1), Term(1, 1)).asExpression()
    expected = Pair(
        listOf(Term(1), Term(1, 1)).asExpression(),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(3), Term(2, 1)).asExpression()
    expected = Pair(
        listOf(Term(3), Term(2, 1)).asExpression(),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(3, -3), Term(3), Term(2, 1)).asExpression()
    expected = Pair(
        listOf(Term(3, -3), Term(3), Term(2, 1)).asExpression(),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(4, -3), Term(12), Term(6, 1)).asExpression()
    expected = Pair(
        listOf(Term(2, -3), Term(6), Term(3, 1)).asExpression(),
        ExactFraction.TWO
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(-4, -3), Term(-12), Term(-6, 1)).asExpression()
    expected = Pair(
        listOf(Term(2, -3), Term(6), Term(3, 1)).asExpression(),
        -ExactFraction.TWO
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = Expression(Term(ExactFraction.HALF))
    expected = Pair(Expression.ONE, ExactFraction.HALF)
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(Term(ExactFraction.HALF), Term(ExactFraction(3, 2))).asExpression()
    expected = Pair(
        listOf(Term(ExactFraction.ONE), Term(ExactFraction.THREE)).asExpression(),
        ExactFraction.HALF
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(
        Term(ExactFraction(6, 5)),
        Term(ExactFraction.SIX)
    ).asExpression()
    expected = Pair(
        listOf(
            Term(ExactFraction(1, 5)),
            Term(ExactFraction.ONE)
        ).asExpression(),
        ExactFraction.SIX
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    e = listOf(
        Term(ExactFraction(6, 5)),
        Term(ExactFraction(3, 10))
    ).asExpression()
    expected = Pair(
        listOf(
            Term(ExactFraction.FOUR),
            Term(ExactFraction.ONE)
        ).asExpression(),
        ExactFraction(3, 10)
    )
    assertEquals(expected, simplifyCoeffsSingleExpr(e))

    // all negative terms
}

fun runSimplifyAllCoeffsTests() {
    var l = listOf(Expression.ONE, Expression.ONE)
    var expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllCoeffs(l))

    l = listOf(Expression(Term(2)), Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.TWO)
    assertEquals(expected, simplifyAllCoeffs(l))

    l = listOf(Expression.ONE, Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllCoeffs(l))

    l = listOf(Expression(Term(2)), Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.TWO)
    assertEquals(expected, simplifyAllCoeffs(l))
}
