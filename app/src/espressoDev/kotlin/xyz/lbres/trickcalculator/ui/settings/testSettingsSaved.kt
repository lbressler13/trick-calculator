package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

fun testSettingsSaved() {
    val settings = Settings()

    // calculator fragment
    openSettingsFromDialog()

    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.historyButton2)).perform(click())
    settings.applyDecimals = !settings.applyDecimals
    settings.shuffleOperators = !settings.shuffleOperators
    settings.historyRandomness = 2

    closeFragment()

    openSettingsFragment()
    checkSettingsDisplayed(settings)
    closeFragment()

    // history fragment
    openHistoryFragment()
    openSettingsFromDialog()

    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    onView(withId(R.id.settingsButtonSwitch)).perform(click())
    settings.applyDecimals = !settings.applyDecimals
    settings.showSettingsButton = !settings.showSettingsButton

    closeFragment() // close settings
    closeFragment() // close history

    openSettingsFragment()
    checkSettingsDisplayed(settings)
    closeFragment()

    // settings fragment
    openSettingsFragment()
    openSettingsFromDialog()

    onView(withId(R.id.clearOnErrorSwitch)).perform(click())
    onView(withId(R.id.historyButton0)).perform(click())
    settings.clearOnError = !settings.clearOnError
    settings.historyRandomness = 0

    closeFragment()

    openSettingsFragment()
    checkSettingsDisplayed(settings)
    closeFragment()

    // attributions fragment
    openAttributionsFragment()
    openSettingsFromDialog()

    onView(withId(R.id.shuffleComputationSwitch)).perform(click())
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    settings.shuffleComputation = !settings.shuffleComputation
    settings.shuffleNumbers = !settings.shuffleNumbers

    closeFragment() // close settings
    closeFragment() // close attributions

    openSettingsFragment()
    checkSettingsDisplayed(settings)
    closeFragment()

    // settings fragment through button
    onView(withId(R.id.settingsButton)).perform(click())
    openSettingsFromDialog()
    checkSettingsDisplayed(settings, settingsSwitchDisplayed = false)

    onView(withId(R.id.historyButton3)).perform(click())
    settings.historyRandomness = 3

    closeFragment()

    openSettingsFragment()
    checkSettingsDisplayed(settings)
    closeFragment()
}

// TODO move this to helper
/**
 * Test that a specific group of settings is displayed in the UI
 *
 * @param settings [Settings]: settings to check
 * @param settingsSwitchDisplayed [Boolean]: is the settings button switch should be visible.
 * Defaults to `true`
 */
fun checkSettingsDisplayed(settings: Settings, settingsSwitchDisplayed: Boolean = true) {
    val checkSetting: (Int, Boolean) -> Unit = { switchId, settingValue ->
        val switch = onView(withId(switchId))
        val checkedMatcher = ternaryIf(settingValue, isChecked(), isNotChecked())
        switch.check(matches(allOf(isDisplayed(), checkedMatcher)))
    }

    // switches
    checkSetting(R.id.applyDecimalsSwitch, settings.applyDecimals)
    checkSetting(R.id.applyParensSwitch, settings.applyParens)
    checkSetting(R.id.clearOnErrorSwitch, settings.clearOnError)
    checkSetting(R.id.shuffleComputationSwitch, settings.shuffleComputation)
    checkSetting(R.id.shuffleNumbersSwitch, settings.shuffleNumbers)
    checkSetting(R.id.shuffleOperatorsSwitch, settings.shuffleOperators)

    // history randomness
    onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
    val historyButtonIds = listOf(R.id.historyButton0, R.id.historyButton1, R.id.historyButton2, R.id.historyButton3)
    historyButtonIds.forEachIndexed { index, buttonId ->
        val checkedMatcher = ternaryIf(index == settings.historyRandomness, isChecked(), isNotChecked())
        onView(withId(buttonId)).check(matches(allOf(isDisplayed(), checkedMatcher)))
    }

    // settings button switch
    if (settingsSwitchDisplayed) {
        checkSetting(R.id.settingsButtonSwitch, settings.showSettingsButton)
    } else {
        onView(withId(R.id.settingsButtonSwitch)).check(isNotPresented())
    }
}
