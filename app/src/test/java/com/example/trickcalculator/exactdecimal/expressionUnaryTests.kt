package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

private val half = ExactFraction(1, 2)

fun runExpressionIsZeroTests() {
    // zero
    var e = listOf<Term>().asExpression()
    assert(e.isZero())

    e = listOf(Term.ZERO).asExpression()
    assert(e.isZero())

    e = listOf(Term(1), Term(-1)).asExpression()
    assert(e.isZero())

    e = listOf(
        Term(0, 1),
        Term(half, 2),
        Term(half * 5, 2),
        Term(-3, 2),
        Term(ExactFraction(17, 7), 3),
        Term(-ExactFraction(17, 7), 3),
        Term(2, -3),
        Term(-2, -3)
    ).asExpression()
    assert(e.isZero())

    // not zero
    e = listOf(Term(1)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(-1)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(half)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(1, 2)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(1, -2)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(0), Term(1)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(0), Term(1, 3)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(-1), Term(1, 3)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(1, -3), Term(1, 3)).asExpression()
    assertFalse(e.isZero())

    e = listOf(Term(-1, -3), Term(1, 3)).asExpression()
    assertFalse(e.isZero())

    e = listOf(
        Term(0),
        Term(0),
        Term(0),
        Term(0),
        Term(0),
        Term(1),
        Term(0),
        Term(0),
        Term(0)
    ).asExpression()
    assertFalse(e.isZero())
}

fun runExpressionUnaryMinusTests() {
    var expected = listOf<Term>()
    assertEquals(Expression(expected), -Expression())

    var terms = listOf(Term.ZERO)
    expected = listOf(Term.ZERO)
    assertEquals(Expression(expected), -Expression(terms))

    terms = listOf(Term(17))
    expected = listOf(Term(-17))
    assertEquals(Expression(expected), -Expression(terms))

    terms = listOf(Term(17, -3))
    expected = listOf(Term(-17, -3))
    assertEquals(Expression(expected), -Expression(terms))

    terms = listOf(Term(17, 3))
    expected = listOf(Term(-17, 3))
    assertEquals(Expression(expected), -Expression(terms))

    terms = listOf(Term(ExactFraction(-33, 4), 3))
    expected = listOf(Term(ExactFraction(33, 4), 3))
    assertEquals(Expression(expected), -Expression(terms))

    terms = listOf(Term(ExactFraction(1, 2), 4), Term(1000000), Term(123456789, Int.MIN_VALUE))
    expected = listOf(Term(ExactFraction(-1, 2), 4), Term(-1000000), Term(-123456789, Int.MIN_VALUE))
    assertEquals(Expression(expected), -Expression(terms))
}
