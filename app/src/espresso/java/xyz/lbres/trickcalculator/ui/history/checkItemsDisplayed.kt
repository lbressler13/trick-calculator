package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.kotlinutils.generic.ext.ifNull
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private const val recyclerId = R.id.itemsRecycler

// mapping of level of randomness to function used to check if values are displayed on screen
private val displayedFunctions: Map<Int, (Pair<String, String>, Int) -> Pair<Boolean, Boolean>> = mapOf(
    1 to { pair, position -> checkMatchedPair(pair, position) },
    2 to { pair, position -> checkUnmatchedPair(pair, position) },
)

/**
 * Check that all ViewHolders contain a valid history item.
 * Method is of checking is based on level of randomness, which must be 1 or 2.
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
fun checkItemsDisplayed(computeHistory: TestHistory, randomness: Int) {
    val checkDisplayed = displayedFunctions[randomness].ifNull {
        throw IllegalArgumentException("checkItemsDisplayed not callable with randomness $randomness")
    }

    computeHistory.forEach {
        var foundComputation = false
        var foundResult = false

        for (position in computeHistory.indices) {
            val checkResult = checkDisplayed(it, position)
            foundComputation = foundComputation || checkResult.first
            foundResult = foundResult || checkResult.second
        }

        if (!(foundComputation && foundResult)) {
            throw AssertionError("ViewHolder with text $it not found. History: $computeHistory")
        }
    }
}

/**
 * Check that a single pair of values is displayed at the specified position.
 * ViewHolder must contain both the computation and result indicated by the item.
 *
 * @param item [Pair]<[String], [String]>: item from test history, consisting of computation and result
 * @param position [Int]: position of ViewHolder to evaluate
 * @return [Pair]<[Boolean], [Boolean]>: pair of `true` if ViewHolder contains both values, or pair of `false` otherwise
 */
private fun checkMatchedPair(item: Pair<String, String>, position: Int): Pair<Boolean, Boolean> {
    onView(withId(recyclerId)).perform(scrollToPosition(position))
    return try {
        onView(withViewHolder(recyclerId, position))
            .check(matches(withHistoryItem(item.first, item.second)))
        Pair(true, true)
    } catch (_: Throwable) {
        Pair(false, false)
    }
}

/**
 * Check if the ViewHolder at the specified position contains the item given.
 * Checks for the first value and second value separately.
 *
 * @param item [Pair]<[String], [String]>: item from test history, consisting of computation and result
 * @param position [Int]: position of ViewHolder to evaluate
 * @return [Pair]<[Boolean], [Boolean]>: a pair where the first value indicates if the ViewHolder contains the computation string,
 * and the second value indicates if it contains the result string
 */
private fun checkUnmatchedPair(item: Pair<String, String>, position: Int): Pair<Boolean, Boolean> {
    onView(withId(recyclerId)).perform(scrollToPosition(position))

    // check computation string
    val first = try {
        onView(withViewHolder(recyclerId, position))
            .check(matches(withChild(withText(item.first))))
        true
    } catch (_: Throwable) {
        false
    }

    // check result string
    val second = try {
        onView(withViewHolder(recyclerId, position))
            .check(matches(withChild(withChild(withText(item.second)))))
        true
    } catch (_: Throwable) {
        false
    }

    return Pair(first, second)
}
