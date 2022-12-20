package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
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

private val reshuffledErrors = mapOf(
    1 to "Items not re-shuffled for history randomness 1.",
    2 to "Items not re-shuffled for history randomness 2.",
)

private const val recyclerId = R.id.itemsRecycler

/**
 * Set the history randomness, open history fragment, and verify that all items from the history are
 * displayed and shuffled/ordered based on randomness.
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 */
fun checkRandomness(history: TestHistory, randomness: Int) {
    setHistoryRandomness(randomness)

    openHistoryFragment()
    history.checkAllDisplayed(randomness)
    closeFragment()

    val correctOrder = when (randomness) {
        0 -> checkOrdered(history)
        else -> checkShuffledCorrectly(history, randomness)
    }

    if (history.size > 1 && !correctOrder) {
        val errorMessage = randomnessErrors[randomness] ?: "Items in unexpected order."
        throw AssertionError("$errorMessage History: $history")
    }
}

/**
 * Check that the elements in the history are displayed in order
 *
 * @return [Boolean]: `true` if the elements are displayed in order, `false` otherwise
 */
private fun checkOrdered(history: TestHistory): Boolean {
    openHistoryFragment()
    val result = history.checkDisplayOrdered()
    closeFragment()
    return result
}

/**
 * Check that the elements in the history are shuffled according to the randomness, including retries
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @return [Boolean]: `true` if elements are shuffled even once, `false` otherwise
 */
private fun checkShuffledCorrectly(history: TestHistory, randomness: Int): Boolean {
    if (history.size <= 1) {
        return true
    }

    val repeatCount = ternaryIf(history.size == 2, 10, 5)
    var shuffled = false
    repeatUntil(repeatCount, { shuffled }) {
        openHistoryFragment()
        shuffled = shuffled || history.checkDisplayShuffled(randomness)
        closeFragment()
    }

    return shuffled
}

/**
 * Check that the order changes when opening and closing the fragment.
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 */
fun runSingleReshuffledCheck(history: TestHistory, randomness: Int) {
    checkRandomness(history, randomness)
    openHistoryFragment()

    // save all current values
    for (position in 0 until history.size) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position))
            .perform(saveTextAtPosition(position, R.id.computeText))
    }

    closeFragment()

    if (history.size > 1 && !checkReshuffledCorrectly(history.size)) {
        val errorMessage = reshuffledErrors[randomness] ?: "Items not re-shuffled."
        throw AssertionError("$errorMessage History: $history")
    }
}

/**
 * Check that the values displayed in the UI do not match the saved values, including retries
 *
 * @param historySize [Int]: number of saved values
 * @return [Boolean]: `true` if any of the values have changed, `false` otherwise
 */
private fun checkReshuffledCorrectly(historySize: Int): Boolean {
    // additional repeats for 2 items due to occasional failures
    val repeats = ternaryIf(historySize == 2, 10, 5)
    var reshuffled = false

    repeatUntil(repeats, { reshuffled }) {
        openHistoryFragment()

        for (position in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            try {
                onView(withViewHolder(recyclerId, position))
                    .check(matches(not(withSavedTextAtPosition(position, R.id.computeText))))
                reshuffled = true
            } catch (_: Throwable) {}
        }

        closeFragment()
    }

    return reshuffled
}
