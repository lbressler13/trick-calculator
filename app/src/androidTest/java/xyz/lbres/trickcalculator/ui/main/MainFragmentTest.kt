package xyz.lbres.trickcalculator.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.MainActivity
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers.not

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MainActivity::class.java)

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
        onView(withId(R.id.mainText)).check(matches(isDisplayed())).check(matches(withText("")))
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))

        // numpad layout
        onView(withId(R.id.oneButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("1")))
        onView(withId(R.id.twoButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("2")))
        onView(withId(R.id.threeButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("3")))
        onView(withId(R.id.fourButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("4")))
        onView(withId(R.id.fiveButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("5")))
        onView(withId(R.id.sixButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("6")))
        onView(withId(R.id.sevenButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("7")))
        onView(withId(R.id.eightButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("8")))
        onView(withId(R.id.nineButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("9")))
        onView(withId(R.id.zeroButton))
            .check(matches(isDisplayed()))
            .check(matches(withText("0")))

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

    @Test
    fun typeInMainText() {
        onView(withId(R.id.mainText)).check(matches(withText("")))

        // digits
        onView(withId(R.id.oneButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1")))

        onView(withId(R.id.twoButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("12")))

        onView(withId(R.id.threeButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("123")))

        onView(withId(R.id.fourButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234")))

        onView(withId(R.id.fiveButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("12345")))

        onView(withId(R.id.sixButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("123456")))

        onView(withId(R.id.sevenButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567")))

        onView(withId(R.id.eightButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("12345678")))

        onView(withId(R.id.nineButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("123456789")))

        onView(withId(R.id.zeroButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890")))

        // operators
        onView(withId(R.id.plusButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+")))

        onView(withId(R.id.minusButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-")))

        onView(withId(R.id.timesButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x")))

        onView(withId(R.id.divideButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x/")))

        onView(withId(R.id.expButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x/^")))

        onView(withId(R.id.lparenButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x/^(")))

        onView(withId(R.id.rparenButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x/^()")))

        onView(withId(R.id.decimalButton)).perform(click())
        onView(withId(R.id.mainText)).check(matches(withText("1234567890+-x/^().")))
    }

    @Test
    fun useEquals() {
        // TODO (computed value and errors)
    }

    @Test
    fun useComputedValue() {
        // TODO
    }

    @Test
    fun useBackspace() {
        // TODO (regular, empty, computed)
    }

    @Test
    fun useClear() {
        // TODO (with text, with computed, with error, while blank)
    }

    @Test
    fun openHistoryFragment() {
        // TODO
    }

    @Test
    fun useLastHistoryItem() {
        // TODO
    }

    @Test
    fun openInfoFragment() {
        // TODO
    }

    // TODO test settings button
}