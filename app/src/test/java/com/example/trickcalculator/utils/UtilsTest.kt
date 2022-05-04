package com.example.trickcalculator.utils

import org.junit.Test

class UtilsTest {
    @Test fun testIsInt() = runIsIntTests()
    @Test fun testIsNumber() = runIsNumberTests()

    @Test fun testIsPartialDecimal() = runIsPartialDecimalTests()
    @Test fun testGetGCD() = runGetGCDTests()
    @Test fun testGetListGCD() = runGetGCDListTests()
}