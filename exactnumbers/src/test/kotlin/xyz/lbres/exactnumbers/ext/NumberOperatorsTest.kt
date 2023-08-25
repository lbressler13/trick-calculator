package xyz.lbres.exactnumbers.ext

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.ext.eq
import xyz.lbres.exactnumbers.ext.toExactFraction
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

internal class NumberOperatorsTest {
    @Test
    internal fun testToExactFraction() {
        // Int
        var i = 0
        assertEquals(ExactFraction(i), i.toExactFraction())

        i = -1
        assertEquals(ExactFraction(i), i.toExactFraction())

        i = 1
        assertEquals(ExactFraction(i), i.toExactFraction())

        i = Int.MIN_VALUE
        assertEquals(ExactFraction(i), i.toExactFraction())

        i = Int.MAX_VALUE
        assertEquals(ExactFraction(i), i.toExactFraction())

        // Long
        var l = 0L
        assertEquals(ExactFraction(l), l.toExactFraction())

        l = -1
        assertEquals(ExactFraction(l), l.toExactFraction())

        l = 1
        assertEquals(ExactFraction(l), l.toExactFraction())

        l = Long.MIN_VALUE
        assertEquals(ExactFraction(l), l.toExactFraction())

        l = Long.MAX_VALUE
        assertEquals(ExactFraction(l), l.toExactFraction())

        // BigInteger
        var bi = BigInteger.ZERO
        assertEquals(ExactFraction(bi), bi.toExactFraction())

        bi = -BigInteger.ONE
        assertEquals(ExactFraction(bi), bi.toExactFraction())

        bi = BigInteger.ONE
        assertEquals(ExactFraction(bi), bi.toExactFraction())

        bi = BigInteger("100000000000")
        assertEquals(ExactFraction(bi), bi.toExactFraction())

        bi = BigInteger("-100000000000")
        assertEquals(ExactFraction(bi), bi.toExactFraction())
    }

    @Test
    internal fun testEq() {
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
}
