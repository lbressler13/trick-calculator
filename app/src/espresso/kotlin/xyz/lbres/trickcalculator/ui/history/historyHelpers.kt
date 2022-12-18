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
 * Type a computation in the main TextView, get the result, and generate a history item.
 *
 * @param computeText [String]: the computation to run
 * @param previousResult [String]: result of previous computation, to use in creating the text for the history item.
 * Defaults to empty string.
 * @param getResult () -> [String]: function to get the result to use in the history item
 * @return [TestHI]: history item with the typed text and generated result
 */
fun generateHI(computeText: String, previousResult: String = "", getResult: () -> String): TestHI {
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
