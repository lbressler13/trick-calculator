package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.kotlinutils.pair.TypePair
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.matchesAtPosition
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testRandomness0() {
    setHistoryRandomness(0)
    toggleShuffleOperators()

    // no history
    checkNoHistoryMessageDisplayed()

    val history: MutableList<TypePair<String>> = mutableListOf()

    // one element
    history.add(generateTestItem("1+2") { "3" })
    openHistoryFragment()
    onView(withId(R.id.itemsRecycler)).check(matches(matchesAtPosition(0, withHistoryItem("1+2", "3"))))

    closeFragment()

    // several elements
    history.add(generateTestItem("-1/2", "3") { "2.5" })
    history.add(generateTestItem("+") { "Syntax error" })
    history.add(generateTestItem("1+2-2^3x1") { "-5" })
    history.add(generateTestItem("3", "-5x") { "-15" })
    history.add(generateTestItem("(1+2)(4-2)") { "6" })

    // duplicate element
    history.add(generateTestItem("(1+2)(4-2)") { "6" })

    // long decimal
    history.add(generateTestItem("0.123456") { "0.12346" })

    // long computation
    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateTestItem(longText) { longResult })

    // TODO use matcher
    // check all items displayed and ordered
    repeat(5) {
        openHistoryFragment()
        history.forEachIndexed { position, item ->
            val vhText = getViewHolderTextAtPosition(position)

            if (vhText != item) {
                throw AssertionError("Expected history item $item at position $position. Found $vhText.")
            }
        }

        try {
            val vhText = getViewHolderTextAtPosition(history.size)
            throw AssertionError("Expected history size ${history.size}. Retrieved item $vhText at position ${history.size}.")
        } catch (_: PerformException) {}

        closeFragment()
    }
}
