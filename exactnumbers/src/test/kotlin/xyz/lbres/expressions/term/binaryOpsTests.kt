package xyz.lbres.expressions.term

import assertDivByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import kotlin.test.assertEquals

private val logNum1 = Log(ExactFraction(15, 4))
private val logNum2 = Log(8)
private val logNum3 = Log(ExactFraction(19, 33))
private val logNum4 = Log(ExactFraction(25, 121))
private val sqrt1 = Sqrt(99)
private val sqrt2 = Sqrt(ExactFraction(64, 121))
private val sqrt3 = Sqrt(ExactFraction(15, 44))
private val one = ExactFraction.ONE

internal fun runTimesTests() {
    // zero
    var term1 = Term.ZERO
    var expected = Term.ZERO

    var term2 = Term.ZERO
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term2 = Term(ExactFraction.EIGHT, listOf())
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term2 = Term(ExactFraction.EIGHT, listOf(logNum3, logNum4, Pi(true)))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    // just logs
    term1 = Term(one, listOf(logNum1, logNum2, logNum3))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(logNum1, logNum2, logNum3))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf(logNum1, logNum2))
    term2 = Term(one, listOf(logNum1, logNum4))
    expected = Term(one, listOf(logNum1, logNum1, logNum2, logNum4))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    // just pi
    term1 = Term(one, listOf(Pi(), Pi()))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(Pi(), Pi()))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf(Pi(), Pi()))
    term2 = Term(one, listOf(Pi(true)))
    expected = Term(one, listOf(Pi(), Pi(), Pi(true)))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf(Pi(), Pi(true), Pi()))
    term2 = Term(one, listOf(Pi(true), Pi()))
    expected = Term(one, listOf(Pi(), Pi(), Pi(), Pi(true), Pi(true)))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    // just sqrt
    term1 = Term(one, listOf(Sqrt(ExactFraction(19, 9)), sqrt1))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(Sqrt(ExactFraction(19, 9)), sqrt1))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf(Sqrt(ExactFraction(19, 9)), sqrt1))
    term2 = Term(one, listOf(sqrt2))
    expected = Term(one, listOf(Sqrt(ExactFraction(19, 9)), sqrt1, sqrt2))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf(sqrt1, sqrt3))
    term2 = Term(one, listOf(sqrt3, sqrt2))
    expected = Term(one, listOf(sqrt1, sqrt2, sqrt3, sqrt3))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    // just exact fraction
    term1 = Term(one, listOf())
    term2 = Term(ExactFraction.TWO, listOf())
    expected = Term(ExactFraction.TWO, listOf())
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(one, listOf())
    term2 = Term(ExactFraction(-17, 3), listOf())
    expected = Term(ExactFraction(-17, 3), listOf())
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(ExactFraction.SEVEN, listOf())
    term2 = Term(ExactFraction.HALF, listOf())
    expected = Term(ExactFraction(7, 2), listOf())
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    // combination
    term1 = Term(ExactFraction(1, 4), listOf(logNum1, sqrt1, Pi()))
    term2 = Term(ExactFraction(-1, 3), listOf(Pi(true), Pi()))
    expected = Term(ExactFraction(-1, 12), listOf(logNum1, sqrt1, Pi(), Pi(), Pi(true)))
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)

    term1 = Term(ExactFraction.EIGHT, listOf(logNum1, Pi(true), logNum4, sqrt3))
    term2 = Term(ExactFraction(-15), listOf(Pi(), logNum1, sqrt1, logNum2, Pi(), logNum3))
    expected = Term(
        ExactFraction(-120),
        listOf(logNum1, logNum1, logNum2, logNum3, logNum4, sqrt3, sqrt1, Pi(), Pi(), Pi(true))
    )
    assertEquals(expected, term1 * term2)
    assertEquals(expected, term2 * term1)
}

