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

// pair of function to get list of values and function to create a matcher from a value
typealias ShuffledCheckInfo = Pair<(TestHistory) -> List<*>, (Any) -> Matcher<View?>>

private const val recyclerId = R.id.itemsRecycler

/**
 * Wrapper function to run a set of checks to determine if the values displayed are shuffled.
 */
fun checkItemsShuffled(computeHistory: TestHistory, shuffledChecks: List<ShuffledCheckInfo>): Boolean {
    if (computeHistory.size < 2) {
        return true
    }

    var allChecksPass = true

    for (check in shuffledChecks) {
        var checkPasses = false
        val values = check.first(computeHistory)

        for (position in computeHistory.indices) {
            checkPasses = checkPasses || doShuffledCheck(values, position, check.second)
        }

        allChecksPass = allChecksPass && checkPasses
    }

    return allChecksPass
}

/**
 * Check that a ViewHolder contains any of the values that are not located at the current position.
 *
 * @param values [List]<*>: list of values to use for check, likely the list of history items,
 * computation strings, or result strings
 * @param position [Int]: position of value in list/ViewHolder
 * @param getMatcher ([Any]) -> [Matcher]<[View]?>: function to get a matcher to use when evaluating a ViewHolder.
 * Uses values from [values] as input
 * @return `true` if the check passes based on the given values and matcher, `false` otherwise
 */
private fun doShuffledCheck(
    values: List<*>,
    position: Int,
    getMatcher: (Any) -> Matcher<View?>
): Boolean {
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
