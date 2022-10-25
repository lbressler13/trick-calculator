package xyz.lbres.trickcalculator.ui.devtools

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

@RunWith(AndroidJUnit4::class)
class DeveloperToolsDialogTest {
    private val devToolsButton = onView(withId(R.id.devToolsButton))

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Test
    fun loadInitialUi() {
        openDialog()

        onView(withText("Developer Tools")).check(matches(isDisplayed()))

        onView(withId(R.id.clearHistoryButton))
            .check(matches(allOf(isDisplayed(), withText("Clear history"))))
        onView(withId(R.id.refreshUIButton))
            .check(matches(allOf(isDisplayed(), withText("Refresh UI"))))
        onView(withId(R.id.settingsDialogButton))
            .check(matches(allOf(isDisplayed(), withText("Show settings dialog"))))
        onView(withId(R.id.hideDevToolsButton))
            .check(matches(allOf(isDisplayed(), withText("Hide dev tools"))))

        onView(withId(R.id.devToolsTimeSpinner))
            .check(matches(allOf(isDisplayed(), withSpinnerText("5000ms"))))
    }

    @Test
    fun clearHistory() {
        val historyButton = onView(withId(R.id.historyButton))
        val useHistoryButton = onView(withId(R.id.useLastHistoryButton))
        val noHistoryMessage = onView(withText("No history"))

        historyButton.check(matches(isDisplayed()))
        useHistoryButton.check(isNotPresented())

        // no history
        historyButton.perform(click())
        noHistoryMessage.check(matches(isDisplayed()))
        onView(withId(R.id.closeButton)).perform(forceClick())

        // create history
        onView(withId(R.id.oneButton)).perform(click())
        onView(withId(R.id.equalsButton)).perform(click())

        useHistoryButton.check(matches(isDisplayed()))
        historyButton.perform(click())
        noHistoryMessage.check(isNotPresented())

        // clear history
        openDialog()
        onView(withId(R.id.clearHistoryButton)).perform(click())
        closeDialog()

        noHistoryMessage.check(matches(isDisplayed()))
        onView(withId(R.id.closeButton)).perform(forceClick())
        useHistoryButton.check(isNotPresented())
    }

    @Test
    fun clearHistoryWhileUsingItem() {
        val historyButton = onView(withId(R.id.historyButton))
        val useHistoryButton = onView(withId(R.id.useLastHistoryButton))

        historyButton.check(matches(isDisplayed()))
        useHistoryButton.check(isNotPresented())

        onView(withId(R.id.oneButton)).perform(click())
        onView(withId(R.id.plusButton)).perform(click())
        onView(withId(R.id.twoButton)).perform(click())
        onView(withId(R.id.equalsButton)).perform(click())

        useHistoryButton.check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.mainText)).check(matches(withText("1+2")))

        openDialog()
        onView(withId(R.id.clearHistoryButton)).perform(click())
        closeDialog()

        useHistoryButton.check(isNotPresented())
        onView(withId(R.id.mainText)).check(matches(withText("1+2")))
        historyButton.perform(click())
        onView(withText("No history")).check(matches(isDisplayed()))
    }

    @Test
    fun showSettingsDialog() {
        openDialog()
        onView(withId(R.id.settingsDialogButton)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))
    }

    @Test
    fun hideDevToolsOptionsDisplayed() = testHideDevToolsOptionsDisplayed()

    @Test
    fun interactWithHideDevToolsSpinner() = testInteractWithHideDevToolsSpinner()

    @Test
    fun hideDevTools() = testHideDevTools()

    @Test
    fun historyFragment() {
        onView(withId(R.id.historyButton)).perform(click())
        devToolsButton.check(matches(isDisplayed())).perform(click())
        onView(withText("Developer Tools")).check(matches(isDisplayed()))
    }

    @Test
    fun attributionsFragment() {
        onView(withId(R.id.infoButton)).perform(click())
        onView(withText("Image Attributions")).check(matches(isDisplayed()))
        devToolsButton.check(matches(isDisplayed())).perform(click())
        onView(withText("Developer Tools")).check(matches(isDisplayed()))
    }

    @Test
    fun settingsFragment() {
        onView(withId(R.id.infoButton)).perform(click())
        onView(withText("Image Attributions")).check(matches(isDisplayed())).perform(click())
        onView(withText("Settings"))

        devToolsButton.check(matches(isDisplayed())).perform(click())
        onView(withText("Developer Tools")).check(matches(isDisplayed()))
    }
}
