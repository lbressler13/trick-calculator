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

        // Term
        var t = Term.ZERO
        expr = Expression(t)
        expected = listOf(t)
        assertEquals(expected, expr.terms)

        t = Term(8, -1)
        expr = Expression(t)
        expected = listOf(t)
        assertEquals(expected, expr.terms)

        t = Term(ExactFraction(101, 7), 104)
        expr = Expression(t)
        expected = listOf(t)
        assertEquals(expected, expr.terms)
    }

    @Test fun testTimes() = runExpressionTimesTests() // TODO (just x expr)
    @Test fun testPlus() = runExpressionPlusTests()
    @Test fun testEquals() = runExpressionEqualsTests()

    @Test fun testIsZero() = runExpressionIsZeroTests()
    @Test fun testUnaryMinus() = runExpressionUnaryMinusTests()
    @Test fun testIsAllConstants() {} // TODO

    @Test
    fun testSimplifyExpression() {
        // TODO
    }
}