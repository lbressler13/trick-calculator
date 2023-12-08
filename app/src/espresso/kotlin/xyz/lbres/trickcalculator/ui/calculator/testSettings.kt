package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.typeText

fun testRandomizedSigns() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.randomizeSignsSwitch))
        .perform(click())
        .check(matches(isChecked()))
    closeFragment()

    var options: Set<Number> = setOf(5, -5)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10) {
        typeText("5")
        equals()
    }
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10) {
        typeText("-5")
        equals()
    }

    options = setOf(3, -1, -3, 1)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10) {
        typeText("1+2")
        equals()
    }
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10) {
        typeText("-1-2")
        equals()
    }

    options = setOf(0.6, -0.6)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10) {
        typeText("-.75/1.25")
        equals()
    }

    options = setOf(0.25, 1.75, -0.25, -1.75)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10) {
        typeText("-1+3/4")
        equals()
    }

    options = setOf(6, 18, -6, -18)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10) {
        typeText("3")
        equals()
        typeText("(4+2)")
        equals()
    }
}
