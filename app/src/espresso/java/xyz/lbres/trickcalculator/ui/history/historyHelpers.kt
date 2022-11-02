package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment

/**
 * Update the history randomness setting.
 * Must be called from main screen, and returns to main screen after updating setting.
 *
 * @param randomness [Int]: new value of randomness setting
 */
fun setHistoryRandomness(randomness: Int) {
    openSettingsFragment()

    when (randomness) {
        0 -> onView(withId(R.id.historyButton0)).perform(click())
        1 -> onView(withId(R.id.historyButton1)).perform(click())
        2 -> onView(withId(R.id.historyButton2)).perform(click())
        3 -> onView(withId(R.id.historyButton3)).perform(click())
    }

    closeFragment()
}
