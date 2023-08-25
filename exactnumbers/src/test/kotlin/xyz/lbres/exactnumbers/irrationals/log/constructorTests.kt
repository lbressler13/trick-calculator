package xyz.lbres.exactnumbers.irrationals.log

import assertDivByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.irrationals.log.Log
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal fun runConstructorTests() {
    // error
    assertDivByZero { Log(ExactFraction.ONE, 10, isDivided = true) }
    assertDivByZero { Log(1, 10, isDivided = true) }
    assertDivByZero { Log(1L, 10, isDivided = true) }
    assertDivByZero { Log(BigInteger.ONE, 10, isDivided = true) }

    assertFailsWith<ArithmeticException>("Cannot calculate log of 0") { Log(ExactFraction.ZERO) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of 0") { Log(0) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of 0") { Log(0L) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of 0") { Log(BigInteger.ZERO) }

    assertFailsWith<ArithmeticException>("Cannot calculate log of negative number") { Log(-ExactFraction.TEN) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of negative number") { Log(-10) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of negative number") { Log(-10L) }
    assertFailsWith<ArithmeticException>("Cannot calculate log of negative number") { Log(-BigInteger.TEN) }

    assertFailsWith<ArithmeticException>("Cannot calculate log of negative number") {
        Log(ExactFraction(-4, 3))
    }

    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(ExactFraction.TEN, -1) }
    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(ExactFraction.TEN, 0) }
    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(ExactFraction.TEN, 1) }
    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(10, -1) }
    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(10L, 0) }
    assertFailsWith<ArithmeticException>("Log base must be greater than 1") { Log(BigInteger.TEN, 1) }

    // ExactFraction
    // zero
    var expectedNumber = ExactFraction.ONE
    var expectedBase = 10
    var logs = listOf(Log(ExactFraction.ONE), Log(1), Log(1L), Log(BigInteger.ONE))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertFalse(it.isDivided)
    }

    // all fields
    var logNum = Log(ExactFraction(13, 100), 100, true)
    expectedNumber = ExactFraction(13, 100)
    expectedBase = 100
    assertEquals(expectedNumber, logNum.argument)
    assertEquals(expectedBase, logNum.base)
    assertTrue(logNum.isDivided)

    expectedNumber = ExactFraction(30)
    expectedBase = 100
    logs = listOf(Log(ExactFraction(30), 100, true), Log(30, 100, true), Log(30L, 100, true), Log(BigInteger("30"), 100, true))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertTrue(it.isDivided)
    }

    // just number
    expectedNumber = ExactFraction.TWO
    expectedBase = 10
    logs = listOf(Log(ExactFraction.TWO), Log(2), Log(2L), Log(BigInteger.TWO))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertFalse(it.isDivided)
    }

    logNum = Log(ExactFraction(107, 3))
    expectedNumber = ExactFraction(107, 3)
    expectedBase = 10
    assertEquals(expectedNumber, logNum.argument)
    assertEquals(expectedBase, logNum.base)
    assertFalse(logNum.isDivided)

    // number + base
    expectedNumber = ExactFraction.TWO
    expectedBase = 2
    logs = listOf(Log(ExactFraction.TWO, 2), Log(2, 2), Log(2L, 2), Log(BigInteger.TWO, 2))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertFalse(it.isDivided)
    }

    logNum = Log(ExactFraction(107, 3), 5)
    expectedNumber = ExactFraction(107, 3)
    expectedBase = 5
    assertEquals(expectedNumber, logNum.argument)
    assertEquals(expectedBase, logNum.base)
    assertFalse(logNum.isDivided)

    // number + divided
    expectedNumber = ExactFraction.TWO
    expectedBase = 10
    logs = listOf(Log(ExactFraction.TWO), Log(2), Log(2L), Log(BigInteger.TWO))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertFalse(it.isDivided)
    }

    expectedNumber = ExactFraction.TWO
    expectedBase = 10
    logs = listOf(Log(ExactFraction.TWO, true), Log(2, true), Log(2L, true), Log(BigInteger.TWO, true))
    logs.forEach {
        assertEquals(expectedNumber, it.argument)
        assertEquals(expectedBase, it.base)
        assertTrue(it.isDivided)
    }
}
