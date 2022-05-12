package com.example.trickcalculator.exactdecimal

import org.junit.Test

class ExactDecimalTest {
    // all constructors
    @Test fun testConstructor() = runConstructorTests()

    // unary operators
    @Test fun testUnaryMinus() = runUnaryMinusTests()
    @Test fun testUnaryPlus() = runUnaryPlusTests()
    @Test fun testNot() = runNotTests()

    // binary operators
    @Test fun testPlus() = runPlusTests() // TODO
    @Test fun testMinus() = runMinusTests() // TODO
    @Test fun testTimes() = runTimesTests() // TODO
    @Test fun testDiv() = runDivTests() // TODO
    @Test fun testEquals() = runEqualsTests() // TODO

    // unary non-operator functions
    @Test fun testInverse() = runInverseTests()
    @Test fun testIsZero() = runIsZeroTests()

    @Test fun testAddExpressionLists() = runAddExpressionListsTests() // TODO

    // parsing + toString
    // @Test fun testParseDecimal() = runParseDecimalTests()
    // @Test fun testParseEDString() = runParseEDStringTests()
    // @Test fun testCheckIsEDString() = runCheckIsEDStringTests()
    // @Test fun testToDecimalString() = runToDecimalStringTests()
    // @Test fun testToFractionString() = runToFractionStringTests()
    // @Test fun testToPairString() = runToPairStringTests()
    // @Test fun testToEDString() = runToEDStringTests()

    // casting
    // @Test fun testToPair() = runToPairTests()
    // @Test fun testToByte() = runToByteTests()
    // @Test fun testToChar() = runToCharTests()
    // @Test fun testToShort() = runToShortTests()
    // @Test fun testToInt() = runToIntTests()
    // @Test fun testToLong() = runToLongTests()
    // @Test fun testToFloat() = runToFloatTests()
    // @Test fun testToDouble() = runToDoubleTests()
    // @Test fun testToBigDecimal() = runToBigDecimalTests()
    // @Test fun testToBigInteger() = runToBigIntegerTests()
}
