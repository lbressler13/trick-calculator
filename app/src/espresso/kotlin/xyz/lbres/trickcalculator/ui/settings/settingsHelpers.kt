package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.general.simpleIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

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
    onView(withId(R.id.randomizeSignsSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
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
    onView(withId(R.id.randomizeSignsSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
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

/**
 * Test that a specific group of settings is displayed in the UI
 *
 * @param settings [Settings]: settings to check
 * @param settingsSwitchDisplayed [Boolean]: is the settings button switch should be visible.
 * Defaults to `true`
 */
fun checkSettingsDisplayed(
    settings: Settings,
    settingsSwitchDisplayed: Boolean = true,
) {
    val checkSetting: (Int, Boolean) -> Unit = { switchId, settingValue ->
        val switch = onView(withId(switchId))
        val checkedMatcher = simpleIf(settingValue, isChecked(), isNotChecked())
        switch.check(matches(allOf(isDisplayed(), checkedMatcher)))
    }

    // switches
    checkSetting(R.id.applyDecimalsSwitch, settings.applyDecimals)
    checkSetting(R.id.applyParensSwitch, settings.applyParens)
    checkSetting(R.id.clearOnErrorSwitch, settings.clearOnError)
    checkSetting(R.id.randomizeSignsSwitch, settings.randomizeSigns)
    checkSetting(R.id.shuffleComputationSwitch, settings.shuffleComputation)
    checkSetting(R.id.shuffleNumbersSwitch, settings.shuffleNumbers)
    checkSetting(R.id.shuffleOperatorsSwitch, settings.shuffleOperators)

    // history randomness
    onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
    val historyButtonIds = listOf(R.id.historyButton0, R.id.historyButton1, R.id.historyButton2, R.id.historyButton3)
    historyButtonIds.forEachIndexed { index, buttonId ->
        val checkedMatcher = simpleIf(index == settings.historyRandomness, isChecked(), isNotChecked())
        onView(withId(buttonId)).check(matches(allOf(isDisplayed(), checkedMatcher)))
    }

    // settings button switch
    if (settingsSwitchDisplayed) {
        checkSetting(R.id.settingsButtonSwitch, settings.showSettingsButton)
    } else {
        onView(withId(R.id.settingsButtonSwitch)).check(isNotPresented())
    }
}
