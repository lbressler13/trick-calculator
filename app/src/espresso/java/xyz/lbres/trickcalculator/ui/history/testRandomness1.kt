package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.clearSavedTextAtPosition
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.saveTextAtPosition
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.withSavedTextAtPosition
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

private const val recyclerId = R.id.itemsRecycler

fun testRandomness1() {
    setHistoryRandomness(1)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val computeHistory: MutableList<Pair<String, String>> = mutableListOf()

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

    // duplicate element
    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(Pair("(3-4)(5+2)", "-7"))
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

fun testRandomness1Reshuffled() {
    setHistoryRandomness(1)
    toggleShuffleOperators()

    val computeHistory: MutableList<Pair<String, String>> = mutableListOf()

    typeText("400/5")
    equals()
    computeHistory.add(Pair("400/5", "80"))

    clearText()
    typeText("15-2.5")
    equals()
    computeHistory.add(Pair("15-2.5", "12.5"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(Pair("(3-4)(5+2)", "-7"))

    typeText("+11")
    equals()
    computeHistory.add(Pair("-7+11", "4"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(Pair("(3-4)(5+2)", "-7"))

    clearText()
    typeText("+")
    equals()
    computeHistory.add(Pair("+", "Syntax error"))

    clearText()
    typeText("2^0.5")
    equals()
    computeHistory.add(Pair("2^0.5", "Exponents must be whole numbers"))

    checkCorrectData(computeHistory)
    val historySize = computeHistory.size
    openHistoryFragment()

    for (position in 0 until historySize) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).perform(saveTextAtPosition(position, R.id.computeText))
    }

    closeFragment()

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(historySize == 2, 10, 5)
    var repeats = 0
    var shuffled = false

    while (repeats < repeatCount && !shuffled) {
        openHistoryFragment()

        for (position in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            try {
                onView(withViewHolder(recyclerId, position)).check(matches(not(withSavedTextAtPosition(position, R.id.computeText))))
                shuffled = true
            } catch (_: Throwable) {}
        }

        repeats++
        closeFragment()
    }

    openHistoryFragment()
    for (position in 0 until historySize) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).perform(clearSavedTextAtPosition(position, R.id.computeText))
    }

}

/**
 * Check that all the expected information is displayed in the history.
 * Verifies that all items are displayed and the order of items is shuffled.
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
private fun checkCorrectData(computeHistory: TestHistory) {
    var shuffled = false
    val historySize = computeHistory.size

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    repeat(repeatCount) {
        openHistoryFragment()

        // check that all items are displayed
        checkItemsDisplayed(computeHistory)

        // check that items are shuffled
        shuffled = shuffled || checkItemsShuffled(computeHistory)

        closeFragment()
    }

    if (historySize > 1 && !shuffled) {
        throw AssertionError("History items should be shuffled in history randomness 1. History: $computeHistory")
    }
}

/**
 * Check that all history items are visible on the screen
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
private fun checkItemsDisplayed(computeHistory: TestHistory) {
    val historySize = computeHistory.size

    computeHistory.forEach {
        var foundItem = false
        for (position in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            try {
                onView(withViewHolder(recyclerId, position))
                    .check(matches(withHistoryItem(it.first, it.second)))
                foundItem = true
                break
            } catch (_: Throwable) {}
        }

        if (!foundItem) {
            throw AssertionError("ViewHolder with text $it not found. History: $computeHistory")
        }
    }
}

/**
 * Determine if the order of the history items is shuffled
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @return [Boolean]: true if at least one item's position does not match its position in the history, false if all items are in order
 */
private fun checkItemsShuffled(computeHistory: TestHistory): Boolean {
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    for (position in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            val start = computeHistory.subList(0, position)
            val end = ternaryIf(
                position == historySize - 1,
                emptyList(),
                computeHistory.subList(position + 1, historySize)
            )
            val reducedHistory = start + end

            checkViewHolderInHistory(position, reducedHistory)

            return true
        } catch (_: Throwable) {}
    }

    return false
}
