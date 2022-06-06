package com.example.trickcalculator.compute

import com.example.trickcalculator.utils.IntList
import com.example.trickcalculator.utils.StringList
import exactfraction.ExactFraction
import org.junit.Test
import org.junit.Assert.*

class ValidateComputeTextTest {
    @Test fun testBuildAndValidateComputeText() = runBuildAndValidateTests()

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
