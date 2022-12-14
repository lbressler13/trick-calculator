package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private const val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)x3"

// TODO history.add(itemWithResult("comp"))

fun testShuffleOperators() {
    val history = TestHistory()

    addItemWithResult("3", history)
    addItemWithResult("-1/2", history, previousResult = "3")

    // long decimal
    addItemWithResult("0.123456", history)

    checkRandomness(history, 0)

    addItemWithResult("1+2-2/3x1", history)
    addItemWithResult("(1+2)(4-2)", history)
    addItemWithResult("2x(1-9)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult(longText, history)

    // with error
    addItemWithError("4+5()-44", "Syntax error", history)

    addItemWithResult("4+(5)-44", history)
    addItemWithResult(".00000003", history)
    addItemWithResult("55^6", history)

    // multiple errors
    addItemWithError("400/3..3", "Syntax error", history)
    addItemWithError("(500-5))-6", "Syntax error", history)

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

fun testShuffleNumbers() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // one digit
    addItemWithResult("1", history)
    addItemWithResult("22.22222", history)
    addItemWithResult("00000", history)

    checkRandomness(history, 0)

    // multiple digits
    addItemWithResult("123.456", history)
    addItemWithResult("8900000", history)

    checkRandomness(history, 0)
    checkRandomness(history, 1)

    // operators
    addItemWithResult("1+2-2/3x1", history)
    addItemWithResult("(1+2)(4-2)", history)
    addItemWithResult("2x(1-9)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult("55+16/3-4-3+23/66x44(20+30)", history)

    // with error
    addItemWithError("4+5()-44", "Syntax error", history)

    addItemWithResult("4+(5)-44", history)
    addItemWithResult(".00000003", history)
    addItemWithResult("55^6", history)
    addItemWithResult(longText, history)

    // multiple errors
    addItemWithError("400/3..3", "Syntax error", history)
    addItemWithError("(500-5))-6", "Syntax error", history)


    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

fun testShuffleComputation() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.shuffleComputationSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    addItemWithResult("3", history)
    addItemWithResult("-1/2", history, previousResult = "3")

    // long decimal
    addItemWithResult("0.123456", history)

    checkRandomness(history, 0)

    addItemWithResult("1+2-2/3x1", history)
    addItemWithResult("(1+2)(4-2)", history)
    addItemWithResult("2x(1-9)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult("1.2x5(4)", history)
    addItemWithResult(longText, history)

    // with error
    addItemWithError("4+5()-44", "Syntax error", history)

    addItemWithResult("4+(5)-44", history)
    addItemWithResult(".00000003", history)
    addItemWithResult("55^6", history)

    // multiple errors
    addItemWithError("400/3..3", "Syntax error", history)
    addItemWithError("(500-5))-6", "Syntax error", history)

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

/**
 * Type a computation in the main TextView, get the result, and add to compute history
 *
 * @param computeText [String]: the computation to run
 * @param computeHistory [TestHistory]: mutable history to add to
 * @param previousResult [String]: result of previous computation, to use in creating the text for the history item.
 * Defaults to empty string.
 */
private fun addItemWithResult(computeText: String, computeHistory: TestHistory, previousResult: String = "") {
    val maxDecimalLength = 5

    if (previousResult == "") {
        clearText()
    }
    typeText(computeText)
    equals()

    var result = getMainText()
    result = result.substring(1, result.lastIndex) // trim [] around result

    // shorten decimal
    if (result.contains('.')) {
        val pieces = result.split('.')
        val decimal = BigDecimal("." + pieces[1])
        val mc = MathContext(maxDecimalLength, RoundingMode.HALF_UP)
        val fullDecimalString = decimal.round(mc).toEngineeringString()
        val decimalString = fullDecimalString.substringAfter('.')

        result = "${pieces[0]}.$decimalString"
    }

    computeHistory.add(TestHI(previousResult + computeText, result))
}

/**
 * Type a computation in the text and add text+error to compute history
 *
 * @param computeText [String]: the computation to run
 * @param error [String]: the expected error message
 * @param computeHistory [TestHistory]: mutable history to add to
 */
private fun addItemWithError(computeText: String, error: String, computeHistory: TestHistory) {
    clearText()
    typeText(computeText)
    equals()
    computeHistory.add(TestHI(computeText, error))
}

/**
 * Run a randomness check for a compute history, including a retry
 *
 * @param history [TestHistory]
 * @param randomness [Int]: history randomness setting
 */
private fun checkRandomness(history: TestHistory, randomness: Int) {
    setHistoryRandomness(randomness)
    openHistoryFragment()

    history.checkDisplayed(randomness)

    if (randomness.isZero()) {
        history.checkOrdered()
    } else {
        // one retry in case of small probability where numbers aren't shuffled
        try {
            history.checkShuffled(randomness)
        } catch (_: Throwable) {
            history.checkShuffled(randomness)
        }
    }

    closeFragment()
}

/**
 * Get the text from the main TextView.
 * Explicitly for using randomized results to generate history items.
 * For other uses, use a [Matcher] or TextSaver
 *
 * @return [String]
 */
private fun getMainText(): String {
    var text = ""

    val viewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = withId(R.id.mainText)
        override fun getDescription(): String = "retrieving main text"

        override fun perform(uiController: UiController?, view: View?) {
            text = (view as TextView).text?.toString() ?: ""
        }
    }

    onView(withId(R.id.mainText)).perform(viewAction)
    return text
}
