package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

private val applyParensSwitch = onView(withId(R.id.applyParensSwitch))
private val applyDecimalsSwitch = onView(withId(R.id.applyDecimalsSwitch))
private val clearOnErrorSwitch = onView(withId(R.id.clearOnErrorSwitch))
private val settingsButtonSwitch = onView(withId(R.id.settingsButtonSwitch))
private val shuffleComputationSwitch = onView(withId(R.id.shuffleComputationSwitch))
private val shuffleNumbersSwitch = onView(withId(R.id.shuffleNumbersSwitch))
private val shuffleOperatorsSwitch = onView(withId(R.id.shuffleOperatorsSwitch))

private val settingsButton = onView(withId(R.id.settingsButton))

fun testSettingsMaintained() {
    // set ui
    applyParensSwitch.perform(click())
    applyDecimalsSwitch.perform(click())
    clearOnErrorSwitch.perform(click())
    shuffleComputationSwitch.perform(click())
    shuffleNumbersSwitch.perform(click())
    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton3)).perform(click())

    settingsButtonSwitch.perform(click())

    applyParensSwitch.check(matches(isNotChecked()))
    applyDecimalsSwitch.check(matches(isNotChecked()))
    clearOnErrorSwitch.check(matches(isChecked()))
    shuffleComputationSwitch.check(matches(isChecked()))
    shuffleNumbersSwitch.check(matches(isChecked()))
    shuffleOperatorsSwitch.check(matches(isNotChecked()))
    onView(withId(R.id.historyButton3)).check(matches(isChecked()))

    settingsButtonSwitch.check(matches(isChecked()))

    closeFragment()

    // check values after re-opening
    openSettingsFragment()

    applyParensSwitch.check(matches(isNotChecked()))
    applyDecimalsSwitch.check(matches(isNotChecked()))
    clearOnErrorSwitch.check(matches(isChecked()))
    shuffleComputationSwitch.check(matches(isChecked()))
    shuffleNumbersSwitch.check(matches(isChecked()))
    shuffleOperatorsSwitch.check(matches(isNotChecked()))
    onView(withId(R.id.historyButton3)).check(matches(isChecked()))

    settingsButtonSwitch.check(matches(allOf(isDisplayed(), isChecked())))

    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton2)).perform(click())
    shuffleOperatorsSwitch.check(matches(isChecked()))
    onView(withId(R.id.historyButton2)).check(matches(isChecked()))

    pressBack()

    // check values after opening with settings button
    settingsButton.perform(click())

    applyParensSwitch.check(matches(isNotChecked()))
    applyDecimalsSwitch.check(matches(isNotChecked()))
    clearOnErrorSwitch.check(matches(isChecked()))
    shuffleComputationSwitch.check(matches(isChecked()))
    shuffleNumbersSwitch.check(matches(isChecked()))
    shuffleOperatorsSwitch.check(matches(isChecked()))
    onView(withId(R.id.historyButton2)).check(matches(isChecked()))

    settingsButtonSwitch.check(isNotPresented())

    applyParensSwitch.perform(click())
    applyParensSwitch.check(matches(isChecked()))

    closeFragment()

    // re-open to check settings from settings fragment
    openSettingsFragment()
    applyParensSwitch.check(matches(isChecked()))
    applyDecimalsSwitch.check(matches(isNotChecked()))
    clearOnErrorSwitch.check(matches(isChecked()))
    shuffleComputationSwitch.check(matches(isChecked()))
    shuffleNumbersSwitch.check(matches(isChecked()))
    shuffleOperatorsSwitch.check(matches(isChecked()))
    onView(withId(R.id.historyButton2)).check(matches(isChecked()))

    settingsButtonSwitch.check(matches(allOf(isDisplayed(), isChecked())))
    settingsButtonSwitch.perform(click())
    settingsButtonSwitch.check(matches(allOf(isDisplayed(), isNotChecked())))

    closeFragment()

    // settings button is not present
    settingsButton.check(isNotPresented())
}

