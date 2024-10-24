package xyz.lbres.trickcalculator

import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.set.multiset.MutableMultiSet
import xyz.lbres.kotlinutils.set.multiset.mutableMultiSetOf
import kotlin.test.assertContains
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
 * Retry a test multiple times, and only fail if all tries fail
 *
 * @param tries [Int]: number of times to run the test. Defaults to 2 (a single retry)
 * @param retryableTest () -> [Unit]: test to run
 */
fun runTestWithRetry(tries: Int = 2, retryableTest: () -> Unit) {
    repeat(tries - 1) {
        try {
            retryableTest()
            return
        } catch (_: AssertionError) {
            println("Test failed on try ${it + 1}. Retrying...")
        }
    }

    retryableTest()
}

/**
 * Repeatedly generate a random value, and check that the results are evenly distributed
 *
 * @param values [Iterable]<T>: all possible values
 * @param iterations [Int]: number of times to generate value
 * @param getValue ([Iterable]<T>) -> T: function that takes [values] as parameter, and uses it to randomly generate a value
 */
fun <T> checkDistributedResults(values: Iterable<T>, iterations: Int, getValue: (Iterable<T>) -> T) {
    val results: MutableMultiSet<T> = mutableMultiSetOf()
    repeat(iterations) {
        val result = getValue(values)
        results.add(result)
        assertContains(values, result)
    }

    val average = iterations / values.count()
    val allowedRange = (average * 2 / 3)..(average * 4 / 3)
    values.forEach {
        assertContains(allowedRange, results.getCountOf(it))
    }
}
