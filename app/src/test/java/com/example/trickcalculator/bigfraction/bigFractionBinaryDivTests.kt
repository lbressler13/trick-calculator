package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger

private const val divByZeroError = "divide by zero"

fun runDivTests() {
    // 0
    var first = BigFraction(0)
    var second = BigFraction(2, 3)
    var expected = BigFraction(0)
    assertEquals(expected, first / second)

    first = BigFraction(1)
    second = BigFraction(0)
    var error = assertThrows(ArithmeticException::class.java) { first / second }
    assertEquals(divByZeroError, error.message)

    first = BigFraction(0)
    second = BigFraction(0)
    error = assertThrows(ArithmeticException::class.java) { first / second }
    assertEquals(divByZeroError, error.message)

    // whole numbers
    first = BigFraction(8)
    second = BigFraction(2)
    expected = BigFraction(4)
    assertEquals(expected, first / second)

    first = BigFraction(2)
    second = BigFraction(8)
    expected = BigFraction(1, 4)
    assertEquals(expected, first / second)

    first = BigFraction(-7)
    second = BigFraction(9)
    expected = BigFraction(-7, 9)
    assertEquals(expected, first / second)

    first = BigFraction(-7)
    second = BigFraction(-9)
    expected = BigFraction(7, 9)
    assertEquals(expected, first / second)

    first = BigFraction(9)
    second = BigFraction(-7)
    expected = BigFraction(-9, 7)
    assertEquals(expected, first / second)

    // fractions
    first = BigFraction(9, 2)
    second = BigFraction(3, 7)
    expected = BigFraction(63, 6)
    assertEquals(expected, first / second)

    first = BigFraction(3, 2)
    second = BigFraction(3, 2)
    expected = BigFraction(1)
    assertEquals(expected, first / second)

    first = BigFraction(3, 2)
    second = BigFraction(3, -2)
    expected = BigFraction(-1)
    assertEquals(expected, first / second)

    first = BigFraction(-2, 13)
    second = BigFraction(-4, 5)
    expected = BigFraction(10, 52)
    assertEquals(expected, first / second)

    first = BigFraction(-3, 10)
    second = BigFraction(3, 2)
    expected = BigFraction(-1, 5)
    assertEquals(expected, first / second)

    first = BigFraction(3, 10)
    second = BigFraction(-3, 2)
    expected = BigFraction(-1, 5)
    assertEquals(expected, first / second)

    // BigInteger
    var bf = BigFraction(1)
    error = assertThrows(ArithmeticException::class.java) { bf / BigInteger.ZERO }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(6) / 3.toBI()
    expected = BigFraction(2)
    assertEquals(expected, bf)

    bf = BigFraction(-6, 7) / 4.toBI()
    expected = BigFraction(-6, 28)
    assertEquals(expected, bf)

    bf = BigFraction(9, 4) / 3.toBI()
    expected = BigFraction(3, 4)
    assertEquals(expected, bf)

    // Int
    bf = BigFraction(1)
    error = assertThrows(ArithmeticException::class.java) { bf / 0 }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(6) / 3
    expected = BigFraction(2)
    assertEquals(expected, bf)

    bf = BigFraction(-6, 7) / 4
    expected = BigFraction(-6, 28)
    assertEquals(expected, bf)

    bf = BigFraction(9, 4) / 3
    expected = BigFraction(3, 4)
    assertEquals(expected, bf)

    // Long
    bf = BigFraction(1)
    error = assertThrows(ArithmeticException::class.java) { bf / 0L }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(6) / 3L
    expected = BigFraction(2)
    assertEquals(expected, bf)

    bf = BigFraction(-6, 7) / 4L
    expected = BigFraction(-6, 28)
    assertEquals(expected, bf)

    bf = BigFraction(9, 4) / 3L
    expected = BigFraction(3, 4)
    assertEquals(expected, bf)
}
