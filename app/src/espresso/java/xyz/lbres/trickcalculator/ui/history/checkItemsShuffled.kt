package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

// pair of function to get list of values and function to create a matcher from a value
typealias ShuffledCheckInfo = Pair<(TestHistory) -> List<*>, (Any) -> Matcher<View?>>

private const val recyclerId = R.id.itemsRecycler

fun checkItemsShuffled(computeHistory: TestHistory, checks: List<ShuffledCheckInfo>): Boolean {
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    var allChecksPass = true

    for (check in checks) {
        var checkPasses = false
        val values = check.first(computeHistory)

        for (position in 0 until historySize) {
            checkPasses = checkPasses || doShuffledCheck(values, position, check.second)
        }

        allChecksPass = allChecksPass && checkPasses
    }

    return allChecksPass
}

private fun doShuffledCheck(
    values: List<*>,
    position: Int,
    getMatcher: (Any) -> Matcher<View?>
): Boolean {
    return try {
        onView(withId(recyclerId)).perform(scrollToPosition(position))

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
        onView(withViewHolder(recyclerId, position)).check(matches(matcher))

        true
    } catch (_: Throwable) {
        false
    }
}
