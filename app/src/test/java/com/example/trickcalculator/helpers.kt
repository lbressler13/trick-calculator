package com.example.trickcalculator

import org.junit.function.ThrowingRunnable
import com.example.trickcalculator.exactdecimal.Term
import com.example.trickcalculator.exactdecimal.asExpression
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.StringList
import org.junit.Assert.*

fun assertDivByZero(function: ThrowingRunnable) {
    val error = assertThrows(ArithmeticException::class.java) {
        function.run()
    }
    assertEquals("divide by zero", error.message)
}

fun <T: Comparable<T>> assertListsEqual(expected: List<T>, actual: List<T>) {
    assertEquals(expected.sorted(), actual.sorted())
}

fun createExprList(strings: StringList): ExprList {
    return strings.map {
        it.split(' ').map { t -> Term(t) }.asExpression()
    }
}
