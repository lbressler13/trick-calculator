package xyz.lbres.exactnumbers.irrationals.log

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {
    @Test
    fun testGetLogOf() {
        // base 10
        var base = 10

        var bi = BigInteger.ONE
        var expected = BigDecimal.ZERO
        assertEquals(expected, getLogOf(bi, base))

        bi = BigInteger.TEN
        expected = BigDecimal.ONE
        assertEquals(expected, getLogOf(bi, base))

        bi = 100.toBigInteger()
        expected = 2.toBigDecimal()
        assertEquals(expected, getLogOf(bi, base))

        bi = 200.toBigInteger()
        expected = BigDecimal("2.301029995663981")
        assertEquals(expected, getLogOf(bi, base))

        bi = 3333.toBigInteger()
        expected = BigDecimal("3.52283531366053")
        assertEquals(expected, getLogOf(bi, base))

        bi = 300.toBigInteger()
        expected = BigDecimal("2.477121254719662")
        assertEquals(expected, getLogOf(bi, base))

        bi = 77.toBigInteger()
        expected = BigDecimal("1.8864907251724818")
        assertEquals(expected, getLogOf(bi, base))

        // base 2
        base = 2

        bi = BigInteger.ONE
        expected = BigDecimal.ZERO
        assertEquals(expected, getLogOf(bi, base))

        bi = 32.toBigInteger()
        expected = 5.toBigDecimal()
        assertEquals(expected, getLogOf(bi, base))

        bi = 200.toBigInteger()
        expected = BigDecimal("7.643856189774724")
        assertEquals(expected, getLogOf(bi, base))

        // other
        bi = BigInteger.ONE
        base = 7
        expected = BigDecimal.ZERO
        assertEquals(expected, getLogOf(bi, base))

        bi = 216.toBigInteger()
        base = 6
        expected = 3.toBigDecimal()
        assertEquals(expected, getLogOf(bi, base))

        base = 24
        bi = 15151515.toBigInteger()
        expected = BigDecimal("5.202432673429519")
        assertEquals(expected, getLogOf(bi, base))
    }
}
