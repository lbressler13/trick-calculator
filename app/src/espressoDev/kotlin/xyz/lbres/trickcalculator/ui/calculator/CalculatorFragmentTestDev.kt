package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeDialog
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.doClearHistory
import xyz.lbres.trickcalculator.testutils.openDevTools
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

@RunWith(AndroidJUnit4::class)
class CalculatorFragmentTestDev {

    private val useHistoryButton = onView(withId(R.id.useLastHistoryButton))

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Test
    fun clearHistory() {
        useHistoryButton.check(isNotPresented())

        typeText("12+34")
        equals()
        useHistoryButton.check(matches(isDisplayed()))

        doClearHistory()
        useHistoryButton.check(isNotPresented())
    }

    @Test
    fun clearHistoryWithFragmentNotDisplayed() {
        useHistoryButton.check(isNotPresented())

        typeText("12+34")
        equals()
        useHistoryButton.check(matches(isDisplayed()))

        openHistoryFragment()
        doClearHistory()
        closeFragment()
        useHistoryButton.check(isNotPresented())
    }

    @Test
    fun clearHistoryWhileUsingLastItem() {
        useHistoryButton.check(isNotPresented())
        typeText("12+34")
        equals()

        useHistoryButton.check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("12+34")))

        openDevTools()
        onView(withId(R.id.clearHistoryButton)).perform(click())
        closeDialog()

        onView(withId(R.id.mainText)).check(matches(withText("12+34")))
        useHistoryButton.check(isNotPresented())
    }

    @Test
    fun changeSettingsButtonThroughDialog() {
        // show
        val settingsButton = onView(withId(R.id.settingsButton))
        settingsButton.check(isNotPresented())

        openSettingsFromDialog()
        onView(withId(R.id.settingsButtonSwitch)).perform(click())
        closeFragment()

        settingsButton.check(matches(isDisplayed()))

        // hide
        openSettingsFromDialog()
        onView(withId(R.id.settingsButtonSwitch)).perform(click())
        closeFragment()

        settingsButton.check(isNotPresented())
    }
}
