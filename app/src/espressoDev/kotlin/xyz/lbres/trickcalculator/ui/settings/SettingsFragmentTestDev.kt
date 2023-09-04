package xyz.lbres.trickcalculator.ui.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.doRefreshUI
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTestDev {

    private val devToolsButton = onView(withId(R.id.devToolsButton))
    private val settingsDialogButton = onView(withId(R.id.openSettingsButton))
    private val closeButton = onView(withId(R.id.closeButton))

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @After
    fun cleanupTest() {
        unmockkAll()
    }

    @Test
    fun openFromCalculatorFragment() {
        onView(withText("Calculator")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Developer Tools")).check(doesNotExist())
        onView(withText("Settings")).check(matches(isDisplayed()))
        checkInitialSettings(checkSettingsButton = true)
    }

    @Test
    fun openFromAttributionsFragment() {
        openAttributionsFragment()
        onView(withText("Image Attributions")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Developer Tools")).check(doesNotExist())
        onView(withText("Settings")).check(matches(isDisplayed()))
        checkInitialSettings(checkSettingsButton = true)
    }

    @Test
    fun openFromHistoryFragment() {
        openHistoryFragment()
        onView(withText("No history")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Developer Tools")).check(doesNotExist())
        onView(withText("Settings")).check(matches(isDisplayed()))
        checkInitialSettings(checkSettingsButton = true)
    }

    @Test
    fun openFromSettingsFragment() {
        openSettingsFragment()
        onView(withText("Settings")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Developer Tools")).check(doesNotExist())
        onView(withText("Settings")).check(matches(isDisplayed()))
        checkInitialSettings(checkSettingsButton = true)
    }

    @Test
    fun openFromSettingsFragmentThroughButton() {
        openSettingsFragment()
        onView(withId(R.id.settingsButtonSwitch)).perform(click())
        closeFragment()

        onView(withId(R.id.settingsButton)).perform(click())
        onView(withId(R.id.settingsButtonSwitch)).check(isNotPresented())

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withId(R.id.settingsButtonSwitch)).check(isNotPresented())
    }

    @Test
    fun closeFromCalculatorFragment() {
        onView(withText("Calculator")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Settings")).check(matches(isDisplayed()))
        onView(withText("Calculator")).check(doesNotExist())
        closeButton.perform(forceClick())

        onView(withText("Calculator")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(doesNotExist())
    }

    @Test
    fun closeFromAttributionsFragment() {
        openAttributionsFragment()
        onView(withText("Image Attributions")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Settings")).check(matches(isDisplayed()))
        onView(withText("Image Attributions")).check(doesNotExist())
        closeButton.perform(forceClick())

        onView(withText("Image Attributions")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(doesNotExist())
    }

    @Test
    fun closeFromHistoryFragment() {
        openHistoryFragment()
        onView(withText("No history")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())

        onView(withText("Settings")).check(matches(isDisplayed()))
        onView(withText("No history")).check(doesNotExist())
        closeButton.perform(forceClick())

        onView(withText("No history")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(doesNotExist())
    }

    @Test
    fun closeFromSettingsFragment() {
        openSettingsFragment()
        onView(withText("Settings")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())
        closeButton.perform(forceClick())

        // should go back to calculator fragment
        onView(withText("Calculator")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(doesNotExist())
    }

    @Test
    fun closeFromSettingsFragmentThroughButton() {
        openSettingsFragment()
        onView(withId(R.id.settingsButtonSwitch)).perform(click())
        closeFragment()

        onView(withId(R.id.settingsButton)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))

        devToolsButton.perform(click())
        settingsDialogButton.perform(click())
        closeButton.perform(forceClick())

        // should go back to calculator fragment
        onView(withText("Calculator")).check(matches(isDisplayed()))
        onView(withText("Settings")).check(doesNotExist())
    }

    @Test
    fun refreshUI() {
        openSettingsFragment()

        // initial settings
        doRefreshUI()
        checkInitialSettings()

        // modified settings
        val settings = Settings()

        onView(withId(R.id.clearOnErrorSwitch)).perform(click())
        onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
        onView(withId(R.id.historyButton0)).perform(click())
        settings.clearOnError = true
        settings.shuffleNumbers = true
        settings.historyRandomness = 0

        doRefreshUI()
        checkSettingsDisplayed(settings)
    }

    @Test fun settingsSaved() = testSettingsSaved()
    @Test fun randomizeButtonSaved() = testRandomizeButtonSaved()
    @Test fun resetButtonSaved() = testResetButtonSaved()
    @Test fun standardFunctionButtonSaved() = testStandardFunctionButtonSaved()
}
