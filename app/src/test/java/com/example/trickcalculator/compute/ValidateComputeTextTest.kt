package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import org.junit.Test

class ValidateComputeTextTest {
    @Test
    fun testValidateComputeTest() {
        val ops = listOf("+", "-", "x", "/")

        var text: StringList = listOf()
        assert(validateComputeText(text, ops))

        // operator at start or end
        text = listOf("+")
        assert(!validateComputeText(text, ops))

        text = "- 3".split(' ')
        assert(!validateComputeText(text, ops))

        text = "x 3 - 4".split(' ')
        assert(!validateComputeText(text, ops))

        text = "3.0 - ( 4 + 2 ) /".split(' ')
        assert(!validateComputeText(text, ops))

        // operator at start or end in parens
        text = "( + )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "( - 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "1 + ( 3 - 4 / )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "3.0 - ( 4 + 3 ( 2 + ) )".split(' ')
        assert(!validateComputeText(text, ops))

        // empty parens
        text = "( )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "3 / ( 2 - 6 ( ) )".split(' ')
        assert(!validateComputeText(text, ops))

        // mismatched parens
        text = listOf("(")
        assert(!validateComputeText(text, ops))

        text = listOf(")")
        assert(!validateComputeText(text, ops))

        text = ") (".split(' ')
        assert(!validateComputeText(text, ops))

        text = "5 - ( 0 + ( 3 - 2 )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "( 1 ) + 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "( 1 ( 8 / 9 ) + 3 ( 2 + 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        // repeated numbers or ops
        text = "1 2".split(' ')
        assert(!validateComputeText(text, ops))

        text = "1 + - 2".split(' ')
        assert(!validateComputeText(text, ops))

        text = "1 + 2 2 / x 3 ".split(' ')
        assert(!validateComputeText(text, ops))

        text = "5 - 6 ( 0 + ( 3 - - 2 ) )".split(' ')
        assert(!validateComputeText(text, ops))

        text = " 1 ( 8 / 9 ) + 3 3 ( 2 + 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        // unknown char/invalid number
        text = listOf("a")
        assert(!validateComputeText(text, ops))

        text = "0 * 3".split(' ')
        assert(!validateComputeText(text, ops))

        text = " 1 ( 8 / 9 ) + 3 * ( 2 + 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        val partialOps = listOf("+", "-")
        text = " 1 ( 8 / 9 ) + 3 x ( 2 + 3 )".split(' ')
        assert(!validateComputeText(text, partialOps))

        text = listOf("5.")
        assert(!validateComputeText(text, ops))

        text = " 1 ( 8 / 9 ) + -3.0.0 x ( 2 + 3 )".split(' ')
        assert(!validateComputeText(text, ops))

        text = "0 x --3".split(' ')
        assert(!validateComputeText(text, ops))

        text = listOf(" ")
        assert(!validateComputeText(text, ops))

        // valid
        text = listOf("5")
        assert(validateComputeText(text, ops))

        text = listOf("-5")
        assert(validateComputeText(text, ops))

        text = listOf("5.01")
        assert(validateComputeText(text, ops))

        text = "( 1 - 4 ) x 5".split(' ')
        assert(validateComputeText(text, ops))

        text = "1 ( 8 / 9 ) + -3.0 x ( 2 + 3 )".split(' ')
        assert(validateComputeText(text, ops))
    }

    @Test
    fun testValidateNumbersOrder() {
        var order: IntList? = null
        assert(!validateNumbersOrder(order))

        order = listOf()
        assert(!validateNumbersOrder(order))

        order = listOf(0)
        assert(!validateNumbersOrder(order))

        order = (1..9).toList()
        assert(!validateNumbersOrder(order))

        order = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 8)
        assert(!validateNumbersOrder(order))

        // sorted list should fail validation
        order = (0..9).toList()
        assert(!validateNumbersOrder(order))

        order = listOf(0, 2, 1, 3, 4, 5, 6, 7, 8, 9)
        assert(validateNumbersOrder(order))

        order = (9 downTo 0).toList()
        assert(validateNumbersOrder(order))

        order = listOf(3, 7, 2, 0, 5, 8, 9, 1, 6, 4)
        assert(validateNumbersOrder(order))
    }
}