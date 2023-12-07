package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.withAnyText
import xyz.lbres.trickcalculator.testutils.withEmptyString
import xyz.lbres.trickcalculator.testutils.withNonEmptyString

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

fun testNumberAndOperatorButtons() {
    mainText.check(matches(withEmptyString()))

    // digits
    onView(withId(R.id.oneButton)).perform(click())
    mainText.check(matches(withText("1")))

    onView(withId(R.id.twoButton)).perform(click())
    mainText.check(matches(withText("12")))

    onView(withId(R.id.threeButton)).perform(click())
    mainText.check(matches(withText("123")))

    onView(withId(R.id.fourButton)).perform(click())
    mainText.check(matches(withText("1234")))

    onView(withId(R.id.fiveButton)).perform(click())
    mainText.check(matches(withText("12345")))

    onView(withId(R.id.sixButton)).perform(click())
    mainText.check(matches(withText("123456")))

    onView(withId(R.id.sevenButton)).perform(click())
    mainText.check(matches(withText("1234567")))

    onView(withId(R.id.eightButton)).perform(click())
    mainText.check(matches(withText("12345678")))

    onView(withId(R.id.nineButton)).perform(click())
    mainText.check(matches(withText("123456789")))

    onView(withId(R.id.zeroButton)).perform(click())
    mainText.check(matches(withText("1234567890")))

    // operators
    onView(withId(R.id.plusButton)).perform(click())
    mainText.check(matches(withText("1234567890+")))

    onView(withId(R.id.minusButton)).perform(click())
    mainText.check(matches(withText("1234567890+-")))

    onView(withId(R.id.timesButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x")))

    onView(withId(R.id.divideButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x/")))

    onView(withId(R.id.expButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x/^")))

    // parentheses
    onView(withId(R.id.lparenButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x/^(")))

    onView(withId(R.id.rparenButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x/^()")))

    // decimal
    onView(withId(R.id.decimalButton)).perform(click())
    mainText.check(matches(withText("1234567890+-x/^().")))
}

fun testClearButton() {
    val clearButton = onView(withId(R.id.clearButton))

    // empty
    mainText.check(matches(withEmptyString()))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))

    // with text
    typeText("123")
    mainText.check(matches(withNonEmptyString()))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))

    typeText("(.7-45+55/5)^3(4.3)")
    mainText.check(matches(withNonEmptyString()))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))

    // with computed
    typeText("123")
    equals()
    mainText.check(matches(withNonEmptyString()))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))

    typeText("12")
    equals()
    typeText("x4-3")
    mainText.check(matches((withNonEmptyString())))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))

    // with error
    typeText("100..3")
    equals()
    mainText.check(matches(withNonEmptyString()))
    errorText.check(matches(allOf(isDisplayed(), withNonEmptyString())))
    clearButton.perform(click())
    mainText.check(matches(withEmptyString()))
    onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
}

fun testBackspaceButton() {
    val backspaceButton = onView(withId(R.id.backspaceButton))

    // blank
    mainText.check(matches(withText("")))
    backspaceAndValidate("")
    backspaceButton.perform(click())
    mainText.check(matches(withEmptyString()))

    // with text
    clearText()
    typeText("1")
    backspaceAndValidate("")

    clearText()
    typeText("123")
    backspaceAndValidate("12")

    clearText()
    typeText("123+0.1")
    backspaceAndValidate("123+0.")
    backspaceAndValidate("123+0")
    backspaceAndValidate("123+")
    backspaceAndValidate("123")
    backspaceAndValidate("12")
    backspaceAndValidate("1")
    backspaceAndValidate("")

    // with computed value
    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))
    backspaceAndValidate("")

    clearText()
    typeText("123")
    equals()
    typeText("+5")
    mainText.check(matches(withText("[123]+5")))
    backspaceAndValidate("[123]+")
    backspaceAndValidate("[123]")
    backspaceAndValidate("")

    // with error
    clearText()
    typeText("+")
    equals()
    errorText.check(matches(isDisplayed()))
    backspaceAndValidate("")
    errorText.check(matches(not(isDisplayed())))

    clearText()
    typeText("1+2.")
    equals()
    errorText.check(matches(isDisplayed()))
    backspaceAndValidate("1+2")
    errorText.check(matches(not(isDisplayed())))
    equals()
    val options = setOf("[3]", "[-1]", "[2]", "[0.5]")
    mainText.check(matches(withAnyText(options)))
}

/**
 * Click backspace button and validate the result
 *
 * @param newText [String]: expected text in main textview after clicking backspace
 */
private fun backspaceAndValidate(newText: String) {
    onView(withId(R.id.backspaceButton)).perform(click())
    mainText.check(matches(withText(newText)))
}
