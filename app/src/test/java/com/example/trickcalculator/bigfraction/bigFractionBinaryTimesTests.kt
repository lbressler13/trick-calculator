package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger

fun runTimesTests() {
    // zero
    var first = BigFraction(0)
    var second = BigFraction(0)
    var expected = BigFraction(0, 1)
    assertEquals(expected, first * second)

    first = BigFraction(1)
    second = BigFraction(0)
    expected = BigFraction(0, 1)
    assertEquals(expected, first * second)

    first = BigFraction(0)
    second = BigFraction(1)
    expected = BigFraction(0, 1)
    assertEquals(expected, first * second)

    // whole numbers
    first = BigFraction(1)
    second = BigFraction(8)
    expected = BigFraction(8, 1)
    assertEquals(expected, first * second)

    first = BigFraction(7)
    second = BigFraction(23)
    expected = BigFraction(161, 1)
    assertEquals(expected, first * second)

    first = BigFraction(-17)
    second = BigFraction(9)
    expected = BigFraction(-153, 1)
    assertEquals(expected, first * second)

    first = BigFraction(-5)
    second = BigFraction(-7)
    expected = BigFraction(35, 1)
    assertEquals(expected, first * second)

    first = BigFraction(-5)
    second = BigFraction(-7)
    expected = BigFraction(35, 1)
    assertEquals(expected, first * second)

    // fractions
    first = BigFraction(7, 11)
    second = BigFraction(3, 12)
    expected = BigFraction(21, 132)
    assertEquals(expected, first * second)

    first = BigFraction(-1, 3)
    second = BigFraction(-4, 12)
    expected = BigFraction(1, 9)
    assertEquals(expected, first * second)

    first = BigFraction(11, 3)
    second = BigFraction(4, 3)
    expected = BigFraction(44, 9)
    assertEquals(expected, first * second)

    first = BigFraction(6, 4)
    second = BigFraction(8, 3)
    expected = BigFraction(4, 1)
    assertEquals(expected, first * second)

    first = BigFraction(-6, 7)
    second = BigFraction(8, 3)
    expected = BigFraction(-48, 21)
    assertEquals(expected, first * second)

    first = BigFraction(6, 7)
    second = BigFraction(-8, 3)
    expected = BigFraction(-48, 21)
    assertEquals(expected, first * second)

    first = BigFraction(6, 7)
    second = BigFraction(7, 6)
    expected = BigFraction(1)
    assertEquals(expected, first * second)

    // BigInteger
    var bf = BigFraction(0) * 3.toBI()
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(3) * BigInteger.ZERO
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(-5) * 4.toBI()
    expected = BigFraction(-20)
    assertEquals(expected, bf)

    bf = BigFraction(5, 3) * 4.toBI()
    expected = BigFraction(20, 3)
    assertEquals(expected, bf)

    bf = BigFraction(-5, 4) * 4.toBI()
    expected = BigFraction(-5)
    assertEquals(expected, bf)

    // Int
    bf = BigFraction(0) * 3
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(3) * 0
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(-5) * 4
    expected = BigFraction(-20)
    assertEquals(expected, bf)

    bf = BigFraction(5, 3) * 4
    expected = BigFraction(20, 3)
    assertEquals(expected, bf)

    bf = BigFraction(-5, 4) * 4
    expected = BigFraction(-5)
    assertEquals(expected, bf)

    // Long
    bf = BigFraction(0) * 3L
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(3) * 0L
    expected = BigFraction(0)
    assertEquals(expected, bf)

    bf = BigFraction(-5) * 4L
    expected = BigFraction(-20)
    assertEquals(expected, bf)

    bf = BigFraction(5, 3) * 4L
    expected = BigFraction(20, 3)
    assertEquals(expected, bf)

    bf = BigFraction(-5, 4) * 4L
    expected = BigFraction(-5)
    assertEquals(expected, bf)
}
