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

    // TODO this isn't finished
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
        checkItemsDisplayed(computeHistory, 1)

        // check that items are shuffled
        val shuffledCheck = ShuffledCheckInfo({ it }, { item ->
            item as Pair<String, String>
            withHistoryItem(item.first, item.second)
        })
        shuffled = shuffled || checkItemsShuffled(computeHistory, listOf(shuffledCheck))

        closeFragment()
    }

    if (historySize > 1 && !shuffled) {
        throw AssertionError("History items should be shuffled in history randomness 1. History: $computeHistory")
    }
}
