package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.assertDivByZero
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("KotlinConstantConditions")
class ComputeHelpersTest {
    @Test fun testGetMatchingParenIndex() = runGetMatchingParenIndexTests()

    @Test fun testGetParseErrorMessage() = runGetParseErrorMessageTests()

    @Test
    fun testIsOperator() {
        // operator
        var ops: StringList = listOf("-")
        assertTrue(isOperator("-", ops))

        ops = listOf("/", "+", "-")
        assertTrue(isOperator("+", ops))

        ops = listOf("abc", "def", "+")
        assertTrue(isOperator("abc", ops))

        // non-operator
        ops = listOf("/", "+", "-")
        assertFalse(isOperator("*", ops))

        ops = emptyList()
        assertFalse(isOperator("+", ops))

        ops = listOf("abc", "def", "+")
        assertFalse(isOperator("ab", ops))

        ops = listOf("abc", "def", "+")
        assertFalse(isOperator("ABC", ops))
    }

    @Test
    fun testValidateNumbersOrder() {
        assertFalse(validateNumbersOrder(null))

        assertFalse(validateNumbersOrder(emptyList()))

        var order = listOf(0)
        assertFalse(validateNumbersOrder(order))

        order = (1..9).toList()
        assertFalse(validateNumbersOrder(order))

        order = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 8)
        assertFalse(validateNumbersOrder(order))

        // sorted list should fail validation
        order = (0..9).toList()
        assertFalse(validateNumbersOrder(order))

        order = listOf(0, 2, 1, 3, 4, 5, 6, 7, 8, 9)
        assertTrue(validateNumbersOrder(order))

        order = (9 downTo 0).toList()
        assertTrue(validateNumbersOrder(order))

        order = listOf(3, 7, 2, 0, 5, 8, 9, 1, 6, 4)
        assertTrue(validateNumbersOrder(order))
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
