package com.example.trickcalculator.bigfraction

import org.junit.Test

class BigFractionTest {
    // all constructors
    @Test fun testConstructor() = runConstructorTests()

    // simplify
    @Test fun testSimplify() = runSimplifyTests()
    @Test fun testSetSign() = runSetSignTests()

    // unary operators
    @Test fun testUnaryMinus() = runUnaryMinusTests()
    @Test fun testUnaryPlus() = runUnaryPlusTests()
    @Test fun testNot() = runNotTests()
    @Test fun testInc() = runIncTests()
    @Test fun testDec() = runDecTests()

    // binary operators
    @Test fun testPlus() = runPlusTests()
    @Test fun testMinus() = runMinusTests()
    @Test fun testTimes() = runTimesTests()
    @Test fun testDiv() = runDivTests()
    @Test fun testCompareTo() = runCompareToTests()
    @Test fun testEquals() = runEqualsTests()

    // unary non-operator functions
    @Test fun testInverse() = runInverseTests()
    @Test fun testAbs() = runAbsTests()
    @Test fun testIsNegative() = runIsNegativeTests()
    @Test fun testIsZero() = runIsZeroTests()

    // parsing + toString
    @Test fun testParseDecimal() = runParseDecimalTests()
    @Test fun testParseBFString() = runParseBFStringTests()
    @Test fun testCheckIsBFString() = runCheckIsBFStringTests()
    @Test fun toDecimalString() = runToDecimalStringTests()
    @Test fun toFractionString() = runToFractionStringTests()
    @Test fun testToPairString() = runToPairStringTests()

    // casting
    @Test fun testToPair() = runToPairTests()
    @Test fun testToBigDecimal() = runToBigDecimalTests()
}
