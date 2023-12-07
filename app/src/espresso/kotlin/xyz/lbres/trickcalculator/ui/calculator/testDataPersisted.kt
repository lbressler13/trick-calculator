package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.saveText
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.withSavedText
import xyz.lbres.trickcalculator.testutils.withAnyText
import xyz.lbres.trickcalculator.testutils.withEmptyString

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

fun testDataPersistedOnLeave() {
    // typed text
    leaveAndReturn()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(not(isDisplayed())))

    clearText()
    typeText("1")
    leaveAndReturn()
    mainText.check(matches(withText("1")))

    clearText()
    val longText = "1.003(8-9+2)/(1.5)^4"
    typeText(longText)
    leaveAndReturn()
    mainText.check(matches(withText(longText)))

    clearText()
    typeText("1+2")
    leaveAndReturn()
    typeText("x4")
    leaveAndReturn()
    mainText.check(matches(withText(("1+2x4"))))

    clearText()
    typeText("3+") // would give error
    leaveAndReturn()
    mainText.check(matches(withText(("3+"))))

    // computed value
    clearText()
    typeText("1.003")
    equals()
    leaveAndReturn()
    mainText.check(matches(withText(("[1.003]"))))

    clearText()
    typeText("(0000000123)")
    equals()
    leaveAndReturn()
    mainText.check(matches(withText(("[123]"))))

    var options = setOf(5, -3, 4, 0.25)
    checkMainTextPersisted("1+4", options)

    options = setOf(
        3, 22, 3.25, // + = +
        1, -18, 0.75, // + = -
        14, 6, 2.5, // + = x
        4.4, -3.6, 1.6 // + = /
    )
    checkMainTextPersisted("2+5-4", options)

    options = setOf(
        10, -21.5, 11.875, -5, -21.875, -5.75, // + = +
        10, 41.5, 8.125, 25, 41.875, 25.75, // + = -
        8.875, -9, 1.125, 19, -12.75, 15.25, // + = *
        -8, 12, 48, 28, 66, 94 // + = /
    )
    checkMainTextPersisted("10-.5x4/2+16", options)

    // error
    clearText()
    typeText("+")
    equals()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))
    leaveAndReturn()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))
    mainText.check(matches(withText("+")))

    clearText()
    openSettingsFragment()
    onView(withId(R.id.clearOnErrorSwitch)).perform(click())
    closeFragment()
    typeText("1+1..0")
    equals()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))
    mainText.check(matches(withText("")))
    leaveAndReturn()
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))
    mainText.check(matches(withText("")))
}

private fun checkMainTextPersisted(text: String, options: Set<Number>) {
    clearText()
    typeText(text)
    equals()
    mainText.check(matches(withAnyText(optionsOf(options))))
    mainText.perform(saveText())
    leaveAndReturn()
    mainText.check(matches(withSavedText()))
}
