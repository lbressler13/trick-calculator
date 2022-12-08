package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
    val computeHistory = mutableListOf<TestHI>()

    // one element
    typeText("1+2")
    equals()
    computeHistory.add(TestHI("1+2", "3"))
    openHistoryFragment()
    onView(withViewHolder(recyclerId, 0)).check(matches(withHistoryItem("1+2", "3")))

    closeFragment()

    // several elements
    typeText("-1/2")
    equals()
    computeHistory.add(TestHI("3-1/2", "2.5"))
    clearText()
    typeText("+")
    equals()
    computeHistory.add(TestHI("+", "Syntax error"))
    clearText()
    typeText("1+2-2^3x1")
    equals()
    computeHistory.add(TestHI("1+2-2^3x1", "-5"))
    typeText("3")
    equals()
    computeHistory.add(TestHI("-5x3", "-15"))
    clearText()
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(TestHI("(1+2)(4-2)", "6"))
    clearText()

    // duplicate element
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(TestHI("(1+2)(4-2)", "6"))
    clearText()

    // long decimal
    typeText("0.123456")
    equals()
    computeHistory.add(TestHI("0.123456", "0.12346"))
    clearText()

    // long computation
    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    typeText(longText)
    equals()
    computeHistory.add(TestHI(longText, longResult))

    // check all items displayed
    val checker = HistoryChecker(computeHistory)
    openHistoryFragment()
    checker.checkDisplayed(0)
    closeFragment()

    // test that order doesn't change
    repeat(5) {
        openHistoryFragment()
        checker.checkOrdered()
        closeFragment()
    }
}
