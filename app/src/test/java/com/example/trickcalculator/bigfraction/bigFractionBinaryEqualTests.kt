package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger

fun runEqualsTests() {
    assertEquals(BigFraction(0, 1), BigFraction(0, 1))
    assertEquals(BigFraction(-1, 3), BigFraction(-1, 3))
    assertEquals(BigFraction(5, 2), BigFraction(5, 2))

    assertNotEquals(BigFraction(1, 3), BigFraction(-1, 3))
    assertNotEquals(BigFraction(2, 3), BigFraction(3, 2))
}

fun runEqTests() {
    var bf = BigFraction(0)
    assert(bf.eq(0))
    assert(bf.eq(0L))
    assert(bf.eq(BigInteger.ZERO))

    bf = BigFraction(-3)
    assert(bf.eq(-3))
    assert(bf.eq(-3L))
    assert(bf.eq((-3).toBI()))

    bf = BigFraction(10, 1)
    assert(!bf.eq(-10))
    assert(!bf.eq(-10L))
    assert(!bf.eq((-10).toBI()))

    bf = BigFraction(10, 7)
    assert(!bf.eq(1))
    assert(!bf.eq(1L))
    assert(!bf.eq(BigInteger.ONE))

    bf = BigFraction(-7, 10)
    assert(!bf.eq(0))
    assert(!bf.eq(0L))
    assert(!bf.eq(BigInteger.ZERO))
}
