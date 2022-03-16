package com.example.trickcalculator.ext

import com.example.trickcalculator.exactfraction.*
import org.junit.Assert.*
import java.math.BigInteger
import org.junit.Test

class NumberOperatorsTest {
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
}