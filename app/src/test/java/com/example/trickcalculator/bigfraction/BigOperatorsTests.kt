package com.example.trickcalculator.bigfraction

import org.junit.Assert.*
import java.math.BigInteger
import org.junit.Test

class BigOperatorsTests {
    @Test
    fun testToBigFraction() {
        // Int
        var i = 0
        assertEquals(BigFraction(i), i.toBF())

        i = -1
        assertEquals(BigFraction(i), i.toBF())

        i = 1
        assertEquals(BigFraction(i), i.toBF())

        i = Int.MIN_VALUE
        assertEquals(BigFraction(i), i.toBF())

        i = Int.MAX_VALUE
        assertEquals(BigFraction(i), i.toBF())

        // Long
        var l = 0L
        assertEquals(BigFraction(l), l.toBF())

        l = -1
        assertEquals(BigFraction(l), l.toBF())

        l = 1
        assertEquals(BigFraction(l), l.toBF())

        l = Long.MIN_VALUE
        assertEquals(BigFraction(l), l.toBF())

        l = Long.MAX_VALUE
        assertEquals(BigFraction(l), l.toBF())

        // BigInteger
        var bi = BigInteger.ZERO
        assertEquals(BigFraction(bi), bi.toBF())

        bi = -BigInteger.ONE
        assertEquals(BigFraction(bi), bi.toBF())

        bi = BigInteger.ONE
        assertEquals(BigFraction(bi), bi.toBF())

        bi = BigInteger("100000000000")
        assertEquals(BigFraction(bi), bi.toBF())

        bi = BigInteger("-100000000000")
        assertEquals(BigFraction(bi), bi.toBF())
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