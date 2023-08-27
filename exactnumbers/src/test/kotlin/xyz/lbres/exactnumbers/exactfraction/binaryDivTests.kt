package xyz.lbres.exactnumbers.exactfraction

import assertDivByZero
import java.math.BigInteger
import kotlin.test.assertEquals

fun runDivTests() {
    // 0
    var first = ExactFraction(0)
    var second = ExactFraction(2, 3)
    var expected = ExactFraction(0)
    assertEquals(expected, first / second)

    first = ExactFraction(1)
    second = ExactFraction(0)
    assertDivByZero { first / second }

    first = ExactFraction(0)
    second = ExactFraction(0)
    assertDivByZero { first / second }

    // whole numbers
    first = ExactFraction(8)
    second = ExactFraction(2)
    expected = ExactFraction(4)
    assertEquals(expected, first / second)

    first = ExactFraction(2)
    second = ExactFraction(8)
    expected = ExactFraction(1, 4)
    assertEquals(expected, first / second)

    first = ExactFraction(-7)
    second = ExactFraction(9)
    expected = ExactFraction(-7, 9)
    assertEquals(expected, first / second)

    first = ExactFraction(-7)
    second = ExactFraction(-9)
    expected = ExactFraction(7, 9)
    assertEquals(expected, first / second)

    first = ExactFraction(9)
    second = ExactFraction(-7)
    expected = ExactFraction(-9, 7)
    assertEquals(expected, first / second)

    // fractions
    first = ExactFraction(9, 2)
    second = ExactFraction(3, 7)
    expected = ExactFraction(63, 6)
    assertEquals(expected, first / second)

    first = ExactFraction(3, 2)
    second = ExactFraction(3, 2)
    expected = ExactFraction(1)
    assertEquals(expected, first / second)

    first = ExactFraction(3, 2)
    second = ExactFraction(3, -2)
    expected = ExactFraction(-1)
    assertEquals(expected, first / second)

    first = ExactFraction(-2, 13)
    second = ExactFraction(-4, 5)
    expected = ExactFraction(10, 52)
    assertEquals(expected, first / second)

    first = ExactFraction(-3, 10)
    second = ExactFraction(3, 2)
    expected = ExactFraction(-1, 5)
    assertEquals(expected, first / second)

    first = ExactFraction(3, 10)
    second = ExactFraction(-3, 2)
    expected = ExactFraction(-1, 5)
    assertEquals(expected, first / second)

    // BigInteger
    var ef = ExactFraction(1)
    assertDivByZero { ef / BigInteger.ZERO }

    ef = ExactFraction(6) / 3.toBigInteger()
    expected = ExactFraction(2)
    assertEquals(expected, ef)

    ef = ExactFraction(-6, 7) / 4.toBigInteger()
    expected = ExactFraction(-6, 28)
    assertEquals(expected, ef)

    ef = ExactFraction(9, 4) / 3.toBigInteger()
    expected = ExactFraction(3, 4)
    assertEquals(expected, ef)

    // Int
    ef = ExactFraction(1)
    assertDivByZero() { ef / 0 }

    ef = ExactFraction(6) / 3
    expected = ExactFraction(2)
    assertEquals(expected, ef)

    ef = ExactFraction(-6, 7) / 4
    expected = ExactFraction(-6, 28)
    assertEquals(expected, ef)

    ef = ExactFraction(9, 4) / 3
    expected = ExactFraction(3, 4)
    assertEquals(expected, ef)

    // Long
    ef = ExactFraction(1)
    assertDivByZero() { ef / 0L }

    ef = ExactFraction(6) / 3L
    expected = ExactFraction(2)
    assertEquals(expected, ef)

    ef = ExactFraction(-6, 7) / 4L
    expected = ExactFraction(-6, 28)
    assertEquals(expected, ef)

    ef = ExactFraction(9, 4) / 3L
    expected = ExactFraction(3, 4)
    assertEquals(expected, ef)
}
