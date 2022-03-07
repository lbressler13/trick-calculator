package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger

// parsing
fun runParseDecimalTests() {
    // whole numbers
    var s = "0"
    var expected = BigFraction(0)
    assertEquals(expected, BigFraction.parse(s))

    s = "0000011"
    expected = BigFraction(11)
    assertEquals(expected, BigFraction.parse(s))

    s = "-31"
    expected = BigFraction(-31)
    assertEquals(expected, BigFraction.parse(s))

    s = "1000"
    expected = BigFraction(1000)
    assertEquals(expected, BigFraction.parse(s))

    // whole w/ decimal
    s = "-31.0000"
    expected = BigFraction(-31)
    assertEquals(expected, BigFraction.parse(s))

    s = "1000.0"
    expected = BigFraction(1000)
    assertEquals(expected, BigFraction.parse(s))

    // decimal w/out whole
    s = "0.1"
    expected = BigFraction(1, 10)
    assertEquals(expected, BigFraction.parse(s))

    s = "-0.1"
    expected = BigFraction(-1, 10)
    assertEquals(expected, BigFraction.parse(s))

    s = "0.25"
    expected = BigFraction(25, 100)
    assertEquals(expected, BigFraction.parse(s))

    s = ".25"
    expected = BigFraction(25, 100)
    assertEquals(expected, BigFraction.parse(s))

    s = ".123568"
    expected = BigFraction(123568, 1000000)
    assertEquals(expected, BigFraction.parse(s))

    s = "-.123568"
    expected = BigFraction(-123568, 1000000)
    assertEquals(expected, BigFraction.parse(s))

    // decimal w/ whole
    s = "1.25"
    expected = BigFraction(125, 100)
    assertEquals(expected, BigFraction.parse(s))

    s = "-12.123568"
    expected = BigFraction(-12123568, 1000000)
    assertEquals(expected, BigFraction.parse(s))

    s = "100.001"
    expected = BigFraction(100001, 1000)
    assertEquals(expected, BigFraction.parse(s))

    s = "100.234"
    expected = BigFraction(100234, 1000)
    assertEquals(expected, BigFraction.parse(s))

    s = "234.001"
    expected = BigFraction(234001, 1000)
    assertEquals(expected, BigFraction.parse(s))

    val n = "11111111111111111111111111"
    val d = "100000000000000000000000000"
    s = "0.11111111111111111111111111"
    expected = BigFraction(BigInteger(n), BigInteger(d))
    assertEquals(expected, BigFraction.parse(s))

    s = ".11111111111111111111111111"
    expected = BigFraction(BigInteger(n), BigInteger(d))
    assertEquals(expected, BigFraction.parse(s))
}

fun runParseBFStringTests() {
    var s = "BF[0 1]"
    var expected = BigFraction(0)
    assertEquals(expected, BigFraction.parse(s))

    s = "BF[-3 1]"
    expected = BigFraction(-3)
    assertEquals(expected, BigFraction.parse(s))

    s = "BF[17 29]"
    expected = BigFraction(17, 29)
    assertEquals(expected, BigFraction.parse(s))

    s = "BF[-17 29]"
    expected = BigFraction(-17, 29)
    assertEquals(expected, BigFraction.parse(s))
}

fun runCheckIsBFStringTests() {
    var s = "BF[10 1]"
    assert(checkIsBFString(s))

    s = "BF[-5 2]"
    assert(checkIsBFString(s))

    s = "BF[0 ]"
    assert(!checkIsBFString(s))

    s = "BF[0]"
    assert(!checkIsBFString(s))

    s = "BF[0.1 2]"
    assert(!checkIsBFString(s))

    s = "BF[]"
    assert(!checkIsBFString(s))

    s = "BF["
    assert(!checkIsBFString(s))

    s = "BF]"
    assert(!checkIsBFString(s))

    s = "hello world"
    assert(!checkIsBFString(s))
}

// toString
fun runToDecimalStringTests() {
    var bf = BigFraction(0)
    var expected = "0"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(4)
    expected = "4"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(-3)
    expected = "-3"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(1, 2)
    expected = "0.5"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(3, 2)
    expected = "1.5"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(3, 8)
    expected = "0.375"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(-1, 9)
    expected = "-0.11111111"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(5, 9)
    expected = "0.55555556"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(-4, 19)
    expected = "-0.21052632"
    assertEquals(expected, bf.toDecimalString())

    bf = BigFraction(3, 8)
    expected = "0.38"
    assertEquals(expected, bf.toDecimalString(2))

    bf = BigFraction(-1, 9)
    expected = "-0.111111111111"
    assertEquals(expected, bf.toDecimalString(12))

    bf = BigFraction(-4, 19)
    expected = "-0.21053"
    assertEquals(expected, bf.toDecimalString(5))

    val veryBig = "100000000000000000000"
    val bi = BigInteger(veryBig)
    bf = BigFraction(bi, 3)
    expected = "33333333333333333333.333333"
    assertEquals(expected, bf.toDecimalString(5))
}

fun runToFractionStringTests() {
    var bf = BigFraction(0)
    var expected = "0"
    assertEquals(expected, bf.toFractionString())

    bf = BigFraction(4)
    expected = "4"
    assertEquals(expected, bf.toFractionString())

    bf = BigFraction(-3)
    expected = "-3"
    assertEquals(expected, bf.toFractionString())

    bf = BigFraction(2, 7)
    expected = "2/7"
    assertEquals(expected, bf.toFractionString())

    bf = BigFraction(-7, 2)
    expected = "-7/2"
    assertEquals(expected, bf.toFractionString())
}

fun runToPairStringTests() {
    var bf = BigFraction(0)
    var expected = "(0, 1)"
    assertEquals(expected, bf.toPairString())

    bf = BigFraction(4)
    expected = "(4, 1)"
    assertEquals(expected, bf.toPairString())

    bf = BigFraction(-3)
    expected = "(-3, 1)"
    assertEquals(expected, bf.toPairString())

    bf = BigFraction(2, 7)
    expected = "(2, 7)"
    assertEquals(expected, bf.toPairString())

    bf = BigFraction(-7, 2)
    expected = "(-7, 2)"
    assertEquals(expected, bf.toPairString())
}

fun runToBFStringTests() {
    var bf = BigFraction(0)
    var expected = "BF[0 1]"
    assertEquals(expected, bf.toBFString())

    bf = BigFraction(-105)
    expected = "BF[-105 1]"
    assertEquals(expected, bf.toBFString())

    bf = BigFraction(19, 32)
    expected = "BF[19 32]"
    assertEquals(expected, bf.toBFString())

    bf = BigFraction(-9, 2)
    expected = "BF[-9 2]"
    assertEquals(expected, bf.toBFString())

    val veryBig = "10000000000000000000"
    bf = BigFraction(19, BigInteger(veryBig))
    expected = "BF[19 $veryBig]"
    assertEquals(expected, bf.toBFString())
}
