package xyz.lbres.exactnumbers.exactfraction

import assertDivByZero
import kotlin.test.assertEquals

fun runInverseTests() {
    var ef = ExactFraction(1, 2)
    var expected = ExactFraction(2, 1)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(2, 1)
    expected = ExactFraction(1, 2)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(-3, 2)
    expected = ExactFraction(-2, 3)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(-3, -9)
    expected = ExactFraction(3, 1)
    assertEquals(expected, ef.inverse())

    ef = ExactFraction(19, 7)
    expected = ExactFraction(7, 19)
    assertEquals(expected, ef.inverse())

    assertDivByZero { ExactFraction.ZERO.inverse() }
}

fun runAbsoluteValueTests() {
    var ef = ExactFraction(0)
    var expected = ExactFraction(0)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(3)
    expected = ExactFraction(3)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(-3)
    expected = ExactFraction(3)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(3, 5)
    expected = ExactFraction(3, 5)
    assertEquals(expected, ef.absoluteValue())

    ef = ExactFraction(-5, 3)
    expected = ExactFraction(5, 3)
    assertEquals(expected, ef.absoluteValue())
}

fun runIsNegativeTests() {
    var ef = ExactFraction(0)
    assert(!ef.isNegative())

    ef = ExactFraction(1)
    assert(!ef.isNegative())

    ef = ExactFraction(2, 7)
    assert(!ef.isNegative())

    ef = ExactFraction(-1)
    assert(ef.isNegative())

    ef = ExactFraction(-2, 7)
    assert(ef.isNegative())
}

fun runIsZeroTests() {
    var ef = ExactFraction(0)
    assert(ef.isZero())

    ef = ExactFraction(1)
    assert(!ef.isZero())

    ef = ExactFraction(2, 7)
    assert(!ef.isZero())

    ef = ExactFraction(-1)
    assert(!ef.isZero())

    ef = ExactFraction(-2, 7)
    assert(!ef.isZero())
}

fun runRoundToWholeTests() {
    // whole
    var ef = ExactFraction.ZERO
    var expected = ExactFraction.ZERO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction.ONE
    expected = ExactFraction.ONE
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(10000)
    expected = ExactFraction(10000)
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-123)
    expected = ExactFraction(-123)
    assertEquals(expected, ef.roundToWhole())

    // up
    ef = ExactFraction.HALF
    expected = ExactFraction.ONE
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-1, 3)
    expected = ExactFraction.ZERO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(5, 3)
    expected = ExactFraction.TWO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-4, 3)
    expected = ExactFraction.NEG_ONE
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(200000, 17)
    expected = ExactFraction(11765)
    assertEquals(expected, ef.roundToWhole())

    // down
    ef = ExactFraction(1, 10)
    expected = ExactFraction.ZERO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-9, 10)
    expected = ExactFraction.NEG_ONE
    assertEquals(expected, ef.roundToWhole())
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-5, 2)
    expected = -ExactFraction.THREE
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(17, 100000)
    expected = ExactFraction.ZERO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(7, 3)
    expected = ExactFraction.TWO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(-5, 3)
    expected = -ExactFraction.TWO
    assertEquals(expected, ef.roundToWhole())

    ef = ExactFraction(100000, 17)
    expected = ExactFraction(5882)
    assertEquals(expected, ef.roundToWhole())
}
