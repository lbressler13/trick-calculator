package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

private const val recyclerId = R.id.itemsRecycler

fun testRandomness2() {
    setHistoryRandomness(2)
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
 * Verifies that all items are displayed, the order of items is shuffled, and the pairs of items are shuffled.
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
private fun checkCorrectData(computeHistory: TestHistory) {
    var shuffledOrder = false
    var shuffledPairs = false
    val historySize = computeHistory.size

    // additional repeats for 2 items because of higher probability of 2 items being ordered
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    repeat(repeatCount) {
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
        throw AssertionError("History items and pairs should be shuffled in history randomness 2. History: $computeHistory")
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
        var foundComputation = false
        var foundResult = false

        for (i in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(i))

            try {
                onView(withText(it.first)).check(matches(isDisplayed()))
                foundComputation = true
            } catch (_: Throwable) {}

            try {
                onView(withText(it.second)).check(matches(isDisplayed()))
                foundResult = true
            } catch (_: Throwable) {}

            if (foundComputation && foundResult) {
                break
            }
        }

        if (!foundComputation || !foundResult) {
            throw AssertionError("ViewHolders with text $it not found")
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

    var computationsShuffled = false
    var resultsShuffled = false

    // check that compuataion strings are shuffled
    val compStrings = computeHistory.map { it.first }
    for (i in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(i))

            val start = compStrings.subList(0, i)
            val end = ternaryIf(i == historySize - 1, emptyList(), compStrings.subList(i + 1, historySize))
            val reducedCompStrings = start + end

            val matcher = anyOf(reducedCompStrings.map { withChild(withText(it)) })
            onView(withId(recyclerId)).perform(scrollToPosition(i))
            onView(withViewHolder(recyclerId, i)).check(matches(matcher))

            computationsShuffled = true
            break
        } catch (_: Throwable) {}
    }

    // check that results are shuffled
    val resultStrings = computeHistory.map { it.second }
    for (i in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(i))

            val start = resultStrings.subList(0, i)
            val end = ternaryIf(i == historySize - 1, emptyList(), resultStrings.subList(i + 1, historySize))
            val reducedResultStrings = start + end

            val matcher = anyOf(reducedResultStrings.map { withChild(withChild(withText(it))) })
            onView(withId(recyclerId)).perform(scrollToPosition(i))
            onView(withViewHolder(recyclerId, i)).check(matches(matcher))

            resultsShuffled = true
            break
        } catch (_: Throwable) {}
    }

    return computationsShuffled && resultsShuffled
}

/**
 * Determine if the pairs of values have been shuffled
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @return [Boolean]: true if at least one viewholder contains a mismatched computation/result, false if all pairs match
 */
private fun checkPairsShuffled(computeHistory: TestHistory): Boolean {
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    val computeTextMatcher = anyOf(computeHistory.map { withChild(withText(it.first)) })
    val resultTextMatcher = anyOf(computeHistory.map { withChild(withChild(withText(it.second))) })

    for (i in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(i))
            // both values are part of the history
            onView(withViewHolder(recyclerId, i)).check(matches(allOf(computeTextMatcher, resultTextMatcher)))
            // the values do not match a specific item in the history
            checkViewHolderNotInHistory(i, computeHistory)

            return true
        } catch (_: Throwable) {}
    }

    return false
}
