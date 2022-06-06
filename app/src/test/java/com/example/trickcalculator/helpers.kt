package com.example.trickcalculator

import com.example.trickcalculator.utils.StringList
import org.junit.function.ThrowingRunnable
import org.junit.Assert.*

fun assertDivByZero(function: ThrowingRunnable) {
    val error = assertThrows(ArithmeticException::class.java) {
        function.run()
    }
    assertEquals("divide by zero", error.message)
}

// by default, splitting on empty space adds a character at start and end
// this helper fixes that
fun splitString(s: String): StringList {
    return when(s.length) {
        0 -> listOf()
        1 -> listOf(s)
        else -> {
            val split = s.split("")
            split.subList(1, split.lastIndex)
        }
    }
}
