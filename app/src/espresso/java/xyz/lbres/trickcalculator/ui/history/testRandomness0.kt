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
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

private const val recyclerId = R.id.itemsRecycler

fun testRandomness0() {
    setHistoryRandomness(0)
    toggleShuffleOperators()

    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    closeFragment()

    // one element
    typeText("1+2")
    equals()
    openHistoryFragment()
    onView(withViewHolder(recyclerId, 0))
        .check(matches(withHistoryItem("1+2", "3")))

    closeFragment()

    // several elements
    typeText("-1/2")
    equals()
    clearText()
    typeText("+")
    equals()
    clearText()
    typeText("1+2-2^3x1")
    equals()
    clearText()
    typeText("(1+2)(4-2)")
    equals()
    clearText()

    // duplicate element
    typeText("(1+2)(4-2)")
    equals()
    clearText()

    // test that order doesn't change
    repeat(5) {
        openHistoryFragment()

        onView(withViewHolder(recyclerId, 0))
            .check(matches(withHistoryItem("1+2", "3")))

        onView(withViewHolder(recyclerId, 1))
            .check(matches(withHistoryItem("3-1/2", "2.5")))

        onView(withViewHolder(recyclerId, 2))
            .check(matches(withHistoryItem("+", "Syntax error")))

        onView(withViewHolder(recyclerId, 3))
            .check(matches(withHistoryItem("1+2-2^3x1", "-5")))

        onView(withViewHolder(recyclerId, 4))
            .check(matches(withHistoryItem("(1+2)(4-2)", "6")))

        onView(withViewHolder(recyclerId, 5))
            .check(matches(withHistoryItem("(1+2)(4-2)", "6")))

        closeFragment()
    }
}
