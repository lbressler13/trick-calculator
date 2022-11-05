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
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.matchers.isEmptyString
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {
    private val recyclerId = R.id.itemsRecycler

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Before
    fun setupTest() {
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
    fun historyRandomness0() = testRandomness0()

    @Test
    fun historyRandomness1() = testRandomness1()

    @Test
    fun historyRandomness2() = testRandomness2()

    // TODO
    // @Test
    // fun historyRandomness3() {
    // }

    // TODO
    // @Test
    // fun longHistory() {
    // }

    @Test
    fun clearOnError() {
        setHistoryRandomness(0)
        toggleShuffleOperators()
        openSettingsFragment()
        onView(withId(R.id.clearOnErrorSwitch)).perform(click())
        closeFragment()

        // one error
        typeText("0xx8")
        equals()
        onView(withId(R.id.mainText)).check(matches(isEmptyString()))

        openHistoryFragment()

        onView(withViewHolder(recyclerId, 0))
            .check(matches(withHistoryItem("0xx8", "Syntax error")))

        // multiple errors
        closeFragment()
        clearText()
        typeText("10/0")
        equals()

        openHistoryFragment()

        onView(withViewHolder(recyclerId, 0))
            .check(matches(withHistoryItem("0xx8", "Syntax error")))

        onView(withViewHolder(recyclerId, 1))
            .check(matches(withHistoryItem("10/0", "Divide by zero")))

        // errors and results
        closeFragment()
        clearText()
        typeText("15+5")
        equals()

        typeText("x")
        equals()

        // don't clear text, should have been cleared by error
        typeText("2x4")
        equals()

        openHistoryFragment()

        onView(withViewHolder(recyclerId, 0))
            .check(matches(withHistoryItem("0xx8", "Syntax error")))

        onView(withViewHolder(recyclerId, 1))
            .check(matches(withHistoryItem("10/0", "Divide by zero")))

        onView(withViewHolder(recyclerId, 2))
            .check(matches(withHistoryItem("15+5", "20")))

        onView(withViewHolder(recyclerId, 3))
            .check(matches(withHistoryItem("20x", "Syntax error")))

        onView(withViewHolder(recyclerId, 4))
            .check(matches(withHistoryItem("2x4", "8")))
    }

    // TODO
    // @Test
    // fun shuffleValues() {
    // test that correct operators/numbers/computation are saved when actual values are shuffled
    // }
}
