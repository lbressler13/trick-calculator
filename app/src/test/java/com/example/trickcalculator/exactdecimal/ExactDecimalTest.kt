package com.example.trickcalculator.exactdecimal

import org.junit.Test

class ExactDecimalTest {
    // all constructors
    // @Test fun testConstructor() = runConstructorTests()

    // simplify
    // includes all private methods used in simplification
    // @Test fun testSimplify() = runSimplifyTests()

    // unary operators
    @Test fun testUnaryMinus() = runUnaryMinusTests()
    @Test fun testUnaryPlus() = runUnaryPlusTests()
    @Test fun testNot() = runNotTests()
    // @Test fun testInc() = runIncTests()
    // @Test fun testDec() = runDecTests()

    // binary operators
    @Test fun testPlus() = runPlusTests()
    @Test fun testMinus() = runMinusTests()
    @Test fun testTimes() = runTimesTests()
    @Test fun testDiv() = runDivTests()
    // @Test fun testCompareTo() = runCompareToTests()
    @Test fun testEquals() = runEqualsTests()
    // @Test fun testEq() = runEqTests()

    // unary non-operator functions
    @Test fun testInverse() = runInverseTests()
    // @Test fun testAbsoluteValue() = runAbsoluteValueTests()
    // @Test fun testIsNegative() = runIsNegativeTests()
    @Test fun testIsZero() = runIsZeroTests()

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