fun testResetButton() {
    val resetButton = onView(withId(R.id.resetSettingsButton))

    // modify settings
    applyParensSwitch.perform(click())
    shuffleComputationSwitch.perform(click())
    shuffleNumbersSwitch.perform(click())
    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton3)).perform(click())

    // reset
    resetButton.perform(click())
    onView(withText("Calculator")).check(matches(isDisplayed()))
    settingsButton.check(isNotPresented())

    // check reset settings
    openSettingsFragment()
    checkInitialSettings()

    // modify settings + enabled settings button
    shuffleNumbersSwitch.perform(click())
    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton2)).perform(click())

    settingsButtonSwitch.perform(click())

    closeFragment()

    // open settings button w/ existing changes + reset
    settingsButton.check(matches(isDisplayed())).perform(click())
    resetButton.perform(click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    // validate that settings button still exists
    settingsButton.check(matches(isDisplayed())).perform(click())
    checkInitialSettings(checkSettingsButton = false)

    // modify settings through settings button + reset
    applyDecimalsSwitch.perform(click())
    clearOnErrorSwitch.perform(click())
    resetButton.perform(click())

    // validate that settings button is still checked
    openSettingsFragment()
    checkInitialSettings(checkSettingsButton = false)
    settingsButtonSwitch.check(matches(isChecked()))

    // reset through regular method + validate settings still exists
    resetButton.perform(click())
    settingsButton.check(matches(isDisplayed()))
}

fun testRandomizeButton() {
    val randomizeButton = onView(withId(R.id.randomizeSettingsButton))

    // fragment closes
    onView(withText("Settings")).check(matches(isDisplayed()))
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    settingsButtonSwitch.perform(click())
    closeFragment()
    settingsButton.perform(click())
    onView(withText("Settings")).check(matches(isDisplayed()))
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    // settings button disappeared
    settingsButton.check(isNotPresented())

    openSettingsFragment()
    settingsButtonSwitch.check(matches(isNotChecked()))
    onView(withId(R.id.clearOnErrorSwitch)).check(matches(isChecked()))

    try {
        onView(isRoot()).check(settingsRandomized())
    } catch (_: AssertionError) {
        // one retry, in case of rare event where randomized = initial settings
        randomizeButton.perform(scrollTo(), click())
        openSettingsFragment()
        onView(isRoot()).check(settingsRandomized())
    }
}

fun testStandardFunctionButton() {
    val standardFunctionButton = onView(withId(R.id.standardFunctionButton))

    // from initial settings
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))
    settingsButton.check(isNotPresented())

    // check standard settings
    openSettingsFragment()
    checkStandardSettings()

    // modify settings
    applyParensSwitch.perform(click())
    shuffleComputationSwitch.perform(click())
    shuffleNumbersSwitch.perform(click())
    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton3)).perform(click())

    // set standard
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))
    settingsButton.check(isNotPresented())

    // check standard settings
    openSettingsFragment()
    checkStandardSettings()

    // modify settings + enable settings button
    shuffleNumbersSwitch.perform(click())
    shuffleOperatorsSwitch.perform(click())
    onView(withId(R.id.historyButton2)).perform(click())

    settingsButtonSwitch.perform(click())

    closeFragment()

    // open settings button w/ existing changes + set standard
    settingsButton.check(matches(isDisplayed())).perform(click())
    standardFunctionButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    // validate that settings button still exists
    settingsButton.check(matches(isDisplayed())).perform(click())
    checkStandardSettings()

    // modify settings through settings button + set standard
    applyDecimalsSwitch.perform(click())
    clearOnErrorSwitch.perform(click())
    standardFunctionButton.perform(scrollTo(), click())

    // validate that settings button is still checked
    openSettingsFragment()
    checkStandardSettings()
    settingsButtonSwitch.check(matches(isChecked()))

    // set standard through regular method + validate settings still exists
    standardFunctionButton.perform(scrollTo(), click())
    settingsButton.check(matches(isDisplayed()))
}
