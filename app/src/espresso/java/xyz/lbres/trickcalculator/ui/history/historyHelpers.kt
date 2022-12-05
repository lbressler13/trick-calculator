package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

/**
 * Test representation of a compute history displayed in the UI.
 */
typealias TestHistory = List<TestHI>

/**
 * Test representation of a history item displayed in the UI.
 * First value is computation string (first row in UI), and second value is result/error (second row in UI).
 */
typealias TestHI = Pair<String, String>

/**
 * Create [Matcher] to identify that a history item displays the expected computation text and result string
 *
 * @param computation [String]: first string displayed in UI
 * @param result [String]: second string displayed in UI
 * @return [Matcher]<[View]?>: matcher that a view contains the result and computation string
 */
fun withHistoryItem(computation: String, result: String): Matcher<View?> {
    return allOf(
        withChild(withText(computation)),
        withChild(withChild(withText(result)))
    )
}

/**
 * Create [Matcher] to identify that a history item displays the expected computation text and result string
 *
 * @param item [TestHI]: item to check
 * @return [Matcher]<[View]?>: matcher that a view contains the specified history item
 */
fun withHistoryItem(item: TestHI): Matcher<View?> = withHistoryItem(item.first, item.second)

/**
 * Update the history randomness setting.
 * Must be called from calculator screen, and returns to calculator screen after updating setting.
 *
 * @param randomness [Int]: new value of randomness setting
 */
fun setHistoryRandomness(randomness: Int) {
    openSettingsFragment()

    when (randomness) {
        0 -> onView(withId(R.id.historyButton0)).perform(click())
        1 -> onView(withId(R.id.historyButton1)).perform(click())
        2 -> onView(withId(R.id.historyButton2)).perform(click())
        3 -> onView(withId(R.id.historyButton3)).perform(click())
    }

    closeFragment()
}

/**
 * Verifies that all items from the history are displayed and shuffled, including multiple repeats of shuffled check
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to show if check fails. Current history will be appended.
 */
fun checkCorrectData(computeHistory: TestHistory, randomness: Int, errorMessage: String) {
    var shuffled = false
    val historySize = computeHistory.size
    val checker = HistoryChecker(computeHistory)

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    // check that all items are displayed, only needs to happen once
    openHistoryFragment()
    checker.checkDisplayed(randomness)
    closeFragment()

    // check that items are shuffled
    repeatUntil(repeatCount, { shuffled }) {
        openHistoryFragment()
        shuffled = shuffled || checker.checkShuffled(randomness)
        closeFragment()
    }

    if (historySize > 1 && !shuffled) {
        throw AssertionError("$errorMessage. History: $computeHistory")
    }
}

/**
 * Check that the order changes when opening and closing the fragment.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to throw if basic checks fail
 */
fun runSingleReshuffledTest(computeHistory: TestHistory, randomness: Int, errorMessage: String) {
    val recyclerId = R.id.itemsRecycler
    checkCorrectData(computeHistory, randomness, errorMessage)
    val historySize = computeHistory.size
    openHistoryFragment()

    // save all current values
    for (position in 0 until historySize) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position))
            .perform(RecyclerViewTextSaver.saveTextAtPosition(position, R.id.computeText))
    }

    closeFragment()

    // additional repeats for 2 items due to occasional failures
    val repeats = ternaryIf(computeHistory.size == 2, 10, 5)
    var shuffled = false

    repeatUntil(repeats, { shuffled }) {
        openHistoryFragment()

        for (position in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            try {
                onView(withViewHolder(recyclerId, position))
                    .check(matches(not(RecyclerViewTextSaver.withSavedTextAtPosition(position, R.id.computeText))))
                shuffled = true
            } catch (_: Throwable) {}
        }

        closeFragment()
    }

    if (!shuffled) {
        throw AssertionError("Items not re-shuffled for history randomness 1. History: $computeHistory")
    }
}
