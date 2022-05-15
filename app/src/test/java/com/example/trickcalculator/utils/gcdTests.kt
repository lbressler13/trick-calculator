package com.example.trickcalculator.utils

import exactfraction.ExactFraction
import org.junit.Assert.*
import java.math.BigInteger

fun runGetGCDTests() {
    runGetGcdBiTests()
    runGetGcdEfTests()
}

private fun runGetGcdBiTests() {
    // zero
    testGcdBI(0, 0, 1)
    testGcdBI(1, 0, 1)
    testGcdBI(4, 0, 4)
    testGcdBI(-4, 0, 4)

    // one
    testGcdBI(1, 1, 1)
    testGcdBI(1, -1, 1)
    testGcdBI(1, 103, 1)
    testGcdBI(1, -103, 1)

    // equal
    testGcdBI(17, 17, 17)
    testGcdBI(17, -17, 17)
    testGcdBI(-81, -81, 81)

    // co-prime
    // primes
    testGcdBI(17, 19, 1)
    testGcdBI(-17, 19, 1)
    testGcdBI(-2, -3, 1)
    // composites
    testGcdBI(14, 81, 1)
    testGcdBI(-15, 1024, 1)

    // exact divisor
    // primes
    testGcdBI(2, 4, 2)
    testGcdBI(-1002, 3, 3)
    testGcdBI(1002, -3, 3)
    // composites
    testGcdBI(1002, 334, 334)

    // common factor
    // prime gcd
    testGcdBI(6, 4, 2)
    testGcdBI(15, -25, 5)
    // composite gcd
    testGcdBI(120, 100, 20)
    testGcdBI(-120, -100, 20)
    testGcdBI(34, 51, 17)
}

private fun runGetGcdEfTests() {
    val zero = ExactFraction.ZERO
    val one = ExactFraction.ONE

    // zero
    testGcdEF(zero, zero, one)
    testGcdEF(zero, one, one)
    testGcdEF(zero, ExactFraction.FOUR, ExactFraction.FOUR)
    testGcdEF(zero, -ExactFraction.FOUR, ExactFraction.FOUR)
    testGcdEF(zero, ExactFraction.HALF, ExactFraction.HALF)
    testGcdEF(zero, ExactFraction(-17, 3), ExactFraction(17, 3))

    // one
    testGcdEF(one, one, one)
    testGcdEF(ExactFraction(10), one, one)
    testGcdEF(ExactFraction(-10), one, one)
    // testGcdEF(one, ExactFraction.HALF, one)
    // testGcdEF(one, ExactFraction(-17, 3), one)

    // equal
    var ef = ExactFraction(17)
    testGcdEF(ef, ef, ef)
    testGcdEF(ef, -ef, ef)

    ef = ExactFraction(81)
    testGcdEF(ef, ef, ef)
    testGcdEF(ef, -ef, ef)

    ef = ExactFraction(19, 909)
    testGcdEF(ef, ef, ef)
    testGcdEF(ef, -ef, ef)

    ef = ExactFraction(2000, 909)
    testGcdEF(ef, ef, ef)
    testGcdEF(ef, -ef, ef)

    // co-prime
    // primes
    testGcdEF(ExactFraction(17), ExactFraction(19), one)
    testGcdEF(ExactFraction(-17), ExactFraction(19), one)
    testGcdEF(ExactFraction(-17), ExactFraction(-19), one)
    // composites
    testGcdEF(ExactFraction(2), ExactFraction(3), one)
    testGcdEF(ExactFraction(81), ExactFraction(14), one)
    testGcdEF(ExactFraction(-15), ExactFraction(1024), one)
    // same denominator
    testGcdEF(ExactFraction(81, 17), ExactFraction(14, 17), ExactFraction(1, 17))
    testGcdEF(ExactFraction(-2, 5), ExactFraction(3, 5), ExactFraction(1, 5))
    // different denominator
    testGcdEF(ExactFraction(9, 11), ExactFraction(10, 13), ExactFraction(1, 143))
    testGcdEF(ExactFraction(-21, 25), ExactFraction(-16), ExactFraction(1, 25))

    // exact divisor
    // primes
    testGcdEF(ExactFraction.TWO, ExactFraction.FOUR, ExactFraction.TWO)
    testGcdEF(ExactFraction(1002), ExactFraction(-3), ExactFraction(3))
    testGcdEF(ExactFraction(-1002), ExactFraction(3), ExactFraction(3))
    // composites
    testGcdEF(ExactFraction(1002), ExactFraction(334), ExactFraction(334))
    // fractional
    testGcdEF(ExactFraction(5, 6), ExactFraction(5, 6), ExactFraction(5, 6))
    testGcdEF(ExactFraction(31, 45), ExactFraction(-31, 45), ExactFraction(31, 45))

    // common factor
    // prime gcd
    testGcdEF(ExactFraction.SIX, ExactFraction.FOUR, ExactFraction.TWO)
    testGcdEF(ExactFraction.SIX, -ExactFraction.FOUR, ExactFraction.TWO)
    testGcdEF(ExactFraction(15), ExactFraction(25), ExactFraction.FIVE)
    // composite gcd
    testGcdEF(ExactFraction(120), ExactFraction(100), ExactFraction(20))
    testGcdEF(ExactFraction(-120), ExactFraction(-100), ExactFraction(20))
    testGcdEF(ExactFraction(34), ExactFraction(51), ExactFraction(17))
    // same denominator
    testGcdEF(ExactFraction(5, 7), ExactFraction(10, 7), ExactFraction(5, 7))
    testGcdEF(ExactFraction(14, 45), ExactFraction(-49, 45), ExactFraction(7, 45))
    // different denominator
    testGcdEF(ExactFraction(42, 5), ExactFraction(12, 7), ExactFraction(6, 35))
    testGcdEF(ExactFraction(9, 14), ExactFraction(12, 11), ExactFraction(3, 154))
}

