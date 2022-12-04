package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private typealias ShuffledCheckInfo = Pair<List<*>, (Any) -> Matcher<View?>>

/**
 * Class to easily run checks on a compute history by specifying the randomness during each method call.
 * Methods include checking for items to be displayed, checking that items are ordered, and checking that items are shuffled.
 *
 * @param computeHistory [TestHistory]: history of computation
 */
class HistoryChecker(private val computeHistory: TestHistory) {
    private val recyclerId = R.id.itemsRecycler

    // order of items is shuffled, but computation/result pairs are kept together
    private val matchingShuffledCheck = ShuffledCheckInfo(computeHistory) { item ->
        @Suppress("UNCHECKED_CAST")
        item as Pair<String, String>
        withHistoryItem(item.first, item.second)
    }
    // order of computation strings is shuffled
    private val shuffledComputationCheck = ShuffledCheckInfo(computeHistory.map { it.first }) {
        computation ->
        withChild(withText(computation as String))
    }
    // order of result strings is shuffled
    private val shuffledResultCheck = ShuffledCheckInfo(computeHistory.map { it.second }) {
        result ->
        withChild(withChild(withText(result as String)))
    }

    /**
     * Check that all ViewHolders contain a valid history item.
     * Method is of checking is based on level of randomness.
     *
     * @param randomness [Int]: history randomness setting
     * @param throwError [Boolean]: if the function should throw an error when the check fails.
     * If `false`, the function will return `false`. Defaults to `true`.
     * @return [Boolean]: `true` if the check passes, `false` if it fails and [throwError] is set to `false`
     */
    fun checkDisplayed(randomness: Int, throwError: Boolean = true): Boolean {
        checkAllowedRandomness(randomness)
        val check: (Pair<String, String>, Int) -> Pair<Boolean, Boolean> = when (randomness) {
            0 -> { item, position -> checkMatchedPair(item, position) }
            1 -> { item, position -> checkMatchedPair(item, position) }
            2 -> { item, position -> checkUnmatchedPair(item, position) }
            else -> { _, _ -> Pair(false, false) }
        }

        computeHistory.forEach {
            var foundComputation = false
            var foundResult = false

            for (position in computeHistory.indices) {
                val checkResult = check(it, position)
                foundComputation = foundComputation || checkResult.first
                foundResult = foundResult || checkResult.second
            }

            if (!(foundComputation && foundResult) && throwError) {
                throw AssertionError("ViewHolder with text $it not found. History: $computeHistory")
            } else if (!(foundComputation && foundResult)) {
                return false
            }
        }

        return true
    }

    /**
     * Check that all items are displayed in the order in the history.
     *
     * @return [Boolean]: `true` if items are ordered, `false` otherwise
     */
    fun checkOrdered(): Boolean {
        return try {
            computeHistory.forEachIndexed { position, item ->
                matchesAtPosition(position, withHistoryItem(item.first, item.second))
            }

            true
        } catch (_: Throwable) {
            false
        }
    }

    /**
     * Run a set of checks to determine if the values displayed are shuffled.
     *
     * @param randomness [Int]: history randomness setting
     * @return [Boolean] `true` if the history passes all the checks, `false` otherwise
     */
    fun checkShuffled(randomness: Int): Boolean {
        if (computeHistory.size < 2) {
            return true
        }

        if (randomness == 0) {
            return checkOrdered()
        }

        val checks = when (randomness) {
            1 -> listOf(matchingShuffledCheck)
            2 -> listOf(shuffledComputationCheck, shuffledResultCheck)
            else -> listOf()
        }

        for (check in checks) {
            var checkPasses = false
            val values = check.first

            for (position in computeHistory.indices) {
                checkPasses = checkPasses || runShuffledCheck(values, position, check.second)
            }

            if (!checkPasses) {
                return false
            }
        }

        if (randomness == 2) {
            return checkPairsShuffled()
        }

        return true
    }

