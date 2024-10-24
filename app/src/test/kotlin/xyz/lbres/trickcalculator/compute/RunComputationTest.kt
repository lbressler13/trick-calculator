package xyz.lbres.trickcalculator.compute

import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.Test

class RunComputationTest {
    @AfterTest
    fun cleanupMockk() {
        unmockkAll()
    }

    @Test fun testRunComputation() = runRunComputationTests()

    @Test fun testParseText() = runParseTextTests()

    @Test fun testParseOperatorRound() = runParseOperatorRoundTests()

    @Test fun testParseParens() = runParseParensTests()
}
