package xyz.lbres.trickcalculator.testutils

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick

/**
 * Click close button
 */
fun closeFragment() {
    onView(withId(R.id.closeButton)).perform(forceClick())
}

/**
 * Open attributions fragment from main screen
 */
fun openAttributionsFragment() {
    onView(withId(R.id.infoButton)).perform(click())
}

/**
 * Open settings fragment from main screen
 */
fun openSettingsFragment() {
    onView(withId(R.id.infoButton)).perform(click())
    onView(withId(R.id.title)).perform(click())
}

/**
 * Open history fragment from main screen
 */
fun openHistoryFragment() {
    onView(withId(R.id.historyButton)).perform(click())
}

/**
 * Toggle shuffle operators setting from main screen
 */
fun toggleShuffleOperators() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    closeFragment()
}

/**
 * Hide the dev tools button for 60 seconds to avoid interfering with tests when expanding/collapsing attributions.
 * Will cause error if called in non-dev variant.
 */
fun hideDevToolsButton() {
    onView(withId(R.id.devToolsButton)).perform(click())
    onView(withId(R.id.devToolsTimeSpinner)).perform(click())
    onData(Matchers.`is`(instanceOf(String::class.java)))
        .inRoot(isPlatformPopup())
        .atPosition(3)
        .perform(click())

    onView(withId(R.id.devToolsTimeSpinner)).check(
        matches(withSpinnerText("60000ms"))
    )
    onView(withId(R.id.hideDevToolsButton)).perform(click())

    onView(withText("Developer Tools")).check(doesNotExist())
    onView(withId(R.id.devToolsButton)).check(matches(not(isDisplayed())))
}
