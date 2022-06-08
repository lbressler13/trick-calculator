package com.example.trickcalculator.compute

import com.example.trickcalculator.assertDivByZero
import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import exactfraction.ExactFraction
import junit.framework.Assert.assertEquals
import org.junit.Test
import kotlin.math.exp

class ComputeHelpersTest {
    @Test
    fun testIsOperator() {
        var element = "-"
        var ops: StringList = listOf("-")
        assert(isOperator(element, ops))

        element = "+"
        ops = listOf("/", "+", "-")
        assert(isOperator(element, ops))

        element = "*"
        ops = listOf("/", "+", "-")
        assert(!isOperator(element, ops))

        element = "+"
        ops = listOf()
        assert(!isOperator(element, ops))

        element = "abc"
        ops = listOf("abc", "def", "+")
        assert(isOperator(element, ops))

        element = "ab"
        ops = listOf("abc", "def", "+")
        assert(!isOperator(element, ops))

        element = "ABC"
        ops = listOf("abc", "def", "+")
        assert(!isOperator(element, ops))
    }

    @Test
    fun testApplyOrderToEF() {
        // nulls
        var expected: ExactFraction? = null
        var ef: ExactFraction? = null
        var order: IntList? = null
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = null
        order = listOf(9, 4, 2, 1, 8, 5, 7, 6, 3, 0)
        expected = null
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction(18, 37)
        order = null
        expected = ExactFraction(18, 37)
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction(-18, 37)
        order = null
        expected = ExactFraction(-18, 37)
        assertEquals(expected, applyOrderToEF(order, ef))

        // no nulls
        ef = ExactFraction.ZERO
        order = listOf(4, 7, 0, 1, 2, 3, 5, 6, 8, 9)
        expected = ExactFraction(4, 7)
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = -ExactFraction.FOUR
        order = listOf(0, 8, 1, 2, 5, 3, 4, 6, 7, 9)
        expected = ExactFraction(-5, 8)
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction(-120, 953)
        order = (9 downTo 0).toList()
        expected = ExactFraction(-879, 46)
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction(17, 14)
        order = listOf(0, 2, 3, 4, 8, 5, 6, 1, 7, 9)
        expected = ExactFraction(3, 4) // 21 / 28
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction(18569, 274380)
        order = listOf(5, 7, 1, 9, 3, 0, 6, 8, 4, 2)
        expected = ExactFraction(74062, 183945)
        assertEquals(expected, applyOrderToEF(order, ef))

        ef = ExactFraction.HALF
        order = listOf(1, 2, 0, 3, 4, 5, 6, 7, 8, 9)
        assertDivByZero { applyOrderToEF(order, ef) }
    }
}