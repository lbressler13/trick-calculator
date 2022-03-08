package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.StringList
import org.junit.Test

class ComputeUtilsTest {
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
}