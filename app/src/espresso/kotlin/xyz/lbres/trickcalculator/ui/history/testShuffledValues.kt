package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun testShuffleOperators() {
    val computeHistory = mutableListOf<TestHI>()

    addItemWithResult("3", computeHistory)
    addItemWithResult("-1/2", computeHistory, previousResult = "3")

    // long decimal
    addItemWithResult("0.123456", computeHistory)

    var checker = HistoryChecker(computeHistory)
    checkRandomness(checker, 0)

    addItemWithResult("1+2-2/3x1", computeHistory)
    addItemWithResult("(1+2)(4-2)", computeHistory)
    addItemWithResult("2x(1-9)", computeHistory)
    addItemWithResult("1.2x5(4)", computeHistory)
    addItemWithResult("1.2x5(4)", computeHistory)

    // with error
    addItemWithError("4+5()-44", "Syntax error", computeHistory)

    addItemWithResult("4+(5)-44", computeHistory)
    addItemWithResult(".00000003", computeHistory)
    addItemWithResult("55^6", computeHistory)

    // multiple errors
    addItemWithError("400/3..3", "Syntax error", computeHistory)
    addItemWithError("(500-5))-6", "Syntax error", computeHistory)

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)x3"
    addItemWithResult(longText, computeHistory)

    checker = HistoryChecker(computeHistory)
    checkRandomness(checker, 0)
    checkRandomness(checker, 1)
    checkRandomness(checker, 2)
}

fun testShuffledNumbers() {}

fun testShuffleComputation() {}

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
 * @param checker [HistoryChecker]
 * @param randomness [Int]: history randomness setting
 */
private fun checkRandomness(checker: HistoryChecker, randomness: Int) {
    setHistoryRandomness(randomness)
    openHistoryFragment()

    if (randomness.isZero()) {
        checker.runAllChecks(0)
    } else {
        // one retry in case of small probability where numbers aren't shuffled
        try {
            checker.runAllChecks(randomness)
        } catch (_: Throwable) {
            checker.runAllChecks(randomness)
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
