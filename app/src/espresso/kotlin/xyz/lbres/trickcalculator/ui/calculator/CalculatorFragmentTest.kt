package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver

@RunWith(AndroidJUnit4::class)
class CalculatorFragmentTest {
    private val mainText = onView(withId(R.id.mainText))
    private val errorText = onView(withId(R.id.errorText))

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @After
    fun cleanupTest() {
        TextSaver.clearAllSavedValues()
    }

    @Test
    fun loadActionBarWithTitle() {
        val expectedTitle = "Calculator"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun loadInitialViews() {
        // top of screen
        onView(withId(R.id.useLastHistoryButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.historyButton)).check(matches(isDisplayed()))
        errorText.check(matches(not(isDisplayed())))
        mainText.check(matches(isDisplayedWithText("")))

        // numpad layout
        onView(withId(R.id.oneButton)).check(matches(isDisplayedWithText("1")))
        onView(withId(R.id.twoButton)).check(matches(isDisplayedWithText("2")))
        onView(withId(R.id.threeButton)).check(matches(isDisplayedWithText("3")))
        onView(withId(R.id.fourButton)).check(matches(isDisplayedWithText("4")))
        onView(withId(R.id.fiveButton)).check(matches(isDisplayedWithText("5")))
        onView(withId(R.id.sixButton)).check(matches(isDisplayedWithText("6")))
        onView(withId(R.id.sevenButton)).check(matches(isDisplayedWithText("7")))
        onView(withId(R.id.eightButton)).check(matches(isDisplayedWithText("8")))
        onView(withId(R.id.nineButton)).check(matches(isDisplayedWithText("9")))
        onView(withId(R.id.zeroButton)).check(matches(isDisplayedWithText("0")))

        onView(withId(R.id.plusButton)).check(matches(isDisplayed()))
        onView(withId(R.id.minusButton)).check(matches(isDisplayed()))
        onView(withId(R.id.timesButton)).check(matches(isDisplayed()))
        onView(withId(R.id.divideButton)).check(matches(isDisplayed()))
        onView(withId(R.id.lparenButton)).check(matches(isDisplayed()))
        onView(withId(R.id.rparenButton)).check(matches(isDisplayed()))
        onView(withId(R.id.decimalButton)).check(matches(isDisplayed()))
        onView(withId(R.id.backspaceButton)).check(matches(isDisplayed()))
        onView(withId(R.id.clearButton)).check(matches(isDisplayed()))
        onView(withId(R.id.expButton)).check(matches(isDisplayed()))
        onView(withId(R.id.equalsButton)).check(matches(isDisplayed()))

        // bottom of screen
        onView(withId(R.id.infoButton)).check(matches(isDisplayed()))
        onView(withId(R.id.settingsButton)).check(matches(not(isDisplayed())))
    }

    // keypad except equals
    @Test fun numpadButtons() = testNumpadButtons()
    @Test fun clearButton() = testClearButton()
    @Test fun backspaceButton() = testBackspaceButton()

    // equals
    @Test fun equalsWithNoOperators() = testEqualsWithNoOperators()
    @Test fun equalsWithSingleOperatorType() = testEqualsWithSingleOperatorType()
    @Test fun equalsWithSeveralOperatorTypes() = testEqualsWithSeveralOperatorTypes()
    @Test fun equalsWithParentheses() = testEqualsWithParentheses()
    @Test fun equalsWithPreviouslyComputed() = testEqualsWithPreviouslyComputed()
    @Test fun equalsWithError() = testEqualsWithError()

    // settings
    @Test fun randomizedSigns() = testRandomizedSigns()
    // TODO additional settings

    @Test fun useLastHistoryItem() = testLastHistoryItem()

    @Test fun dataPersistedOnLeave() = testDataPersistedOnLeave()

    // navigation
    @Test
    fun openHistoryFragment() {
        val historyButton = onView(withId(R.id.historyButton))
        historyButton.check(matches(isDisplayed()))
        historyButton.perform(click())
        onView(withText("Computation History")).check(matches(isDisplayed()))
    }

    @Test
    fun openInfoFragment() {
        val infoButton = onView(withId(R.id.infoButton))
        infoButton.check(matches(isDisplayed()))
        infoButton.perform(click())
        onView(withText("Image Attributions")).check(matches(isDisplayed()))
    }
}
