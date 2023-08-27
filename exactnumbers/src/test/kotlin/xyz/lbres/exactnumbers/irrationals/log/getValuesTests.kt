package xyz.lbres.exactnumbers.irrationals.log

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal fun runGetValueTests() {
    // base 10
    var logNum = Log(ExactFraction.ONE)
    var expected = 0.toBigDecimal()
    assertEquals(expected, logNum.getValue())

    logNum = Log(100)
    expected = 2.toBigDecimal()
    assertEquals(expected, logNum.getValue())

    logNum = Log(3333)
    expected = BigDecimal("3.52283531366053")
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction.HALF)
    expected = BigDecimal("-0.30102999566398114")
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction(21, 2))
    expected = BigDecimal("1.02118929906993786")
    assertEquals(expected, logNum.getValue())

    // base 2
    logNum = Log(ExactFraction.ONE, 2)
    expected = 0.toBigDecimal()
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction(1, 8), 2)
    expected = (-3).toBigDecimal()
    assertEquals(expected, logNum.getValue())

    logNum = Log(200, 2)
    expected = BigDecimal("7.643856189774724")
    assertEquals(expected, logNum.getValue())

    // other
    logNum = Log(216, 6)
    expected = 3.toBigDecimal()
    assertEquals(expected, logNum.getValue())

    logNum = Log(15151515, 24)
    expected = BigDecimal("5.202432673429519")
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction(18, 109), 9)
    expected = BigDecimal("-0.8196595572931246")
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction(2000, 3), 3)
    expected = BigDecimal("5.9186395764396105")
    assertEquals(expected, logNum.getValue())

    // divided
    logNum = Log(10, 10, true)
    expected = BigDecimal.ONE
    assertEquals(expected, logNum.getValue())

    logNum = Log(8, 2, true)
    expected = BigDecimal("0.33333333333333333333")
    assertEquals(expected, logNum.getValue())

    logNum = Log(ExactFraction(1, 4), 10, true)
    expected = BigDecimal("-1.6609640474436814234")
    assertEquals(expected, logNum.getValue())

    logNum = Log(12, 4, true)
    expected = BigDecimal("0.55788589130225962125")
    assertEquals(expected, logNum.getValue())
}

internal fun runGetRationalValueTests() {
    // irrational
    var logNum = Log(6)
    assertNull(logNum.getRationalValue())

    logNum = Log(1000, 5)
    assertNull(logNum.getRationalValue())

    logNum = Log(ExactFraction(5, 12), 5)
    assertNull(logNum.getRationalValue())

    logNum = Log(ExactFraction(5, 12), 5, true)
    assertNull(logNum.getRationalValue())

    // rational
    logNum = Log.ZERO
    var expected = ExactFraction.ZERO
    assertEquals(expected, logNum.getRationalValue())

    logNum = Log(32, 2)
    expected = ExactFraction.FIVE
    assertEquals(expected, logNum.getRationalValue())

    logNum = Log(ExactFraction(1, 27), 3)
    expected = -ExactFraction.THREE
    assertEquals(expected, logNum.getRationalValue())

    logNum = Log(ExactFraction(1, 1000))
    expected = -ExactFraction.THREE
    assertEquals(expected, logNum.getRationalValue())

    logNum = Log(ExactFraction(1, 1000), true)
    expected = ExactFraction(-1, 3)
    assertEquals(expected, logNum.getRationalValue())
}
