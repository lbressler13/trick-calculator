package xyz.lbres.expressions.term

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.common.Irrational
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import java.math.BigDecimal
import kotlin.test.assertEquals

private val logNum1 = Log(ExactFraction(15, 4))
private val logNum2 = Log(8, 7)
private val sqrt1 = Sqrt(64)
private val sqrt2 = Sqrt(ExactFraction(20, 33))
private val one = ExactFraction.ONE

internal fun runGetSimplifiedTests() {
    // simplified
    var term = Term(ExactFraction.EIGHT, listOf(Pi(), Pi(true)))
    var expectedCoeff = ExactFraction.EIGHT
    var expectedNumbers: List<Irrational> = listOf()
    var result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction(-3, 2), listOf(Pi(), Pi(true), Pi(), logNum1))
    expectedCoeff = ExactFraction(-3, 2)
    expectedNumbers = listOf(logNum1, Pi())
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.HALF, listOf(Log.ONE, logNum1))
    expectedCoeff = ExactFraction.HALF
    expectedNumbers = listOf(logNum1)
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(-ExactFraction.HALF, listOf(Log.ONE, Pi(true)))
    expectedCoeff = -ExactFraction.HALF
    expectedNumbers = listOf(Pi(true))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.TEN, listOf(Sqrt.ONE, sqrt2))
    expectedCoeff = ExactFraction(20)
    expectedNumbers = listOf(Sqrt(ExactFraction(5, 33)))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.TWO, listOf(Sqrt(64), Sqrt(ExactFraction(75, 98)), Sqrt(26)))
    expectedCoeff = ExactFraction(80, 7)
    expectedNumbers = listOf(Sqrt(ExactFraction(39)))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(
        ExactFraction(18, 5),
        listOf(Pi(true), logNum2, Pi(true), logNum2, logNum1, logNum2.swapDivided(), Pi(true), Pi())
    )
    expectedCoeff = ExactFraction(18, 5)
    expectedNumbers = listOf(logNum2, logNum1, Pi(true), Pi(true))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.FOUR, listOf(Log(100), Sqrt(9), Sqrt(ExactFraction(1, 4))))
    expectedCoeff = ExactFraction(12)
    expectedNumbers = listOf()
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(-ExactFraction.EIGHT, listOf(Pi(true), Sqrt(ExactFraction(27, 98))))
    expectedCoeff = ExactFraction(-24, 7)
    expectedNumbers = listOf(Sqrt(ExactFraction(3, 2)), Pi(true))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction(20), listOf(Log(ExactFraction(1, 27), 3, true)))
    expectedCoeff = ExactFraction(-20, 3)
    expectedNumbers = listOf()
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(
        ExactFraction(3, 5),
        listOf(
            Log(4),
            Log(ExactFraction(625), 5),
            Sqrt(ExactFraction(18, 7)),
            Pi(),
            Sqrt(25),
            Sqrt(ExactFraction(13, 3)),
            Log(1000, 12),
            Log(ExactFraction(1, 16), 4, true)
        )
    )
    expectedCoeff = ExactFraction(-6)
    expectedNumbers = listOf(
        Log(4),
        Log(1000, 12),
        Sqrt(ExactFraction(78, 7)),
        Pi()
    )
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    // no changes
    term = Term(ExactFraction.EIGHT, listOf())
    expectedCoeff = ExactFraction.EIGHT
    expectedNumbers = listOf()
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.EIGHT, listOf(logNum1))
    expectedCoeff = ExactFraction.EIGHT
    expectedNumbers = listOf(logNum1)
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.EIGHT, listOf(Sqrt(ExactFraction(1, 46))))
    expectedCoeff = ExactFraction.EIGHT
    expectedNumbers = listOf(Sqrt(ExactFraction(1, 46)))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction(-5, 6), listOf(Pi(true)))
    expectedCoeff = ExactFraction(-5, 6)
    expectedNumbers = listOf(Pi(true))
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)

    term = Term(ExactFraction.SEVEN, listOf(Pi(), Pi(), logNum1, logNum1, logNum2.swapDivided(), Sqrt(5)))
    expectedCoeff = ExactFraction.SEVEN
    expectedNumbers = listOf(logNum1, logNum1, logNum2.swapDivided(), Sqrt(5), Pi(), Pi())
    result = term.getSimplified()
    assertEquals(expectedCoeff, result.coefficient)
    assertEquals(expectedNumbers, result.numbers)
}

internal fun runGetValueTests() {
    // just number
    var term = Term.ZERO
    var expected = BigDecimal.ZERO
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction.EIGHT, listOf())
    expected = BigDecimal("8")
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction(-1, 3), listOf())
    expected = BigDecimal("-0.33333333333333333333")
    assertEquals(expected, term.getValue())

    // just logs
    term = Term(one, listOf(logNum1, logNum1.swapDivided()))
    expected = BigDecimal.ONE
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(Log(3333)))
    expected = BigDecimal("3.52283531366053")
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(logNum1, logNum2))
    expected = BigDecimal("0.61342218956802803344500481172832")
    assertEquals(expected, term.getValue())

    // just pi
    term = Term(one, listOf(Pi(), Pi(true)))
    expected = BigDecimal.ONE
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(Pi(true)))
    expected = BigDecimal("0.31830988618379069570")
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(Pi(), Pi(), Pi()))
    expected = BigDecimal("31.006276680299813114880451174049119330924860257")
    assertEquals(expected, term.getValue())

    // just sqrt
    term = Term(one, listOf(Sqrt(ExactFraction(9, 5))))
    expected = BigDecimal("1.34164078649987381784")
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(Sqrt(121)))
    expected = BigDecimal("11")
    assertEquals(expected, term.getValue())

    term = Term(one, listOf(Sqrt(12), Sqrt(ExactFraction(9, 25))))
    expected = BigDecimal("2.0784609690826527522")
    assertEquals(expected, term.getValue())

    // combination
    term = Term(ExactFraction(-8, 3), listOf(Pi()))
    expected = BigDecimal("-8.3775804095727813333")
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction.EIGHT, listOf(Sqrt(49), Sqrt(2), Log(9)))
    expected = BigDecimal("75.57215112395364893851321831545508672")
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction.SEVEN, listOf(Log(ExactFraction(6, 7), 4), Pi(), Log(40)))
    expected = BigDecimal("-3.9175691871908989550925271506970548502975209152")
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction.HALF, listOf(Log(4, 2, true), Pi(true), Log(123456789)))
    expected = BigDecimal("0.64390230285929702583103243749028475")
    assertEquals(expected, term.getValue())

    term = Term(ExactFraction(3, 11), listOf(Log(5, 2), Pi(), Sqrt(122)))
    expected = BigDecimal("21.973899001484265398")
    assertEquals(expected, term.getValue())
}