internal fun runDivTests() {
    // divide by zero
    assertDivByZero { Term.ONE / Term.ZERO }

    // zero
    var term1 = Term.ZERO
    var term2 = Term.ONE
    var expected = Term.ZERO
    assertEquals(expected, term1 / term2)

    // just logs
    term1 = Term(one, listOf(logNum1, logNum2, logNum3))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(logNum1, logNum2, logNum3))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf())
    term2 = Term(one, listOf(logNum1, logNum2, logNum3))
    expected = Term(one, listOf(logNum1.swapDivided(), logNum2.swapDivided(), logNum3.swapDivided()))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf(logNum1, logNum2))
    term2 = Term(one, listOf(logNum3))
    expected = Term(one, listOf(logNum1, logNum2, logNum3.swapDivided()))
    assertEquals(expected, term1 / term2)

    // just pi
    term1 = Term(one, listOf(Pi()))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(Pi()))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf())
    term2 = Term(one, listOf(Pi()))
    expected = Term(one, listOf(Pi().swapDivided()))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf(Pi(), Pi(), Pi(true)))
    term2 = Term(one, listOf(Pi(true), Pi(), Pi(true)))
    expected = Term(one, listOf(Pi(), Pi(), Pi(), Pi(), Pi(true), Pi(true)))
    assertEquals(expected, term1 / term2)

    // just sqrt
    term1 = Term(one, listOf(sqrt1, sqrt2, sqrt3))
    term2 = Term(one, listOf())
    expected = Term(one, listOf(sqrt1, sqrt2, sqrt3))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf())
    term2 = Term(one, listOf(sqrt1, sqrt2, sqrt3))
    expected = Term(one, listOf(sqrt1.swapDivided(), sqrt2.swapDivided(), sqrt3.swapDivided()))
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf(sqrt1, sqrt2))
    term2 = Term(one, listOf(sqrt3))
    expected = Term(one, listOf(sqrt1, sqrt2, sqrt3.swapDivided()))
    assertEquals(expected, term1 / term2)

    // just exact fraction
    term1 = Term(ExactFraction(-4, 3), listOf())
    term2 = Term(one, listOf())
    expected = Term(ExactFraction(-4, 3), listOf())
    assertEquals(expected, term1 / term2)

    term1 = Term(one, listOf())
    term2 = Term(ExactFraction.TWO, listOf())
    expected = Term(ExactFraction.HALF, listOf())
    assertEquals(expected, term1 / term2)

    term1 = Term(ExactFraction(-5, 2), listOf())
    term2 = Term(ExactFraction(3, 4), listOf())
    expected = Term(ExactFraction(-10, 3), listOf())
    assertEquals(expected, term1 / term2)

    // mix
    term1 = Term(ExactFraction(1, 4), listOf(logNum1))
    term2 = Term(ExactFraction(-1, 3), listOf(Pi(), sqrt1))

    expected = Term(ExactFraction(-3, 4), listOf(logNum1, sqrt1.swapDivided(), Pi(true)))
    assertEquals(expected, term1 / term2)

    expected = Term(ExactFraction(-4, 3), listOf(logNum1.swapDivided(), sqrt1, Pi()))
    assertEquals(expected, term2 / term1)

    term1 = Term(ExactFraction.EIGHT, listOf(logNum3, Pi(true), logNum1, sqrt2, Pi(true)))
    term2 = Term(ExactFraction(15), listOf(sqrt3, Pi(), logNum4, logNum1.swapDivided()))

    expected = Term(
        ExactFraction(8, 15),
        listOf(logNum1, logNum1, logNum3, logNum4.swapDivided(), sqrt2, sqrt3.swapDivided(), Pi(true), Pi(true), Pi(true))
    )
    assertEquals(expected, term1 / term2)

    expected = Term(
        ExactFraction(15, 8),
        listOf(logNum1.swapDivided(), logNum1.swapDivided(), logNum3.swapDivided(), logNum4, sqrt2.swapDivided(), sqrt3, Pi(), Pi(), Pi())
    )
    assertEquals(expected, term2 / term1)
}
