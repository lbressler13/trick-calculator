package xyz.lbres.exactnumbers.irrationals.sqrt

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import xyz.lbres.exactnumbers.irrationals.common.Memoize
import xyz.lbres.exactnumbers.irrationals.sqrt.extractWholeOf
import xyz.lbres.exactnumbers.irrationals.sqrt.getRootOf
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class HelpersTest {
    @AfterTest
    fun clearMocks() {
        unmockkAll()
    }

    @Test
    internal fun testGetRootOf() {
        mockkObject(Memoize)
        every { Memoize.individualWholeNumber } answers { mutableMapOf() }

        // rational
        var num = BigInteger.ZERO
        var expected = BigDecimal.ZERO
        assertEquals(expected, getRootOf(num))

        num = BigInteger.ONE
        expected = BigDecimal.ONE
        assertEquals(expected, getRootOf(num))

        num = BigInteger("961")
        expected = BigDecimal("31")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("17424")
        expected = BigDecimal("132")
        assertEquals(expected, getRootOf(num))

        // irrational
        num = BigInteger.TWO
        expected = BigDecimal("1.4142135623730950488")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("8")
        expected = BigDecimal("2.8284271247461900976")
        assertEquals(expected, getRootOf(num))

        num = BigInteger("1333333")
        expected = BigDecimal("1154.7003940416752105")
        assertEquals(expected, getRootOf(num))
    }

    @Test
    fun testExtractWholeOf() {
        val one = BigInteger.ONE
        val two = BigInteger.TWO
        val three = BigInteger("3")
        val five = BigInteger("5")
        val seven = BigInteger("7")
        val ten = BigInteger("10")

        mockkObject(Memoize)

        // exception
        assertFailsWith<ArithmeticException>("Cannot calculate root of negative number") { extractWholeOf(BigInteger("-25")) }

        // rational
        var num = BigInteger.ZERO
        var expected = one
        runSingleExtractWholeOfTest(num, expected, mapOf(), listOf(Pair(BigInteger.ZERO, one)))

        num = one
        expected = one
        runSingleExtractWholeOfTest(num, expected, mapOf(), listOf(Pair(one, one)))

        num = BigInteger("100")
        expected = ten
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(one, one),
                Pair(two, one),
                Pair(five, one),
                Pair(BigInteger("25"), five),
                Pair(BigInteger("100"), ten)
            )
        )

        num = BigInteger("36")
        expected = BigInteger("6")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(one, one),
                Pair(two, one),
                Pair(three, one),
                Pair(BigInteger("9"), BigInteger("3")),
                Pair(BigInteger("36"), BigInteger("6"))
            )
        )

        num = BigInteger("81")
        expected = BigInteger("9")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(one, one),
                Pair(three, one),
                Pair(BigInteger("81"), BigInteger("9"))
            )
        )

        num = BigInteger("4900")
        expected = BigInteger("70")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(one, one),
                Pair(two, one),
                Pair(five, one),
                Pair(seven, one),
                Pair(BigInteger("49"), seven),
                Pair(BigInteger("1225"), BigInteger("35")),
                Pair(BigInteger("4900"), BigInteger("70"))
            )
        )

        num = BigInteger("8281")
        expected = BigInteger("91")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(one, one),
                Pair(seven, one),
                Pair(BigInteger("13"), one),
                Pair(BigInteger("169"), BigInteger("13")),
                Pair(BigInteger("8281"), BigInteger("91"))
            )
        )

        // irrational w/ whole
        num = BigInteger("8")
        expected = two
        runSingleExtractWholeOfTest(
            num, expected, mapOf(), listOf(Pair(two, one), Pair(BigInteger("8"), two))
        )

        num = BigInteger("18")
        expected = BigInteger("3")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(Pair(two, one), Pair(three, one), Pair(BigInteger("18"), three))
        )

        num = BigInteger("200")
        expected = ten
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(two, one),
                Pair(five, one),
                Pair(BigInteger("50"), five),
                Pair(BigInteger("200"), ten)
            )
        )

        num = BigInteger("296208")
        expected = BigInteger("132")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(),
            listOf(
                Pair(two, one),
                Pair(three, one),
                Pair(BigInteger("11"), one),
                Pair(BigInteger("17"), one),
                Pair(BigInteger("2057"), BigInteger("11")),
                Pair(BigInteger("18513"), BigInteger("33")),
                Pair(BigInteger("296208"), BigInteger("132"))
            )
        )

        // no whole
        expected = one

        num = two
        assertEquals(expected, extractWholeOf(num))
        runSingleExtractWholeOfTest(num, expected, mapOf(), listOf(Pair(two, one)))

        num = BigInteger("55")
        assertEquals(expected, extractWholeOf(num))
        runSingleExtractWholeOfTest(num, expected, mapOf(), listOf(Pair(BigInteger("55"), one)))

        num = BigInteger("52910")
        assertEquals(expected, extractWholeOf(num))
        runSingleExtractWholeOfTest(num, expected, mapOf(), listOf(Pair(BigInteger("52910"), one)))

        // fully memoized
        num = BigInteger("9800")
        expected = BigInteger("70")
        runSingleExtractWholeOfTest(num, expected, mapOf(num to expected), listOf())

        num = BigInteger("917")
        expected = one
        runSingleExtractWholeOfTest(num, expected, mapOf(num to expected), listOf())

        num = BigInteger("81")
        expected = BigInteger("9")
        runSingleExtractWholeOfTest(num, expected, mapOf(num to expected), listOf())

        num = BigInteger("25")
        expected = five
        runSingleExtractWholeOfTest(num, expected, mapOf(num to expected, ten to one, BigInteger("75") to five), listOf())

        // partially memoized
        num = BigInteger("36")
        expected = BigInteger("6")
        runSingleExtractWholeOfTest(
            num,
            expected,
            mapOf(BigInteger("9") to three),
            listOf(Pair(one, one), Pair(two, one), Pair(num, expected))
        )

        num = BigInteger("4900")
        expected = BigInteger("70")
        runSingleExtractWholeOfTest(
            num, expected, mapOf(one to one, two to one, five to one, seven to one),
            listOf(
                Pair(BigInteger("49"), seven),
                Pair(BigInteger("1225"), BigInteger("35")),
                Pair(BigInteger("4900"), BigInteger("70"))
            )
        )

        num = BigInteger("4900")
        expected = BigInteger("70")
        runSingleExtractWholeOfTest(
            num, expected,
            mapOf(two to one, BigInteger("1225") to BigInteger("35")),
            listOf(Pair(one, one), Pair(BigInteger("4900"), BigInteger("70")))
        )

        num = BigInteger("200")
        expected = ten
        runSingleExtractWholeOfTest(
            num, expected, mapOf(five to one, BigInteger("20") to two),
            listOf(
                Pair(two, one),
                Pair(BigInteger("50"), five),
                Pair(BigInteger("200"), ten)
            )
        )
    }

    private fun runSingleExtractWholeOfTest(
        num: BigInteger,
        expected: BigInteger,
        memo: Map<BigInteger, BigInteger>,
        calls: List<Pair<BigInteger, BigInteger>>
    ) {
        val mapSpy = spyk(memo.toMutableMap())
        for (pair in memo) {
            every { mapSpy[pair.key] } returns pair.value
        }

        every { Memoize.individualWholeNumber } answers { mapSpy }

        assertEquals(expected, extractWholeOf(num))
        verify(exactly = calls.size) { mapSpy[any()] = any() }
        for (call in calls) {
            verify { mapSpy[call.first] = call.second }
        }
    }
}
