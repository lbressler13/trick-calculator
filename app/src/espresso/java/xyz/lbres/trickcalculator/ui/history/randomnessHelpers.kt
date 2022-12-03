package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private const val recyclerId = R.id.itemsRecycler

private val displayedFunctions: Map<Int, (Pair<String, String>, Int) -> Pair<Boolean, Boolean>> = mapOf(
    1 to { pair: Pair<String, String>, position: Int -> checkMatchedPair(pair, position) },
    2 to { pair: Pair<String, String>, position: Int -> checkUnmatchedPair(pair, position) },
)

/**
 * Check that all history items are visible on the screen
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
fun checkItemsDisplayed(computeHistory: TestHistory, randomness: Int) {
    val historySize = computeHistory.size
    val checkDisplayed = displayedFunctions[randomness]!!

    computeHistory.forEach {
        var foundComputation = false
        var foundResult = false

        for (position in 0 until historySize) {
            val checkResult = checkDisplayed(it, position)
            foundComputation = foundComputation || checkResult.first
            foundResult = foundResult || checkResult.second
        }

        if (!(foundComputation && foundResult)) {
            throw AssertionError("ViewHolder with text $it not found. History: $computeHistory")
        }
    }
}

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

private fun checkUnmatchedPair(item: Pair<String, String>, position: Int): Pair<Boolean, Boolean> {
    onView(withId(recyclerId)).perform(scrollToPosition(position))

    val first = try {
        onView(withViewHolder(recyclerId, position))
            .check(matches(withChild(withText(item.first))))
        true
    } catch (_: Throwable) {
        false
    }

    val second = try {
        onView(withViewHolder(recyclerId, position))
            .check(matches(withChild(withChild(withText(item.second)))))
        true
    } catch (_: Throwable) {
        false
    }

    return Pair(first, second)
}
