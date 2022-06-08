package com.example.trickcalculator.compute

import org.junit.Test

class RunComputationTest {
    @Test fun testRunComputation() = runRunComputationTests()
    @Test fun testParseText() = runParseTextTests()
    @Test fun testParseSetOfOps() = runParseSetOfOpsTests()
    @Test fun testParseParens() = runParseParensTests()
}
