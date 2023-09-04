package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import xyz.lbres.kotlinutils.pair.TypePair
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Test representation of a history item displayed in the UI.
 * First value is computation string (first row in UI), and second value is result/error (second row in UI).
 */
typealias TestHI = Pair<String, String>

/**
 * [Matcher] to identify that a history item displays the expected computation text and result string
 */
val withHistoryItem: (String, String) -> Matcher<View?> = { computation, errorResult ->
    allOf(withChild(withText(computation)), withChild(withChild(withText(errorResult))))
}

/**
 * Wrapper function to scroll a RecyclerView to [position], with ViewHolder type [HistoryItemViewHolder]
 *
 * @param position [Int]: position to scroll to
 * @return [ViewAction]: action to scroll to the given position
 */
fun scrollToHistoryItemAtPosition(position: Int): ViewAction {
    return RecyclerViewActions.scrollToPosition<HistoryItemViewHolder>(position)
}

/**
 * Wrapper function to perform an action at a given index in a RecyclerView, with ViewHolder type [HistoryItemViewHolder]
 *
 * @param position [Int]: position to perform action
 * @param action [ViewAction]: action to perform
 * @return [ViewAction]: action to perform given [action] on the ViewHolder at position [position]
 */
fun actionOnHistoryItemAtPosition(position: Int, action: ViewAction): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<HistoryItemViewHolder>(position, action)
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

/**
 * Test that the "No history" message is displayed.
 * Must be called from calculator screen, and returns to calculator screen after check is performed
 */
fun checkNoHistoryMessageDisplayed() {
    openHistoryFragment()
    onView(withText("No history")).check(matches(isDisplayed()))
    closeFragment()
}

/**
 * Test that the "No history" message is not presented.
 * Must be called from calculator screen, and returns to calculator screen after check is performed
 */
fun checkNoHistoryMessageNotPresented() {
    openHistoryFragment()
    onView(withText("No history")).check(isNotPresented())
    closeFragment()
}

/**
 * Type a computation in the main TextView, get the result, and generate a history item.
 *
 * @param computeText [String]: the computation to run
 * @param previousResult [String]: result of previous computation, to use in creating the text for the history item.
 * Defaults to empty string.
 * @param getResult () -> [String]: function to get the result of the computation
 * @return [TestHI]: history item with the typed text and generated result
 */
fun generateTestItem(computeText: String, previousResult: String = "", getResult: () -> String): TestHI {
    val maxDecimalLength = 5

    if (previousResult == "") {
        clearText()
    }
    typeText(computeText)
    equals()

    var result = getResult()

    // shorten decimal
    if (result.contains('.')) {
        val pieces = result.split('.', 'E')
        val decimal = BigDecimal("." + pieces[1])
        val mc = MathContext(maxDecimalLength, RoundingMode.HALF_UP)
        val fullDecimalString = decimal.round(mc).toEngineeringString()
        val decimalString = fullDecimalString.substringAfter('.')
        val eString = if (pieces.size == 3) {
            "E${pieces[2]}"
        } else {
            ""
        }

        result = "${pieces[0]}.$decimalString$eString"
    }

    return TestHI(previousResult + computeText, result)
}

/**
 * Get the computation and result/error text for a history item ViewHolder at a given position
 *
 * @param position [Int]: position of ViewHolder
 * @return [TypePair]<String>: pair where first value represents the compute text and second value represents result/error
 */
fun getViewHolderTextAtPosition(position: Int): TypePair<String> {
    var computation = ""
    var errorResult = ""

    val getViewHolderText = object : ViewAction {
        override fun getConstraints(): Matcher<View> = isDisplayed()
        override fun getDescription(): String = "retrieving text from viewholder at position $position"

        override fun perform(uiController: UiController?, view: View?) {
            computation = view?.findViewById<TextView>(R.id.computeText)?.text?.toString() ?: ""

            val number = view?.findViewById<TextView>(R.id.resultText)?.text?.toString()
            val error = view?.findViewById<TextView>(R.id.errorText)?.text?.toString()
            errorResult = when {
                !number.isNullOrBlank() -> number
                !error.isNullOrBlank() -> error
                else -> ""
            }
        }
    }

    onView(withId(R.id.itemsRecycler)).perform(
        actionOnHistoryItemAtPosition(position, getViewHolderText)
    )

    return Pair(computation, errorResult)
}
