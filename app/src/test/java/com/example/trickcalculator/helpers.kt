package com.example.trickcalculator

import org.junit.function.ThrowingRunnable
import org.junit.Assert.*

fun assertDivByZero(function: ThrowingRunnable) {
    val error = assertThrows(ArithmeticException::class.java) {
        function.run()
    }
    assertEquals("divide by zero", error.message)
}
