package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.matchers.isEmptyString
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule(0) // TODO undo this

    @Before
    fun setupTest() {
        RecyclerViewTextSaver.clearAllSavedValues()

        if (ProductFlavor.devMode) {
            hideDevToolsButton()
        }
    }

    @Test
    fun loadActionBarWithTitle() {
        openHistoryFragment()
        val expectedTitle = "Calculator"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun closeButton() {
        openHistoryFragment()
        onView(withText("No history")).check(matches(isDisplayed()))
        onView(withId(R.id.closeButton)).perform(forceClick())
        onView(withText("No history")).check(isNotPresented())
        onView(withId(R.id.mainText)).check(matches(isDisplayed()))
    }

    @Test
    fun randomness0() = testRandomness0()

    @Test
    fun randomness1() = testRandomness1()

    @Test
    fun randomness1Reshuffled() = testRandomness1Reshuffled()

    @Test
    fun randomness2() = testRandomness2()

    // @Test
    // fun randomness2Reshuffled() {} // TODO

    // @Test
    // fun randomness3() {} // TODO

    @Test
    fun longHistory() = testLongHistory()

    @Test
    fun clearOnError() {
        setHistoryRandomness(0)
        toggleShuffleOperators()
        openSettingsFragment()
        onView(withId(R.id.clearOnErrorSwitch)).perform(click())
        closeFragment()

        val computeHistory = mutableListOf<TestHI>()

        // one error
        typeText("0xx8")
        equals()
        computeHistory.add(TestHI("0xx8", "Syntax error"))
        onView(withId(R.id.mainText)).check(matches(isEmptyString()))

        openHistoryFragment()
        HistoryChecker(computeHistory).runAllChecks(0)

        // multiple errors
        closeFragment()
        clearText()
        typeText("10/0")
        equals()
        computeHistory.add(TestHI("10/0", "Divide by zero"))

        openHistoryFragment()
        HistoryChecker(computeHistory).runAllChecks(0)

        // errors and results
        closeFragment()
        clearText()
        typeText("15+5")
        equals()
        computeHistory.add(TestHI("15+5", "20"))

        typeText("x")
        equals()
        computeHistory.add(TestHI("20x", "Syntax error"))

        // don't clear text, should have been cleared by error
        typeText("2x4")
        equals()
        computeHistory.add(TestHI("2x4", "8"))

        openHistoryFragment()
        HistoryChecker(computeHistory).runAllChecks(0)
    }

    // TODO
    // @Test
    // fun shuffleValues() {
    // test that correct operators/numbers/computation are saved when actual values are shuffled
    // }

    // TODO
    // @Test
    // fun changeRandomness() {
    // change randomness setting for same history
    // }
}
