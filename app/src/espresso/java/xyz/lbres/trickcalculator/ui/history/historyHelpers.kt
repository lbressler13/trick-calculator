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
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

/**
 * Representation of a history item displayed in the UI.
 * First value is computation string (first row in UI),
 * and second value is result/error (second row in UI).
 */
typealias TestHistory = List<Pair<String, String>>

/**
 * [Matcher] to identify that a history item displays the expected computation text and result string
 * First value is computation string (first row in UI),
 * and second value is result/error (second row in UI).
 */
val withHistoryItem: (String, String) -> Matcher<View> = { computation: String, result: String ->
    allOf(
        withChild(withText(computation)),
        withChild(withChild(withText(result)))
    )
}

/**
 * Check that the values in a view holder match one of the items in the compute history
 *
 * @param position [Int]: position of view holder to check
 * @param computeHistory [TestHistory]: list of items in history
 */
fun checkViewHolderInHistory(position: Int, computeHistory: TestHistory) {
    val recyclerId = R.id.itemsRecycler
    val historyMatcher = anyOf(computeHistory.map { withHistoryItem(it.first, it.second) })

    onView(withId(recyclerId)).perform(scrollToPosition(position))
    onView(withViewHolder(recyclerId, position)).check(matches(historyMatcher))
}

/**
 * Check that the values in a view holder do not match any of the items in the compute history
 *
 * @param position [Int]: position of view holder to check
 * @param computeHistory [TestHistory]: list of items in history
 */
fun checkViewHolderNotInHistory(position: Int, computeHistory: TestHistory) {
    val recyclerId = R.id.itemsRecycler
    val historyMatcher = anyOf(computeHistory.map { withHistoryItem(it.first, it.second) })

    onView(withId(recyclerId)).perform(scrollToPosition(position))
    onView(withViewHolder(recyclerId, position)).check(matches(not(historyMatcher)))
}

/**
 * Update the history randomness setting.
 * Must be called from main screen, and returns to main screen after updating setting.
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
