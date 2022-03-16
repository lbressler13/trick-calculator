package com.example.trickcalculator.exactfraction

import org.junit.Assert.*

fun runMinusTests() {
    // zero
    var first = ExactFraction(0)
    var second = ExactFraction(0)
    var expected = ExactFraction(0, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(4)
    second = ExactFraction(0)
    expected = ExactFraction(4, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(-4)
    second = ExactFraction(0)
    expected = ExactFraction(-4, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(0)
    second = ExactFraction(4)
    expected = ExactFraction(-4, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(0)
    second = ExactFraction(-4)
    expected = ExactFraction(4, 1)
    assertEquals(expected, first - second)

    // whole numbers
    first = ExactFraction(4)
    second = ExactFraction(1)
    expected = ExactFraction(3, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(-12)
    second = ExactFraction(33)
    expected = ExactFraction(-45, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(12)
    second = ExactFraction(-33)
    expected = ExactFraction(45, 1)
    assertEquals(expected, first - second)

    first = ExactFraction(-12)
    second = ExactFraction(-6)
    expected = ExactFraction(-6, 1)
    assertEquals(expected, first - second)

    // same denominator
    first = ExactFraction(5, 3)
    second = ExactFraction(-6, 3)
    expected = ExactFraction(11, 3)
    assertEquals(expected, first - second)

    first = ExactFraction(-5, 3)
    second = ExactFraction(2, 3)
    expected = ExactFraction(-7, 3)
    assertEquals(expected, first - second)

    first = ExactFraction(5, 19)
    second = ExactFraction(11, 19)
    expected = ExactFraction(-6, 19)
    assertEquals(expected, first - second)

    first = ExactFraction(-24, 19)
    second = ExactFraction(-32, 19)
    expected = ExactFraction(8, 19)
    assertEquals(expected, first - second)

    // different denominator
    first = ExactFraction(5, 1)
    second = ExactFraction(-4, 3)
    expected = ExactFraction(19, 3)
    assertEquals(expected, first - second)

    first = ExactFraction(5, 2)
    second = ExactFraction(7, 3)
    expected = ExactFraction(1, 6)
    assertEquals(expected, first - second)

    first = ExactFraction(-5, 12)
    second = ExactFraction(3, 11)
    expected = ExactFraction(-91, 132)
    assertEquals(expected, first - second)

    first = ExactFraction(-4, 8)
    second = ExactFraction(-1, 3)
    expected = ExactFraction(-4, 24)
    assertEquals(expected, first - second)

    // BigInteger
    var ef = ExactFraction(0, 1) - 3.toBI()
    expected = ExactFraction(-3)
    assertEquals(ef, expected)

    ef = ExactFraction(-6) - 20.toBI()
    expected = ExactFraction(-26)
    assertEquals(ef, expected)

    ef = ExactFraction(3) - 5.toBI()
    expected = ExactFraction(-2L)
    assertEquals(ef, expected)

    ef = ExactFraction(3, 7) - 4.toBI()
    expected = ExactFraction(-25, 7)
    assertEquals(ef, expected)

    // Int
    ef = ExactFraction(0, 1) - 3
    expected = ExactFraction(-3)
    assertEquals(ef, expected)

    ef = ExactFraction(-6) - 20
    expected = ExactFraction(-26)
    assertEquals(ef, expected)

    ef = ExactFraction(3) - 5
    expected = ExactFraction(-2L)
    assertEquals(ef, expected)

    ef = ExactFraction(3, 7) - 4
    expected = ExactFraction(-25, 7)
    assertEquals(ef, expected)

    // Long
    ef = ExactFraction(0, 1) - 3L
    expected = ExactFraction(-3)
    assertEquals(ef, expected)

    ef = ExactFraction(-6) - 20L
    expected = ExactFraction(-26)
    assertEquals(ef, expected)

    ef = ExactFraction(3) - 5L
    expected = ExactFraction(-2L)
    assertEquals(ef, expected)

    ef = ExactFraction(3, 7) - 4L
    expected = ExactFraction(-25, 7)
    assertEquals(ef, expected)
}
