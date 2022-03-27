package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

private val half = ExactFraction(1, 2)

fun runExpressionEqualsTests() {
    equalsTests()
    notEqualsTests()

    // bad value
    var e = Expression()
    var other: Any? = null
    assert(e != other)

    other = Term(5)
    e = Expression(listOf(Term(5)))
    assert(e != other)

    e = Expression()
    other = listOf<Term>()
    assert(e != other)
}

private fun equalsTests() {
    var e1 = Expression()
    var e2 = Expression()
    assertEquals(e1, e2)

    e1 = Expression()
    e2 = Expression(listOf())
    assertEquals(e1, e2)

    // same values
    var t1 = listOf(Term(3))
    e1 = Expression(t1)
    e2 = Expression(t1)
    assertEquals(e1, e2)

    t1 = listOf(Term(3, 7), Term(ExactFraction(5, 3), -2))
    e1 = Expression(t1)
    e2 = Expression(t1)
    assertEquals(e1, e2)

    t1 = listOf(Term(1), Term(2, 1))
    var t2 = listOf(Term(2, 1), Term(1))
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertEquals(e1, e2)

    var ef = ExactFraction(-17, 32)
    t1 = listOf(Term(1), Term(2, 1), Term(ef, -3))
    t2 = listOf(Term(2, 1), Term(ef, -3), Term(1))
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertEquals(e1, e2)

    // different values
    t1 = listOf(Term(2), Term(2))
    t2 = listOf(Term(3), Term(1))
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertEquals(e1, e2)

    t1 = listOf(Term(half,2), Term(2), Term(-half, 2))
    t2 = listOf(Term(-1), Term(3))
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertEquals(e1, e2)

    t1 = listOf(
        Term(ExactFraction(4, 9), 0),
        Term(ExactFraction(-3, 2), 2),
        Term(1),
        Term(4, 1),
        Term(-half, 3),
        Term(ExactFraction(8, 3), 2)
    )
    t2 = listOf(
        Term(ExactFraction(-11, 6), 2),
        Term(1, 1),
        Term(3, 2),
        Term(ExactFraction(13, 9)),
        Term(3, 3),
        Term(ExactFraction(7, 8), 1),
        Term(1, 3),
        Term(half, 3),
        Term(ExactFraction(17, 8), 1),
        Term(-5, 3),
    )
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertEquals(e1, e2)
}

private fun notEqualsTests() {
    var e1 = Expression()
    var e2 = Expression(listOf(Term(3)))
    assertNotEquals(e1, e2)

    e1 = Expression(listOf(Term(2)))
    e2 = Expression(listOf(Term(3)))
    assertNotEquals(e1, e2)

    e1 = Expression(listOf(Term(-3)))
    e2 = Expression(listOf(Term(3)))
    assertNotEquals(e1, e2)

    e1 = Expression(listOf(Term(half)))
    e2 = Expression(listOf(Term(half.inverse())))
    assertNotEquals(e1, e2)

    e1 = Expression(listOf(Term(3, 1)))
    e2 = Expression(listOf(Term(3, 2)))
    assertNotEquals(e1, e2)

    e1 = Expression(listOf(Term(3, 1)))
    e2 = Expression(listOf(Term(3, -1)))
    assertNotEquals(e1, e2)

    var t1 = listOf(Term(2), Term(2))
    var t2 = listOf(Term(3), Term(2))
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertNotEquals(e1, e2)

    t1 = listOf(
        Term(ExactFraction(4, 9), 0),
        Term(ExactFraction(-3, 2), 2),
        Term(1),
        Term(4, 1),
        Term(-half, 3),
        Term(ExactFraction(8, 3), 2)
    )
    t2 = listOf(
        Term(ExactFraction(-11, 6), 2),
        Term(3, 2),
        Term(ExactFraction(13, 9)),
        Term(3, 3),
        Term(ExactFraction(7, 8), 1),
        Term(1, 3),
        Term(half, 3),
        Term(ExactFraction(17, 8), 1),
        Term(-5, 3),
    )
    e1 = Expression(t1)
    e2 = Expression(t2)
    assertNotEquals(e1, e2)
}
