package xyz.lbres.trickcalculator.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R

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
