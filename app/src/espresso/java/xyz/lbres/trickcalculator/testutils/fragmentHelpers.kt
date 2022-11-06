package xyz.lbres.trickcalculator.testutils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick

/**
 * Click close button
 */
fun closeFragment() {
    onView(withId(R.id.closeButton)).perform(forceClick())
}

/**
 * Open attributions fragment from calculator screen
 */
fun openAttributionsFragment() {
    onView(withId(R.id.infoButton)).perform(click())
}

/**
 * Open settings fragment from calculator screen
 */
fun openSettingsFragment() {
    onView(withId(R.id.infoButton)).perform(click())
    onView(withId(R.id.title)).perform(click())
}

/**
 * Open history fragment from calculator screen
 */
fun openHistoryFragment() {
    onView(withId(R.id.historyButton)).perform(click())
}

/**
 * Toggle shuffle operators setting from calculator screen
 */
fun toggleShuffleOperators() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    closeFragment()
}
