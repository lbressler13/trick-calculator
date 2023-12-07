package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.saveText
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.withSavedText
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.withEmptyString

// TODO tests for settings

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
        mainText.check(matches(allOf(isDisplayed(), withEmptyString())))

        // numpad layout
        onView(withId(R.id.oneButton)).check(matches(allOf(isDisplayed(), withText("1"))))
        onView(withId(R.id.twoButton)).check(matches(allOf(isDisplayed(), withText("2"))))
        onView(withId(R.id.threeButton)).check(matches(allOf(isDisplayed(), withText("3"))))
        onView(withId(R.id.fourButton)).check(matches(allOf(isDisplayed(), withText("4"))))
        onView(withId(R.id.fiveButton)).check(matches(allOf(isDisplayed(), withText("5"))))
        onView(withId(R.id.sixButton)).check(matches(allOf(isDisplayed(), withText("6"))))
        onView(withId(R.id.sevenButton)).check(matches(allOf(isDisplayed(), withText("7"))))
        onView(withId(R.id.eightButton)).check(matches(allOf(isDisplayed(), withText("8"))))
        onView(withId(R.id.nineButton)).check(matches(allOf(isDisplayed(), withText("9"))))
        onView(withId(R.id.zeroButton)).check(matches(allOf(isDisplayed(), withText("0"))))

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

    @Test fun numpadButtons() = testNumpadButtons()
    @Test fun clearButton() = testClearButton()
    @Test fun backspaceButton() = testBackspaceButton()

    @Test fun equalsWithNoOperators() = testEqualsWithNoOperators()
    @Test fun equalsWithSingleOperatorType() = testEqualsWithSingleOperatorType()
    @Test fun equalsWithSeveralOperatorTypes() = testEqualsWithSeveralOperatorTypes()
    @Test fun equalsWithParentheses() = testEqualsWithParentheses()
    @Test fun equalsWithPreviouslyComputed() = testEqualsWithPreviouslyComputed()
    @Test fun equalsWithError() = testEqualsWithError()

    @Test
    fun equalsWithRandomizedSigns() {
        toggleShuffleOperators()
        openSettingsFragment()
        onView(withId(R.id.randomizeSignsSwitch))
            .perform(click())
            .check(matches(isChecked()))
        closeFragment()

        var options = setOf("[-5]", "[5]")
        checkMainTextMatchesMultiple(options, 2, 2, 10) {
            typeText("5")
            equals()
        }
        checkMainTextMatchesMultiple(options, 2, 2, 10) {
            typeText("-5")
            equals()
        }

        options = setOf("[3]", "[-1]", "[-3]", "[1]")
        checkMainTextMatchesMultiple(options, 2, 5, 10) {
            typeText("1+2")
            equals()
        }
        checkMainTextMatchesMultiple(options, 2, 2, 10) {
            typeText("-1-2")
            equals()
        }

        options = setOf("[0.6]", "[-0.6]")
        checkMainTextMatchesMultiple(options, 2, 2, 10) {
            typeText("-.75/1.25")
            equals()
        }

        options = setOf("[0.25]", "[1.75]", "[-0.25]", "[-1.75]")
        checkMainTextMatchesMultiple(options, 2, 5, 10) {
            typeText("-1+3/4")
            equals()
        }

        options = setOf("[6]", "[18]", "[-6]", "[-18]")
        checkMainTextMatchesMultiple(options, 2, 2, 10) {
            typeText("3")
            equals()
            typeText("(4+2)")
            equals()
        }
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

    @Test
    fun dataPersistedOnLeave() {
        // typed text
        leaveAndReturn()
        checkMainTextMatches("")
        errorText.check(matches(not(isDisplayed())))

        clearText()
        typeText("1")
        leaveAndReturn()
        checkMainTextMatches("1")

        clearText()
        typeText("1.003(8-9+2)/(1.5)^4")
        leaveAndReturn()
        checkMainTextMatches("1.003(8-9+2)/(1.5)^4")

        clearText()
        typeText("1+2")
        leaveAndReturn()
        typeText("x4")
        leaveAndReturn()
        checkMainTextMatches("1+2x4")

        clearText()
        typeText("3+") // would give error
        leaveAndReturn()
        checkMainTextMatches("3+")

        // computed value
        clearText()
        typeText("1.003")
        equals()
        leaveAndReturn()
        checkMainTextMatches("[1.003]")

        clearText()
        typeText("(0000000123)")
        equals()
        leaveAndReturn()
        checkMainTextMatches("[123]")

        clearText()
        typeText("1+4")
        equals()
        checkMainTextMatchesAny(setOf("[5]", "[-3]", "[4]", "[0.25]"))
        mainText.perform(saveText())
        leaveAndReturn()
        mainText.check(matches(withSavedText()))

        clearText()
        typeText("2+5-4")
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[3]", "[22]", "[3.25]", // + = +
                "[1]", "[-18]", "[0.75]", // + = -
                "[14]", "[6]", "[2.5]", // + = x
                "[4.4]", "[-3.6]", "[1.6]" // + = /
            )
        )
        mainText.perform(saveText())
        leaveAndReturn()
        mainText.check(matches(withSavedText()))

        clearText()
        typeText("10-.5x4/2+16")
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[10]", "[-21.5]", "[11.875]", "[-5]", "[-21.875]", "[-5.75]", // + = +
                "[10]", "[41.5]", "[8.125]", "[25]", "[41.875]", "[25.75]", // + = -
                "[8.875]", "[-9]", "[1.125]", "[19]", "[-12.75]", "[15.25]", // + = *
                "[-8]", "[12]", "[48]", "[28]", "[66]", "[94]" // + = /
            )
        )
        mainText.perform(saveText())
        leaveAndReturn()
        mainText.check(matches(withSavedText()))

        // error
        clearText()
        typeText("+")
        equals()
        errorText.check(matches(allOf(isDisplayed(), withText("Error: Syntax error"))))
        leaveAndReturn()
        errorText.check(matches(allOf(isDisplayed(), withText("Error: Syntax error"))))
        checkMainTextMatches("+")

        clearText()
        openSettingsFragment()
        onView(withId(R.id.clearOnErrorSwitch)).perform(click())
        closeFragment()
        typeText("1+1..0")
        equals()
        errorText.check(matches(allOf(isDisplayed(), withText("Error: Syntax error"))))
        checkMainTextMatches("")
        leaveAndReturn()
        errorText.check(matches(allOf(isDisplayed(), withText("Error: Syntax error"))))
        checkMainTextMatches("")
    }
}
