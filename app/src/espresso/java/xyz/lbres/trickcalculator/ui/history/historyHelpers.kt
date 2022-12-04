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
 * Representation of a history item displayed in the UI.
 * First value is computation string (first row in UI), and second value is result/error (second row in UI).
 */
typealias TestHistory = List<Pair<String, String>>

/**
 * [Matcher] to identify that a history item displays the expected computation text and result string
 * First value is computation string (first row in UI),
 * and second value is result/error (second row in UI).
 */
val withHistoryItem: (String, String) -> Matcher<View?> = { computation: String, result: String ->
    allOf(
        withChild(withText(computation)),
        withChild(withChild(withText(result)))
    )
}

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