fun runGetGCDListTests() {
    // empty list
    var l: List<BigInteger> = listOf()
    var expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    // single value
    l = listOf(BigInteger.ZERO)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(BigInteger.ONE)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(17.toBigInteger())
    expected = 17.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-17).toBigInteger())
    expected = 17.toBigInteger()
    assertEquals(expected, getListGCD(l))

    // 2 values
    l = listOf(2.toBigInteger(), (-2).toBigInteger())
    expected = 2.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(11.toBigInteger(), 23.toBigInteger())
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(22.toBigInteger(), 121.toBigInteger())
    expected = 11.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(96.toBigInteger(), 180.toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    // multiple values
    l = listOf(BigInteger.ONE, BigInteger.ONE, BigInteger.TEN)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(21.toBigInteger(), 3.toBigInteger(), 7.toBigInteger(), 14.toBigInteger())
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(63.toBigInteger(), 27.toBigInteger(), 102.toBigInteger())
    expected = 3.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(96.toBigInteger(), 180.toBigInteger(), 372.toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-96).toBigInteger(), 180.toBigInteger(), (-372).toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-96).toBigInteger(), (-180).toBigInteger(), (-372).toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(
        12.toBigInteger(),
        (-18).toBigInteger(),
        11111000.toBigInteger(),
        4444444.toBigInteger(),
        100.toBigInteger(),
        12345678.toBigInteger()
    )
    expected = 2.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(
        12.toBigInteger(),
        (-18).toBigInteger(),
        11111000.toBigInteger(),
        4444444.toBigInteger(),
        81.toBigInteger(),
        12345678.toBigInteger()
    )
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(
        1000.toBigInteger(),
        (-180).toBigInteger(),
        11111000.toBigInteger(),
        44444440.toBigInteger(),
        80.toBigInteger(),
        123456785.toBigInteger(),
        (-275).toBigInteger(),
        4545454545.toBigInteger(),
        0.toBigInteger(),
        98765.toBigInteger()
    )
    expected = 5.toBigInteger()
    assertEquals(expected, getListGCD(l))
}

// helpers
private fun testGcdBI(num1: Int, num2: Int, expected: Int) {
    val b1 = num1.toBigInteger()
    val b2 = num2.toBigInteger()
    val expectedBI = expected.toBigInteger()
    assertEquals(expectedBI, getGCD(b1, b2))
    assertEquals(expectedBI, getGCD(b2, b1))
}

private fun testGcdEF(ef1: ExactFraction, ef2: ExactFraction, expected: ExactFraction) {
    assertEquals(expected, getGCD(ef1, ef2))
    assertEquals(expected, getGCD(ef2, ef1))
}
