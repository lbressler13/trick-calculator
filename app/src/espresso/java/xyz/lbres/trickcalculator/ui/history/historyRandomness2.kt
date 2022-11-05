package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private const val recyclerId = R.id.itemsRecycler

fun testRandomness2() {
    if (ProductFlavor.devMode) {
        hideDevToolsButton()
    }

    setHistoryRandomness(2)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val computeHistory: MutableList<TestCompItem> = mutableListOf()

    // one element
}

/**
 * Check that all the expected information is displayed in the history.
 * Verifies that all items are displayed, the order of items is shuffled, and the pairs of items are shuffled.
 *
 * @param computeHistory [List]<[TestCompItem]>: list of items in history
 */
private fun checkCorrectData(computeHistory: List<TestCompItem>) {
    var shuffledOrder = false
    var shuffledPairs = false
    val historySize = computeHistory.size

    repeat(5) {
        openHistoryFragment()

        // check that all items are displayed
        checkItemsDisplayed(computeHistory)

        // check that items are shuffled
        shuffledOrder = shuffledOrder || checkItemsShuffled(computeHistory)
        shuffledPairs = shuffledPairs || checkPairsShuffled(computeHistory)

        closeFragment()
    }

    // check shuffled after all repeats, because each repeat has some probability of correct order
    if (historySize > 1 && (!shuffledOrder || !shuffledPairs)) {
        throw AssertionError("History items and pairs should be shuffled in history randomness 2")
    }
}

/**
 * Check that all history items are visible on the screen
 *
 * @param computeHistory [List]<[TestCompItem]>: list of items in history
 */
private fun checkItemsDisplayed(computeHistory: List<TestCompItem>) {
    val historySize = computeHistory.size

    computeHistory.forEach {
        var foundItem = false
        for (i in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(i))

            try {
                onView(withText(it.first)).check(matches(isDisplayed()))
                onView(withText(it.second)).check(matches(isDisplayed()))
                foundItem = true
                break
            } catch (_: Throwable) {}
        }

        if (!foundItem) {
            throw AssertionError("ViewHolder with text $it not found")
        }
    }
}

/**
 * Determine if the order of the history items is shuffled
 *
 * @param computeHistory [List]<[TestCompItem]>: list of items in history
 * @return [Boolean]: true if at least one item's position does not match its position in the history, false if all items are in order
 */
private fun checkItemsShuffled(computeHistory: List<TestCompItem>): Boolean {
    return false
}

/**
 * Determine if the pairs of values have been shuffled
 *
 * @param computeHistory [List]<[TestCompItem]>: list of items in history
 * @return [Boolean]: true if at least one viewholder contains a mismatched computation/result, false if all pairs match
 */
private fun checkPairsShuffled(computeHistory: List<TestCompItem>): Boolean {
    return false
}
