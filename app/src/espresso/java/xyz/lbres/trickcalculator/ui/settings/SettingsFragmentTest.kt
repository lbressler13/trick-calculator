package xyz.lbres.trickcalculator.ui.settings

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
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.forceClick

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {
    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setupTest() {
        // open fragment
        val infoButton = onView(withId(R.id.infoButton))
        infoButton.check(matches(isDisplayed()))
        infoButton.perform(click())
        onView(withId(R.id.title)).perform(click())
    }

    @Test
    fun loadActionBarWithTitle() {
        val expectedTitle = "Settings"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun loadFullUi() {
        onView(withId(R.id.applyParensSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
        onView(withId(R.id.applyDecimalsSwitch)).check(matches(allOf(isDisplayed(), isChecked())))
        onView(withId(R.id.shuffleComputationSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
        onView(withId(R.id.shuffleNumbersSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
        onView(withId(R.id.shuffleOperatorsSwitch)).check(matches(allOf(isDisplayed(), isChecked())))

        onView(withId(R.id.clearOnErrorSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))
        onView(withId(R.id.settingsButtonSwitch)).check(matches(allOf(isDisplayed(), isNotChecked())))

        onView(withId(R.id.historyRandomnessGroup)).check(matches(isDisplayed()))
        onView(withId(R.id.historyButton1)).check(matches(isChecked()))

        onView(withId(R.id.resetSettingsButton)).check(matches(isDisplayed()))
        onView(withId(R.id.randomizeSettingsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun interactWithUi() {
        // TODO
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
}
