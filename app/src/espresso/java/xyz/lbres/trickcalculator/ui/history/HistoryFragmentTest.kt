package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.main.clearText
import xyz.lbres.trickcalculator.ui.main.equals
import xyz.lbres.trickcalculator.ui.main.typeText

// TODO test with shuffled operators, shuffled numbers, etc

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // @Rule
    // @JvmField
    // val retryRule = RetryRule()

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
    fun historyRandomness0() {
        openSettingsFragment()
        onView(withId(R.id.historyButton0)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()).check(matches(isNotChecked()))
        closeFragment()

        openHistoryFragment()

        // no history
        onView(withText("No history")).check(matches(isDisplayed()))

        closeFragment()

        typeText("1+2")
        equals()
        openHistoryFragment()
        onView(withViewHolder(R.id.itemsRecycler, 0))
            .check(matches(withChild(withText("1+2"))))
            .check(matches(withChild(withChild(withText("3")))))

        closeFragment()

        typeText("-1/2")
        equals()
        clearText()
        typeText("+")
        equals()
        clearText()
        typeText("1+2-2^3x1")
        equals()
        clearText()
        typeText("(1+2)(4-2)")
        equals()
        clearText()

        // test that order doesn't change
        repeat(5) {
            openHistoryFragment()

            onView(withViewHolder(R.id.itemsRecycler, 0))
                .check(matches(withChild(withText("1+2"))))
                .check(matches(withChild(withChild(withText("3")))))

            onView(withViewHolder(R.id.itemsRecycler, 1))
                .check(matches(withChild(withText("3-1/2"))))
                .check(matches(withChild(withChild(withText("2.5")))))

            onView(withViewHolder(R.id.itemsRecycler, 2))
                .check(matches(withChild(withText("+"))))
                .check(matches(withChild(withChild(withText("Syntax error")))))

            onView(withViewHolder(R.id.itemsRecycler, 3))
                .check(matches(withChild(withText("1+2-2^3x1"))))
                .check(matches(withChild(withChild(withText("-5")))))

            onView(withViewHolder(R.id.itemsRecycler, 4))
                .check(matches(withChild(withText("(1+2)(4-2)"))))
                .check(matches(withChild(withChild(withText("6")))))

            closeFragment()
        }

        // TODO
    }

    @Test
    fun historyRandomness1() = testRandomness1() // TODO

    @Test
    fun historyRandomness2() {
        setHistoryRandomness(2)
        openHistoryFragment()

        onView(withText("No history")).check(matches(isDisplayed()))
        // TODO
    }

    @Test
    fun historyRandomness3() {
        setHistoryRandomness(2)
        openHistoryFragment()

        // TODO
    }

    @Test
    fun longHistory() {
        // TODO
    }

    @Test
    fun clearOnError() {
        // TODO
    }

    // maybe shuffle numbers? make sure it's always saving the right version of numbers
}
