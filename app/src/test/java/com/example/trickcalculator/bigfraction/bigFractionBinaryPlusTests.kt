package com.example.trickcalculator.bigfraction

import org.junit.Assert.*

fun runPlusTests() {
    // add zero
    var first = BigFraction(0)
    var second = BigFraction(0)
    var expected = BigFraction(0, 1)
    assertEquals(expected, first + second)

    first = BigFraction(4)
    second = BigFraction(0)
    expected = BigFraction(4, 1)
    assertEquals(expected, first + second)

    first = BigFraction(-4)
    second = BigFraction(0)
    expected = BigFraction(-4, 1)
    assertEquals(expected, first + second)

    first = BigFraction(0)
    second = BigFraction(-4)
    expected = BigFraction(-4, 1)
    assertEquals(expected, first + second)

    // whole numbers
    first = BigFraction(4)
    second = BigFraction(1)
    expected = BigFraction(5, 1)
    assertEquals(expected, first + second)

    first = BigFraction(-12)
    second = BigFraction(33)
    expected = BigFraction(21, 1)
    assertEquals(expected, first + second)

    first = BigFraction(-12)
    second = BigFraction(-6)
    expected = BigFraction(-18, 1)
    assertEquals(expected, first + second)

    // same denominator
    first = BigFraction(5, 3)
    second = BigFraction(-6, 3)
    expected = BigFraction(-1, 3)
    assertEquals(expected, first + second)

    first = BigFraction(5, 3)
    second = BigFraction(-2, 3)
    expected = BigFraction(1, 1)
    assertEquals(expected, first + second)

    first = BigFraction(5, 19)
    second = BigFraction(11, 19)
    expected = BigFraction(16, 19)
    assertEquals(expected, first + second)

    first = BigFraction(24, 19)
    second = BigFraction(32, 19)
    expected = BigFraction(56, 19)
    assertEquals(expected, first + second)

    // different denominator
    first = BigFraction(5, 1)
    second = BigFraction(-4, 3)
    expected = BigFraction(11, 3)
    assertEquals(expected, first + second)

    first = BigFraction(5, 2)
    second = BigFraction(7, 3)
    expected = BigFraction(29, 6)
    assertEquals(expected, first + second)

    first = BigFraction(5, 12)
    second = BigFraction(3, 11)
    expected = BigFraction(91, 132)
    assertEquals(expected, first + second)

    first = BigFraction(4, 8)
    second = BigFraction(-1, 3)
    expected = BigFraction(1, 6)
    assertEquals(expected, first + second)

    // BigInteger
    var bf = BigFraction(0, 1) + 3.toBI()
    expected = BigFraction(3)
    assertEquals(bf, expected)

    bf = BigFraction(4, 7) + 12.toBI()
    expected = BigFraction(88, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-20, 7) + 3.toBI()
    expected = BigFraction(1, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-12, 5) + 1.toBI()
    expected = BigFraction(-7, 5)
    assertEquals(bf, expected)

    // Int
    bf = BigFraction(0, 1) + 3
    expected = BigFraction(3)
    assertEquals(bf, expected)

    bf = BigFraction(4, 7) + 12
    expected = BigFraction(88, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-20, 7) + 3
    expected = BigFraction(1, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-12, 5) + 1
    expected = BigFraction(-7, 5)
    assertEquals(bf, expected)

    // Long
    bf = BigFraction(0, 1) + 3L
    expected = BigFraction(3)
    assertEquals(bf, expected)

    bf = BigFraction(4, 7) + 12L
    expected = BigFraction(88, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-20, 7) + 3L
    expected = BigFraction(1, 7)
    assertEquals(bf, expected)

    bf = BigFraction(-12, 5) + 1L
    expected = BigFraction(-7, 5)
    assertEquals(bf, expected)
}
