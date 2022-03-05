package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigDecimal
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

// TODO numbers, BI, BD
