package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

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

/**
 * Check that all the expected information is displayed in the history.
 * Verifies that all items are displayed, the order of items is shuffled, and the pairs of items are shuffled.
 *
 * @param computeHistory [TestHistory]: list of items in history
 */
private fun checkCorrectData(computeHistory: TestHistory) {
    var orderShuffled = false
    var pairsShuffled = false
    val historySize = computeHistory.size

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    repeatUntil(repeatCount, { orderShuffled && pairsShuffled }) {
        openHistoryFragment()

        // check that all items are displayed
        checkItemsDisplayed(computeHistory, 2)

        // order of computation strings is shuffled
        val shuffledComputationCheck = ShuffledCheckInfo(
            { it.map { it.first } },
            { computation -> withChild(withText(computation as String)) }
        )
        // order of result strings is shuffled
        val shuffledResultCheck = ShuffledCheckInfo(
            { it.map { it.second } },
            { result -> withChild(withChild(withText(result as String))) }
        )

        orderShuffled = orderShuffled || checkItemsShuffled(computeHistory, listOf(shuffledComputationCheck, shuffledResultCheck))
        pairsShuffled = pairsShuffled || checkPairsShuffled(computeHistory)

        closeFragment()
    }

    if (historySize > 1 && !(orderShuffled && pairsShuffled)) {
        throw AssertionError("History items and pairs should be shuffled in history randomness 2. History: $computeHistory")
    }
}

/**
 * Determine if the computation/result matches have been shuffled
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @return [Boolean]: true if at least one viewholder contains a mismatched computation/result, false if all pairs match
 */
private fun checkPairsShuffled(computeHistory: TestHistory): Boolean {
    val recyclerId = R.id.itemsRecycler
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    val computeTextMatcher = anyOf(computeHistory.map { withChild(withText(it.first)) })
    val resultTextMatcher = anyOf(computeHistory.map { withChild(withChild(withText(it.second))) })
    val historyMatcher = anyOf(computeHistory.map { withHistoryItem(it.first, it.second) })

    for (position in 0 until historySize) {
        try {
            onView(withId(recyclerId)).perform(scrollToPosition(position))
            // both values are part of the history, but value doesn't match a real item
            onView(withViewHolder(recyclerId, position)).check(
                matches(allOf(computeTextMatcher, resultTextMatcher, not(historyMatcher)))
            )

            return true
        } catch (_: Throwable) {}
    }

    return false
}
