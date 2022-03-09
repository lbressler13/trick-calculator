package com.example.trickcalculator.ext

import org.junit.Test
import org.junit.Assert.*

class StringExtTest {
    @Test
    fun testSubstringTo() {
        assertThrows(Exception::class.java) { "".substringTo(1) }
        assertThrows(Exception::class.java) { "a".substringTo(-1) }
        assertThrows(Exception::class.java) { "a b".substringTo(4) }

        var s = ""
        var i = 0
        var expected = ""
        assertEquals(expected, s.substringTo(i))

        s = "hello world"

        i = 11
        expected = "hello world"
        assertEquals(expected, s.substringTo(i))

        i = 1
        expected = "h"
        assertEquals(expected, s.substringTo(i))

        i = 6
        expected = "hello "
        assertEquals(expected, s.substringTo(i))
    }
}