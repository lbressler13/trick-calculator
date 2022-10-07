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
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    lateinit var mainText: ViewInteraction
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
        onView(withId(R.id.mainText)).check(matches(withText("")))

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
        // TODO (with text, with computed, with error, while blank)
    }

    @Test
    fun useEquals() {
        // blank
        mainText.check(matches(withText("")))
        equals()
        mainText.check(matches(withText("")))

        // one number
        clearText()
        typeText("123")
        equals()
        mainText.check(matches(withText("[123]")))

        clearText()
        typeText("9.876")
        equals()
        mainText.check(matches(withText("[9.876]")))

        clearText()
        typeText("000.05")
        equals()
        mainText.check(matches(withText("[0.05]")))

        // one operator
        repeat(10) {
            clearText()
            typeText("12+10")
            equals()
            checkMainTextOptions(listOf("[22]", "[2]", "[120]", "[1.2]"))
        }

        repeat(10) {
            clearText()
            typeText("2^3") // exponent
            equals()
            checkMainTextOptions(listOf("[5]", "[-1]", "[6]", "[0.66666667]", "[8]"))
        }

        repeat(10) {
            clearText()
            typeText("1.5x4") // decimal
            equals()
            checkMainTextOptions(listOf("[5.5]", "[-2.5]", "[6]", "[0.375]"))
        }

        repeat(10) {
            clearText()
            typeText("2x5x4") // several same op
            equals()
            checkMainTextOptions(listOf("[11]", "[-7]", "[40]", "[0.1]"))
        }

        // several operators
        repeat(10) {
            clearText()
            typeText("2+5-4")
            equals()
            checkMainTextOptions(
                listOf(
                    "[3]", "[22]", "[3.25]", // + = +
                    "[1]", "[-18]", "[0.75]", // + = -
                    "[14]", "[6]", "[2.5]", // + = x
                    "[4.4]", "[-3.6]", "[1.6]" // + = /
                )
            )
        }

        repeat(10) {
            clearText()
            typeText("5^2-4") // exponent
            equals()
            checkMainTextOptions(
                listOf(
                    "[3]", "[13]", "[5.5]", "[21]", // + = +
                    "[7]", "[-3]", "[4.5]", "[-11]", // + = -
                    "[14]", "[6]", "[2.5]", "[80]", // + = x
                    "[6.5]", "[-1.5]", "[10]", "[0.3125]", // + = /
                    "[29]", "[21]", "[100]", "[6.25]" // + = ^
                )
            )
        }

        repeat(10) {
            clearText()
            typeText("10-.5x4/2+16")
            equals()
            checkMainTextOptions(
                listOf(
                    "[10]", "[-21.5]", "[11.875]", "[-5]", "[-21.875]", "[-5.75]", // + = +
                    "[10]", "[41.5]", "[8.125]", "[25]", "[41.875]", "[25.75]", // + = -
                    "[8.875]", "[-9]", "[1.125]", "[19]", "[-12.75]", "[15.25]", // + = *
                    "[-8]", "[12]", "[48]", "[28]", "[66]", "[94]" // + = /
                )
            )
        }

        // with previous computed

        // error

        // TODO
    }

    @Test
    fun useComputedValue() {
        // TODO
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
        backspace()
        mainText.check(matches(withText("123+0.")))
        backspace()
        mainText.check(matches(withText("123+0")))
        backspace()
        mainText.check(matches(withText("123+")))
        backspace()
        mainText.check(matches(withText("123")))
        backspace()
        mainText.check(matches(withText("12")))
        backspace()
        mainText.check(matches(withText("1")))
        backspace()
        mainText.check(matches(withText("")))

        // with computed value
        clearText()
        typeText("123")
        equals()
        mainText.check(matches(withText("[123]")))
        backspace()
        mainText.check(matches(withText("")))

        clearText()
        typeText("123")
        equals()
        typeText("+5")
        mainText.check(matches(withText("[123]+5")))
        backspace()
        backspace()
        mainText.check(matches(withText("[123]")))
        backspace()
        mainText.check(matches(withText("")))

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

        // TODO with clear on error
    }

    @Test
    fun openHistoryFragment() {
        val historyButton = onView(withId(R.id.historyButton))
        historyButton.check(matches(isDisplayed()))
        historyButton.perform(click())
        onView(withText("Computation History")).check(matches(isDisplayed()))
        // TODO
    }

    @Test
    fun useLastHistoryItem() {
        val useLastButton = onView(withId(R.id.useLastHistoryButton))
        var lastComputation = ""

        // none

        // one value

        // multiple values

        // error

        // TODO
    }

    @Test
    fun openInfoFragment() {
        // TODO
    }

    private fun clearText() {
        onView(withId(R.id.clearButton)).perform(click())
    }

    private fun backspace() {
        onView(withId(R.id.backspaceButton)).perform(click())
    }

    private fun equals() {
        onView(withId(R.id.equalsButton)).perform(click())
    }

    private fun checkMainTextOptions(options: StringList) {
        val matchers = options.map { withText(it) }.toMutableList()
        mainText.check(matches(anyOf(matchers)))
    }

    // TODO test settings button
}
