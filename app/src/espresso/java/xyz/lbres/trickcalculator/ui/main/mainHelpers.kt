package xyz.lbres.trickcalculator.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers
import xyz.lbres.trickcalculator.R

private val mainText = onView(withId(R.id.mainText))

/**
 * Type text in main textview by clicking appropriate button for each character.
 *
 * @param text [String]: text to type
 */
fun typeText(text: String) {
    for (c in text) {
        val buttonId = when (c) {
            '1' -> R.id.oneButton
            '2' -> R.id.twoButton
            '3' -> R.id.threeButton
            '4' -> R.id.fourButton
            '5' -> R.id.fiveButton
            '6' -> R.id.sixButton
            '7' -> R.id.sevenButton
            '8' -> R.id.eightButton
            '9' -> R.id.nineButton
            '0' -> R.id.zeroButton
            '+' -> R.id.plusButton
            '-' -> R.id.minusButton
            'x' -> R.id.timesButton
            '/' -> R.id.divideButton
            '^' -> R.id.expButton
            '(' -> R.id.lparenButton
            ')' -> R.id.rparenButton
            '.' -> R.id.decimalButton
            else -> null
        }

        if (buttonId != null) {
            onView(withId(buttonId)).perform(ViewActions.click())
        }
    }
}

/**
 * Click clear button
 */
fun clearText() {
    onView(withId(R.id.clearButton)).perform(click())
}

/**
 * Click backspace button
 */
fun backspace() {
    onView(withId(R.id.backspaceButton)).perform(click())
}

/**
 * Click backspace button and validate the result
 *
 * @param newText [String]: expected text in main textview after clicking backspace
 */
fun backspaceTo(newText: String) {
    backspace()
    mainText.check(ViewAssertions.matches(withText(newText)))
}

/**
 * Click equals button
 */
fun equals() {
    onView(withId(R.id.equalsButton)).perform(scrollTo(), click())
}

/**
 * Check that the main textview matches one out of a list of options
 *
 * @param options [Set]<[String]>: list of valid values for main textview
 */
fun checkMainTextOptions(options: Set<String>) {
    val matchers = options.map { ViewMatchers.withText(it) }.toMutableList()
    mainText.check(ViewAssertions.matches(Matchers.anyOf(matchers)))
}
