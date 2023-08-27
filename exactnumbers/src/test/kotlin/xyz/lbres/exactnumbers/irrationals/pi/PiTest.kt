package xyz.lbres.exactnumbers.irrationals.pi

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PiTest {
    @Test
    fun testConstructor() {
        var pi = Pi()
        assertFalse(pi.isDivided)

        pi = Pi(isDivided = false)
        assertFalse(pi.isDivided)

        pi = Pi(isDivided = true)
        assertTrue(pi.isDivided)
    }

    @Test
    fun testEquals() {
        var pi1 = Pi()
        assertEquals(pi1, pi1)

        pi1 = Pi(true)
        assertEquals(pi1, pi1)

        val pi2 = Pi()
        assertNotEquals(pi1, pi2)
        assertNotEquals(pi2, pi1)
    }

    @Test
    fun testGetValue() {
        // kotlin representation of pi: 3.141592653589793
        var pi = Pi()
        var expected = BigDecimal("3.141592653589793")
        assertEquals(expected, pi.getValue())

        pi = Pi(true)
        expected = BigDecimal("0.31830988618379069570")
        assertEquals(expected, pi.getValue())
    }

    @Test
    fun testIsZero() {
        var pi = Pi()
        assertFalse(pi.isZero())

        pi = Pi(true)
        assertFalse(pi.isZero())
    }

    @Test
    fun testIsRational() {
        var pi = Pi()
        assertFalse(pi.isRational())

        pi = Pi(true)
        assertFalse(pi.isRational())
    }

    @Test
    fun testGetRationalValue() {
        var pi = Pi()
        assertNull(pi.getRationalValue())

        pi = Pi(true)
        assertNull(pi.getRationalValue())
    }

    @Test
    fun testSwapDivided() {
        var pi = Pi()
        assertTrue(pi.swapDivided().isDivided)

        pi = Pi(true)
        assertFalse(pi.swapDivided().isDivided)
    }

    @Test
    fun testToString() {
        var piNum = Pi()
        var expected = "[π]"
        assertEquals(expected, piNum.toString())

        piNum = Pi(true)
        expected = "[1/π]"
        assertEquals(expected, piNum.toString())
    }

    @Test fun testSimplifyList() = runSimplifyListTests()
}
