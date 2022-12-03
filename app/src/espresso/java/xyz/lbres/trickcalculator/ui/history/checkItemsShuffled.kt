package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

// pair of function to get list of values and function to create a matcher from a value
private typealias ShuffledCheckInfo = Pair<(TestHistory) -> List<*>, (Any) -> Matcher<View?>>

private const val recyclerId = R.id.itemsRecycler

private val matchedPairCheckInfo = ShuffledCheckInfo({ it }, {
    it as Pair<String, String>
    withHistoryItem(it.first, it.second)
})
private val computationCheckInfo = ShuffledCheckInfo(
    { it.map { it.first } },
    { withChild(withText(it as String)) }
)
private val resultCheckInfo = ShuffledCheckInfo(
    { it.map { it.second } },
    { withChild(withChild(withText(it as String))) }
)

private val checkInfoValues: Map<Int, List<ShuffledCheckInfo>> = mapOf(
    1 to listOf(matchedPairCheckInfo),
    2 to listOf(computationCheckInfo, resultCheckInfo)
)

fun checkItemsShuffled(computeHistory: TestHistory, randomness: Int): Boolean {
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    if (randomness !in checkInfoValues) {
        throw IllegalArgumentException("checkItemsShuffled not callable with randomness $randomness")
    }

    val checkInfo = checkInfoValues[randomness]!!
    var allChecksPass = true

    for (info in checkInfo) {
        var checkPasses = false
        val values = info.first(computeHistory)

        for (position in 0 until historySize) {
            checkPasses = checkPasses || doShuffledCheck(values, position, info.second)
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
