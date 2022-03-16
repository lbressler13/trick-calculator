package com.example.trickcalculator.exactfraction

import org.junit.Assert.*

fun runInverseTests() {
    var ef = ExactFraction(1, 2)
    var expected = ExactFraction(2, 1)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(2, 1)
    expected = ExactFraction(1, 2)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(-3, 2)
    expected = ExactFraction(-2, 3)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(-3, -9)
    expected = ExactFraction(3, 1)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(19, 7)
    expected = ExactFraction(7, 19)
    assertEquals(expected, ef.inverse())

    val error = assertThrows(ArithmeticException::class.java) { ExactFraction(0).inverse() }
    assertEquals("divide by zero", error.message)
}

fun runAbsoluteValueTests() {
    var ef = ExactFraction(0)
    var expected = ExactFraction(0)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(3)
    expected = ExactFraction(3)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(-3)
    expected = ExactFraction(3)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(3, 5)
    expected = ExactFraction(3, 5)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(-5, 3)
    expected = ExactFraction(5, 3)
    assertEquals(expected, ef.absoluteValue())
}

fun runIsNegativeTests() {
    var ef = ExactFraction(0)
    assert(!ef.isNegative())

    ef = ExactFraction(1)
    assert(!ef.isNegative())

    ef = ExactFraction(2, 7)
    assert(!ef.isNegative())

    ef = ExactFraction(-1)
    assert(ef.isNegative())

    ef = ExactFraction(-2, 7)
    assert(ef.isNegative())
}

fun runIsZeroTests() {
    var ef = ExactFraction(0)
    assert(ef.isZero())

    ef = ExactFraction(1)
    assert(!ef.isZero())

    ef = ExactFraction(2, 7)
    assert(!ef.isZero())

    ef = ExactFraction(-1)
    assert(!ef.isZero())

    ef = ExactFraction(-2, 7)
    assert(!ef.isZero())
}
