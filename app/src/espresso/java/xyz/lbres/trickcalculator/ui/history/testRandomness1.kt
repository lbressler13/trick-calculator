package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

private const val recyclerId = R.id.itemsRecycler

fun testRandomness1() {
    setHistoryRandomness(1)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val computeHistory: MutableList<TestCompItem> = mutableListOf()

    // one element
    closeFragment()
    typeText("400/5")
    equals()
    computeHistory.add(Pair("400/5", "80"))
    checkCorrectData(computeHistory)

    // several elements
    clearText()
    typeText("15-2.5")
    equals()
    computeHistory.add(Pair("15-2.5", "12.5"))
    checkCorrectData(computeHistory)

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(Pair("(3-4)(5+2)", "-7"))
    checkCorrectData(computeHistory)

    // previously computed
    typeText("+11")
    equals()
    computeHistory.add(Pair("-7+11", "4"))
    checkCorrectData(computeHistory)

    // error
    clearText()
    typeText("+")
    equals()
    computeHistory.add(Pair("+", "Syntax error"))
    checkCorrectData(computeHistory)

    clearText()
    typeText("2^0.5")
    equals()
    computeHistory.add(Pair("2^0.5", "Exponents must be whole numbers"))
    checkCorrectData(computeHistory)
}

/**
 * Check that all the expected information is displayed in the history.
 * Verifies that all items are displayed and the order of items is shuffled.
 *
 * @param computeHistory [List]<[TestCompItem]>: list of items in history
 */
private fun checkCorrectData(computeHistory: List<TestCompItem>) {
    var shuffled = false
    val historySize = computeHistory.size
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    repeat(repeatCount) {
        openHistoryFragment()

        // check that all items are displayed
        checkItemsDisplayed(computeHistory)

        // check that items are shuffled
        shuffled = shuffled || checkItemsShuffled(computeHistory)

        closeFragment()
    }

    // check shuffled after all repeats, because each repeat has some probability of correct order
    if (historySize > 1 && !shuffled) {
        throw AssertionError("History items should be shuffled in history randomness 1")
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
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    for (i in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(i))

            val start = computeHistory.subList(0, i)
            val end = ternaryIf(i == historySize - 1, emptyList(), computeHistory.subList(i + 1, historySize))
            val reducedHistory = start + end

            checkViewHolderInHistory(i, reducedHistory)

            return true
        } catch (_: Throwable) {}
    }

    return false
}
