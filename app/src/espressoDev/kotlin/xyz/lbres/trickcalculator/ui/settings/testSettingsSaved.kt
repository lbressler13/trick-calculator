package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog

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

    onView(withId(R.id.randomizeSignsSwitch)).perform(click())
    onView(withId(R.id.settingsButtonSwitch)).perform(click())
    settings.applyDecimals = settings.randomizeSigns
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
