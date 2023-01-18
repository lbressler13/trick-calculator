package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.doClearHistory
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

@RunWith(AndroidJUnit4::class)
class HistoryDevToolsTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Test
    fun clearHistoryWithFragmentClosed() {
        checkNoHistoryMessageDisplayed()

        // no history
        doClearHistory()
        checkNoHistoryMessageDisplayed()

        // add history
        typeText("12+34")
        equals()
        typeText("-.5")
        equals()

        checkNoHistoryMessageNotPresented()
        doClearHistory()
        checkNoHistoryMessageDisplayed()
    }

    @Test
    fun clearHistoryWithFragmentOpen() {
        val noHistoryMessage = onView(withId(R.id.noHistoryMessage))
        openHistoryFragment()
        noHistoryMessage.check(matches(isDisplayed()))

        // no history
        doClearHistory()
        noHistoryMessage.check(matches(isDisplayed()))
        closeFragment()

        // add history
        typeText("12+34")
        equals()
        typeText("-.5")
        equals()

        openHistoryFragment()
        noHistoryMessage.check(isNotPresented())

        doClearHistory()
        noHistoryMessage.check(matches(isDisplayed()))
    }

    @Test
    fun applyUpdatedHistoryRandomness() {
        toggleShuffleOperators()

        val noHistoryMessage = onView(withId(R.id.noHistoryMessage))
        val history = TestHistory()

        // no history
        openHistoryFragment()
        noHistoryMessage.check(matches(isDisplayed()))
        openSettingsFromDialog()
        onView(withId(R.id.historyButton0)).perform(click())
        closeFragment() // settings fragment
        noHistoryMessage.check(matches(isDisplayed()))
        closeFragment() // history fragment

        // long history to reduce likeliness of being ordered on randomness 1 or 2
        history.add(generateTestItem("1+2") { "3" })
        history.add(generateTestItem("-1/2", "3") { "2.5" })
        history.add(generateTestItem("+") { "Syntax error" })
        history.add(generateTestItem("1+2-2^3x1") { "-5" })
        history.add(generateTestItem("(1+2)(4-2)") { "6" })
        history.add(generateTestItem("(1+2)(4-2)") { "6" })
        history.add(generateTestItem("2x(1-9)") { "-16" })
        history.add(generateTestItem("/5", "-16") { "-3.2" })
        history.add(generateTestItem("2^6-7x8") { "8" })

        // check randomness 0
        openHistoryFragment()
        history.checkAllDisplayed(0)
        if (!history.checkDisplayOrdered()) {
            throw AssertionError("History items should by ordered in history randomness 0. History $history")
        }

        // check randomness 1
        openSettingsFromDialog()
        onView(withId(R.id.historyButton1)).perform(click())
        closeFragment()
        history.checkAllDisplayed(1)
        if (!history.checkDisplayShuffled(1)) {
            throw AssertionError("History items should be shuffled in history randomness 1. History: $history")
        }

        // check randomness 2
        openSettingsFromDialog()
        onView(withId(R.id.historyButton2)).perform(click())
        closeFragment()
        history.checkAllDisplayed(2)
        if (!history.checkDisplayShuffled(2)) {
            throw AssertionError("History items and pairs should be shuffled in history randomness 2. History: $history")
        }

        // switch back to 0
        openSettingsFromDialog()
        onView(withId(R.id.historyButton0)).perform(click())
        closeFragment()
        history.checkAllDisplayed(0)
        if (!history.checkDisplayOrdered()) {
            throw AssertionError("History items should by ordered in history randomness 0. History $history")
        }
    }

    @Test
    fun historyNotReshuffled() {
        val history = TestHistory()
        history.add(generateTestItem("1+2") { "3" })
        history.add(generateTestItem("-1/2", "3") { "2.5" })
        history.add(generateTestItem("+") { "Syntax error" })
        history.add(generateTestItem("1+2-2^3x1") { "-5" })
        history.add(generateTestItem("(1+2)(4-2)") { "6" })

        openHistoryFragment()
        runSingleNotReshuffledCheck(history, 2) // start with 2, because initial randomness is 1
        runSingleNotReshuffledCheck(history, 1)
    }
}
