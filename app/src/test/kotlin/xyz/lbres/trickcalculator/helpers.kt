package xyz.lbres.trickcalculator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.function.ThrowingRunnable
import xyz.lbres.kotlinutils.list.StringList

private const val iterations = 20

/**
 * Assert that a function throws a divide by zero error.
 *
 * @param function [ThrowingRunnable]: function that throws exception
 */
fun assertDivByZero(function: ThrowingRunnable) {
    val error = assertThrows(ArithmeticException::class.java) {
        function.run()
    }
    assertEquals("divide by zero", error.message)
}

/**
 * Split a string into individual characters.
 * By default, splitting adds a space at the beginning and end, which this helper does not do.
 *
 * @param s [String]: string to split
 * @return [StringList]: list of characters in [s], without any added spaces at start or end
 */
fun splitString(s: String): StringList {
    return when (s.length) {
        0 -> emptyList()
        1 -> listOf(s)
        else -> {
            val split = s.split("")
            split.subList(1, split.lastIndex)
        }
    }
}

/**
 * Perform a random action repeatedly, checking to ensure that the result that was randomized.
 * The action should also contain any assertions about a single result.
 *
 * @param randomAction [() -> T]: generate a single result, and perform any assertions about that result
 * @param randomCheck [(T) -> Boolean]: check if a result meets criteria for randomization
 */
fun <T> runRandomTest(randomAction: () -> T, randomCheck: (T) -> Boolean) {
    var checkPassed = false

    repeat(iterations) {
        val result = randomAction()
        if (randomCheck(result)) {
            checkPassed = true
        }
    }

    assertTrue(checkPassed)
}
