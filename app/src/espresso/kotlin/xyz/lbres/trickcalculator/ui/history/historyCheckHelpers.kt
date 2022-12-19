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
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.saveTextAtPosition
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.withSavedTextAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private val randomnessErrors = mapOf(
    0 to "History items should be ordered in history randomness 0.",
    1 to "History items should be shuffled in history randomness 1.",
    2 to "History items and pairs should be shuffled in history randomness 2.",
)

/**
 * Verify that all items from the history are displayed and shuffled, including multiple repeats of shuffled check
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 */
fun checkRandomness(history: TestHistory, randomness: Int) {
    setHistoryRandomness(randomness)

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(history.size == 2, 10, 5)

    openHistoryFragment()
    history.checkAllDisplayed(randomness)
    closeFragment()

    if (randomness.isZero()) {
        openHistoryFragment()
        if (!history.checkDisplayOrdered()) {
            throw AssertionError(randomnessErrors[0])
        }
        closeFragment()
    } else {
        var shuffled = false
        repeatUntil(repeatCount, { shuffled }) {
            openHistoryFragment()
            shuffled = shuffled || history.checkDisplayShuffled(randomness)
            closeFragment()
        }

        if (history.size > 1 && !shuffled) {
            val errorMessage = randomnessErrors[randomness] ?: ""
            throw AssertionError("$errorMessage History: $history")
        }
    }
}

/**
 * Check that the order changes when opening and closing the fragment.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 */
fun runSingleReshuffledCheck(computeHistory: TestHistory, randomness: Int) {
    val recyclerId = R.id.itemsRecycler
    checkRandomness(computeHistory, randomness)
    val historySize = computeHistory.size
    openHistoryFragment()

    // save all current values
    for (position in 0 until historySize) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position))
            .perform(saveTextAtPosition(position, R.id.computeText))
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
                    .check(matches(not(withSavedTextAtPosition(position, R.id.computeText))))
                shuffled = true
            } catch (_: Throwable) {}
        }

        closeFragment()
    }

    if (!shuffled) {
        throw AssertionError("Items not re-shuffled for history randomness 1. History: $computeHistory")
    }
}
