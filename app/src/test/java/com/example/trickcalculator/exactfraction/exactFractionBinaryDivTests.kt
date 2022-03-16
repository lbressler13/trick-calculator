package com.example.trickcalculator.exactfraction

import com.example.trickcalculator.ext.toBI
import org.junit.Assert.*
import java.math.BigInteger

private const val divByZeroError = "divide by zero"

fun runDivTests() {
    // 0
    var first = ExactFraction(0)
    var second = ExactFraction(2, 3)
    var expected = ExactFraction(0)
    assertEquals(expected, first / second)

    first = ExactFraction(1)
    second = ExactFraction(0)
    var error = assertThrows(ArithmeticException::class.java) { first / second }
    assertEquals(divByZeroError, error.message)

    first = ExactFraction(0)
    second = ExactFraction(0)
    error = assertThrows(ArithmeticException::class.java) { first / second }
    assertEquals(divByZeroError, error.message)

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
    error = assertThrows(ArithmeticException::class.java) { ef / BigInteger.ZERO }
    assertEquals(divByZeroError, error.message)

    ef = ExactFraction(6) / 3.toBI()
    expected = ExactFraction(2)
    assertEquals(expected, ef)

    ef = ExactFraction(-6, 7) / 4.toBI()
    expected = ExactFraction(-6, 28)
    assertEquals(expected, ef)

    ef = ExactFraction(9, 4) / 3.toBI()
    expected = ExactFraction(3, 4)
    assertEquals(expected, ef)

    // Int
    ef = ExactFraction(1)
    error = assertThrows(ArithmeticException::class.java) { ef / 0 }
    assertEquals(divByZeroError, error.message)

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
    error = assertThrows(ArithmeticException::class.java) { ef / 0L }
    assertEquals(divByZeroError, error.message)

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
