package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.assertListsEqual
import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*
import org.junit.Test

class SimplifyExactDecimalTest {
    @Test
    fun testSimplify() {
        var ed = ExactDecimal(ExactFraction.ZERO)
        var expected = ExactDecimal(listOf(Expression.ZERO), listOf(Expression.ONE))
        assertEquals(expected, simplify(ed))

        ed = ExactDecimal(ExactFraction.SIX)
        expected = ExactDecimal(
            listOf(Expression(Term(ExactFraction.SIX))),
            listOf(Expression.ONE)
        )
        assertEquals(expected, simplify(ed))

        ed = ExactDecimal(ExactFraction(14, -23))
        expected = ExactDecimal(
            listOf(Expression(Term(ExactFraction(-14)))),
            listOf(Expression(Term(23)))
        )
        assertEquals(expected, simplify(ed))

        var num = listOf(Expression(Term(3, 1)))
        var denom = listOf(Expression(Term(ExactFraction.HALF)))
        expected = ExactDecimal(
            listOf(Expression(Term(1, 1)), Expression(Term(3))),
            listOf(Expression(Term(ExactFraction.HALF)))
        )
        assertEquals(expected, simplify(ExactDecimal(num, denom)))

        num = listOf(listOf(Term(3), Term(-9)).asExpression())
        denom = listOf(Expression(Term(27, 2)))
        expected = ExactDecimal(
            listOf(Expression(Term(-1))),
            listOf(Expression(Term(1, 2)))
        )
        assertEquals(expected, simplify(ExactDecimal(num, denom)))

        // TODO

        // multiple expressions

        // can't be simplified

        // multiple w/ same exp
    }

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