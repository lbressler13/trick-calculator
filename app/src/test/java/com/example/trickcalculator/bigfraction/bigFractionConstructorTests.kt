package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger

// Numerator and denominator are explicitly checked to ensure correct initialization

private val zero = BigInteger.ZERO
private val pos1 = BigInteger.ONE
private val neg1 = -BigInteger.ONE
private val pos3 = 3.toBI()
private val neg3 = (-3).toBI()
private val pos4 = 4.toBI()
private val neg4 = (-4).toBI()
private val pos7 = 7.toBI()
private val neg7 = (-7).toBI()
private val pos18 = 18.toBI()
private val neg18 = (-18).toBI()

private const val divByZeroError = "divide by zero"

fun runConstructorTests() {
    testSingleValConstructor()
    testPairValConstructor()
    testStringConstructor()
}

// thorough testing is done in parsing tests
private fun testStringConstructor() {
    // Decimal string
    var bf = BigFraction("1.5")
    assertEquals(15.toBI(), bf.numerator)
    assertEquals(10.toBI(), bf.denominator)

    // BF string
    bf = BigFraction("BF[-7 3]")
    assertEquals(neg7, bf.numerator)
    assertEquals(pos3, bf.denominator)

    // Invalid
    assertThrows(Exception::class.java) { BigFraction("[]") }
}

private fun testSingleValConstructor() {
    // BigInteger
    var bf = BigFraction(zero)
    assertEquals(bf.numerator, zero)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(pos3)
    assertEquals(bf.numerator, pos3)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(neg3)
    assertEquals(bf.numerator, neg3)
    assertEquals(bf.denominator, pos1)

    // Int
    bf = BigFraction(0)
    assertEquals(bf.numerator, zero)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(3)
    assertEquals(bf.numerator, pos3)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(-3)
    assertEquals(bf.numerator, neg3)
    assertEquals(bf.denominator, pos1)

    // Long
    bf = BigFraction(0L)
    assertEquals(bf.numerator, zero)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(3L)
    assertEquals(bf.numerator, pos3)
    assertEquals(bf.denominator, pos1)

    bf = BigFraction(-3L)
    assertEquals(bf.numerator, neg3)
    assertEquals(bf.denominator, pos1)
}

/**
 *
 * Cases for each pair:
 * Numerator of 0
 * Denominator of 0 (throws error)
 * Positive whole
 * Positive < 1
 * Positive > 1
 * Negative whole
 * Negative > -1
 * Negative < -1
 */
private fun testPairValConstructor() {
    // BigInteger, BigInteger
    var bf = BigFraction(zero, pos1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    var error = assertThrows(ArithmeticException::class.java) {
        BigFraction(pos1, zero)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(pos4, pos1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(pos7, pos18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(pos4, pos3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg4, pos1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(neg1, pos3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg18, pos4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Int, Int
    bf = BigFraction(0, 1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1, 0)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4, 1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7, 18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4, 3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4, 1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1, 3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18, 4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Long, Long
    bf = BigFraction(0L, 1L)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1L, 0L)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4L, 1L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7L, 18L)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4L, 3L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4L, 1L)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1L, 3L)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18L, 4L)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // BigInteger, Int
    bf = BigFraction(zero, 1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(pos1, 0)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(pos4, 1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(pos7, 18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(pos4, 3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg4, 1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(neg1, 3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg18, 4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Int, BigInteger
    bf = BigFraction(0, pos1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1, zero)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4, pos1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7, pos18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4, pos3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4, pos1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1, pos3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18, pos4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Int, Long
    bf = BigFraction(0, 1L)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1, 0L)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4, 1L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7, 18L)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4, 3L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4, 1L)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1, 3L)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18, 4L)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Long, Int
    bf = BigFraction(0L, 1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1L, 0)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4L, 1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7L, 18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4L, 3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4L, 1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1L, 3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18L, 4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // BigInteger, Long
    bf = BigFraction(zero, 1L)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(pos1, 0L)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(pos4, 1L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(pos7, 18L)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(pos4, 3L)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg4, 1L)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(neg1, 3L)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(neg18, 4L)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)

    // Long, BigInteger
    bf = BigFraction(0L, pos1)
    assertEquals(zero, bf.numerator)
    assertEquals(pos1, bf.denominator)

    error = assertThrows("divide by zero", ArithmeticException::class.java) {
        BigFraction(1L, zero)
    }
    assertEquals(divByZeroError, error.message)

    bf = BigFraction(4L, pos1)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(7L, pos18)
    assertEquals(pos7, bf.numerator)
    assertEquals(pos18, bf.denominator)

    bf = BigFraction(4L, pos3)
    assertEquals(pos4, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-4L, pos1)
    assertEquals(neg4, bf.numerator)
    assertEquals(pos1, bf.denominator)

    bf = BigFraction(-1L, pos3)
    assertEquals(neg1, bf.numerator)
    assertEquals(pos3, bf.denominator)

    bf = BigFraction(-18L, pos4)
    assertEquals(neg18, bf.numerator)
    assertEquals(pos4, bf.denominator)
}