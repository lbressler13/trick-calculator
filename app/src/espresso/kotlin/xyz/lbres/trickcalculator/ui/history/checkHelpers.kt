package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

/**
 * Verify that all items from the history are displayed and shuffled, including multiple repeats of shuffled check
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to show if check fails. Current history will be appended.
 */
fun checkCorrectData(history: TestHistory, randomness: Int, errorMessage: String) {
    var shuffled = false

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(history.size == 2, 10, 5)

    // check that all items are displayed, only needs to happen once
    openHistoryFragment()
    history.checkAllDisplayed(randomness)
    closeFragment()

    // check that items are shuffled
    repeatUntil(repeatCount, { shuffled }) {
        openHistoryFragment()
        shuffled = shuffled || history.checkDisplayShuffled(randomness)
        closeFragment()
    }

    if (history.size > 1 && !shuffled) {
        throw AssertionError("$errorMessage. History: $history")
    }
}

/**
 * Check that the order changes when opening and closing the fragment.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to throw if basic checks fail
 */
fun runSingleReshuffledCheck(computeHistory: TestHistory, randomness: Int, errorMessage: String) {
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

/**
 * Run a randomness check for a compute history, including a retry
 *
 * @param history [TestHistory]
 * @param randomness [Int]: history randomness setting
 */
fun checkRandomness(history: TestHistory, randomness: Int) {
    setHistoryRandomness(randomness)
    openHistoryFragment()

    history.checkAllDisplayed(randomness)

    if (randomness.isZero()) {
        history.checkDisplayOrdered()
    } else {
        // one retry in case of small probability where numbers aren't shuffled
        try {
            history.checkDisplayShuffled(randomness)
        } catch (_: Throwable) {
            history.checkDisplayShuffled(randomness)
        }
    }

    closeFragment()
}
