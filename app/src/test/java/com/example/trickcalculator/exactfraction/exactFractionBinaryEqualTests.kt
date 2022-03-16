package com.example.trickcalculator.exactfraction

import com.example.trickcalculator.ext.toBI
import org.junit.Assert.*
import java.math.BigInteger

fun runEqualsTests() {
    assertEquals(ExactFraction(0, 1), ExactFraction(0, 1))
    assertEquals(ExactFraction(-1, 3), ExactFraction(-1, 3))
    assertEquals(ExactFraction(5, 2), ExactFraction(5, 2))

    assertNotEquals(ExactFraction(1, 3), ExactFraction(-1, 3))
    assertNotEquals(ExactFraction(2, 3), ExactFraction(3, 2))
}

fun runEqTests() {
    var ef = ExactFraction(0)
    assert(ef.eq(0))
    assert(ef.eq(0L))
    assert(ef.eq(BigInteger.ZERO))

    ef = ExactFraction(-3)
    assert(ef.eq(-3))
    assert(ef.eq(-3L))
    assert(ef.eq((-3).toBI()))

    ef = ExactFraction(10, 1)
    assert(!ef.eq(-10))
    assert(!ef.eq(-10L))
    assert(!ef.eq((-10).toBI()))

    ef = ExactFraction(10, 7)
    assert(!ef.eq(1))
    assert(!ef.eq(1L))
    assert(!ef.eq(BigInteger.ONE))

    ef = ExactFraction(-7, 10)
    assert(!ef.eq(0))
    assert(!ef.eq(0L))
    assert(!ef.eq(BigInteger.ZERO))
}
