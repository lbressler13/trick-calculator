package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.testutils.withAnyText
import xyz.lbres.trickcalculator.testutils.withEmptyString

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

fun testClearOnError() {
    openSettingsFragment()
    onView(withId(R.id.clearOnErrorSwitch)).perform(click())
    closeFragment()

    runSingleClearErrorTest("+", "Error: Syntax error")
    runSingleClearErrorTest("0.", "Error: Syntax error")

    typeText("0.")
    errorText.check(isNotPresented())
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // previously computed
    typeText("3")
    equals()
    typeText(".")
    errorText.check(isNotPresented())
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // valid text not cleared
    typeText("3/4")
    equals()
    val options = setOf(7, -1, 12, 0.75)
    mainText.check(matches(withAnyText(resultsOf(options))))

    // divide by zero
    toggleShuffleOperators()
    clearText()
    runSingleClearErrorTest("1/0", "Error: Divide by zero")
}

fun testRandomizeSigns() {
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

fun testUnshuffleOperators() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    closeFragment()

    repeat(5) {
        clearText()
        typeText("-.2+5^2/(8-4x3)")
        equals()
        mainText.check(matches(withText("[-6.45]")))
    }

    clearText()
    typeText("1+2")
    equals()
    typeText("6")
    equals()
    mainText.check(matches(withText("[18]")))

    repeat(5) {
        clearText()
        typeText("1/0")
        equals()
        errorText.check(matches(isDisplayedWithText("Error: Divide by zero")))
    }
}

private fun runSingleClearErrorTest(text: String, error: String) {
    typeText(text)
    mainText.check(matches(withText(text)))
    errorText.check(matches(not(isDisplayed())))
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText(error)))
}
