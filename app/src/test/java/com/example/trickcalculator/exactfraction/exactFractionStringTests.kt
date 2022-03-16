package com.example.trickcalculator.exactfraction

import org.junit.Assert.*
import java.math.BigInteger

// parsing
fun runParseDecimalTests() {
    // whole numbers
    var s = "0"
    var expected = ExactFraction(0)
    assertEquals(expected, parseDecimal(s))

    s = "0000011"
    expected = ExactFraction(11)
    assertEquals(expected, parseDecimal(s))

    s = "-31"
    expected = ExactFraction(-31)
    assertEquals(expected, parseDecimal(s))

    s = "1000"
    expected = ExactFraction(1000)
    assertEquals(expected, parseDecimal(s))

    // whole w/ decimal
    s = "-31.0000"
    expected = ExactFraction(-31)
    assertEquals(expected, parseDecimal(s))

    s = "1000.0"
    expected = ExactFraction(1000)
    assertEquals(expected, parseDecimal(s))

    // decimal w/out whole
    s = "0.1"
    expected = ExactFraction(1, 10)
    assertEquals(expected, parseDecimal(s))

    s = "-0.1"
    expected = ExactFraction(-1, 10)
    assertEquals(expected, parseDecimal(s))

    s = "0.25"
    expected = ExactFraction(25, 100)
    assertEquals(expected, parseDecimal(s))

    s = ".25"
    expected = ExactFraction(25, 100)
    assertEquals(expected, parseDecimal(s))

    s = ".123568"
    expected = ExactFraction(123568, 1000000)
    assertEquals(expected, parseDecimal(s))

    s = "-.123568"
    expected = ExactFraction(-123568, 1000000)
    assertEquals(expected, parseDecimal(s))

    // decimal w/ whole
    s = "1.25"
    expected = ExactFraction(125, 100)
    assertEquals(expected, parseDecimal(s))

    s = "-12.123568"
    expected = ExactFraction(-12123568, 1000000)
    assertEquals(expected, parseDecimal(s))

    s = "100.001"
    expected = ExactFraction(100001, 1000)
    assertEquals(expected, parseDecimal(s))

    s = "100.234"
    expected = ExactFraction(100234, 1000)
    assertEquals(expected, parseDecimal(s))

    s = "234.001"
    expected = ExactFraction(234001, 1000)
    assertEquals(expected, parseDecimal(s))

    val n = "11111111111111111111111111"
    val d = "100000000000000000000000000"
    s = "0.11111111111111111111111111"
    expected = ExactFraction(BigInteger(n), BigInteger(d))
    assertEquals(expected, parseDecimal(s))

    s = ".11111111111111111111111111"
    expected = ExactFraction(BigInteger(n), BigInteger(d))
    assertEquals(expected, parseDecimal(s))

    // errors
    s = "abc"
    assertThrows(NumberFormatException::class.java) { parseDecimal(s) }

    s = "1.1.1"
    assertThrows(NumberFormatException::class.java) { parseDecimal(s) }

    s = "EF[1 1]"
    assertThrows(NumberFormatException::class.java) { parseDecimal(s) }
}

fun runParseEFStringTests() {
    var s = "EF[0 1]"
    var expected = ExactFraction(0)
    assertEquals(expected, parseEFString(s))

    s = "EF[-3 1]"
    expected = ExactFraction(-3)
    assertEquals(expected, parseEFString(s))

    s = "EF[17 29]"
    expected = ExactFraction(17, 29)
    assertEquals(expected, parseEFString(s))

    s = "EF[-17 29]"
    expected = ExactFraction(-17, 29)
    assertEquals(expected, parseEFString(s))

    // errors
    s = "abc"
    assertThrows(NumberFormatException::class.java) { parseEFString(s) }

    s = "1.1"
    assertThrows(NumberFormatException::class.java) { parseEFString(s) }

    s = "EF[1]"
    assertThrows(NumberFormatException::class.java) { parseEFString(s) }

    s = "EF[1 1 1]"
    assertThrows(NumberFormatException::class.java) { parseEFString(s) }
}

