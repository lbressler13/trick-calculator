package xyz.lbres.trickcalculator.testutils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R

/**
 * Alternate to standard [repeat] function, to allow breaking out of the loop.
 *
 * @param maxIterations [Int]: number of times to repeat code without breaking loop
 * @param breakCondition () -> [Boolean]: condition to break out of loop
 * @param function () -> [Unit]: code to execute repeatedly
 */
fun repeatUntil(maxIterations: Int, breakCondition: () -> Boolean, function: () -> Unit) {
    var repeats = 0
    while (repeats < maxIterations && !breakCondition()) {
        function()
        repeats++
    }
}

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
 * Click equals button
 */
fun equals() {
    onView(withId(R.id.equalsButton)).perform(click())
}
