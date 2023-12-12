package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
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

fun testNoApplyDecimals() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    // no decimal
    repeat(3) {
        clearText()
        typeText("15-6x3")
        equals()
        mainText.check(matches(withText("[-3]")))
    }

    // with decimal
    repeat(3) {
        clearText()
        typeText("-.3")
        equals()
        mainText.check(matches(withText("[-3]")))

        clearText()
        typeText("1.0/10+1.3")
        equals()
        mainText.check(matches(withText("[14]")))
    }

    // decimal error
    clearText()
    typeText(".")
    equals()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // decimal in result
    clearText()
    typeText("1/2")
    equals()
    mainText.check(matches(withText("[0.5]")))

    typeText("x1")
    equals()
    mainText.check(matches(withText("[0.5]")))

    typeText(".4")
    equals()
    mainText.check(matches(withText("[2]")))
}

fun testNoApplyParentheses() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.applyParensSwitch)).perform(click())
    closeFragment()

    // no parens
    repeat(3) {
        clearText()
        typeText("1+2")
        equals()
        mainText.check(matches(withText("[3]")))
    }

    // with parens
    repeat(3) {
        clearText()
        typeText("(-4)3")
        equals()
        mainText.check(matches(withText("[-12]")))

        clearText()
        typeText("5^(3/(2+.5))")
        equals()
        mainText.check(matches(withText("[63]")))
    }

    // parens error
    clearText()
    typeText("()")
    equals()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // using result
    clearText()
    typeText("2")
    equals()
    typeText("(1+3)")
    equals()
    mainText.check(matches(withText("[5]")))
}

private fun runSingleClearErrorTest(text: String, error: String) {
    typeText(text)
    mainText.check(matches(withText(text)))
    errorText.check(matches(not(isDisplayed())))
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText(error)))
}
