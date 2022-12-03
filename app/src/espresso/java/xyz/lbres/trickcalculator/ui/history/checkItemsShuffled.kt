package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.anyOf
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private const val recyclerId = R.id.itemsRecycler

private val computationShuffledFunctions: Map<Int, (Int, TestHistory) -> Boolean> = mapOf(
    1 to { position, history -> checkMatchedShuffled(position, history) },
    2 to { position, history -> checkUnmatchedComputation(position, history) }
)
private val resultShuffledFunctions: Map<Int, ((Int, TestHistory) -> Boolean)?> = mapOf(
    1 to null,
    2 to { position, history -> checkUnmatchedResult(position, history) }
)

fun checkItemsShuffled(computeHistory: TestHistory, randomness: Int): Boolean {
    val historySize = computeHistory.size
    if (historySize < 2) {
        return true
    }

    if (randomness !in computationShuffledFunctions || randomness !in resultShuffledFunctions) {
        throw IllegalArgumentException("checkItemsShuffled not callable with randomness $randomness")
    }

    val checkComputation = computationShuffledFunctions[randomness]!!
    val checkResult = resultShuffledFunctions[randomness]

    var computationsShuffled = false
    var resultsShuffled = false

    for (position in 0 until historySize) {
        computationsShuffled = computationsShuffled || checkComputation(position, computeHistory)
    }

    if (checkResult == null) {
        resultsShuffled = computationsShuffled
    } else {
        for (position in 0 until historySize) {
            resultsShuffled = resultsShuffled || checkResult(position, computeHistory)
        }
    }

    return computationsShuffled && resultsShuffled
}

private fun checkMatchedShuffled(position: Int, history: TestHistory): Boolean {
    val historySize = history.size

    return try {
        onView(withId(recyclerId)).perform(scrollToPosition(position))

        val start = history.subList(0, position)
        val end = ternaryIf(
            position == historySize - 1,
            emptyList(),
            history.subList(position + 1, historySize)
        )

        val reducedHistory = start + end
        checkViewHolderInHistory(position, reducedHistory)

        true
    } catch (_: Throwable) {
        false
    }
}

private fun checkUnmatchedComputation(position: Int, history: TestHistory): Boolean {
    val computations = history.map { it.first }
    val historySize = history.size

    return try {
        onView(withId(recyclerId)).perform(scrollToPosition(position))

        val start = computations.subList(0, position)
        val end = ternaryIf(
            position == historySize - 1,
            emptyList(),
            computations.subList(position + 1, historySize)
        )
        val reducedString = start + end

        val matcher = anyOf(reducedString.map { withChild(withText(it)) })
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).check(matches(matcher))

        true
    } catch (_: Throwable) {
        false
    }
}

private fun checkUnmatchedResult(position: Int, history: TestHistory): Boolean {
    val results = history.map { it.second }
    val historySize = history.size

    return try {
        onView(withId(recyclerId)).perform(scrollToPosition(position))

        val start = results.subList(0, position)
        val end = ternaryIf(
            position == historySize - 1,
            emptyList(),
            results.subList(position + 1, historySize)
        )
        val reducedStrings = start + end

        val matcher = anyOf(reducedStrings.map { withChild(withChild(withText(it))) })
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).check(matches(matcher))

        true
    } catch (_: Throwable) {
        false
    }
}
