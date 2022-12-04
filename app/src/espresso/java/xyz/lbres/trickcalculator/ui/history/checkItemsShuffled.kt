package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

/**
 * Pair of function to check if a list of values is shuffled.
 * Fist function extracts values from the history, and second function generates a [Matcher] based on a single value.
 */
typealias ShuffledCheckInfo = Pair<(TestHistory) -> List<*>, (Any) -> Matcher<View?>>

private const val recyclerId = R.id.itemsRecycler

/**
 * Run a set of checks to determine if the values displayed are shuffled.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param shuffledChecks [List]<[ShuffledCheckInfo]>: list of checks to determine if the history meets some criteria for being shuffled
 * @return [Boolean] `true` if the history passes all the checks, `false` otherwise
 */
fun checkItemsShuffled(computeHistory: TestHistory, shuffledChecks: List<ShuffledCheckInfo>): Boolean {
    if (computeHistory.size < 2) {
        return true
    }

    for (check in shuffledChecks) {
        var checkPasses = false
        val values = check.first(computeHistory)

        for (position in computeHistory.indices) {
            checkPasses = checkPasses || runCheck(values, position, check.second)
        }

        if (!checkPasses) {
            return false
        }
    }

    return true
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
private fun runCheck(values: List<*>, position: Int, getMatcher: (Any) -> Matcher<View?>): Boolean {
    return try {
        // get all values except value at current position
        val start = values.subList(0, position)
        val end = ternaryIf(
            position == values.lastIndex,
            emptyList(),
            values.subList(position + 1, values.size)
        )
        val reducedValues = start + end

        val matcher = anyOf(reducedValues.map { getMatcher(it!!) })
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        // viewholder should match any item except value at current position
        onView(withViewHolder(recyclerId, position)).check(matches(matcher))

        true
    } catch (_: Throwable) {
        false
    }
}
