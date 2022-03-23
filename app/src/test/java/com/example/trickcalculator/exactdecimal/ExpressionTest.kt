package com.example.trickcalculator.exactdecimal

import org.junit.Test
import org.junit.Assert.*

class ExpressionTest {
    @Test
    fun testConstructor() {
        // TODO
    }

    @Test fun testTimes() = runExpressionTimesTests()
    @Test fun testPlus() = runExpressionPlusTests()
    @Test fun testEquals() = runExpressionEqualsTests()

    @Test
    fun testDropConstant() {
        // TODO
    }

    @Test
    fun testSimplifyExpression() {
        // TODO
    }

    @Test
    fun testToString() {
        // TODO
    }
}