package com.example.trickcalculator.utils

import org.junit.Test

class UtilsTest {
    @Test
    fun testIsInt() {
        // int
        var value = "0"
        assert(isInt(value))

        value = "123490"
        assert(isInt(value))

        value = "-34"
        assert(isInt(value))

        value = Int.MAX_VALUE.toString()
        assert(isInt(value))

        value = Int.MIN_VALUE.toString()
        assert(isInt(value))

        // not int
        value = ""
        assert(!isInt(value))

        value = " "
        assert(!isInt(value))

        value = "0.0"
        assert(!isInt(value))

        value = "-.3"
        assert(!isInt(value))

        value = "1+2"
        assert(!isInt(value))

        value = "1 + 2"
        assert(!isInt(value))

        value = "+"
        assert(!isInt(value))

        value = Long.MAX_VALUE.toString()
        assert(!isInt(value))

        value = "a"
        assert(!isInt(value))
    }

    @Test
    fun testIsNumber() {
        // number
        var value = "0"
        assert(isNumber(value))

        value = "123"
        assert(isNumber(value))

        value = "-123"
        assert(isNumber(value))

        value = "1.2349"
        assert(isNumber(value))

        value = ".987"
        assert(isNumber(value))

        value = "-.10"
        assert(isNumber(value))

        value = "123232323232323232323232323232323"
        assert(isNumber(value))

        value = "-100000000000000000000000000000000.4444444444444444444444"
        assert(isNumber(value))

        value = "BF[-123 457]"
        assert(isNumber(value))

        // not number
        value = ""
        assert(!isNumber(value))

        value = " "
        assert(!isNumber(value))

        value = "1 2"
        assert(!isNumber(value))

        value = "--3"
        assert(!isNumber(value))

        value = "10.10.10"
        assert(!isNumber(value))

        value = "5."
        assert(!isNumber(value))

        value = "2+3"
        assert(!isNumber(value))

        value = "2 + 3"
        assert(!isNumber(value))

        value = "+"
        assert(!isNumber(value))

        value = "a"
        assert(!isNumber(value))

        value = "hello world"
        assert(!isNumber(value))
    }

    @Test
    fun testIsPartialDecimal() {
        // partial decimal
        var value = "0"
        assert(isPartialDecimal(value))

        value = "123"
        assert(isPartialDecimal(value))

        value = "-123"
        assert(isPartialDecimal(value))

        value = "1.2349"
        assert(isPartialDecimal(value))

        value = ".987"
        assert(isPartialDecimal(value))

        value = "-.10"
        assert(isPartialDecimal(value))

        value = "."
        assert(isPartialDecimal(value))

        value = "51."
        assert(isPartialDecimal(value))

        value = "1.1.1.1.1"
        assert(isPartialDecimal(value))

        value = "123232323232323232323232323232323"
        assert(isPartialDecimal(value))

        value = "-100000000000000000000000000000000.4444444444444444444444"
        assert(isPartialDecimal(value))

        // not partial decimal
        value = ""
        assert(!isPartialDecimal(value))

        value = " "
        assert(!isPartialDecimal(value))

        value = "-"
        assert(!isPartialDecimal(value))

        value = "--3"
        assert(!isPartialDecimal(value))

        value = "1+2"
        assert(!isPartialDecimal(value))

        value = "+1"
        assert(!isPartialDecimal(value))

        value = "a"
        assert(!isPartialDecimal(value))

        value = "hello world"
        assert(!isPartialDecimal(value))
    }
}