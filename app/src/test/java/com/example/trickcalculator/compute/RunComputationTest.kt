package com.example.trickcalculator.compute

import org.junit.Test

class RunComputationTest {
    // main functions
    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseSetOfOps() = runParseSetOfOpsTests()
    @Test fun testParseParens() = runParseParensTests()

    // functions that modify compute text
    // @Test fun testStripParens() = runStripParensTests()
    // @Test fun testStripDecimals() = runStripDecimalsTests()
    // @Test fun testReplaceNumbers() = runReplaceNumbersTests()
    // @Test fun testAddMultToParens() = runAddMultToParensTests()

    // helpers
    @Test fun testGetMatchingParenIndex() = runGetMatchingParenIndexTests()
    @Test fun testGetParsingError() = runGetParsingErrorTests()
}
