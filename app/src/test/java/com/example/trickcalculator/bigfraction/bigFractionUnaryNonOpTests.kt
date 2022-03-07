package com.example.trickcalculator.bigfraction

import org.junit.Assert.*

fun runInverseTests() {
    var bf = BigFraction(1, 2)
    var expected = BigFraction(2, 1)
    assertEquals(expected, bf.inverse())

    bf = BigFraction(2, 1)
    expected = BigFraction(1, 2)
    assertEquals(expected, bf.inverse())

    bf = BigFraction(-3, 2)
    expected = BigFraction(-2, 3)
    assertEquals(expected, bf.inverse())

    bf = BigFraction(-3, -9)
    expected = BigFraction(3, 1)
    assertEquals(expected, bf.inverse())

    bf = BigFraction(19, 7)
    expected = BigFraction(7, 19)
    assertEquals(expected, bf.inverse())

    val error = assertThrows(ArithmeticException::class.java) { BigFraction(0).inverse() }
    assertEquals("divide by zero", error.message)
}

fun runAbsoluteValueTests() {
    var bf = BigFraction(0)
    var expected = BigFraction(0)
    assertEquals(expected, bf.absoluteValue())

    bf = BigFraction(3)
    expected = BigFraction(3)
    assertEquals(expected, bf.absoluteValue())

    bf = BigFraction(-3)
    expected = BigFraction(3)
    assertEquals(expected, bf.absoluteValue())

    bf = BigFraction(3, 5)
    expected = BigFraction(3, 5)
    assertEquals(expected, bf.absoluteValue())

    bf = BigFraction(-5, 3)
    expected = BigFraction(5, 3)
    assertEquals(expected, bf.absoluteValue())
}

fun runIsNegativeTests() {
    var bf = BigFraction(0)
    assert(!bf.isNegative())

    bf = BigFraction(1)
    assert(!bf.isNegative())

    bf = BigFraction(2, 7)
    assert(!bf.isNegative())

    bf = BigFraction(-1)
    assert(bf.isNegative())

    bf = BigFraction(-2, 7)
    assert(bf.isNegative())
}

fun runIsZeroTests() {
    var bf = BigFraction(0)
    assert(bf.isZero())

    bf = BigFraction(1)
    assert(!bf.isZero())

    bf = BigFraction(2, 7)
    assert(!bf.isZero())

    bf = BigFraction(-1)
    assert(!bf.isZero())

    bf = BigFraction(-2, 7)
    assert(!bf.isZero())
}
