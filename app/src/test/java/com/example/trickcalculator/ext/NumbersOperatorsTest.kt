package com.example.trickcalculator.ext

import exactfraction.*
import org.junit.Assert.*
import java.math.BigInteger
import org.junit.Test

class NumbersOperatorsTest {
    @Test
    fun testToExactFraction() {
        // Int
        var i = 0
        assertEquals(ExactFraction(i), i.toEF())

        i = -1
        assertEquals(ExactFraction(i), i.toEF())

        i = 1
        assertEquals(ExactFraction(i), i.toEF())

        i = Int.MIN_VALUE
        assertEquals(ExactFraction(i), i.toEF())

        i = Int.MAX_VALUE
        assertEquals(ExactFraction(i), i.toEF())

        // Long
        var l = 0L
        assertEquals(ExactFraction(l), l.toEF())

        l = -1
        assertEquals(ExactFraction(l), l.toEF())

        l = 1
        assertEquals(ExactFraction(l), l.toEF())

        l = Long.MIN_VALUE
        assertEquals(ExactFraction(l), l.toEF())

        l = Long.MAX_VALUE
        assertEquals(ExactFraction(l), l.toEF())

        // BigInteger
        var bi = BigInteger.ZERO
        assertEquals(ExactFraction(bi), bi.toEF())

        bi = -BigInteger.ONE
        assertEquals(ExactFraction(bi), bi.toEF())

        bi = BigInteger.ONE
        assertEquals(ExactFraction(bi), bi.toEF())

        bi = BigInteger("100000000000")
        assertEquals(ExactFraction(bi), bi.toEF())

        bi = BigInteger("-100000000000")
        assertEquals(ExactFraction(bi), bi.toEF())
    }

    @Test
    fun testEq() {
        // BigInteger, Int
        var i = 0
        var bi = BigInteger(i.toString())
        assert(bi.eq(i))

        i = -1
        bi = BigInteger(i.toString())
        assert(bi.eq(i))

        i = 1
        bi = BigInteger(i.toString())
        assert(bi.eq(i))

        i = Int.MIN_VALUE
        bi = BigInteger(i.toString())
        assert(bi.eq(i))

        i = Int.MAX_VALUE
        bi = BigInteger(i.toString())
        assert(bi.eq(i))

        // BigInteger, Long
        var l = 0L
        bi = BigInteger(l.toString())
        assert(bi.eq(l))

        l = -1
        bi = BigInteger(l.toString())
        assert(bi.eq(l))

        l = 1
        bi = BigInteger(l.toString())
        assert(bi.eq(l))

        l = Long.MIN_VALUE
        bi = BigInteger(l.toString())
        assert(bi.eq(l))

        l = Long.MAX_VALUE
        bi = BigInteger(l.toString())
        assert(bi.eq(l))
    }

    @Test
    fun testIsNegative() {
        var bi = BigInteger("0")
        assert(!bi.isNegative())

        bi = BigInteger("1")
        assert(!bi.isNegative())

        bi = BigInteger("100")
        assert(!bi.isNegative())

        bi = BigInteger("-1")
        assert(bi.isNegative())

        bi = BigInteger("-100")
        assert(bi.isNegative())
    }

    @Test
    fun testIsZero() {
        var bi = BigInteger("0")
        assert(bi.isZero())

        bi = BigInteger("1")
        assert(!bi.isZero())

        bi = BigInteger("-1")
        assert(!bi.isZero())

        bi = BigInteger("100")
        assert(!bi.isZero())

        bi = BigInteger("-100")
        assert(!bi.isZero())
    }

    @Test
    fun testMax() {
        // zero
        var bi1 = BigInteger("0")

        var bi2 = BigInteger("0")
        var expected = BigInteger("0")
        assertEquals(expected, max(bi1, bi2))

        bi2 = BigInteger("1")
        expected = BigInteger("1")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi2 = BigInteger("-1")
        expected = BigInteger("0")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        // non-zero
        bi1 = BigInteger("18")
        bi2 = BigInteger("18")
        expected = BigInteger("18")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi1 = BigInteger("-18")
        bi2 = BigInteger("-18")
        expected = BigInteger("-18")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi1 = BigInteger("-5")
        bi2 = BigInteger("4")
        expected = BigInteger("4")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi1 = BigInteger("-4")
        bi2 = BigInteger("5")
        expected = BigInteger("5")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi1 = BigInteger("-4")
        bi2 = BigInteger("-5")
        expected = BigInteger("-4")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))

        bi1 = BigInteger("4")
        bi2 = BigInteger("5")
        expected = BigInteger("5")
        assertEquals(expected, max(bi1, bi2))
        assertEquals(expected, max(bi2, bi1))
    }

    @Test
    fun testMin() {
        // zero
        var bi1 = BigInteger("0")

        var bi2 = BigInteger("0")
        var expected = BigInteger("0")
        assertEquals(expected, min(bi1, bi2))

        bi2 = BigInteger("1")
        expected = BigInteger("0")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi2 = BigInteger("-1")
        expected = BigInteger("-1")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        // non-zero
        bi1 = BigInteger("18")
        bi2 = BigInteger("18")
        expected = BigInteger("18")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi1 = BigInteger("-18")
        bi2 = BigInteger("-18")
        expected = BigInteger("-18")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi1 = BigInteger("-5")
        bi2 = BigInteger("4")
        expected = BigInteger("-5")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi1 = BigInteger("-4")
        bi2 = BigInteger("5")
        expected = BigInteger("-4")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi1 = BigInteger("-4")
        bi2 = BigInteger("-5")
        expected = BigInteger("-5")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))

        bi1 = BigInteger("4")
        bi2 = BigInteger("5")
        expected = BigInteger("4")
        assertEquals(expected, min(bi1, bi2))
        assertEquals(expected, min(bi2, bi1))
    }
}