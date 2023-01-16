package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog

fun testRandomizeButtonSaved() {
    val randomizeButton = onView(withId(R.id.randomizeSettingsButton))

    // calculator fragment
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    onView(isRoot()).check(settingsRandomized())
    closeFragment()

    // history fragment
    openHistoryFragment()
    openSettingsFromDialog()
    onView(withText("No history")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("No history")).check(matches(isDisplayed()))
    closeFragment() // close history fragment

    openSettingsFragment()
    onView(isRoot()).check(settingsRandomized())
    closeFragment()

    // settings fragment
    openSettingsFragment()
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    onView(isRoot()).check(settingsRandomized())
    closeFragment()

    // attributions fragment
    openAttributionsFragment()
    openSettingsFromDialog()
    onView(withText("Image Attributions")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Image Attributions")).check(matches(isDisplayed()))
    closeFragment()

    openSettingsFragment()
    onView(isRoot()).check(settingsRandomized())
    closeFragment()

    // settings fragment through button
    openSettingsFragment()
    onView(withId(R.id.settingsButtonSwitch)).perform(click())
    closeFragment()

    onView(withId(R.id.settingsButton)).perform(click())
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    onView(isRoot()).check(settingsRandomized())
    closeFragment()
}

fun testResetButtonSaved() {
    val resetButton = onView(withId(R.id.resetSettingsButton))

    // calculator fragment
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    resetButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkInitialSettings()
    closeFragment()

    // history fragment
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    openHistoryFragment()
    openSettingsFromDialog()
    onView(withText("No history")).check(doesNotExist())
    resetButton.perform(scrollTo(), click())
    onView(withText("No history")).check(matches(isDisplayed()))
    closeFragment() // close history fragment

    openSettingsFragment()
    checkInitialSettings()
    closeFragment()

    // settings fragment
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    openSettingsFragment()
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    resetButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkInitialSettings()
    closeFragment()

    // attributions fragment
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    openAttributionsFragment()
    openSettingsFromDialog()
    onView(withText("Image Attributions")).check(doesNotExist())
    resetButton.perform(scrollTo(), click())
    onView(withText("Image Attributions")).check(matches(isDisplayed()))
    closeFragment()

    openSettingsFragment()
    checkInitialSettings()
    closeFragment()

    // settings fragment through button
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    onView(withId(R.id.settingsButtonSwitch)).perform(click())
    closeFragment()

    onView(withId(R.id.settingsButton)).perform(click())
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    resetButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkInitialSettings(checkSettingsButton = false)
    closeFragment()
}

fun testStandardFunctionButtonSaved() {
    val standardFunctionButton = onView(withId(R.id.standardFunctionButton))

    // calculator fragment
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkStandardSettings()
    closeFragment()

    // history fragment
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    openHistoryFragment()
    openSettingsFromDialog()
    onView(withText("No history")).check(doesNotExist())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("No history")).check(matches(isDisplayed()))
    closeFragment() // close history fragment

    openSettingsFragment()
    checkStandardSettings()
    closeFragment()

    // settings fragment
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    openSettingsFragment()
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkStandardSettings()
    closeFragment()

    // attributions fragment
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    openAttributionsFragment()
    openSettingsFromDialog()
    onView(withText("Image Attributions")).check(doesNotExist())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Image Attributions")).check(matches(isDisplayed()))
    closeFragment()

    openSettingsFragment()
    checkStandardSettings()
    closeFragment()

    // settings fragment through button
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    onView(withId(R.id.settingsButtonSwitch)).perform(click())
    closeFragment()

    onView(withId(R.id.settingsButton)).perform(click())
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    checkStandardSettings()
    closeFragment()
}
