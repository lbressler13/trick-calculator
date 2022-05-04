package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.assertListsEqual
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*
import org.junit.Test

class SimplifyExactDecimalTest {
    @Test fun testSimplify() = runSimplifyTests()

    @Test fun testCombineByExp() {
        var expr = Expression.ZERO
        var expected = listOf(Term.ZERO)
        assertEquals(expected, combineByExp(expr).terms)

        expr = Expression(Term(ExactFraction(-19, 7)))
        expected = listOf((Term(ExactFraction(-19, 7))))
        assertEquals(expected, combineByExp(expr).terms)

        expr = listOf(Term(3), Term(0)).asExpression()
        expected = listOf(Term(0))
        assertEquals(expected, combineByExp(expr).terms)

        expr = listOf(Term(-8), Term(ExactFraction(7, 6))).asExpression()
        expected = listOf(Term(ExactFraction(-28, 3)))
        assertEquals(expected, combineByExp(expr).terms)

        expr = listOf(Term(-8, 4), Term(ExactFraction(7, 6), 4)).asExpression()
        expected = listOf(Term(ExactFraction(-28, 3), 4))
        assertEquals(expected, combineByExp(expr).terms)

        expr = listOf(
            Term(3, 1),
            Term(8),
            Term(-2, 1)
        ).asExpression()
        expected = listOf(Term(8), Term(-6, 1))
        assertListsEqual(expected, combineByExp(expr).terms)

        expr = listOf(
            Term(ExactFraction.HALF),
            Term(-9, -1),
            Term(-2, 1),
            Term(ExactFraction(3, 7)),
            Term(-2, -1)
        ).asExpression()
        expected = listOf(
            Term(ExactFraction(3, 14)),
            Term(18, -1),
            Term(-2, 1)
        )
        assertListsEqual(expected, combineByExp(expr).terms)
    }

    @Test fun testRemoveCommon() = runRemoveCommonTests()
    @Test fun testSimplifyCoeffsSingleExpr() = runSimplifyCoeffsSingleExprTests()
    @Test fun testSimplifyAllCoeffs() = runSimplifyAllCoeffsTests()
}