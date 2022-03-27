package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Test
import org.junit.Assert.*

class TermExprExtTest {
    @Test
    fun testAsExpression() {
        var terms = listOf<Term>()
        var expected = Expression(terms)
        assertEquals(expected, terms.asExpression())

        terms = listOf(Term.ZERO)
        expected = Expression(terms)
        assertEquals(expected, terms.asExpression())

        terms = listOf(Term.ZERO, Term.ZERO)
        expected = Expression(terms)
        assertEquals(expected, terms.asExpression())

        terms = listOf(Term(1), Term(-1))
        expected = Expression(terms)
        assertEquals(expected, terms.asExpression())

        terms = listOf(Term(1, 4), Term(-7, 2))
        expected = Expression(terms)
        assertEquals(expected, terms.asExpression())

        terms = listOf(
            Term(ExactFraction(-18, 7), 3),
            Term(ExactFraction(11, 1000), 4),
            Term(ExactFraction(123, 999999), 4),
            Term(99, -1),
            Term(1, -3),
            Term(ExactFraction(-1, 5))
        )
        expected = Expression(terms)
        assertEquals(expected, terms.asExpression())
    }

    @Test
    // tests assume that Expression.isZero() is correct
    fun testIsZero() {
        // zero
        var e = listOf<Expression>()
        assert(e.isZero())

        e = listOf(Expression.ZERO)
        assert(e.isZero())

        e = listOf(Expression.ZERO, Expression.ONE)
        assert(e.isZero())

        var t = listOf(Term.ZERO)
        e = listOf(Expression(t))
        assert(e.isZero())

        // not zero
        e = listOf(Expression.ONE, -Expression.ONE)
        assertFalse(e.isZero())

        t = listOf(Term.ZERO, Term(4))
        e = listOf(Expression(t))
        assertFalse(e.isZero())
    }
}