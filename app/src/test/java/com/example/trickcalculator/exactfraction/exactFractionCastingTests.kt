package com.example.trickcalculator.exactfraction

import org.junit.Assert.*
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

fun runToPairTests() {
    var ef = ExactFraction(0)
    var expected = Pair(0.toBI(), 1.toBI())
    assertEquals(expected, ef.toPair())

    ef = ExactFraction(1)
    expected = Pair(1.toBI(), 1.toBI())
    assertEquals(expected, ef.toPair())

    ef = ExactFraction(2, 7)
    expected = Pair(2.toBI(), 7.toBI())
    assertEquals(expected, ef.toPair())

    ef = ExactFraction(-1)
    expected = Pair((-1).toBI(), 1.toBI())
    assertEquals(expected, ef.toPair())

    ef = ExactFraction(-2, 7)
    expected = Pair((-2).toBI(), 7.toBI())
    assertEquals(expected, ef.toPair())
}

fun runToByteTests() {
    var ef = ExactFraction(0)
    var expected: Byte = 0
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(5)
    expected = 5
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(-5)
    expected = -5
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(2, 5)
    expected = 0
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(-18, 5)
    expected = -3
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(Byte.MIN_VALUE.toInt())
    expected = Byte.MIN_VALUE
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(Byte.MAX_VALUE.toInt())
    expected = Byte.MAX_VALUE
    assertEquals(expected, ef.toByte())

    ef = ExactFraction(Byte.MAX_VALUE.toInt())
    ef++
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toByte() }
    assertEquals("Overflow when casting to Byte", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Byte.MIN_VALUE.toInt())
    ef--
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toByte() }
    assertEquals("Overflow when casting to Byte", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

// test account for fact that Char can't be negative
fun runToCharTests() {
    var ef = ExactFraction(0)
    var expected = Char(0)
    assertEquals(expected, ef.toChar())

    ef = ExactFraction(5)
    expected = Char(5)
    assertEquals(expected, ef.toChar())

    ef = ExactFraction(-5)
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(2, 5)
    expected = Char(0)
    assertEquals(expected, ef.toChar())

    ef = ExactFraction(-18, 5)
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Char.MAX_VALUE.code)
    expected = Char.MAX_VALUE
    assertEquals(expected, ef.toChar())

    ef = ExactFraction(Char.MIN_VALUE.code)
    expected = Char.MIN_VALUE
    assertEquals(expected, ef.toChar())

    ef = ExactFraction(Char.MAX_VALUE.code)
    ef++
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Char.MIN_VALUE.code)
    ef--
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToShortTests() {
    var ef = ExactFraction(0)
    var expected: Short = 0
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(5)
    expected = 5
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(-5)
    expected = -5
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(2, 5)
    expected = 0
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(-18, 5)
    expected = -3
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(Short.MIN_VALUE.toInt())
    expected = Short.MIN_VALUE
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(Short.MAX_VALUE.toInt())
    expected = Short.MAX_VALUE
    assertEquals(expected, ef.toShort())

    ef = ExactFraction(Short.MAX_VALUE.toInt())
    ef++
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toShort() }
    assertEquals("Overflow when casting to Short", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Short.MIN_VALUE.toInt())
    ef--
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toShort() }
    assertEquals("Overflow when casting to Short", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToIntTests() {
    var ef = ExactFraction(0)
    var expected = 0
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(5)
    expected = 5
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(-5)
    expected = -5
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(2, 5)
    expected = 0
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(-18, 5)
    expected = -3
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(Int.MIN_VALUE)
    expected = Int.MIN_VALUE
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(Int.MAX_VALUE)
    expected = Int.MAX_VALUE
    assertEquals(expected, ef.toInt())

    ef = ExactFraction(Int.MAX_VALUE)
    ef++
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toInt() }
    assertEquals("Overflow when casting to Int", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Int.MIN_VALUE)
    ef--
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toInt() }
    assertEquals("Overflow when casting to Int", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToLongTests() {
    var ef = ExactFraction(0)
    var expected = 0L
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(5)
    expected = 5
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(-5)
    expected = -5
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(2, 5)
    expected = 0
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(-18, 5)
    expected = -3
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(Long.MIN_VALUE)
    expected = Long.MIN_VALUE
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(Long.MAX_VALUE)
    expected = Long.MAX_VALUE
    assertEquals(expected, ef.toLong())

    ef = ExactFraction(Long.MAX_VALUE)
    ef++
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toLong() }
    assertEquals("Overflow when casting to Long", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(Long.MIN_VALUE)
    ef--
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toLong() }
    assertEquals("Overflow when casting to Long", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToDoubleTests() {
    var ef = ExactFraction(0)
    var expected = 0.0
    assert(expected == ef.toDouble())

    ef = ExactFraction(5)
    expected = 5.0
    assert(expected == ef.toDouble())

    ef = ExactFraction(-5)
    expected = -5.0
    assert(expected == ef.toDouble())

    ef = ExactFraction(1, 2)
    expected = 0.5
    assert(expected == ef.toDouble())

    ef = ExactFraction(-3, 8)
    expected = -0.375
    assert(expected == ef.toDouble())

    ef = ExactFraction(1, 3)
    expected = 0.33333333333333333333
    assert(expected == ef.toDouble())

    ef = ExactFraction(2, 3)
    expected = 0.66666666666666666667
    assert(expected == ef.toDouble())

    ef = ExactFraction(-4, 19)
    expected = -0.21052631578947368421
    assert(expected == ef.toDouble())

    val veryBig = Double.MAX_VALUE.toBigDecimal().toBigInteger()
    val verySmall = (-Double.MAX_VALUE).toBigDecimal().toBigInteger()

    ef = ExactFraction(veryBig)
    ef *= 2
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toDouble() }
    assertEquals("Overflow when casting to Double", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(verySmall)
    ef *= 2
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toDouble() }
    assertEquals("Overflow when casting to Double", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToFloatTests() {
    var ef = ExactFraction(0)
    var expected = 0f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(5)
    expected = 5f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(-5)
    expected = -5f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(1, 2)
    expected = 0.5f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(-3, 8)
    expected = -0.375f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(1, 3)
    expected = 0.33333333333333333333f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(2, 3)
    expected = 0.66666666666666666667f
    assertEquals(expected, ef.toFloat())

    ef = ExactFraction(-4, 19)
    expected = -0.21052631578947368421f
    assertEquals(expected, ef.toFloat())

    val veryBig = Float.MAX_VALUE.toBigDecimal().toBigInteger()
    val verySmall = (-Float.MAX_VALUE).toBigDecimal().toBigInteger()

    ef = ExactFraction(veryBig)
    ef *= 2
    var error = assertThrows(ExactFractionOverflowException::class.java) { ef.toFloat() }
    assertEquals("Overflow when casting to Float", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)

    ef = ExactFraction(verySmall)
    ef *= 2
    error = assertThrows(ExactFractionOverflowException::class.java) { ef.toFloat() }
    assertEquals("Overflow when casting to Float", error.message)
    assertEquals(ef.toFractionString(), error.overflowValue)
}

fun runToBigIntegerTests() {
    var ef = ExactFraction(0)
    var expected = BigInteger.ZERO
    assertEquals(expected, ef.toBigInteger())

    ef = ExactFraction(2)
    expected = 2.toBI()
    assertEquals(expected, ef.toBigInteger())

    ef = ExactFraction(-4)
    expected = (-4).toBI()
    assertEquals(expected, ef.toBigInteger())

    ef = ExactFraction(3, 7)
    expected = BigInteger.ZERO
    assertEquals(expected, ef.toBigInteger())

    ef = ExactFraction(-12, 5)
    expected = (-2).toBI()
    assertEquals(expected, ef.toBigInteger())

    val big = "10000000000000000000000000000"
    ef = ExactFraction(big)
    expected = BigInteger(big)
    assertEquals(expected, ef.toBigInteger())
}

fun runToBigDecimalTests() {
    var ef = ExactFraction(0)
    var bd = BigDecimal(0)
    assertEquals(bd, ef.toBigDecimal())

    ef = ExactFraction(10)
    bd = BigDecimal(10)
    assertEquals(bd, ef.toBigDecimal())

    ef = ExactFraction(-10)
    bd = BigDecimal(-10)
    assertEquals(bd, ef.toBigDecimal())

    ef = ExactFraction(1, 2)
    bd = BigDecimal(0.5)
    assertEquals(bd, ef.toBigDecimal())

    ef = ExactFraction(-5, 4)
    bd = BigDecimal(-1.25)
    assertEquals(bd, ef.toBigDecimal())

    var mc = MathContext(8, RoundingMode.HALF_UP)
    ef = ExactFraction(-4, 19)
    bd = BigDecimal(-0.21052632, mc)
    assertEquals(bd, ef.toBigDecimal(8))

    mc = MathContext(4, RoundingMode.HALF_UP)
    ef = ExactFraction(1, 9)
    bd = BigDecimal(0.1111, mc)
    assertEquals(bd, ef.toBigDecimal(4))

    mc = MathContext(20, RoundingMode.HALF_UP)
    ef = ExactFraction(5, 3)
    bd = BigDecimal("1.66666666666666666667", mc)
    assertEquals(bd, ef.toBigDecimal())
}
