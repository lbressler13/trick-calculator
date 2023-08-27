package xyz.lbres.exactnumbers.exactfraction

import xyz.lbres.exactnumbers.exception.NumberOverflowException
import xyz.lbres.exactnumbers.ext.toExactFraction
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

fun runPowTests() {
    var base = ExactFraction.NINE
    var exp = ExactFraction.ZERO
    var expected = ExactFraction.ONE
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.NINE
    exp = ExactFraction.ONE
    expected = ExactFraction.NINE
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.ONE
    exp = 1000000.toExactFraction()
    expected = ExactFraction.ONE
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.NEG_ONE
    exp = 1000000.toExactFraction()
    expected = ExactFraction.ONE
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.NEG_ONE
    exp = 1000001.toExactFraction()
    expected = ExactFraction.NEG_ONE
    assertEquals(expected, base.pow(exp))

    // exp > 0
    base = ExactFraction.EIGHT
    exp = ExactFraction.ONE
    expected = ExactFraction.EIGHT
    assertEquals(expected, base.pow(exp))

    base = 23.toExactFraction()
    exp = ExactFraction.FOUR
    expected = 279841.toExactFraction()
    assertEquals(expected, base.pow(exp))

    base = -ExactFraction.TWO
    exp = 20.toExactFraction()
    expected = 1048576.toExactFraction()
    assertEquals(expected, base.pow(exp))

    base = -ExactFraction.TWO
    exp = ExactFraction.SEVEN
    expected = (-128).toExactFraction()
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.ONE
    exp = ExactFraction("3147483647") // bigger than int max
    expected = ExactFraction.ONE
    assertEquals(expected, base.pow(exp)) // tests that it doesn't throw, can't do much else

    base = ExactFraction(3, 8)
    exp = ExactFraction.THREE
    expected = ExactFraction(27, 512)
    assertEquals(expected, base.pow(exp))

    base = ExactFraction(-2, 5)
    exp = ExactFraction.NINE
    expected = ExactFraction(-512, 1953125)
    assertEquals(expected, base.pow(exp))

    // exp < 0
    base = ExactFraction.EIGHT
    exp = ExactFraction.NEG_ONE
    expected = ExactFraction(1, 8)
    assertEquals(expected, base.pow(exp))

    base = 23.toExactFraction()
    exp = -ExactFraction.FOUR
    expected = ExactFraction(1, 279841)
    assertEquals(expected, base.pow(exp))

    base = -ExactFraction.TWO
    exp = (-20).toExactFraction()
    expected = ExactFraction(1, 1048576)
    assertEquals(expected, base.pow(exp))

    base = -ExactFraction.TWO
    exp = -ExactFraction.SEVEN
    expected = ExactFraction(-1, 128)
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.ONE
    exp = ExactFraction("-3147483647") // smaller than int min
    expected = ExactFraction.ONE
    assertEquals(expected, base.pow(exp)) // tests that it doesn't throw, can't do much else

    base = ExactFraction(3, 8)
    exp = -ExactFraction.THREE
    expected = ExactFraction(512, 27)
    assertEquals(expected, base.pow(exp))

    base = ExactFraction(-2, 5)
    exp = -ExactFraction.NINE
    expected = ExactFraction(1953125, -512)
    assertEquals(expected, base.pow(exp))

    base = ExactFraction.TWO
    exp = ExactFraction(6666666)
    try {
        base.pow(exp)
    } catch (e: Exception) {
        throw AssertionError("Computation expected to succeed")
    }

    base = -ExactFraction.TWO
    exp = ExactFraction(6666666)
    try {
        base.pow(exp)
    } catch (e: Exception) {
        throw AssertionError("Computation expected to succeed")
    }

    base = ExactFraction(59)
    exp = ExactFraction(1000000)
    try {
        base.pow(exp)
    } catch (e: Exception) {
        throw AssertionError("Computation expected to succeed")
    }

    // non-whole
    val expectedError = "Exponents must be whole numbers"
    base = ExactFraction.FOUR
    exp = ExactFraction(1, 2)
    assertFailsWith<ArithmeticException>(expectedError) { base.pow(exp) }

    exp = ExactFraction(-8, 5)
    assertFailsWith<ArithmeticException>(expectedError) { base.pow(exp) }

    base = ExactFraction(3, 7)
    exp = ExactFraction(3, 7)
    assertFailsWith<ArithmeticException>(expectedError) { base.pow(exp) }

    base = ExactFraction.TWO
    exp = ExactFraction(999999999999)
    assertFailsWith<NumberOverflowException> { base.pow(exp) }

    base = ExactFraction.HALF
    exp = ExactFraction(999999999999)
    assertFailsWith<NumberOverflowException> { base.pow(exp) }
}