fun runCheckIsEFStringTests() {
    var s = "EF[10 1]"
    assert(checkIsEFString(s))

    s = "EF[-5 2]"
    assert(checkIsEFString(s))

    s = "EF[0 ]"
    assert(!checkIsEFString(s))

    s = "EF[0]"
    assert(!checkIsEFString(s))

    s = "EF[0 0 0]"
    assert(!checkIsEFString(s))

    s = "EF[0.1 2]"
    assert(!checkIsEFString(s))

    s = "EF[]"
    assert(!checkIsEFString(s))

    s = "EF["
    assert(!checkIsEFString(s))

    s = "EF]"
    assert(!checkIsEFString(s))

    s = "hello world"
    assert(!checkIsEFString(s))
}

// toString
fun runToDecimalStringTests() {
    var ef = ExactFraction(0)
    var expected = "0"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(4)
    expected = "4"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(-3)
    expected = "-3"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(1, 2)
    expected = "0.5"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(3, 2)
    expected = "1.5"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(3, 8)
    expected = "0.375"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(-1, 9)
    expected = "-0.11111111"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(5, 9)
    expected = "0.55555556"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(-4, 19)
    expected = "-0.21052632"
    assertEquals(expected, ef.toDecimalString())

    ef = ExactFraction(3, 8)
    expected = "0.38"
    assertEquals(expected, ef.toDecimalString(2))

    ef = ExactFraction(-1, 9)
    expected = "-0.111111111111"
    assertEquals(expected, ef.toDecimalString(12))

    ef = ExactFraction(-4, 19)
    expected = "-0.21053"
    assertEquals(expected, ef.toDecimalString(5))

    val veryBig = "100000000000000000000"
    val bi = BigInteger(veryBig)
    ef = ExactFraction(bi, 3)
    expected = "33333333333333333333.333333"
    assertEquals(expected, ef.toDecimalString(5))
}

fun runToFractionStringTests() {
    var ef = ExactFraction(0)
    var expected = "0"
    assertEquals(expected, ef.toFractionString())

    ef = ExactFraction(4)
    expected = "4"
    assertEquals(expected, ef.toFractionString())

    ef = ExactFraction(-3)
    expected = "-3"
    assertEquals(expected, ef.toFractionString())

    ef = ExactFraction(2, 7)
    expected = "2/7"
    assertEquals(expected, ef.toFractionString())

    ef = ExactFraction(-7, 2)
    expected = "-7/2"
    assertEquals(expected, ef.toFractionString())
}

fun runToPairStringTests() {
    var ef = ExactFraction(0)
    var expected = "(0, 1)"
    assertEquals(expected, ef.toPairString())

    ef = ExactFraction(4)
    expected = "(4, 1)"
    assertEquals(expected, ef.toPairString())

    ef = ExactFraction(-3)
    expected = "(-3, 1)"
    assertEquals(expected, ef.toPairString())

    ef = ExactFraction(2, 7)
    expected = "(2, 7)"
    assertEquals(expected, ef.toPairString())

    ef = ExactFraction(-7, 2)
    expected = "(-7, 2)"
    assertEquals(expected, ef.toPairString())
}

fun runToEFStringTests() {
    var ef = ExactFraction(0)
    var expected = "EF[0 1]"
    assertEquals(expected, ef.toEFString())

    ef = ExactFraction(-105)
    expected = "EF[-105 1]"
    assertEquals(expected, ef.toEFString())

    ef = ExactFraction(19, 32)
    expected = "EF[19 32]"
    assertEquals(expected, ef.toEFString())

    ef = ExactFraction(-9, 2)
    expected = "EF[-9 2]"
    assertEquals(expected, ef.toEFString())

    val veryBig = "10000000000000000000"
    ef = ExactFraction(19, BigInteger(veryBig))
    expected = "EF[19 $veryBig]"
    assertEquals(expected, ef.toEFString())
}
