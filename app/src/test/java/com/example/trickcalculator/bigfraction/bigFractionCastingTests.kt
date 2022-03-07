package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

fun runToPairTests() {
    var bf = BigFraction(0)
    var expected = Pair(0.toBI(), 1.toBI())
    assertEquals(expected, bf.toPair())

    bf = BigFraction(1)
    expected = Pair(1.toBI(), 1.toBI())
    assertEquals(expected, bf.toPair())

    bf = BigFraction(2, 7)
    expected = Pair(2.toBI(), 7.toBI())
    assertEquals(expected, bf.toPair())

    bf = BigFraction(-1)
    expected = Pair((-1).toBI(), 1.toBI())
    assertEquals(expected, bf.toPair())

    bf = BigFraction(-2, 7)
    expected = Pair((-2).toBI(), 7.toBI())
    assertEquals(expected, bf.toPair())
}

fun runToByteTests() {
    var bf = BigFraction(0)
    var expected: Byte = 0
    assertEquals(expected, bf.toByte())

    bf = BigFraction(5)
    expected = 5
    assertEquals(expected, bf.toByte())

    bf = BigFraction(-5)
    expected = -5
    assertEquals(expected, bf.toByte())

    bf = BigFraction(2, 5)
    expected = 0
    assertEquals(expected, bf.toByte())

    bf = BigFraction(-18, 5)
    expected = -3
    assertEquals(expected, bf.toByte())

    bf = BigFraction(Byte.MIN_VALUE.toInt())
    expected = Byte.MIN_VALUE
    assertEquals(expected, bf.toByte())

    bf = BigFraction(Byte.MAX_VALUE.toInt())
    expected = Byte.MAX_VALUE
    assertEquals(expected, bf.toByte())

    bf = BigFraction(Byte.MAX_VALUE.toInt())
    bf++
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toByte() }
    assertEquals("Overflow when casting to Byte", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Byte.MIN_VALUE.toInt())
    bf--
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toByte() }
    assertEquals("Overflow when casting to Byte", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

// test account for fact that Char can't be negative
fun runToCharTests() {
    var bf = BigFraction(0)
    var expected = Char(0)
    assertEquals(expected, bf.toChar())

    bf = BigFraction(5)
    expected = Char(5)
    assertEquals(expected, bf.toChar())

    bf = BigFraction(-5)
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(2, 5)
    expected = Char(0)
    assertEquals(expected, bf.toChar())

    bf = BigFraction(-18, 5)
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Char.MAX_VALUE.code)
    expected = Char.MAX_VALUE
    assertEquals(expected, bf.toChar())

    bf = BigFraction(Char.MIN_VALUE.code)
    expected = Char.MIN_VALUE
    assertEquals(expected, bf.toChar())

    bf = BigFraction(Char.MAX_VALUE.code)
    bf++
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Char.MIN_VALUE.code)
    bf--
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toChar() }
    assertEquals("Overflow when casting to Char", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToShortTests() {
    var bf = BigFraction(0)
    var expected: Short = 0
    assertEquals(expected, bf.toShort())

    bf = BigFraction(5)
    expected = 5
    assertEquals(expected, bf.toShort())

    bf = BigFraction(-5)
    expected = -5
    assertEquals(expected, bf.toShort())

    bf = BigFraction(2, 5)
    expected = 0
    assertEquals(expected, bf.toShort())

    bf = BigFraction(-18, 5)
    expected = -3
    assertEquals(expected, bf.toShort())

    bf = BigFraction(Short.MIN_VALUE.toInt())
    expected = Short.MIN_VALUE
    assertEquals(expected, bf.toShort())

    bf = BigFraction(Short.MAX_VALUE.toInt())
    expected = Short.MAX_VALUE
    assertEquals(expected, bf.toShort())

    bf = BigFraction(Short.MAX_VALUE.toInt())
    bf++
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toShort() }
    assertEquals("Overflow when casting to Short", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Short.MIN_VALUE.toInt())
    bf--
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toShort() }
    assertEquals("Overflow when casting to Short", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToIntTests() {
    var bf = BigFraction(0)
    var expected = 0
    assertEquals(expected, bf.toInt())

    bf = BigFraction(5)
    expected = 5
    assertEquals(expected, bf.toInt())

    bf = BigFraction(-5)
    expected = -5
    assertEquals(expected, bf.toInt())

    bf = BigFraction(2, 5)
    expected = 0
    assertEquals(expected, bf.toInt())

    bf = BigFraction(-18, 5)
    expected = -3
    assertEquals(expected, bf.toInt())

    bf = BigFraction(Int.MIN_VALUE)
    expected = Int.MIN_VALUE
    assertEquals(expected, bf.toInt())

    bf = BigFraction(Int.MAX_VALUE)
    expected = Int.MAX_VALUE
    assertEquals(expected, bf.toInt())

    bf = BigFraction(Int.MAX_VALUE)
    bf++
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toInt() }
    assertEquals("Overflow when casting to Int", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Int.MIN_VALUE)
    bf--
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toInt() }
    assertEquals("Overflow when casting to Int", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToLongTests() {
    var bf = BigFraction(0)
    var expected = 0L
    assertEquals(expected, bf.toLong())

    bf = BigFraction(5)
    expected = 5
    assertEquals(expected, bf.toLong())

    bf = BigFraction(-5)
    expected = -5
    assertEquals(expected, bf.toLong())

    bf = BigFraction(2, 5)
    expected = 0
    assertEquals(expected, bf.toLong())

    bf = BigFraction(-18, 5)
    expected = -3
    assertEquals(expected, bf.toLong())

    bf = BigFraction(Long.MIN_VALUE)
    expected = Long.MIN_VALUE
    assertEquals(expected, bf.toLong())

    bf = BigFraction(Long.MAX_VALUE)
    expected = Long.MAX_VALUE
    assertEquals(expected, bf.toLong())

    bf = BigFraction(Long.MAX_VALUE)
    bf++
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toLong() }
    assertEquals("Overflow when casting to Long", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(Long.MIN_VALUE)
    bf--
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toLong() }
    assertEquals("Overflow when casting to Long", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToDoubleTests() {
    var bf = BigFraction(0)
    var expected = 0.0
    assert(expected == bf.toDouble())

    bf = BigFraction(5)
    expected = 5.0
    assert(expected == bf.toDouble())

    bf = BigFraction(-5)
    expected = -5.0
    assert(expected == bf.toDouble())

    bf = BigFraction(1, 2)
    expected = 0.5
    assert(expected == bf.toDouble())

    bf = BigFraction(-3, 8)
    expected = -0.375
    assert(expected == bf.toDouble())

    bf = BigFraction(1, 3)
    expected = 0.33333333333333333333
    assert(expected == bf.toDouble())

    bf = BigFraction(2, 3)
    expected = 0.66666666666666666667
    assert(expected == bf.toDouble())

    bf = BigFraction(-4, 19)
    expected = -0.21052631578947368421
    assert(expected == bf.toDouble())

    val veryBig = Double.MAX_VALUE.toBigDecimal().toBigInteger()
    val verySmall = (-Double.MAX_VALUE).toBigDecimal().toBigInteger()

    bf = BigFraction(veryBig)
    bf *= 2
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toDouble() }
    assertEquals("Overflow when casting to Double", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(verySmall)
    bf *= 2
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toDouble() }
    assertEquals("Overflow when casting to Double", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToFloatTests() {
    var bf = BigFraction(0)
    var expected = 0f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(5)
    expected = 5f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(-5)
    expected = -5f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(1, 2)
    expected = 0.5f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(-3, 8)
    expected = -0.375f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(1, 3)
    expected = 0.33333333333333333333f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(2, 3)
    expected = 0.66666666666666666667f
    assertEquals(expected, bf.toFloat())

    bf = BigFraction(-4, 19)
    expected = -0.21052631578947368421f
    assertEquals(expected, bf.toFloat())

    val veryBig = Float.MAX_VALUE.toBigDecimal().toBigInteger()
    val verySmall = (-Float.MAX_VALUE).toBigDecimal().toBigInteger()

    bf = BigFraction(veryBig)
    bf *= 2
    var error = assertThrows(BigFractionOverFlowException::class.java) { bf.toFloat() }
    assertEquals("Overflow when casting to Float", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)

    bf = BigFraction(verySmall)
    bf *= 2
    error = assertThrows(BigFractionOverFlowException::class.java) { bf.toFloat() }
    assertEquals("Overflow when casting to Float", error.message)
    assertEquals(bf.toFractionString(), error.overflowValue)
}

fun runToBigIntegerTests() {
    var bf = BigFraction(0)
    var expected = BigInteger.ZERO
    assertEquals(expected, bf.toBigInteger())

    bf = BigFraction(2)
    expected = 2.toBI()
    assertEquals(expected, bf.toBigInteger())

    bf = BigFraction(-4)
    expected = (-4).toBI()
    assertEquals(expected, bf.toBigInteger())

    bf = BigFraction(3, 7)
    expected = BigInteger.ZERO
    assertEquals(expected, bf.toBigInteger())

    bf = BigFraction(-12, 5)
    expected = (-2).toBI()
    assertEquals(expected, bf.toBigInteger())

    val big = "10000000000000000000000000000"
    bf = BigFraction(big)
    expected = BigInteger(big)
    assertEquals(expected, bf.toBigInteger())
}

fun runToBigDecimalTests() {
    var bf = BigFraction(0)
    var bd = BigDecimal(0)
    assertEquals(bd, bf.toBigDecimal())

    bf = BigFraction(10)
    bd = BigDecimal(10)
    assertEquals(bd, bf.toBigDecimal())

    bf = BigFraction(-10)
    bd = BigDecimal(-10)
    assertEquals(bd, bf.toBigDecimal())

    bf = BigFraction(1, 2)
    bd = BigDecimal(0.5)
    assertEquals(bd, bf.toBigDecimal())

    bf = BigFraction(-5, 4)
    bd = BigDecimal(-1.25)
    assertEquals(bd, bf.toBigDecimal())

    var mc = MathContext(8, RoundingMode.HALF_UP)
    bf = BigFraction(-4, 19)
    bd = BigDecimal(-0.21052632, mc)
    assertEquals(bd, bf.toBigDecimal(8))

    mc = MathContext(4, RoundingMode.HALF_UP)
    bf = BigFraction(1, 9)
    bd = BigDecimal(0.1111, mc)
    assertEquals(bd, bf.toBigDecimal(4))

    mc = MathContext(20, RoundingMode.HALF_UP)
    bf = BigFraction(5, 3)
    bd = BigDecimal("1.66666666666666666667", mc)
    assertEquals(bd, bf.toBigDecimal())
}
