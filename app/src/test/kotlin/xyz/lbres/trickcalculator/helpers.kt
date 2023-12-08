package xyz.lbres.trickcalculator

import xyz.lbres.kotlinutils.list.StringList
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Assert that a function throws a divide by zero error.
 *
 * @param function () -> Unit: function that throws exception
 */
fun assertDivByZero(function: () -> Unit) {
    assertFailsWithMessage<ArithmeticException>("divide by zero", function)
}

/**
 * Assert that a function throws an error with a given message.
 *
 * @param message [String]: expected message
 * @param function () -> Unit: function that throws exception
 */
inline fun assertFailsWithMessage(message: String, function: () -> Unit) {
    assertFailsWithMessage<Exception>(message, function)
}

/**
 * Assert that a function throws an error of a specific type, with a given message.
 *
 * @param message [String]: expected message
 * @param function () -> Unit: function that throws exception
 */
@JvmName("assertFailsWithMessageWithType")
inline fun <reified T : Exception> assertFailsWithMessage(message: String, function: () -> Unit) {
    val error = assertFailsWith<T> { function() }
    assertEquals(message, error.message)
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
 * @param randomAction () -> T: generate a single result, and perform any assertions about that result
 * @param randomCheck (T) -> Boolean: check if a result meets criteria for randomization
 */
fun <T> runRandomTest(randomAction: () -> T, randomCheck: (T) -> Boolean) {
    val iterations = 20
    var checkPassed = false
    var i = 0

    while (i < iterations && !checkPassed) {
        val result = randomAction()
        checkPassed = randomCheck(result)
        i++
    }

    if (!checkPassed) {
        throw AssertionError("Randomized test failed")
    }
}
