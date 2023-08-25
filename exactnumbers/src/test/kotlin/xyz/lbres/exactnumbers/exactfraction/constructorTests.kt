package xyz.lbres.exactnumbers.exactfraction

import assertDivByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertFails

// Numerator and denominator are explicitly checked to ensure correct initialization

private val zero = BigInteger.ZERO
private val pos1 = BigInteger.ONE
private val neg1 = -BigInteger.ONE
private val pos3 = 3.toBigInteger()
private val neg3 = (-3).toBigInteger()
private val pos4 = 4.toBigInteger()
private val neg4 = (-4).toBigInteger()
private val pos7 = 7.toBigInteger()
private val neg7 = (-7).toBigInteger()
private val pos18 = 18.toBigInteger()

internal fun runConstructorTests() {
    testSingleValConstructor()
    testPairValConstructor()
    testStringConstructor()
}

// thorough testing is done in parsing tests
private fun testStringConstructor() {
    // Decimal string
    var ef = ExactFraction("1.51")
    assertEquals(151.toBigInteger(), ef.numerator)
    assertEquals(100.toBigInteger(), ef.denominator)

    // EF string
    ef = ExactFraction("EF[-7 3]")
    assertEquals(neg7, ef.numerator)
    assertEquals(pos3, ef.denominator)

    // Invalid
    assertFails { ExactFraction("[]") }
}

private fun testSingleValConstructor() {
    // BigInteger
    var ef = ExactFraction(zero)
    assertEquals(ef.numerator, zero)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(pos3)
    assertEquals(ef.numerator, pos3)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(neg3)
    assertEquals(ef.numerator, neg3)
    assertEquals(ef.denominator, pos1)

    // Int
    ef = ExactFraction(0)
    assertEquals(ef.numerator, zero)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(3)
    assertEquals(ef.numerator, pos3)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(-3)
    assertEquals(ef.numerator, neg3)
    assertEquals(ef.denominator, pos1)

    // Long
    ef = ExactFraction(0L)
    assertEquals(ef.numerator, zero)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(3L)
    assertEquals(ef.numerator, pos3)
    assertEquals(ef.denominator, pos1)

    ef = ExactFraction(-3L)
    assertEquals(ef.numerator, neg3)
    assertEquals(ef.denominator, pos1)
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
    var ef = ExactFraction(zero, pos1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(pos1, zero) }

    ef = ExactFraction(pos4, pos1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(pos7, pos18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(pos4, pos3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg4, pos1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(neg1, pos3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg7, pos4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Int, Int
    ef = ExactFraction(0, 1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1, 0) }

    ef = ExactFraction(4, 1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7, 18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4, 3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4, 1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1, 3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7, 4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Long, Long
    ef = ExactFraction(0L, 1L)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1L, 0L) }

    ef = ExactFraction(4L, 1L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7L, 18L)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4L, 3L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4L, 1L)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1L, 3L)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7L, 4L)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // BigInteger, Int
    ef = ExactFraction(zero, 1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(pos1, 0) }

    ef = ExactFraction(pos4, 1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(pos7, 18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(pos4, 3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg4, 1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(neg1, 3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg7, 4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Int, BigInteger
    ef = ExactFraction(0, pos1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1, zero) }

    ef = ExactFraction(4, pos1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7, pos18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4, pos3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4, pos1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1, pos3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7, pos4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Int, Long
    ef = ExactFraction(0, 1L)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1, 0L) }

    ef = ExactFraction(4, 1L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7, 18L)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4, 3L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4, 1L)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1, 3L)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7, 4L)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Long, Int
    ef = ExactFraction(0L, 1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1L, 0) }

    ef = ExactFraction(4L, 1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7L, 18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4L, 3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4L, 1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1L, 3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7L, 4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // BigInteger, Long
    ef = ExactFraction(zero, 1L)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(pos1, 0L) }

    ef = ExactFraction(pos4, 1L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(pos7, 18L)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(pos4, 3L)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg4, 1L)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(neg1, 3L)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(neg7, 4L)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)

    // Long, BigInteger
    ef = ExactFraction(0L, pos1)
    assertEquals(zero, ef.numerator)
    assertEquals(pos1, ef.denominator)

    assertDivByZero { ExactFraction(1L, zero) }

    ef = ExactFraction(4L, pos1)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(7L, pos18)
    assertEquals(pos7, ef.numerator)
    assertEquals(pos18, ef.denominator)

    ef = ExactFraction(4L, pos3)
    assertEquals(pos4, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-4L, pos1)
    assertEquals(neg4, ef.numerator)
    assertEquals(pos1, ef.denominator)

    ef = ExactFraction(-1L, pos3)
    assertEquals(neg1, ef.numerator)
    assertEquals(pos3, ef.denominator)

    ef = ExactFraction(-7L, pos4)
    assertEquals(neg7, ef.numerator)
    assertEquals(pos4, ef.denominator)
}
