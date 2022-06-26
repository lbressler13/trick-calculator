package com.example.trickcalculator

import kotlinutils.list.StringList
import org.junit.function.ThrowingRunnable
import org.junit.Assert.*

private val iterations = 20

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

// perform a random action repeatedly, checking to ensure that the result was randomized at least once
// action should contain any other assertions about result
fun <T> runRandomTest(randomAction: () -> T, randomCheck: (T) -> Boolean) {
    var checkPassed = false

    for (i in 0 until iterations) {
        val result = randomAction()
        if (randomCheck(result)) {
            checkPassed = true
        }
    }

    assert(checkPassed)
}

// run a function several times
fun repeat(function: () -> Unit) {
    for (i in 0 until iterations) {
        function()
    }
}
