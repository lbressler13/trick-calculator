package com.example.trickcalculator.exactdecimal

import exactfraction.ExactFraction
import com.example.trickcalculator.ext.toEF
import org.junit.Assert.*
import java.math.BigInteger

fun runTermConstructorTests() {
    // ExactFraction, Int
    var ef = ExactFraction.ZERO
    var exp = 0
    var t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${ef}p$exp", t.shortString)

    ef = ExactFraction.ZERO
    exp = 4
    t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    ef = ExactFraction.ZERO
    exp = -4
    t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    ef = ExactFraction(-1, 2)
    exp = 0
    t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${ef}p$exp", t.shortString)

    ef = ExactFraction(17, 10000)
    exp = 10000
    t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${ef}p$exp", t.shortString)

    ef = -ExactFraction.FIVE
    exp = -12
    t = Term(ef, exp)
    assertEquals(ef, t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${ef}p$exp", t.shortString)

    // Int, Int
    var i = 0
    exp = 0
    t = Term(i, exp)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${i.toEF()}p$exp", t.shortString)

    i = -13
    exp = 0
    t = Term(i, exp)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${i.toEF()}p$exp", t.shortString)

    i = 10000
    exp = 10000
    t = Term(i, exp)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${i.toEF()}p$exp", t.shortString)

    i = Int.MAX_VALUE
    exp = -12
    t = Term(i, exp)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(exp, t.exp)
    assertEquals("${i.toEF()}p$exp", t.shortString)

    // ExactFraction
    ef = ExactFraction.ZERO
    t = Term(ef)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    ef = ExactFraction(-1, 2)
    t = Term(ef)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    ef = ExactFraction(17, 10000)
    t = Term(ef)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    ef = -ExactFraction.FIVE
    t = Term(ef)
    assertEquals(ef, t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${ef}p0", t.shortString)

    // BigInteger
    var bi: BigInteger = BigInteger.ZERO
    t = Term(bi)
    assertEquals(bi.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${bi.toEF()}p0", t.shortString)

    bi = (-13).toBigInteger()
    t = Term(bi)
    assertEquals(bi.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${bi.toEF()}p0", t.shortString)

    bi = 10000.toBigInteger()
    t = Term(bi)
    assertEquals(bi.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${bi.toEF()}p0", t.shortString)

    bi = Int.MAX_VALUE.toBigInteger() * 10.toBigInteger()
    t = Term(bi)
    assertEquals(bi.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${bi.toEF()}p0", t.shortString)

    // Int
    i = 0
    t = Term(i)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${i.toEF()}p0", t.shortString)

    i = -13
    t = Term(i)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${i.toEF()}p0", t.shortString)

    i = 10000
    t = Term(i)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${i.toEF()}p0", t.shortString)

    i = Int.MAX_VALUE
    t = Term(i)
    assertEquals(i.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${i.toEF()}p0", t.shortString)

    // Long
    var l: Long = 0
    t = Term(l)
    assertEquals(l.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${l.toEF()}p0", t.shortString)

    l = -13
    t = Term(l)
    assertEquals(l.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${l.toEF()}p0", t.shortString)

    l = 10000
    t = Term(l)
    assertEquals(l.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${l.toEF()}p0", t.shortString)

    l = Long.MAX_VALUE
    t = Term(l)
    assertEquals(l.toEF(), t.coefficient)
    assertEquals(0, t.exp)
    assertEquals("${l.toEF()}p0", t.shortString)
}
