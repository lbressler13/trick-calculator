package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprList
import org.junit.Assert.*

fun runSimplifyAllStringsTests() {
    // empty list
    var l: ExprList = listOf()
    var expected: Pair<ExprList, ExactFraction> =
        Pair(listOf(), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    // single term
    l = listOf(Expression.ONE)
    expected = Pair(listOf(Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(ExactFraction.HALF, 3)))
    expected = Pair(listOf(Expression(Term(1, 3))), ExactFraction.HALF)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(2)))
    expected = Pair(listOf(Expression(Term.ONE)), ExactFraction.TWO)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(-ExactFraction.THREE)))
    expected = Pair(listOf(Expression(Term(1))), -ExactFraction.THREE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(-ExactFraction.THREE, -1)))
    expected = Pair(listOf(Expression(Term(1, -1))), -ExactFraction.THREE)
    assertEquals(expected, simplifyAllStrings(l))

    // single expression
    l = listOf(Expression.ONE, Expression.ONE)
    expected = Pair(listOf(Expression.ONE, Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    l = listOf(Expression(Term(2)), Expression.ONE)
    expected = Pair(listOf(Expression(Term(2)), Expression.ONE), ExactFraction.ONE)
    assertEquals(expected, simplifyAllStrings(l))

    // not done yet

    // multiple expressions
}