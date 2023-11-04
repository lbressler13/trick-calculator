package xyz.lbres.trickcalculator.utils

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.Test

class UtilsTest {
    @Test
    fun testIsNumber() {
        // number
        var value = "0"
        assertTrue(isNumber(value))

        value = "123"
        assertTrue(isNumber(value))

        value = "-123"
        assertTrue(isNumber(value))

        value = "1.2349"
        assertTrue(isNumber(value))

        value = ".987"
        assertTrue(isNumber(value))

        value = "-.10"
        assertTrue(isNumber(value))

        value = "123232323232323232323232323232323"
        assertTrue(isNumber(value))

        value = "-100000000000000000000000000000000.4444444444444444444444"
        assertTrue(isNumber(value))

        value = "EF[-123 457]"
        assertTrue(isNumber(value))

        // not number
        value = ""
        assertFalse(isNumber(value))

        value = " "
        assertFalse(isNumber(value))

        value = "1 2"
        assertFalse(isNumber(value))

        value = "--3"
        assertFalse(isNumber(value))

        value = "10.10.10"
        assertFalse(isNumber(value))

        value = "5."
        assertFalse(isNumber(value))

        value = "2+3"
        assertFalse(isNumber(value))

        value = "2 + 3"
        assertFalse(isNumber(value))

        value = "+"
        assertFalse(isNumber(value))

        value = "a"
        assertFalse(isNumber(value))

        value = "hello world"
        assertFalse(isNumber(value))
    }

    @Test
    fun testIsNumberChar() {
        // number chats
        var value = "0"
        assertTrue(isNumberChar(value))

        value = "1"
        assertTrue(isNumberChar(value))

        value = "2"
        assertTrue(isNumberChar(value))

        value = "3"
        assertTrue(isNumberChar(value))

        value = "4"
        assertTrue(isNumberChar(value))

        value = "5"
        assertTrue(isNumberChar(value))

        value = "6"
        assertTrue(isNumberChar(value))

        value = "7"
        assertTrue(isNumberChar(value))

        value = "8"
        assertTrue(isNumberChar(value))

        value = "9"
        assertTrue(isNumberChar(value))

        value = "."
        assertTrue(isNumberChar(value))

        // multiple numbers
        value = "00"
        assertFalse(isNumberChar(value))

        value = ".1"
        assertFalse(isNumberChar(value))

        value = "10.0"
        assertFalse(isNumberChar(value))

        // non-numbers
        value = "a"
        assertFalse(isNumberChar(value))

        value = "0a"
        assertFalse(isNumberChar(value))

        value = "-1"
        assertFalse(isNumberChar(value))

        value = "2/0.1"
        assertFalse(isNumberChar(value))

        value = "+"
        assertFalse(isNumberChar(value))
    }
}
