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
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.*
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
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
    // TODO test not in order
    var shuffled = false
    val historySize = computeHistory.size

    repeat(5) {
        val itemOrder = IntArray(historySize)

        openHistoryFragment()
        computeHistory.forEachIndexed { itemIndex, item ->
            var foundIndex = -1
            for (i in 0 until historySize) {
                onView(withId(recyclerId))
                    .perform(RecyclerViewActions.scrollToPosition<HistoryItemViewHolder>(i))

                try {
                    onView(withText(item.first)).check(matches(isDisplayed()))
                    onView(withText(item.second)).check(matches(isDisplayed()))
                    foundIndex = i
                    break
                } catch (_: Throwable) {}
            }

            if (foundIndex == -1) {
                throw AssertionError("ViewHolder with text $it not found")
            } else {
                itemOrder[itemIndex] = foundIndex
            }
        }

        for (i in computeHistory.indices) {
            checkMatchesAny(i, computeHistory)
        }
        closeFragment()
    }
}
