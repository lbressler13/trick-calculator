package xyz.lbres.exactnumbers.irrationals.log

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val one = Log.ONE
private val fractionOne = ExactFraction.ONE

internal fun runGetSimplifiedTests() {
    // zero
    var logNum = Log.ZERO
    var expected = Pair(ExactFraction.ONE, Log.ZERO)
    assertEquals(expected, logNum.getSimplified())

    // one
    logNum = one
    expected = Pair(fractionOne, one)
    assertEquals(expected, logNum.getSimplified())

    // rational
    logNum = Log(1000)
    expected = Pair(ExactFraction.THREE, one)
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(1, 1000))
    expected = Pair(-ExactFraction.THREE, one)
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(32), 2)
    expected = Pair(ExactFraction.FIVE, one)
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(32), 2, isDivided = true)
    expected = Pair(ExactFraction(1, 5), one)
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(1, 81), 3)
    expected = Pair(-ExactFraction.FOUR, one)
    assertEquals(expected, logNum.getSimplified())

    // irrational
    logNum = Log(30)
    expected = Pair(fractionOne, Log(30))
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(100, 9))
    expected = Pair(fractionOne, Log(ExactFraction(100, 9)))
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(100, 2)
    expected = Pair(fractionOne, Log(100, 2))
    assertEquals(expected, logNum.getSimplified())

    logNum = Log(ExactFraction(2, 15), 7)
    expected = Pair(fractionOne, Log(ExactFraction(2, 15), 7))
    assertEquals(expected, logNum.getSimplified())
}

internal fun runSimplifyListTests() {
    // error
    assertFailsWith<ClassCastException> { Log.simplifyList(listOf(Pi(), Log.ONE)) }

    // empty
    var expected: Pair<ExactFraction, List<Log>> = Pair(fractionOne, listOf())

    assertEquals(expected, Log.simplifyList(null))

    var logs: List<Log> = listOf()
    assertEquals(expected, Log.simplifyList(logs))

    // zero
    expected = Pair(ExactFraction.ZERO, listOf())

    logs = listOf(Log.ZERO)
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(Log.ZERO, Log.ZERO)
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(Log.ONE, Log.ZERO, Log(ExactFraction(18, 91))).sorted()
    assertEquals(expected, Log.simplifyList(logs))

    // ones
    logs = listOf(one)
    expected = Pair(fractionOne, listOf())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(one, one, one)
    expected = Pair(fractionOne, listOf())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(one, Log(8), Log(4, 3, true)).sorted()
    expected = Pair(fractionOne, listOf(Log(8), Log(4, 3, true)).sorted())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(one, Log(8), one, one, one, Log(4, 3, true)).sorted()
    expected = Pair(fractionOne, listOf(Log(8), Log(4, 3, true)).sorted())
    assertEquals(expected, Log.simplifyList(logs))

    // inverses
    logs = listOf(Log(8), Log(8, 10, true)).sorted()
    expected = Pair(fractionOne, listOf())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(
        Log(4, 3, true),
        Log(4, 3, true),
        Log(4, 3, false)
    )
    expected = Pair(fractionOne, listOf(Log(4, 3, true)))
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(
        Log(4, 3, true),
        Log(4, 3, false),
        Log(4, 3, false)
    )
    expected = Pair(fractionOne, listOf(Log(4, 3, false)))
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(
        Log(ExactFraction(7, 3)),
        Log(ExactFraction(5, 51)),
        Log(ExactFraction(7, 3), 10, true),
        Log(4, 3, true),
        Log(ExactFraction(7, 3), 10, true),
        Log(ExactFraction(5, 51), 5, true),
        Log(4, 3)
    ).sorted()
    expected = Pair(
        fractionOne,
        listOf(
            Log(ExactFraction(7, 3), 10, true),
            Log(ExactFraction(5, 51)),
            Log(ExactFraction(5, 51), 5, true)
        ).sorted()
    )
    assertEquals(expected, Log.simplifyList(logs))

    // rationals
    logs = listOf(Log(1000))
    expected = Pair(ExactFraction.THREE, listOf())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(Log(9, 3), Log(ExactFraction.HALF, 2))
    expected = Pair(-ExactFraction.TWO, listOf())
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(
        Log(ExactFraction(1, 1000), true),
        Log(6),
        Log(ExactFraction(15, 4), 4),
        Log(ExactFraction(1, 16), 2)
    ).sorted()
    expected = Pair(ExactFraction(4, 3), listOf(Log(6), Log(ExactFraction(15, 4), 4)).sorted())
    assertEquals(expected, Log.simplifyList(logs))

    // no changes
    logs = listOf(Log(3))
    expected = Pair(fractionOne, logs)
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(Log(ExactFraction(3, 7)), Log(ExactFraction(7, 3))).sorted()
    expected = Pair(fractionOne, logs)
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(Log(3), Log(3, 9, true)).sorted()
    expected = Pair(fractionOne, logs)
    assertEquals(expected, Log.simplifyList(logs))

    logs = listOf(
        Log(ExactFraction(5, 51)),
        Log(4, 3, true),
        Log(ExactFraction(7, 3), 10, true),
        Log(ExactFraction(5, 51), 5, true),
        Log(1000005, 3)
    ).sorted()
    expected = Pair(fractionOne, logs)
    assertEquals(expected, Log.simplifyList(logs))
}
