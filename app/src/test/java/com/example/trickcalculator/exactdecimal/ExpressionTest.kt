package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Test
import org.junit.Assert.*
import java.math.BigInteger

class ExpressionTest {
    @Test
    fun testConstructor() {
        // empty
        var terms = listOf<Term>()
        var expr = Expression()
        assertEquals(terms, expr.terms)

        // TermList
        terms = listOf()
        expr = Expression(terms)
        assertEquals(terms, expr.terms)

        terms = listOf(Term(3))
        expr = Expression(terms)
        assertEquals(terms, expr.terms)

        terms = listOf(Term(3), Term(12, -7), Term(ExactFraction(17, 12)))
        expr = Expression(terms)
        assertEquals(terms, expr.terms)

        terms = listOf(Term(1, -1))
        expr = Expression(terms)
        assertEquals(terms, expr.terms)

        // BigInteger
        var bi = BigInteger.ZERO
        expr = Expression(bi)
        var expected = listOf(Term(bi))
        assertEquals(expected, expr.terms)

        bi = 47.toBigInteger()
        expr = Expression(bi)
        expected = listOf(Term(bi))
        assertEquals(expected, expr.terms)

        bi = (-11).toBigInteger()
        expr = Expression(bi)
        expected = listOf(Term(bi))
        assertEquals(expected, expr.terms)

        bi = Long.MAX_VALUE.toBigInteger()
        expr = Expression(bi)
        expected = listOf(Term(bi))
        assertEquals(expected, expr.terms)
    }

    @Test fun testTimes() = runExpressionTimesTests() // TODO
    @Test fun testPlus() = runExpressionPlusTests() // TODO
    @Test fun testEquals() = runExpressionEqualsTests()

    @Test fun testIsZero() = runExpressionIsZeroTests()
    @Test fun testUnaryMinus() = runExpressionUnaryMinusTests()

    @Test
    fun testSimplifyExpression() {
        // TODO
    }

    @Test
    fun testToString() {
        // TODO
    }
}