package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.withEmptyString

private val mainText = onView(withId(R.id.mainText))

fun testEqualsWithNoOperators() {
    mainText.check(matches(withEmptyString()))
    equals()
    mainText.check(matches(withEmptyString()))

    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))

    clearText()
    typeText("-5")
    equals()
    mainText.check(matches(withText("[-5]")))

    clearText()
    typeText("9.876")
    equals()
    mainText.check(matches(withText("[9.876]")))

    clearText()
    typeText("000.05")
    equals()
    mainText.check(matches(withText("[0.05]")))

    clearText()
    typeText("7.0")
    equals()
    mainText.check(matches(withText("[7]")))
}

fun testEqualsWithSingleOperatorType() {
    var options = setOf(1, -3, -2, -0.5)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, "-1+2")

    // exponent
    options = setOf(2, 6, 8, 16)
    checkMainTextMatchesMultiple(resultsOf(options), 4, 4, 40, "4^2")

    // decimal
    options = setOf(5.5, -2.5, 6, 0.375)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, "1.5x4")

    // repeat single operator
    options = setOf(11, -7, 40, 0.1)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, "2x5x4")
}

fun testEqualsWithMultipleOperatorTypes() {
    var options = setOf(
        3, 22, 3.25, // + = +
        1, -18, 0.75, // + = -
        14, 6, 2.5, // + = x
        4.4, -3.6, 1.6 // + = /
    )
    checkMainTextMatchesMultiple(resultsOf(options), 4, 4, 20, "2+5-4")

    // exponent
    options = setOf(
        3, 13, 5.5, 21, // + = +
        7, -3, 4.5, -11, // + = -
        14, 6, 2.5, 80, // + = x
        6.5, -1.5, 10, 0.3125, // + = /
        29, 21, 100, 6.25 // + = ^
    )
    checkMainTextMatchesMultiple(resultsOf(options), 5, 5, 20, "5^2-4")

    // decimal
    options = setOf(
        10, -21.5, 11.875, -5, -21.875, -5.75, // + = +
        10, 41.5, 8.125, 25, 41.875, 25.75, // + = -
        8.875, -9, 1.125, 19, -12.75, 15.25, // + = *
        -8, 12, 48, 28, 66, 94 // + = /
    )
    val text = "10-.5x4/2+16"
    checkMainTextMatchesMultiple(resultsOf(options), 3, 5, 10, text)
}

fun testEqualsWithParentheses() {
    typeText("(000.05)")
    equals()
    mainText.check(matches(withText("[0.05]")))

    clearText()
    typeText("(-5)")
    equals()
    mainText.check(matches(withText("[-5]")))

    // exponent
    val options = setOf(
        3, 22, 3.25, // + = +
        -7, -18, 0.75, // + = -
        18, 2, 2.5, // + = x
        0.22222222, 2, 0.1 // + = /
    )
    checkMainTextMatchesMultiple(resultsOf(options), 3, 5, 10, "2(5-4)")
}

fun testEqualsWithPreviouslyComputed() {
    var options = setOf(125, 121, 246, 61.5)
    var texts = listOf("123", "+2")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, texts)

    texts = listOf("123", "2") // add times between values
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, texts)

    options = setOf(0, -1, -0.25, -1)
    texts = listOf("-.5", ".5")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, texts)
}

fun testEqualsWithError() {
    val errorText = onView(withId(R.id.errorText))
    val syntaxError = "Error: Syntax error"

    // syntax errors
    typeText("+")
    equals()
    errorText.check(matches(isDisplayedWithText(syntaxError)))

    clearText()
    typeText("1+")
    equals()
    errorText.check(matches(isDisplayedWithText(syntaxError)))
    typeText("2")
    equals()
    errorText.check(matches(not(isDisplayed()))) // error text goes away with valid text

    clearText()
    typeText("()")
    equals()
    errorText.check(matches(isDisplayedWithText(syntaxError)))

    // divide by zero
    repeatUntilDivideByZero("1/0", iterations = 10)
}
