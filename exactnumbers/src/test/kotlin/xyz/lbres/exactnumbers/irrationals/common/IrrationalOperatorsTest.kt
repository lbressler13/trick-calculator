package xyz.lbres.exactnumbers.irrationals.common

import assertDivByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.log.Log
import xyz.lbres.exactnumbers.irrationals.pi.Pi
import xyz.lbres.expressions.term.Term
import kotlin.test.Test
import kotlin.test.assertEquals

internal class IrrationalOperatorsTest {
    @Test
    internal fun testTimes() {
        // Log
        var num1: Irrational = Log.ZERO
        var num2: Irrational = Log.ZERO
        var expected = Term(ExactFraction.ONE, listOf(Log.ZERO, Log.ZERO))
        assertEquals(expected, num1 * num2)

        num1 = Log(ExactFraction(4, 5))
        num2 = Log(ExactFraction(5, 4), true)
        expected = Term(
            ExactFraction.ONE,
            listOf(Log(ExactFraction(4, 5)), Log(ExactFraction(5, 4), true))
        )
        assertEquals(expected, num1 * num2)

        num1 = Log(ExactFraction(4, 5), 3)
        num2 = Log(ExactFraction(5, 4))
        expected = Term(
            ExactFraction.ONE,
            listOf(Log(ExactFraction(4, 5), 3), Log(ExactFraction(5, 4)))
        )
        assertEquals(expected, num1 * num2)

        // Pi
        num1 = Pi()
        num2 = Pi()
        expected = Term(ExactFraction.ONE, listOf(Pi(), Pi()))
        assertEquals(expected, num1 * num2)

        num1 = Pi(true)
        num2 = Pi()
        expected = Term(ExactFraction.ONE, listOf(Pi(true), Pi()))
        assertEquals(expected, num1 * num2)

        num1 = Pi(true)
        num2 = Pi(true)
        expected = Term(ExactFraction.ONE, listOf(Pi(true), Pi(true)))
        assertEquals(expected, num1 * num2)

        // Both
        num1 = Log(ExactFraction(25), 4)
        num2 = Pi(true)
        expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction(25), 4), Pi(true)))
        assertEquals(expected, num1 * num2)

        num1 = Log(ExactFraction(25, 92))
        num2 = Pi()
        expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction(25, 92)), Pi()))
        assertEquals(expected, num1 * num2)
    }

    @Test
    internal fun testDiv() {
        // error
        assertDivByZero { Log.ONE / Log.ZERO }

        // Log
        var num1: Irrational = Log(ExactFraction.EIGHT)
        var num2: Irrational = Log(ExactFraction(15, 4), 7)
        var expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction.EIGHT), Log(ExactFraction(15, 4), 7, true)))
        assertEquals(expected, num1 / num2)

        num1 = Log(ExactFraction(1, 7))
        num2 = Log(ExactFraction.FOUR, true)
        expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction(1, 7)), Log(ExactFraction.FOUR)))
        assertEquals(expected, num1 / num2)

        num1 = Log(ExactFraction(1, 7), true)
        num2 = Log(ExactFraction.FOUR, true)
        expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction(1, 7), true), Log(ExactFraction.FOUR)))
        assertEquals(expected, num1 / num2)

        // Pi
        num1 = Pi()
        num2 = Pi()
        expected = Term(ExactFraction.ONE, listOf(Pi(), Pi(true)))
        assertEquals(expected, num1 / num2)

        num1 = Pi()
        num2 = Pi(true)
        expected = Term(ExactFraction.ONE, listOf(Pi(), Pi()))
        assertEquals(expected, num1 / num2)

        num1 = Pi(true)
        num2 = Pi(true)
        expected = Term(ExactFraction.ONE, listOf(Pi(true), Pi()))
        assertEquals(expected, num1 / num2)

        // Both
        num1 = Log(ExactFraction.HALF)
        num2 = Pi(true)
        expected = Term(ExactFraction.ONE, listOf(Log(ExactFraction.HALF), Pi()))
        assertEquals(expected, num1 / num2)

        num1 = Pi(true)
        num2 = Log(ExactFraction(15, 7), 4)
        expected = Term(ExactFraction.ONE, listOf(Pi(true), Log(ExactFraction(15, 7), 4, true)))
        assertEquals(expected, num1 / num2)

        num1 = Pi()
        num2 = Log(ExactFraction(15, 7), true)
        expected = Term(ExactFraction.ONE, listOf(Pi(), Log(ExactFraction(15, 7))))
        assertEquals(expected, num1 / num2)
    }
}
