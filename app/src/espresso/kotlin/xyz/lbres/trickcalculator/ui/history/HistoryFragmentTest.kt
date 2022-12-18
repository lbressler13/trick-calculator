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
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.isEmptyString
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented

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

    @Test
    fun randomness2Reshuffled() = testRandomness2Reshuffled()

    // @Test fun randomness3() {} // TODO

    @Test
    fun longHistory() = testLongHistory()

    @Test
    fun clearOnError() {
        setHistoryRandomness(0)
        toggleShuffleOperators()
        openSettingsFragment()
        onView(withId(R.id.clearOnErrorSwitch)).perform(click())
        closeFragment()

        val history = TestHistory()

        // one error
        history.add(generateHI("0xx8") { "Syntax error" })
        onView(withId(R.id.mainText)).check(matches(isEmptyString()))

        openHistoryFragment()
        history.runAllChecks(0)

        // multiple errors
        closeFragment()
        history.add(generateHI("10/0") { "Divide by zero" })

        openHistoryFragment()
        history.runAllChecks(0)

        // errors and results
        closeFragment()
        history.add(generateHI("15+5") { "20" })
        history.add(generateHI("x", "20") { "Syntax error" })

        // don't clear text, should have been cleared by error
        history.add(generateHI("2x4") { "8" })

        openHistoryFragment()
        history.runAllChecks(0)
    }

    @Test fun shuffleOperators() = testShuffleOperators()
    @Test fun shuffleNumbers() = testShuffleNumbers()
    @Test fun shuffleComputation() = testShuffleComputation()

    @Test fun noApplyDecimals() = testNoApplyDecimals()
    @Test fun noApplyParens() = testNoApplyParens()

    @Test fun multipleSettings() = testMultipleSettings() // TODO enable this
}
