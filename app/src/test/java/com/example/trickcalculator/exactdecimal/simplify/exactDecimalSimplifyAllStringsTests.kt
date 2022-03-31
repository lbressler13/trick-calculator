package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprList
import org.junit.Assert.*

fun runSimplifyAllStringsTests() {
    var l: ExprList = listOf()
    var expected: Pair<ExprList, ExactFraction> =
        Pair(listOf(), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression.ONE)
    expected = Pair(listOf(Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    var e = Expression(Term(ExactFraction.HALF, 3))
    l = listOf(e)
    expected = Pair(listOf(e), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))
}