package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprList
import org.junit.Assert.*

fun runSimplifyAllStringsTests() {
    val l: ExprList = listOf()
    val expected: Pair<ExprList, ExactFraction> =
        Pair(listOf(), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    testSingleExpression()
    testMultipleExpressions()
}

private fun testSingleExpression() {
    var l = listOf(Expression.ONE)
    var expected = Pair(listOf(Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

//    l = listOf(Expression(Term(ExactFraction.HALF, 3)))
//    expected = Pair(listOf(Expression(Term(1, 3))), ExactFraction.HALF)
//    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(2)))
    expected = Pair(listOf(Expression(Term.ONE)), ExactFraction.TWO)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(-ExactFraction.THREE)))
    expected = Pair(listOf(Expression.ONE), -ExactFraction.THREE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(-ExactFraction.THREE, -1)))
    expected = Pair(listOf(Expression(Term(1, -1))), -ExactFraction.THREE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(Term(1), Term(1, 1)).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(Term(1), Term(1, 1)).asExpression()
        ),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(Term(3), Term(2, 1)).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(Term(3), Term(2, 1)).asExpression()
        ),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(Term(3, -3), Term(3), Term(2, 1)).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(Term(3, -3), Term(3), Term(2, 1)).asExpression()
        ),
        ExactFraction.ONE
    )
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(Term(4, -3), Term(12), Term(6, 1)).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(Term(2, -3), Term(6), Term(3, 1)).asExpression()
        ),
        ExactFraction.TWO
    )
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(Term(-4, -3), Term(-12), Term(-6, 1)).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(Term(2, -3), Term(6), Term(3, 1)).asExpression()
        ),
        -ExactFraction.TWO
    )
    assertEquals(expected, simplifyAllStrings(l))

//    l = listOf(Expression(Term(ExactFraction.HALF)))
//    expected = Pair(listOf(Expression.ONE), ExactFraction.HALF)
//    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(
            Term(ExactFraction.HALF),
            Term(ExactFraction(3, 2))
        ).asExpression()
    )
    expected = Pair(listOf(
        listOf(
            Term(ExactFraction.ONE),
            Term(ExactFraction.THREE)
        ).asExpression()
    ), ExactFraction.HALF)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(
            Term(ExactFraction(6, 5)),
            Term(ExactFraction.SIX)
        ).asExpression()
    )
    expected = Pair(listOf(
        listOf(
            Term(ExactFraction(1, 5)),
            Term(ExactFraction.ONE)
        ).asExpression()
    ), ExactFraction.SIX)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(
        listOf(
            Term(ExactFraction(6, 5)),
            Term(ExactFraction(3, 10))
        ).asExpression()
    )
    expected = Pair(listOf(
        listOf(
            Term(ExactFraction.FOUR),
            Term(ExactFraction.ONE)
        ).asExpression()
    ), ExactFraction(3, 10))
    assertEquals(expected, simplifyAllStrings(l))

    // all negative terms
}

private fun testMultipleExpressions() {
    var l = listOf(Expression.ONE, Expression.ONE)
    var expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(2)), Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.TWO)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression.ONE, Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(2)), Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.TWO)
    assertEquals(expected, simplifyAllStrings(l))

}
