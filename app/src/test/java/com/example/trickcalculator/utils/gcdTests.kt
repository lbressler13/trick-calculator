package com.example.trickcalculator.utils

import org.junit.Assert.*
import java.math.BigInteger

// TODO tests for EF

fun runGetGCDTests() {
    // zero
    var b1 = BigInteger.ZERO

    var b2 = BigInteger.ZERO
    var expected = BigInteger.ONE
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b2 = BigInteger.ONE
    expected = BigInteger.ONE
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b2 = 4.toBigInteger()
    expected = 4.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b2 = (-4).toBigInteger()
    expected = 4.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    // one
    b1 = BigInteger.ONE
    expected = BigInteger.ONE

    b2 = BigInteger.ONE
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b2 = BigInteger.TEN
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b2 = -BigInteger.TEN
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    // equal
    b1 = 17.toBigInteger()
    b2 = 17.toBigInteger()
    expected = 17.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 17.toBigInteger()
    b2 = (-17).toBigInteger()
    expected = 17.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 81.toBigInteger()
    b2 = 81.toBigInteger()
    expected = 81.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 81.toBigInteger()
    b2 = (-81).toBigInteger()
    expected = 81.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    // co-prime
    expected = BigInteger.ONE

    b1 = 17.toBigInteger()
    b2 = 19.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = (-17).toBigInteger()
    b2 = 19.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = (-17).toBigInteger()
    b2 = (-19).toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 2.toBigInteger()
    b2 = 3.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 14.toBigInteger()
    b2 = 81.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = (-15).toBigInteger()
    b2 = 1024.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    // exact divisor
    b1 = 2.toBigInteger()
    b2 = 4.toBigInteger()
    expected = 2.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 1002.toBigInteger()
    b2 = 334.toBigInteger()
    expected = 334.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 1002.toBigInteger()
    b2 = (-3).toBigInteger()
    expected = 3.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = (-1002).toBigInteger()
    b2 = 3.toBigInteger()
    expected = 3.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    // common factor
    b1 = 6.toBigInteger()
    b2 = 4.toBigInteger()
    expected = 2.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 6.toBigInteger()
    b2 = (-4).toBigInteger()
    expected = 2.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 15.toBigInteger()
    b2 = 25.toBigInteger()
    expected = 5.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 120.toBigInteger()
    b2 = 100.toBigInteger()
    expected = 20.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = (-120).toBigInteger()
    b2 = (-100).toBigInteger()
    expected = 20.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))

    b1 = 34.toBigInteger()
    b2 = 51.toBigInteger()
    expected = 17.toBigInteger()
    assertEquals(expected, getGCD(b1, b2))
    assertEquals(expected, getGCD(b2, b1))
}

fun runGetGCDListTests() {
    // empty list
    var l: List<BigInteger> = listOf()
    var expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    // single value
    l = listOf(BigInteger.ZERO)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(BigInteger.ONE)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(17.toBigInteger())
    expected = 17.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-17).toBigInteger())
    expected = 17.toBigInteger()
    assertEquals(expected, getListGCD(l))

    // 2 values
    l = listOf(2.toBigInteger(), (-2).toBigInteger())
    expected = 2.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(11.toBigInteger(), 23.toBigInteger())
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(22.toBigInteger(), 121.toBigInteger())
    expected = 11.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(96.toBigInteger(), 180.toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    // multiple values
    l = listOf(BigInteger.ONE, BigInteger.ONE, BigInteger.TEN)
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(21.toBigInteger(), 3.toBigInteger(), 7.toBigInteger(), 14.toBigInteger())
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(63.toBigInteger(), 27.toBigInteger(), 102.toBigInteger())
    expected = 3.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(96.toBigInteger(), 180.toBigInteger(), 372.toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-96).toBigInteger(), 180.toBigInteger(), (-372).toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf((-96).toBigInteger(), (-180).toBigInteger(), (-372).toBigInteger())
    expected = 12.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(
        12.toBigInteger(),
        (-18).toBigInteger(),
        11111000.toBigInteger(),
        4444444.toBigInteger(),
        100.toBigInteger(),
        12345678.toBigInteger()
    )
    expected = 2.toBigInteger()
    assertEquals(expected, getListGCD(l))

    l = listOf(
        12.toBigInteger(),
        (-18).toBigInteger(),
        11111000.toBigInteger(),
        4444444.toBigInteger(),
        81.toBigInteger(),
        12345678.toBigInteger()
    )
    expected = BigInteger.ONE
    assertEquals(expected, getListGCD(l))

    l = listOf(
        1000.toBigInteger(),
        (-180).toBigInteger(),
        11111000.toBigInteger(),
        44444440.toBigInteger(),
        80.toBigInteger(),
        123456785.toBigInteger(),
        (-275).toBigInteger(),
        4545454545.toBigInteger(),
        0.toBigInteger(),
        98765.toBigInteger()
    )
    expected = 5.toBigInteger()
    assertEquals(expected, getListGCD(l))
}
