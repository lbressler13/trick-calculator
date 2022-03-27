package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runTermTimesTests() {
    // Term
    var t = Term(0)
    var t2 = Term(1)
    var expected = Term(0)
    assertEquals(expected, t * t2)

    t = Term(3, -1)
    t2 = Term(2, 1)
    expected = Term(6, 0)
    assertEquals(expected, t * t2)

    t = Term(3, -3)
    t2 = Term(-2, 1)
    expected = Term(-6, -2)
    assertEquals(expected, t * t2)

    t = Term(ExactFraction(7, 2), 0)
    t2 = Term(ExactFraction(2, 9), 4)
    expected = Term(ExactFraction(7, 9), 4)
    assertEquals(expected, t * t2)

    t = Term(5, 2)
    t2 = Term(3, 2)
    expected = Term(15, 4)
    assertEquals(expected, t * t2)

    // TermList + Expression
    t = Term(0)
    var tl = listOf(Term(3))
    var expectedL = listOf(Term(0))
    assertEquals(expectedL, t * tl)

    var expr = Expression(tl)
    var expectedE = Expression(expectedL)
    assertEquals(expectedE, t * expr)

    t = Term(0)
    tl = listOf(Term(3), Term(2, 7))
    expectedL = listOf(Term(0), Term(0))
    assertEquals(expectedL, t * tl)

    expr = Expression(tl)
    expectedE = Expression(expectedL)
    assertEquals(expectedE, t * expr)

    t = Term(2, -2)
    tl = listOf(Term(3), Term(2, 7))
    expectedL = listOf(Term(6, -2), Term(4, 5))
    assertEquals(expectedL, t * tl)

    expr = Expression(tl)
    expectedE = Expression(expectedL)
    assertEquals(expectedE, t * expr)

    t = Term(ExactFraction(7, 2), 3)
    tl = listOf(Term(ExactFraction(-3, 7)), Term(2, 4))
    expectedL = listOf(
        Term(ExactFraction(-3, 2), 3),
        Term(7, 7)
    )
    assertEquals(expectedL, t * tl)

    expr = Expression(tl)
    expectedE = Expression(expectedL)
    assertEquals(expectedE, t * expr)

    t = Term(3, -1)
    tl = listOf(
        Term(8, -2),
        Term(0, -3),
        Term(1000, 12),
        Term(-12, 0),
        Term(ExactFraction(1, 2), 1),
        Term(ExactFraction(123, 65), 65)
    )
    expectedL = listOf(
        Term(24, -3),
        Term(0),
        Term(3000, 11),
        Term(-36, -1),
        Term(ExactFraction(3, 2), 0),
        Term(ExactFraction(369, 65), 64)
    )
    assertEquals(expectedL, t * tl)

    expr = Expression(tl)
    expectedE = Expression(expectedL)
    assertEquals(expectedE, t * expr)
}

fun runTermPlusTests() {
    var t1 = Term(2, 0)
    var t2 = Term(-3, 0)
    var expected = Term(-1, 0)
    assertEquals(expected, t1 + t2)

    t1 = Term(ExactFraction(4, 7), 3)
    t2 = Term(ExactFraction(1, 3), 3)
    expected = Term(ExactFraction(19, 21), 3)
    assertEquals(expected, t1 + t2)

    t1 = Term(ExactFraction(4, 7), -3)
    t2 = Term(ExactFraction(1, 3), -3)
    expected = Term(ExactFraction(19, 21), -3)
    assertEquals(expected, t1 + t2)

    t1 = Term(0, 18)
    t2 = Term(0, 5)
    expected = Term(0, 0)
    assertEquals(expected, t1 + t2)

    t1 = Term(0, 18)
    t2 = Term(3, -5)
    expected = Term(3, -5)
    assertEquals(expected, t1 + t2)

    t1 = Term(3, -5)
    t2 = Term(0, 18)
    expected = Term(3, -5)
    assertEquals(expected, t1 + t2)

    t1 = Term(1, 3)
    t2 = Term(1, 1)
    val error = assertThrows(ArithmeticException::class.java) { t1 + t2 }
    val expectedError = "Cannot add Terms with different exp values"
    assertEquals(expectedError, error.message)
}

fun runTermEqualsTests() {
    var t = Term(0, 0)

    var other: Any? = null
    assertNotEquals(other, t)

    other = "hello world"
    assertNotEquals(other, t)

    other = Term(1, 0)
    assertNotEquals(other, t)

    other = Term(0, 0)
    assertEquals(other, t)

    other = Term(0, 3)
    assertEquals(other, t)

    var ef = ExactFraction(3, 2)
    t = Term(ef, -5)

    other = Term(ef, -5)
    assertEquals(other, t)

    other = Term(ef, -4)
    assertNotEquals(other, t)

    other = Term(ef, 5)
    assertNotEquals(other, t)

    ef = ExactFraction(2, 3)
    other = Term(ef, -5)
    assertNotEquals(other, t)
}

fun runTermCompareToTests() {
    var t1 = Term(0)

    var t2 = Term(0)
    assertFalse(t1 < t2)
    assertFalse(t1 > t2)

    t2 = Term(3)
    assert(t1 < t2)
    assert(t2 > t1)

    t2 = Term(-3)
    assert(t1 > t2)
    assert(t2 < t1)

    t2 = Term(ExactFraction(-3, 5))
    assert(t1 > t2)
    assert(t2 < t1)

    t1 = Term(1, 0)

    t2 = Term(1, 1)
    assert(t1 < t2)
    assert(t2 > t1)

    t2 = Term(1, -1)
    assert(t1 > t2)
    assert(t2 < t1)

    t1 = Term(3, 5)

    t2 = Term(3, 5)
    assertFalse(t1 < t2)
    assertFalse(t1 > t2)

    t2 = Term(3, -5)
    assert(t1 > t2)
    assert(t2 < t1)
}
