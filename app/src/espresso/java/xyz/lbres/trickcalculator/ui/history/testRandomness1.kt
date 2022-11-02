package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.anyOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

private typealias ComputeItem = Pair<String, String>
private const val recyclerId = R.id.itemsRecycler

fun testRandomness1() {
    if (ProductFlavor.devMode) {
        hideDevToolsButton()
    }

    setHistoryRandomness(1)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val computeHistory: MutableList<ComputeItem> = mutableListOf()

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

private fun checkMatchesAny(position: Int, historyItems: List<ComputeItem>) {
    val matchers = historyItems.map {
        allOf(
            withChild(withText(it.first)),
            withChild(withChild(withText(it.second)))
        )
    }

    onView(withId(recyclerId)).perform(
        RecyclerViewActions.scrollToPosition<HistoryItemViewHolder>(position)
    )
    onView(withViewHolder(recyclerId, position))
        .check(matches(anyOf(matchers)))
}

private fun checkCorrectData(computeHistory: List<ComputeItem>) {
    var shuffled = false
    val historySize = computeHistory.size

    repeat(5) {
        openHistoryFragment()

        // check that all items are displayed
        checkItemsDisplayed(computeHistory)

        // check that items are shuffled
        if (historySize > 1 && !shuffled) {
            shuffled = shuffled || checkItemsShuffled(computeHistory)
        }

        closeFragment()
    }

    // check shuffled after all repeats, because each repeat has some probability of correct order
    if (historySize > 1 && !shuffled) {
        throw AssertionError("History items should be shuffled in history randomness 1")
    }
}

private fun checkItemsDisplayed(computeHistory: List<ComputeItem>) {
    val historySize = computeHistory.size

    computeHistory.forEach {
        var foundItem = false
        for (i in 0 until historySize) {
            onView(withId(recyclerId))
                .perform(RecyclerViewActions.scrollToPosition<HistoryItemViewHolder>(i))

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

private fun checkItemsShuffled(computeHistory: List<ComputeItem>): Boolean {
    val historySize = computeHistory.size

    for (i in 0 until historySize) {
        try {
            onView(withId(recyclerId))
                .perform(RecyclerViewActions.scrollToPosition<HistoryItemViewHolder>(i))

            val start = computeHistory.subList(0, i)
            val end = ternaryIf(i == historySize - 1, emptyList(), computeHistory.subList(i + 1, historySize))
            val reducedHistory = start + end

            checkMatchesAny(i, reducedHistory)

            return true
        } catch (_: Throwable) {}
    }

    return false
}