    /**
     * Check that a single history item is displayed at the specified position.
     * ViewHolder must contain both the computation and result indicated by the item.
     *
     * @param item [Pair]<[String], [String]>: item from test history, consisting of computation and result
     * @param position [Int]: position of ViewHolder to evaluate
     * @return [Pair]<[Boolean], [Boolean]>: pair of `true` if ViewHolder contains both values, or pair of `false` otherwise
     */
    private fun checkMatchedPair(item: Pair<String, String>, position: Int): Pair<Boolean, Boolean> {
        return try {
            matchesAtPosition(position, withHistoryItem(item.first, item.second))
            Pair(true, true)
        } catch (_: Throwable) {
            Pair(false, false)
        }
    }

    /**
     * Check if any part of a history item is displayed at the specified position.
     * Checks for the computation and result separately.
     *
     * @param item [Pair]<[String], [String]>: item from test history, consisting of computation and result
     * @param position [Int]: position of ViewHolder to evaluate
     * @return [Pair]<[Boolean], [Boolean]>: a pair where the first value indicates if the ViewHolder contains the computation string,
     * and the second value indicates if it contains the result string
     */
    private fun checkUnmatchedPair(item: Pair<String, String>, position: Int): Pair<Boolean, Boolean> {
        val matchesComputation = try {
            matchesAtPosition(position, withChild(withText(item.first)))
            onView(withViewHolder(recyclerId, position))
                .check(matches(withChild(withText(item.first))))
            true
        } catch (_: Throwable) {
            false
        }

        val matchesResult = try {
            matchesAtPosition(position, withChild(withChild(withText(item.second))))
            true
        } catch (_: Throwable) {
            false
        }

        return Pair(matchesComputation, matchesResult)
    }

    /**
     * Check that a ViewHolder contains any of the values that are not located at the current position.
     *
     * @param values [List]<*>: list of values to use for check, likely the list of history items,
     * computation strings, or result strings
     * @param position [Int]: position of value in list
     * @param getMatcher ([Any]) -> [Matcher]<[View]?>: function to get a matcher to use when evaluating a ViewHolder.
     * Uses values from [values] as input
     * @return `true` if the check passes based on the given values and matcher, `false` otherwise
     */
    private fun runShuffledCheck(values: List<*>, position: Int, getMatcher: (Any) -> Matcher<View?>): Boolean {
        return try {
            // get all values except value at current position
            val start = values.subList(0, position)
            val end = ternaryIf(
                position == values.lastIndex,
                emptyList(),
                values.subList(position + 1, values.size)
            )
            val reducedValues = start + end

            // viewholder should match any item except value at current position
            val matcher = anyOf(reducedValues.map { getMatcher(it!!) })
            matchesAtPosition(position, matcher)

            true
        } catch (_: Throwable) {
            false
        }
    }

    /**
     * Determine if the computation/result matches have been shuffled
     *
     * @return [Boolean]: true if at least one viewholder contains a mismatched computation/result, false if all pairs match
     */
    private fun checkPairsShuffled(): Boolean {
        if (computeHistory.size < 2) {
            return true
        }

        val computeTextMatcher = anyOf(computeHistory.map { withChild(withText(it.first)) })
        val resultTextMatcher = anyOf(computeHistory.map { withChild(withChild(withText(it.second))) })
        var historyMatcher: Matcher<View?> = anyOf(computeHistory.map { withHistoryItem(it.first, it.second) })
        if (computeHistory.size == 2) {
            // normal matcher has issues when size is 2
            historyMatcher = withHistoryItem(computeHistory[0].first, computeHistory[1].second)
        }

        for (position in computeHistory.indices) {
            try {
                // both values are part of the history, but value doesn't match a real item
                matchesAtPosition(position, allOf(computeTextMatcher, resultTextMatcher, not(historyMatcher)))

                return true
            } catch (_: Throwable) {}
        }

        return false
    }

    /**
     * Scroll to a given position and check for match at that position
     *
     * @param position [Int]: position to scroll to
     * @param matcher [Matcher]<[View]?>: matcher to use at position
     */
    private fun matchesAtPosition(position: Int, matcher: Matcher<View?>) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).check(matches(matcher))
    }

    /**
     * Check that the randomness is in the allowed range, and throw an exception if it is not
     *
     * @param randomness [Int]: value to check
     */
    private fun checkAllowedRandomness(randomness: Int) {
        if (randomness !in 0..2) {
            throw IllegalArgumentException("Invalid randomness value: $randomness")
        }
    }
}
