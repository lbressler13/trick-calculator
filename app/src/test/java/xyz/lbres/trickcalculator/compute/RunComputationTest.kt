package xyz.lbres.trickcalculator.compute

import org.junit.Test

class RunComputationTest {
    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseOperatorRound() = runParseOperatorRoundTests()
    @Test fun testParseParens() = runParseParensTests()
}
