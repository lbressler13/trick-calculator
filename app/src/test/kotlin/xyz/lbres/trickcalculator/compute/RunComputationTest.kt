package xyz.lbres.trickcalculator.compute

import io.mockk.unmockkAll
import org.junit.After
import org.junit.Test

class RunComputationTest {
    @After
    fun cleanupMockk() {
        unmockkAll()
    }

    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseOperatorRound() = runParseOperatorRoundTests()
    @Test fun testParseParens() = runParseParensTests()
}
