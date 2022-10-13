package xyz.lbres.trickcalculator.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.checkVisibleWithText

// TODO verify that values are saved when leaving/returning

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    private lateinit var mainText: ViewInteraction

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setupTest() {
        mainText = onView(withId(R.id.mainText))
        clearText()
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
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
        checkVisibleWithText(R.id.mainText, "")

        // numpad layout
        checkVisibleWithText(R.id.oneButton, "1")
        checkVisibleWithText(R.id.twoButton, "2")
        checkVisibleWithText(R.id.threeButton, "3")
        checkVisibleWithText(R.id.fourButton, "4")
        checkVisibleWithText(R.id.fiveButton, "5")
        checkVisibleWithText(R.id.sixButton, "6")
        checkVisibleWithText(R.id.sevenButton, "7")
        checkVisibleWithText(R.id.eightButton, "8")
        checkVisibleWithText(R.id.nineButton, "9")
        checkVisibleWithText(R.id.zeroButton, "0")

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
        mainText.check(matches(withText("")))

        // digits
        onView(withId(R.id.oneButton)).perform(click())
        mainText.check(matches(withText("1")))

        onView(withId(R.id.twoButton)).perform(click())
        mainText.check(matches(withText("12")))

        onView(withId(R.id.threeButton)).perform(click())
        mainText.check(matches(withText("123")))

        onView(withId(R.id.fourButton)).perform(click())
        mainText.check(matches(withText("1234")))

        onView(withId(R.id.fiveButton)).perform(click())
        mainText.check(matches(withText("12345")))

        onView(withId(R.id.sixButton)).perform(click())
        mainText.check(matches(withText("123456")))

        onView(withId(R.id.sevenButton)).perform(click())
        mainText.check(matches(withText("1234567")))

        onView(withId(R.id.eightButton)).perform(click())
        mainText.check(matches(withText("12345678")))

        onView(withId(R.id.nineButton)).perform(click())
        mainText.check(matches(withText("123456789")))

        onView(withId(R.id.zeroButton)).perform(click())
        mainText.check(matches(withText("1234567890")))

        // operators
        onView(withId(R.id.plusButton)).perform(click())
        mainText.check(matches(withText("1234567890+")))

        onView(withId(R.id.minusButton)).perform(click())
        mainText.check(matches(withText("1234567890+-")))

        onView(withId(R.id.timesButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x")))

        onView(withId(R.id.divideButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x/")))

        onView(withId(R.id.expButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x/^")))

        onView(withId(R.id.lparenButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x/^(")))

        onView(withId(R.id.rparenButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x/^()")))

        onView(withId(R.id.decimalButton)).perform(click())
        mainText.check(matches(withText("1234567890+-x/^().")))
    }

    @Test
    fun useClear() {
        val clearButton = onView(withId(R.id.clearButton))

        // empty
        mainText.check(matches(withText("")))
        clearButton.perform(click())
        mainText.check(matches(withText("")))

        // with text
        typeText("123")
        mainText.check(matches(not(withText(""))))
        clearButton.perform(click())
        mainText.check(matches(withText("")))

        typeText("(.7-45+55/5)^3(4.3)")
        mainText.check(matches(not(withText(""))))
        clearButton.perform(click())
        mainText.check(matches(withText("")))

        // with computed
        typeText("123")
        equals()
        mainText.check(matches(not(withText(""))))
        clearButton.perform(click())
        mainText.check(matches(withText("")))

        typeText("100x44-3")
        equals()
        mainText.check(matches(not(withText(""))))
        clearButton.perform(click())
        mainText.check(matches(withText("")))

        // with error
        typeText("100..3")
        equals()
        mainText.check(matches(not(withText(""))))
        onView(withId(R.id.errorText))
            .check(matches(isDisplayed()))
            .check(matches(not(withText(""))))
        clearButton.perform(click())
        mainText.check(matches(withText("")))
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
    }

    @Test
    fun useEqualsResult() = testEqualsResult(mainText)

    @Test
    fun useEqualsError() {
        // syntax errors
        typeText("+")
        equals()
        checkVisibleWithText(R.id.errorText, "Error: Syntax error")

        clearText()
        typeText("1+")
        equals()
        checkVisibleWithText(R.id.errorText, "Error: Syntax error")
        typeText("2")
        equals()
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))

        clearText()
        typeText("()")
        equals()
        checkVisibleWithText(R.id.errorText, "Error: Syntax error")

        // divide by zero
        repeat(10) {
            clearText()
            typeText("1/0")
            equals()
            onView(withId(R.id.errorText)).check(
                matches(anyOf(not(isDisplayed()), withText("Error: Divide by zero")))
            )
        }
    }

    @Test
    fun useBackspace() {
        // blank
        mainText.check(matches(withText("")))
        backspace()
        mainText.check(matches(withText("")))

        // with text
        clearText()
        typeText("1")
        backspace()
        mainText.check(matches(withText("")))

        clearText()
        typeText("123")
        backspace()
        mainText.check(matches(withText("12")))

        clearText()
        typeText("123+0.1")
        backspaceTo("123+0.")
        backspaceTo("123+0")
        backspaceTo("123+")
        backspaceTo("123")
        backspaceTo("12")
        backspaceTo("1")
        backspaceTo("")

        // with computed value
        clearText()
        typeText("123")
        equals()
        mainText.check(matches(withText("[123]")))
        backspaceTo("")

        clearText()
        typeText("123")
        equals()
        typeText("+5")
        mainText.check(matches(withText("[123]+5")))
        backspace()
        backspaceTo("[123]")
        backspaceTo("")

        // with error
        clearText()
        typeText("+")
        mainText.check(matches(withText("+")))
        equals()
        onView(withId(R.id.errorText)).check(matches(isDisplayed()))
        backspace()
        mainText.check(matches(withText("")))
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))

        clearText()
        typeText("1+")
        mainText.check(matches(withText("1+")))
        equals()
        onView(withId(R.id.errorText)).check(matches(isDisplayed()))
        backspace()
        mainText.check(matches(withText("1")))
        onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
        equals()
        mainText.check(matches(withText("[1]")))
    }

    @Test
    fun openHistoryFragment() {
        val historyButton = onView(withId(R.id.historyButton))
        historyButton.check(matches(isDisplayed()))
        historyButton.perform(click())
        onView(withText("Computation History")).check(matches(isDisplayed()))
    }

    @Test
    fun useLastHistoryItem() = testLastHistoryItem(mainText)

    @Test
    fun openInfoFragment() {
        val infoButton = onView(withId(R.id.infoButton))
        infoButton.check(matches(isDisplayed()))
        infoButton.perform(click())
        onView(withText("Image Attributions")).check(matches(isDisplayed()))
    }
}
