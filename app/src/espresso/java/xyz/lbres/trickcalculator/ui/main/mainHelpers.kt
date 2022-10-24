package xyz.lbres.trickcalculator.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.anyOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick

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
            onView(withId(buttonId)).perform(click())
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
    mainText.check(matches(withText(newText)))
}

/**
 * Click equals button
 */
fun equals() {
    onView(withId(R.id.equalsButton)).perform(click())
}

/**
 * Check that the main textview matches a specific value
 *
 * @param text [String]: expected text
 */
fun checkMainTextMatches(text: String) {
    mainText.check(matches(withText(text)))
}

/**
 * Check that the main textview matches one out of a list of options
 *
 * @param options [Set]<[String]>: list of valid values for main textview
 */
fun checkMainTextMatchesAny(options: Set<String>) {
    val matchers = options.map { withText(it) }.toMutableList()
    mainText.check(matches(anyOf(matchers)))
}

/**
 * Leave main fragment by opening attributions fragment, then return by closing attributions fragment
 */
fun leaveAndReturn() {
    onView(withId(R.id.infoButton)).perform(click())
    onView(withId(R.id.closeButton)).perform(forceClick())
}

/**
 * Open settings fragment through attributions fragment
 */
fun openSettingsFragment() {
    onView(withId(R.id.infoButton)).perform(click())
    onView(withId(R.id.title)).perform(click())
}

/**
 * Close settings fragment when open
 */
fun closeSettingsFragment() {
    onView(withId(R.id.closeButton)).perform(forceClick())
}
