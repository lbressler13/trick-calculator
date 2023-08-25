package xyz.lbres.exactnumbers.exactfraction

import kotlin.test.Test

internal class ExactFractionTest {
    // all constructors
    @Test fun testConstructor() = xyz.lbres.exactnumbers.exactfraction.runConstructorTests()

    // simplify
    // includes all private methods used in simplification
    @Test fun testSimplify() = xyz.lbres.exactnumbers.exactfraction.runSimplifyTests()

    // unary operators
    @Test fun testUnaryMinus() = xyz.lbres.exactnumbers.exactfraction.runUnaryMinusTests()
    @Test fun testUnaryPlus() = xyz.lbres.exactnumbers.exactfraction.runUnaryPlusTests()
    @Test fun testNot() = xyz.lbres.exactnumbers.exactfraction.runNotTests()
    @Test fun testInc() = xyz.lbres.exactnumbers.exactfraction.runIncTests()
    @Test fun testDec() = xyz.lbres.exactnumbers.exactfraction.runDecTests()

    // binary operators
    @Test fun testPlus() = xyz.lbres.exactnumbers.exactfraction.runPlusTests()
    @Test fun testMinus() = xyz.lbres.exactnumbers.exactfraction.runMinusTests()
    @Test fun testTimes() = xyz.lbres.exactnumbers.exactfraction.runTimesTests()
    @Test fun testDiv() = xyz.lbres.exactnumbers.exactfraction.runDivTests()
    @Test fun testCompareTo() = xyz.lbres.exactnumbers.exactfraction.runCompareToTests()
    @Test fun testEquals() = xyz.lbres.exactnumbers.exactfraction.runEqualsTests()
    @Test fun testEq() = xyz.lbres.exactnumbers.exactfraction.runEqTests()
    @Test fun testPow() = xyz.lbres.exactnumbers.exactfraction.runPowTests()

    // unary non-operator functions
    @Test fun testInverse() = xyz.lbres.exactnumbers.exactfraction.runInverseTests()
    @Test fun testAbsoluteValue() = xyz.lbres.exactnumbers.exactfraction.runAbsoluteValueTests()
    @Test fun testIsNegative() = xyz.lbres.exactnumbers.exactfraction.runIsNegativeTests()
    @Test fun testIsZero() = xyz.lbres.exactnumbers.exactfraction.runIsZeroTests()
    @Test fun testRoundToWhole() = xyz.lbres.exactnumbers.exactfraction.runRoundToWholeTests()

    // parsing + toString
    @Test fun testParseDecimal() = xyz.lbres.exactnumbers.exactfraction.runParseDecimalTests()
    @Test fun testParseEFString() = xyz.lbres.exactnumbers.exactfraction.runParseEFStringTests()
    @Test fun testCheckIsEFString() = xyz.lbres.exactnumbers.exactfraction.runCheckIsEFStringTests()
    @Test fun testToDecimalString() = xyz.lbres.exactnumbers.exactfraction.runToDecimalStringTests()
    @Test fun testToFractionString() =
        xyz.lbres.exactnumbers.exactfraction.runToFractionStringTests()
    @Test fun testToPairString() = xyz.lbres.exactnumbers.exactfraction.runToPairStringTests()
    @Test fun testToEFString() = xyz.lbres.exactnumbers.exactfraction.runToEFStringTests()

    // casting
    @Test fun testToPair() = xyz.lbres.exactnumbers.exactfraction.runToPairTests()
    @Test fun testToByte() = xyz.lbres.exactnumbers.exactfraction.runToByteTests()
    @Test fun testToChar() = xyz.lbres.exactnumbers.exactfraction.runToCharTests()
    @Test fun testToShort() = xyz.lbres.exactnumbers.exactfraction.runToShortTests()
    @Test fun testToInt() = xyz.lbres.exactnumbers.exactfraction.runToIntTests()
    @Test fun testToLong() = xyz.lbres.exactnumbers.exactfraction.runToLongTests()
    @Test fun testToFloat() = xyz.lbres.exactnumbers.exactfraction.runToFloatTests()
    @Test fun testToDouble() = xyz.lbres.exactnumbers.exactfraction.runToDoubleTests()
    @Test fun testToBigDecimal() = xyz.lbres.exactnumbers.exactfraction.runToBigDecimalTests()
    @Test fun testToBigInteger() = xyz.lbres.exactnumbers.exactfraction.runToBigIntegerTests()
}
