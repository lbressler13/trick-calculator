package com.example.trickcalculator.exactfraction

import org.junit.Assert.*
import java.math.BigInteger

fun runTimesTests() {
    // zero
    var first = ExactFraction(0)
    var second = ExactFraction(0)
    var expected = ExactFraction(0, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(1)
    second = ExactFraction(0)
    expected = ExactFraction(0, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(0)
    second = ExactFraction(1)
    expected = ExactFraction(0, 1)
    assertEquals(expected, first * second)

    // whole numbers
    first = ExactFraction(1)
    second = ExactFraction(8)
    expected = ExactFraction(8, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(7)
    second = ExactFraction(23)
    expected = ExactFraction(161, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(-17)
    second = ExactFraction(9)
    expected = ExactFraction(-153, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(-5)
    second = ExactFraction(-7)
    expected = ExactFraction(35, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(-5)
    second = ExactFraction(-7)
    expected = ExactFraction(35, 1)
    assertEquals(expected, first * second)

    // fractions
    first = ExactFraction(7, 11)
    second = ExactFraction(3, 12)
    expected = ExactFraction(21, 132)
    assertEquals(expected, first * second)

    first = ExactFraction(-1, 3)
    second = ExactFraction(-4, 12)
    expected = ExactFraction(1, 9)
    assertEquals(expected, first * second)

    first = ExactFraction(11, 3)
    second = ExactFraction(4, 3)
    expected = ExactFraction(44, 9)
    assertEquals(expected, first * second)

    first = ExactFraction(6, 4)
    second = ExactFraction(8, 3)
    expected = ExactFraction(4, 1)
    assertEquals(expected, first * second)

    first = ExactFraction(-6, 7)
    second = ExactFraction(8, 3)
    expected = ExactFraction(-48, 21)
    assertEquals(expected, first * second)

    first = ExactFraction(6, 7)
    second = ExactFraction(-8, 3)
    expected = ExactFraction(-48, 21)
    assertEquals(expected, first * second)

    first = ExactFraction(6, 7)
    second = ExactFraction(7, 6)
    expected = ExactFraction(1)
    assertEquals(expected, first * second)

    // BigInteger
    var ef = ExactFraction(0) * 3.toBI()
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(3) * BigInteger.ZERO
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(-5) * 4.toBI()
    expected = ExactFraction(-20)
    assertEquals(expected, ef)

    ef = ExactFraction(5, 3) * 4.toBI()
    expected = ExactFraction(20, 3)
    assertEquals(expected, ef)

    ef = ExactFraction(-5, 4) * 4.toBI()
    expected = ExactFraction(-5)
    assertEquals(expected, ef)

    // Int
    ef = ExactFraction(0) * 3
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(3) * 0
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(-5) * 4
    expected = ExactFraction(-20)
    assertEquals(expected, ef)

    ef = ExactFraction(5, 3) * 4
    expected = ExactFraction(20, 3)
    assertEquals(expected, ef)

    ef = ExactFraction(-5, 4) * 4
    expected = ExactFraction(-5)
    assertEquals(expected, ef)

    // Long
    ef = ExactFraction(0) * 3L
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(3) * 0L
    expected = ExactFraction(0)
    assertEquals(expected, ef)

    ef = ExactFraction(-5) * 4L
    expected = ExactFraction(-20)
    assertEquals(expected, ef)

    ef = ExactFraction(5, 3) * 4L
    expected = ExactFraction(20, 3)
    assertEquals(expected, ef)

    ef = ExactFraction(-5, 4) * 4L
    expected = ExactFraction(-5)
    assertEquals(expected, ef)
}
