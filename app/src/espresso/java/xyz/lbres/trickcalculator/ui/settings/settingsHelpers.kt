package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick

/**
 * Open settings fragment through attributions fragment
 */
fun openFragment() {
    val infoButton = onView(withId(R.id.infoButton))
    infoButton.check(matches(isDisplayed()))
    infoButton.perform(click())
    onView(withId(R.id.title)).perform(click())
}

/**
 * Click close button
 */
fun closeFragment() {
    onView(withId(R.id.closeButton)).perform(forceClick())
}

/**
 * Wrapper function to create a [SettingsModifiedViewAssertion]
 */
fun settingsRandomized() = SettingsModifiedViewAssertion()

/**
 * Check that settings match initial settings.
 * Optionally checks switch to show/hide settings button.
 *
 * @param checkSettingsButton [Boolean]: if the setting for showing settings button should be checked.
 * Defaults to true.
 */
fun checkInitialSettings(checkSettingsButton: Boolean = true) {
    onView(withId(R.id.applyParensSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
    onView(withId(R.id.applyDecimalsSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
    onView(withId(R.id.shuffleComputationSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
    onView(withId(R.id.shuffleNumbersSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
    onView(withId(R.id.shuffleOperatorsSwitch)).check(matches(allOf(isDisplayed(), isChecked())))

    onView(withId(R.id.clearOnErrorSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))

    onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
    onView(withId(R.id.historyButton0)).check(matches(isNotChecked()))
    onView(withId(R.id.historyButton1)).check(matches(isChecked()))
    onView(withId(R.id.historyButton2)).check(matches(isNotChecked()))
    onView(withId(R.id.historyButton3)).check(matches(isNotChecked()))

    if (checkSettingsButton) {
        onView(withId(R.id.settingsButtonSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
    }
}
