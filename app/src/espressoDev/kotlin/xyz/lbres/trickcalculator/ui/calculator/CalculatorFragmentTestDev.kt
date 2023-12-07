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
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.doClearHistory
import xyz.lbres.trickcalculator.testutils.doRefreshUI
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

@RunWith(AndroidJUnit4::class)
class CalculatorFragmentTestDev {
    private val mainText = onView(withId(R.id.mainText))
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
        mainText.check(matches(withText("12+34")))

        doClearHistory()

        mainText.check(matches(withText("12+34")))
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

    @Test
    fun refreshUI() {
        toggleShuffleOperators()

        // with text
        typeText("1-3+4")
        doRefreshUI()

        mainText.check(matches(withText("1-3+4")))
        useHistoryButton.check(isNotPresented())

        // with result
        equals()
        typeText("x6")

        doRefreshUI()
        mainText.check(matches(withText("[2]x6")))
        useHistoryButton.check(matches(isDisplayed()))

        // with error
        clearText()
        typeText("5/0")
        equals()

        doRefreshUI()
        mainText.check(matches(withText("5/0")))
        onView(withId(R.id.errorText)).check(matches(withText("Error: Divide by zero")))
    }
}
