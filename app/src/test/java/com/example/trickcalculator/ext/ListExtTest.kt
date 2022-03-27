package com.example.trickcalculator.ext

import org.junit.Test
import org.junit.Assert.*

class ListExtTest {
    @Test
    fun testCopyWithReplacement() {
        assertThrows(Exception::class.java) {
            listOf<String>().copyWithReplacement(0, "a")
        }

        assertThrows(Exception::class.java) {
            listOf("a").copyWithReplacement(-1, "b")
        }

        assertThrows(Exception::class.java) {
            listOf("a", "b").copyWithReplacement(2, "c")
        }

        assertThrows(Exception::class.java) {
            listOf("a", "b").copyWithReplacement(8, "c")
        }

        var l = listOf("a")
        var i = 0
        var expected = listOf("abc")
        assertEquals(expected, l.copyWithReplacement(i, "abc"))

        l = listOf("0", "1", "1", "2", "3", "6", "8")
        i = 5
        expected = listOf("0", "1", "1", "2", "3", "5", "8")
        assertEquals(expected, l.copyWithReplacement(i, "5"))

        l = listOf("0", "1", "1", "2", "3", "6", "8")
        i = 5
        expected = listOf("0", "1", "1", "2", "3", "5", "8")
        assertEquals(expected, l.copyWithReplacement(i, "5"))

        l = listOf("hello", "goodbye", "greetings", "hey y'all")
            .copyWithReplacement(2, "farewell")
            .copyWithReplacement(0, "hey")
            .copyWithReplacement(3, "what's up")
            .copyWithReplacement(1, "byeeee")
        expected = listOf("hey", "byeeee", "farewell", "what's up")
        assertEquals(expected, l)

        val ln = listOf(0, 1, 1, 2, 3, 6, 8)
        i = 5
        val expectedNum = listOf(0, 1, 1, 2, 3, 5, 8)
        assertEquals(expectedNum, ln.copyWithReplacement(i, 5))

        val e1 = ArithmeticException()
        val e2 = NumberFormatException()
        val e3 = RuntimeException()
        val le = listOf(e1, e2, e3)
        i = 0
        val expectedErr = listOf(e3, e2, e3)
        assertEquals(expectedErr, le.copyWithReplacement(i, e3))
    }

    @Test
    fun testCopyWithLastReplaced() {
        assertThrows(Exception::class.java) {
            listOf<String>().copyWithLastReplaced("a")
        }

        var l = listOf("a")
        var expected = listOf("b")
        assertEquals(expected, l.copyWithLastReplaced("b"))

        l = listOf("abc", "abc")
        expected = listOf("abc", "12")
        assertEquals(expected, l.copyWithLastReplaced("12"))

        l = listOf("a b", "a b")
        expected = listOf("a b", "b")
        assertEquals(expected, l.copyWithLastReplaced("b"))

        l = "A word another word and another and another and another".split(' ')
        expected = "A word another word and another and another and".split(' ') + "not another"
        assertEquals(expected, l.copyWithLastReplaced("not another"))

        var ln = listOf(19, 107, 3)
        var expectedNum = listOf(19, 107, -1)
        assertEquals(expectedNum, ln.copyWithLastReplaced(-1))

        ln = (-1000..10000).toList()
        expectedNum = (-1000..9999).toList() + (-1001)
        assertEquals(expectedNum, ln.copyWithLastReplaced(-1001))

        val e1 = ArithmeticException()
        val e2 = NumberFormatException()
        val e3 = RuntimeException()
        val le = listOf(e1, e2, e3)
        val expectedErr = listOf(e1, e2, e2)
        assertEquals(expectedErr, le.copyWithLastReplaced(e2))
    }
}
