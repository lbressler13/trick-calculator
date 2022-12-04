package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment

/**
 * Representation of a compute history displayed in the UI.
 */
typealias TestHistory = List<TestHI>

/**
 * Representation of a history item displayed in the UI.
 * First value is computation string (first row in UI), and second value is result/error (second row in UI).
 */
typealias TestHI = Pair<String, String>

/**
 * Create [Matcher] to identify that a history item displays the expected computation text and result string
 *
 * @param computation [String]: first string displayed in UI
 * @param result [String]: second string displayed in UI
 * @return [Matcher]<[View]?>: matcher that a view contains the result and computation string
 */
fun withHistoryItem(computation: String, result: String): Matcher<View?> {
    return allOf(
        withChild(withText(computation)),
        withChild(withChild(withText(result)))
    )
}

/**
 * Create [Matcher] to identify that a history item displays the expected computation text and result string
 *
 * @param item [TestHI]: item to check
 * @return [Matcher]<[View]?>: matcher that a view contains the specified history item
 */
fun withHistoryItem(item: TestHI): Matcher<View?> = withHistoryItem(item.first, item.second)

/**
 * Update the history randomness setting.
 * Must be called from calculator screen, and returns to calculator screen after updating setting.
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
