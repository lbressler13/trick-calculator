package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.allOf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openSettingsFromDialog
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.saveTextAtPosition
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.withSavedTextAtPosition
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition

private val historyButtonIds = listOf(R.id.historyButton0, R.id.historyButton1, R.id.historyButton2, R.id.historyButton3)
private const val recyclerId = R.id.itemsRecycler

/**
 * From the history fragment, open the settings fragment, set the randomness,
 * and verify that values don't change when settings is closed and re-opened
 *
 * @param history [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 */
fun runSingleNotReshuffledCheck(history: TestHistory, randomness: Int) {
    val buttonId = historyButtonIds[randomness]

    openSettingsFromDialog()
    onView(withId(buttonId)).perform(click())
    closeFragment()

    // save values
    for (position in 0 until history.size) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).perform(
            saveTextAtPosition(position, R.id.computeText),
            saveTextAtPosition(position, R.id.resultText),
            saveTextAtPosition(position, R.id.errorText)
        )
    }

    openSettingsFromDialog()
    onView(withId(R.id.clearOnErrorSwitch)).perform(click()) // unrelated settings change
    closeFragment()

    // match saved values
    for (position in 0 until history.size) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position)).check(
            matches(
                allOf(
                    withSavedTextAtPosition(position, R.id.computeText),
                    withSavedTextAtPosition(position, R.id.resultText),
                    withSavedTextAtPosition(position, R.id.errorText)
                )
            )
        )
    }
}