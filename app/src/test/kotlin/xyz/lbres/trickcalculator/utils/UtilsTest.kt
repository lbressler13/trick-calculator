package xyz.lbres.trickcalculator.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTest {
    @Test
    fun testIsNumber() {
        // number
        assertTrue(isNumber("0"))
        assertTrue(isNumber("123"))
        assertTrue(isNumber("-123"))
        assertTrue(isNumber("1.2349"))
        assertTrue(isNumber(".987"))
        assertTrue(isNumber("-.10"))
        assertTrue(isNumber("EF[-123 457]"))

        var longValue = "123232323232323232323232323232323"
        assertTrue(isNumber(longValue))

        longValue = "-100000000000000000000000000000000.4444444444444444444444"
        assertTrue(isNumber(longValue))

        // not number
        assertFalse(isNumber(""))
        assertFalse(isNumber(" "))
        assertFalse(isNumber(" 1 2"))
        assertFalse(isNumber("--3"))
        assertFalse(isNumber("10.10.10"))
        assertFalse(isNumber("5."))
        assertFalse(isNumber("2+3"))
        assertFalse(isNumber("2 + 3"))
        assertFalse(isNumber("+"))
        assertFalse(isNumber("a"))
        assertFalse(isNumber("hello world"))
    }

    @Test
    fun testIsNumberChar() {
        // number chats
        assertTrue(isNumberChar("0"))
        assertTrue(isNumberChar("1"))
        assertTrue(isNumberChar("2"))
        assertTrue(isNumberChar("3"))
        assertTrue(isNumberChar("4"))
        assertTrue(isNumberChar("5"))
        assertTrue(isNumberChar("6"))
        assertTrue(isNumberChar("7"))
        assertTrue(isNumberChar("8"))
        assertTrue(isNumberChar("0"))
        assertTrue(isNumberChar("."))

        // multiple numbers
        assertFalse(isNumberChar("00"))
        assertFalse(isNumberChar(".1"))
        assertFalse(isNumberChar("10.0"))

        // non-numbers
        assertFalse(isNumberChar("a"))
        assertFalse(isNumberChar("0a"))
        assertFalse(isNumberChar("-1"))
        assertFalse(isNumberChar("2/0.1"))
        assertFalse(isNumberChar("+"))
        assertFalse(isNumberChar("-"))
    }
}
