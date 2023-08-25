package xyz.lbres.expressions.term

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.exactnumbers.irrationals.sqrt.Sqrt
import kotlin.test.assertEquals

private val logNum1 = Log(ExactFraction(15, 4))
private val logNum2 = Log(8, 7)
private val logNum3 = Log(ExactFraction(19, 33), true)
private val logNum4 = Log(ExactFraction(25, 121))
private val one = ExactFraction.ONE

internal fun runGetLogsTests() {
    // empty
    var expected: List<Log> = listOf()

    var term = Term(one, listOf())
    assertEquals(expected, term.getLogs())

    term = Term(ExactFraction.TEN, listOf())
    assertEquals(expected, term.getLogs())

    term = Term(one, listOf(Pi(), Pi(), Sqrt(ExactFraction(64, 9))))
    assertEquals(expected, term.getLogs())

    // just logs
    term = Term(one, listOf(logNum1))
    expected = listOf(logNum1)
    assertEquals(expected, term.getLogs())

    term = Term(one, listOf(logNum1, logNum1))
    expected = listOf(logNum1, logNum1)
    assertEquals(expected, term.getLogs())

    term = Term(one, listOf(logNum1, logNum1.swapDivided()))
    expected = listOf(logNum1, logNum1.swapDivided())
    assertEquals(expected, term.getLogs())

    term = Term(one, listOf(logNum3, logNum4, logNum1))
    expected = listOf(logNum3, logNum4, logNum1)
    assertEquals(expected, term.getLogs())

    // mix
    term = Term(one, listOf(Pi(), logNum3, Sqrt(2)))
    expected = listOf(logNum3)
    assertEquals(expected, term.getLogs())

    term = Term(ExactFraction.EIGHT, listOf(logNum2, Pi(), Pi(true), logNum2, logNum3, Sqrt(32), logNum4, Sqrt(15)))
    expected = listOf(logNum2, logNum2, logNum3, logNum4)
    assertEquals(expected, term.getLogs())
}

internal fun runGetPiCountTests() {
    // zero
    var expected = 0

    var term = Term(one, listOf())
    assertEquals(expected, term.getPiCount())

    term = Term(ExactFraction.TEN, listOf())
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(Pi(), Pi(true)))
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(Pi(), Pi(true), Pi(), Pi(true), Pi(), Pi(true)))
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(logNum1, logNum4, Sqrt(ExactFraction(64, 9))))
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(logNum3, logNum4, Pi(true), Pi(), logNum2, Pi(true), Pi()))
    assertEquals(expected, term.getPiCount())

    // just pi
    term = Term(one, listOf(Pi()))
    expected = 1
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(Pi(true)))
    expected = -1
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(Pi(true), Pi(true), Pi(true)))
    expected = -3
    assertEquals(expected, term.getPiCount())

    term = Term(one, listOf(Pi(), Pi(true), Pi(), Pi(), Pi(true)))
    expected = 1
    assertEquals(expected, term.getPiCount())

    // mix
    term = Term(one, listOf(Pi(true), logNum2, Sqrt(2)))
    expected = -1
    assertEquals(expected, term.getPiCount())

    term = Term(ExactFraction.EIGHT, listOf(logNum3, Pi(), Pi(), Sqrt(36), logNum2, Pi(true), Pi()))
    expected = 2
    assertEquals(expected, term.getPiCount())
}

internal fun runGetSquareRootsTests() {
    // zero
    var expected: List<Sqrt> = listOf()

    var term = Term(one, listOf())
    assertEquals(expected, term.getSquareRoots())

    term = Term(ExactFraction.TEN, listOf())
    assertEquals(expected, term.getSquareRoots())

    term = Term(one, listOf(Pi(), logNum1, Pi(true), logNum2))
    assertEquals(expected, term.getSquareRoots())

    // just sqrt
    term = Term(one, listOf(Sqrt(4)))
    expected = listOf(Sqrt(4))
    assertEquals(expected, term.getSquareRoots())

    term = Term(one, listOf(Sqrt(4), Sqrt(4)))
    expected = listOf(Sqrt(4), Sqrt(4))
    assertEquals(expected, term.getSquareRoots())

    term = Term(one, listOf(Sqrt(ExactFraction(9, 23)), Sqrt(ExactFraction(23, 9))))
    expected = listOf(Sqrt(ExactFraction(9, 23)), Sqrt(ExactFraction(23, 9)))
    assertEquals(expected, term.getSquareRoots())

    term = Term(one, listOf(Sqrt.ONE, Sqrt(97), Sqrt(ExactFraction(9, 25))))
    expected = listOf(Sqrt.ONE, Sqrt(97), Sqrt(ExactFraction(9, 25)))
    assertEquals(expected, term.getSquareRoots())

    // mix
    term = Term(one, listOf(Pi(true), logNum2, Sqrt(2)))
    expected = listOf(Sqrt(2))
    assertEquals(expected, term.getSquareRoots())

    term = Term(ExactFraction.EIGHT, listOf(Pi(), Sqrt.ONE, logNum1, Sqrt(97), Sqrt(ExactFraction(9, 25)), logNum2))
    expected = listOf(Sqrt.ONE, Sqrt(97), Sqrt(ExactFraction(9, 25)))
    assertEquals(expected, term.getSquareRoots())
}
