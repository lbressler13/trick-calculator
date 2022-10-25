package xyz.lbres.trickcalculator.ui.settings

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Before
    fun setupTest() {
        openSettingsFragment()
    }

    @Test
    fun loadActionBarWithTitle() {
        val expectedTitle = "Settings"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun loadFullUi() {
        checkInitialSettings()
        onView(withId(R.id.resetSettingsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.randomizeSettingsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun interactWithUi() {
        // switches
        testSwitch(R.id.applyParensSwitch, true)
        testSwitch(R.id.applyDecimalsSwitch, true)
        testSwitch(R.id.shuffleComputationSwitch, false)
        testSwitch(R.id.shuffleNumbersSwitch, false)
        testSwitch(R.id.shuffleOperatorsSwitch, true)

        testSwitch(R.id.clearOnErrorSwitch, false)
        testSwitch(R.id.settingsButtonSwitch, false)

        // history group
        onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
        checkHistoryChecked(1)

        onView(withId(R.id.historyButton0)).perform(click())
        checkHistoryChecked(0)

        onView(withId(R.id.historyButton2)).perform(click())
        checkHistoryChecked(2)

        onView(withId(R.id.historyButton3)).perform(click())
        checkHistoryChecked(3)

        onView(withId(R.id.historyButton1)).perform(click())
        checkHistoryChecked(1)
    }

    @Test
    fun closeButton() {
        onView(withId(R.id.closeButton)).perform(forceClick())
        onView(withText("Calculator")).check(matches(isDisplayed()))
    }

    @Test
    fun deviceBackButton() {
        pressBack()
        onView(withText("Calculator")).check(matches(isDisplayed()))
    }

    @Test
    fun settingsButton() {
        // enabled settings button
        onView(withId(R.id.settingsButtonSwitch)).perform(click())
        closeFragment()

        // open settings
        onView(withText("Calculator")).check(matches(isDisplayed()))
        onView(withId(R.id.settingsButton)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))

        // settings button switch not displayed
        onView(withId(R.id.settingsButtonSwitch)).check(isNotPresented())

        // full ui otherwise displayed
        checkInitialSettings(checkSettingsButton = false)
        onView(withId(R.id.resetSettingsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.randomizeSettingsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun switchSettingsMaintained() = testSwitchSettingsMaintained()

    @Test
    fun resetButton() = testResetButton()

    @Test
    fun randomizeButton() = testRandomizeButton()

    /**
     * Run basic tests on a switch by checking and unchecking
     *
     * @param id [IdRes]: view ID of the switch
     * @param initialChecked [Boolean]: if the switch is expected to be checked initially, before any interaction
     */
    private fun testSwitch(@IdRes id: Int, initialChecked: Boolean) {
        val switch = onView(withId(id))
        val inititalCheck = ternaryIf(initialChecked, isChecked(), isNotChecked())
        val oppositeCheck = ternaryIf(initialChecked, isNotChecked(), isChecked())

        switch.check(matches(allOf(isDisplayed(), inititalCheck)))

        switch.perform(click())
        switch.check(matches(allOf(isDisplayed(), oppositeCheck)))

        switch.perform(click())
        switch.check(matches(allOf(isDisplayed(), inititalCheck)))
    }

    /**
     * Check the history radio group to ensure that the correct button is checked, and all others are unchecked.
     * Checks all buttons because some radio groups may allow multiple buttons to be checked.
     *
     * @param checked [Int]: index of button expected to be checked
     */
    private fun checkHistoryChecked(checked: Int) {
        val buttons = listOf(
            onView(withId(R.id.historyButton0)),
            onView(withId(R.id.historyButton1)),
            onView(withId(R.id.historyButton2)),
            onView(withId(R.id.historyButton3)),
        )

        for (button in buttons.withIndex()) {
            if (button.index == checked) {
                button.value.check(matches(isChecked()))
            } else {
                button.value.check(matches(isNotChecked()))
            }
        }
    }
}
