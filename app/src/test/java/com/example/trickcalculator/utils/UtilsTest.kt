package com.example.trickcalculator.utils

import org.junit.Test

class UtilsTest {
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

        value = "EF[-123 457]"
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
    fun testIsNumberChar() {
        // number chats
        var value = "0"
        assert(isNumberChar(value))

        value = "1"
        assert(isNumberChar(value))

        value = "2"
        assert(isNumberChar(value))

        value = "3"
        assert(isNumberChar(value))

        value = "4"
        assert(isNumberChar(value))

        value = "5"
        assert(isNumberChar(value))

        value = "6"
        assert(isNumberChar(value))

        value = "7"
        assert(isNumberChar(value))

        value = "8"
        assert(isNumberChar(value))

        value = "9"
        assert(isNumberChar(value))

        value = "."
        assert(isNumberChar(value))

        // multiple numbers
        value = "00"
        assert(!isNumberChar(value))

        value = ".1"
        assert(!isNumberChar(value))

        value = "10.0"
        assert(!isNumberChar(value))

        // non-numbers
        value = "a"
        assert(!isNumberChar(value))

        value = "0a"
        assert(!isNumberChar(value))

        value = "-1"
        assert(!isNumberChar(value))

        value = "2/0.1"
        assert(!isNumberChar(value))

        value = "+"
        assert(!isNumberChar(value))
    }
}
