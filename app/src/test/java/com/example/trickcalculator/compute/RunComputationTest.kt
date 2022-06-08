package com.example.trickcalculator.compute

import org.junit.Test

class RunComputationTest {
    // main functions
    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseSetOfOps() = runParseSetOfOpsTests()
    @Test fun testParseParens() = runParseParensTests()

    // helpers
    @Test fun testGetMatchingParenIndex() = runGetMatchingParenIndexTests()
    @Test fun testGetParsingError() = runGetParsingErrorTests()
}
