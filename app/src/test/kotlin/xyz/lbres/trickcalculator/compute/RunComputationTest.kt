package xyz.lbres.trickcalculator.compute

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import xyz.lbres.trickcalculator.utils.AppLogger

class RunComputationTest {
    @Before
    fun setupTest() {
        mockkStatic(AppLogger::class)
        every { AppLogger.e(any(), any()) } returns 0
    }

    @After
    fun cleanupTest() {
        unmockkAll()
    }

    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseOperatorRound() = runParseOperatorRoundTests()
    @Test fun testParseParens() = runParseParensTests()
}
