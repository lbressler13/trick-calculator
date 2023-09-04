package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import xyz.lbres.kotlinutils.list.BoolList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.SharedValues
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import kotlin.random.Random
import kotlin.random.nextInt

fun testRandomizeButtonSaved() {
    val settingsFromValues: (Pair<BoolList, Int>) -> Settings = {
        Settings(
            applyDecimals = it.first[0],
            applyParens = it.first[1],
            clearOnError = true,
            historyRandomness = it.second,
            showSettingsButton = false,
            shuffleComputation = it.first[2],
            shuffleNumbers = it.first[3],
            shuffleOperators = it.first[4]
        )
    }

    val calcValues = Pair(listOf(false, true, true, true, true), 1)
    val historyValues = Pair(listOf(true, false, true, false, true), 2)
    val settingsValues = Pair(listOf(false, false, false, false, false), 1)
    val attributionsValues = Pair(listOf(true, true, false, false, true), 0)
    val settingsThroughButtonValues = Pair(listOf(true, true, true, false, true), 3)
    val allValues = listOf(calcValues, historyValues, settingsValues, attributionsValues, settingsThroughButtonValues)

    val nextBooleanValues = allValues.flatMap { it.first }
    val nextIntValues = allValues.map { it.second }
    val mockRandom = mockk<Random> {
        every { nextBoolean() } returnsMany nextBooleanValues
        every { nextFloat() } returns 0f
        every { nextInt(0..3) } returnsMany nextIntValues
    }

    mockkObject(SharedValues)
    every { SharedValues.random } returns mockRandom

    val randomizeButton = onView(withId(R.id.randomizeSettingsButton))

    // calculator fragment
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    var expectedSettings = settingsFromValues(calcValues)
    checkSettingsDisplayed(expectedSettings)
    closeFragment()

    // history fragment
    openHistoryFragment()
    openSettingsFromDialog()
    onView(withText("Computation History")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Computation History")).check(matches(isDisplayed()))
    closeFragment()

    openSettingsFragment()
    expectedSettings = settingsFromValues(historyValues)
    checkSettingsDisplayed(expectedSettings)
    closeFragment()

    // settings fragment
    openSettingsFragment()
    openSettingsFromDialog()
    onView(withText("Calculator")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Calculator")).check(matches(isDisplayed()))

    openSettingsFragment()
    expectedSettings = settingsFromValues(settingsValues)
    checkSettingsDisplayed(expectedSettings)
    closeFragment()

    // attributions fragment
    openAttributionsFragment()
    openSettingsFromDialog()
    onView(withText("Image Attributions")).check(doesNotExist())
    randomizeButton.perform(scrollTo(), click())
    onView(withText("Image Attributions")).check(matches(isDisplayed()))
    closeFragment()

    openSettingsFragment()
    expectedSettings = settingsFromValues(attributionsValues)
    checkSettingsDisplayed(expectedSettings)
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
    expectedSettings = settingsFromValues(settingsThroughButtonValues)
    checkSettingsDisplayed(expectedSettings)
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
