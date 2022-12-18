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

private const val recyclerId = R.id.itemsRecycler

fun testRandomness0() {
    setHistoryRandomness(0)
    toggleShuffleOperators()

    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    closeFragment()
    val history = TestHistory()

    // one element
    history.add(generateHI("1+2") { "3" })
    openHistoryFragment()
    onView(withViewHolder(recyclerId, 0)).check(matches(withHistoryItem("1+2", "3")))

    closeFragment()

    // several elements
    history.add(generateHI("-1/2", "3") { "2.5" })
    history.add(generateHI("+") { "Syntax error" })
    history.add(generateHI("1+2-2^3x1") { "-5" })
    history.add(generateHI("3", "-5x") { "-15" })
    history.add(generateHI("(1+2)(4-2)") { "6" })

    // duplicate element
    history.add(generateHI("(1+2)(4-2)") { "6" })

    // long decimal
    history.add(generateHI("0.123456") { "0.12346" })

    // long computation
    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateHI(longText) { longResult })

    // check all items displayed
    openHistoryFragment()
    history.checkAllDisplayed(0)
    closeFragment()

    // test that order doesn't change
    repeat(5) {
        openHistoryFragment()
        history.checkDisplayOrdered()
        closeFragment()
    }
}
