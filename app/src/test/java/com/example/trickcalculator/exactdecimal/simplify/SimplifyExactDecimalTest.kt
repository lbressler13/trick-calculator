package com.example.trickcalculator.exactdecimal

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
        ed = ExactDecimal(num, denom)
        expected = ExactDecimal(
            listOf(Expression(Term(1, 1)), Expression(Term(3))),
            listOf(Expression(Term(ExactFraction.HALF)))
        )
        assertEquals(expected, simplify(ed))

        // TODO
    }

    @Test fun testRemoveCommon() = runRemoveCommonTests()
    @Test fun testSimplifyCoeffsSingleExpr() = runSimplifyCoeffsSingleExprTests()
    @Test fun testSimplifyAllCoeffs() = runSimplifyAllCoeffsTests()
}