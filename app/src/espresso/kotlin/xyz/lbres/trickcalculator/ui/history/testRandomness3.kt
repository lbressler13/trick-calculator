package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testRandomness3() {
    setHistoryRandomness(3)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("Error retrieving history")).check(matches(isDisplayed()))

    val history = TestHistory()

    // one element
    closeFragment()

    history.add(generateTestItem("400/5") { "80" })
    checkRandomness(history, 3)

    // several elements
    history.add(generateTestItem("15-2.5") { "12.5" })
    history.add(generateTestItem("(3+4)(5+2)") { "49" })

    // previously computed
    history.add(generateTestItem("+11", "49") { "60" })
    checkRandomness(history, 3)

    // duplicate element
    history.add(generateTestItem("(3+4)(5+2)") { "49" })

    // error
    history.add(generateTestItem("+") { "Syntax error" })
    history.add(generateTestItem("2^0.5") { "Exponents must be whole numbers" })

    // val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    // val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateTestItem(longText) { longResult })
    checkRandomness(history, 3)

    var noErrorRetrievingHistory = false
    var errorRetrievingHistory = false
    var errorGeneratedInHI = false

    repeatUntil(20, { errorRetrievingHistory && errorGeneratedInHI && noErrorRetrievingHistory }) {
        openHistoryFragment()

        var currentErrorGenerating = false

        try {
            onView(withText("Error retrieving history")).check(matches(isDisplayed()))
            errorRetrievingHistory = true
            currentErrorGenerating = true
        } catch (_: Throwable) {
            currentErrorGenerating = false
        }

        if (!currentErrorGenerating && history.checkErrorInDisplayedHistory()) {
            errorGeneratedInHI = true
        } else if (!currentErrorGenerating) {
            noErrorRetrievingHistory = true
        }

        closeFragment()
    }

    if (!noErrorRetrievingHistory) {
        throw AssertionError("Error retrieving history always displayed after 20 retries in history randomness 3.")
    }

    if (!errorRetrievingHistory) {
        throw AssertionError("Error retrieving history not displayed after 20 retries in history randomness 3.")
    }

    if (!errorGeneratedInHI) {
        throw AssertionError("No error in generated history item after 20 retries in history randomness 3.")
    }
}
