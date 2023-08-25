package xyz.lbres.exactnumbers.expressions.term

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import xyz.lbres.exactnumbers.expressions.term.Term
import kotlin.test.assertEquals

private val logNum1 = Log(ExactFraction(15, 4))
private val logNum2 = Log(8, 7)
private val logNum3 = Log(ExactFraction(19, 33), true)
private val logNum4 = Log(ExactFraction(25, 121))
private val one = ExactFraction.ONE

internal fun runConstructorTests() {
    // zero
    var expectedCoeff = ExactFraction.ZERO
    var expectedNumbers: List<Irrational> = listOf()

    var term = Term(ExactFraction.ZERO, listOf())
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(ExactFraction.ZERO, listOf(logNum1, Sqrt(35), logNum3, Pi()))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(ExactFraction.FIVE, listOf(logNum1, Log.ZERO))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(ExactFraction.FIVE, listOf(logNum1, Sqrt.ZERO))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    // others
    term = Term(-ExactFraction.FIVE, listOf())
    expectedCoeff = -ExactFraction.FIVE
    expectedNumbers = listOf()
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(ExactFraction(17, 3), listOf(Pi(), logNum2))
    expectedCoeff = ExactFraction(17, 3)
    expectedNumbers = listOf(Pi(), logNum2)
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(
        ExactFraction(
            17,
            3
        ), listOf(Pi(), logNum2, Sqrt(ExactFraction(9, 25))))
    expectedCoeff = ExactFraction(17, 3)
    expectedNumbers = listOf(Pi(), logNum2, Sqrt(
        ExactFraction(
            9,
            25
        )
    ))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term(
        ExactFraction(-4, 5),
        listOf(Pi(true), logNum2, logNum1, Pi(), logNum1.swapDivided(), Sqrt(32), logNum2, Sqrt(109))
    )
    expectedCoeff = ExactFraction(-4, 5)
    expectedNumbers = listOf(Pi(true), logNum2, logNum1, Pi(), logNum1.swapDivided(), Sqrt(32), logNum2, Sqrt(109))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)
}

internal fun runFromValuesTests() {
    // zero
    var expectedCoeff = ExactFraction.ZERO
    var expectedNumbers: List<Irrational> = listOf()

    var term = Term.fromValues(ExactFraction.ZERO, listOf(), listOf(), 0)
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(ExactFraction.ZERO, listOf(logNum1, Log.ONE), listOf(Sqrt.ONE), 8)
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(
        -ExactFraction.EIGHT,
        listOf(logNum1, logNum2, Log.ZERO, logNum3),
        listOf(Sqrt(64)),
        5
    )
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(
        ExactFraction.TWO, listOf(logNum1), listOf(
            Sqrt(64), Sqrt.ZERO, Sqrt(
                ExactFraction(3, 19)
            )
        ), -2
    )
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    // nonzero
    term = Term.fromValues(ExactFraction(-5, 7), listOf(), listOf(), 0)
    expectedCoeff = ExactFraction(-5, 7)
    expectedNumbers = listOf()
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(ExactFraction.EIGHT, listOf(), listOf(), 3)
    expectedCoeff = ExactFraction.EIGHT
    expectedNumbers = listOf(Pi(), Pi(), Pi())
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(ExactFraction.EIGHT, listOf(), listOf(), -3)
    expectedCoeff = ExactFraction.EIGHT
    expectedNumbers = listOf(Pi(true), Pi(true), Pi(true))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(ExactFraction(-2, 191), listOf(logNum1, logNum2, logNum1), listOf(), 0)
    expectedCoeff = ExactFraction(-2, 191)
    expectedNumbers = listOf(logNum1, logNum2, logNum1)
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(ExactFraction(-2, 191), listOf(), listOf(Sqrt.ONE, Sqrt(52)), 0)
    expectedCoeff = ExactFraction(-2, 191)
    expectedNumbers = listOf(Sqrt.ONE, Sqrt(52))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(
        ExactFraction(-2, 191),
        listOf(logNum1, logNum2, logNum3),
        listOf(Sqrt(12), Sqrt(99)),
        2
    )
    expectedCoeff = ExactFraction(-2, 191)
    expectedNumbers = listOf(logNum1, logNum2, logNum3, Sqrt(12), Sqrt(99), Pi(), Pi())
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)

    term = Term.fromValues(
        ExactFraction(22),
        listOf(logNum1, logNum2, logNum3),
        listOf(Sqrt(12), Sqrt(99)),
        -2
    )
    expectedCoeff = ExactFraction(22)
    expectedNumbers = listOf(logNum1, logNum2, logNum3, Sqrt(12), Sqrt(99), Pi(true), Pi(true))
    assertEquals(expectedCoeff, term.coefficient)
    assertEquals(expectedNumbers, term.numbers)
}
