package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R

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

/**
 * Check that settings match the config needed to function as a standard calculator.
 */
fun checkStandardSettings() {
    onView(withId(R.id.applyParensSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
    onView(withId(R.id.applyDecimalsSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
    onView(withId(R.id.shuffleComputationSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
    onView(withId(R.id.shuffleNumbersSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
    onView(withId(R.id.shuffleOperatorsSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))

    onView(withId(R.id.clearOnErrorSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))

    onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
    onView(withId(R.id.historyButton0)).check(matches(isChecked()))
    onView(withId(R.id.historyButton1)).check(matches(isNotChecked()))
    onView(withId(R.id.historyButton2)).check(matches(isNotChecked()))
    onView(withId(R.id.historyButton3)).check(matches(isNotChecked()))
}
