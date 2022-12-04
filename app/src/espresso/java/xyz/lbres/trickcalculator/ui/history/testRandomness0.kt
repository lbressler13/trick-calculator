package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

private const val recyclerId = R.id.itemsRecycler

fun testRandomness0() {
    setHistoryRandomness(0)
    toggleShuffleOperators()

    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    closeFragment()
    val computeHistory = mutableListOf<Pair<String, String>>()

    // one element
    typeText("1+2")
    equals()
    computeHistory.add(Pair("1+2", "3"))
    openHistoryFragment()
    onView(withViewHolder(recyclerId, 0))
        .check(matches(withHistoryItem("1+2", "3")))

    closeFragment()

    // several elements
    typeText("-1/2")
    equals()
    computeHistory.add(Pair("3-1/2", "2.5"))
    clearText()
    typeText("+")
    equals()
    computeHistory.add(Pair("+", "Syntax error"))
    clearText()
    typeText("1+2-2^3x1")
    equals()
    computeHistory.add(Pair("1+2-2^3x1", "-5"))
    clearText()
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(Pair("(1+2)(4-2)", "6"))
    clearText()

    // duplicate element
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(Pair("(1+2)(4-2)", "6"))
    clearText()

    // test that order doesn't change
    val checker = HistoryChecker(computeHistory)
    repeat(5) {
        openHistoryFragment()
        checker.checkDisplayed(0)
        checker.checkOrdered()
        closeFragment()
    }
}
