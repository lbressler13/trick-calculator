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

fun testShuffleOperators() {
    val history = TestHistory()

    history.add(generateItemWithResult("3"))
    history.add(generateItemWithResult("-1/2", previousResult = "3"))

    // long decimal
    history.add(generateItemWithResult("0.123456"))

    checkRandomness(history, 0)

    history.add(generateItemWithResult("1+2-2/3x1"))
    history.add(generateItemWithResult("(1+2)(4-2)"))
    history.add(generateItemWithResult("2x(1-9)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult(longText))

    // with error
    history.add(generateItemWithError("4+5()-44", "Syntax error"))

    history.add(generateItemWithResult("4+(5)-44"))
    history.add(generateItemWithResult(".00000003"))
    history.add(generateItemWithResult("55^6"))

    // multiple errors
    history.add(generateItemWithError("400/3..3", "Syntax error"))
    history.add(generateItemWithError("(500-5))-6", "Syntax error"))

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
    history.add(generateItemWithResult("1"))
    history.add(generateItemWithResult("22.22222"))
    history.add(generateItemWithResult("00000"))

    checkRandomness(history, 0)

    // multiple digits
    history.add(generateItemWithResult("123.456"))
    history.add(generateItemWithResult("8900000"))

    checkRandomness(history, 0)
    checkRandomness(history, 1)

    // operators
    history.add(generateItemWithResult("1+2-2/3x1"))
    history.add(generateItemWithResult("(1+2)(4-2)"))
    history.add(generateItemWithResult("2x(1-9)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult("55+16/3-4-3+23/66x44(20+30)"))

    // with error
    history.add(generateItemWithError("4+5()-44", "Syntax error"))

    history.add(generateItemWithResult("4+(5)-44"))
    history.add(generateItemWithResult(".00000003"))
    history.add(generateItemWithResult("55^6"))
    history.add(generateItemWithResult(longText))

    // multiple errors
    history.add(generateItemWithError("400/3..3", "Syntax error"))
    history.add(generateItemWithError("(500-5))-6", "Syntax error"))

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

    history.add(generateItemWithResult("3"))
    history.add(generateItemWithResult("-1/2", previousResult = "3"))

    // long decimal
    history.add(generateItemWithResult("0.123456"))

    checkRandomness(history, 0)

    history.add(generateItemWithResult("1+2-2/3x1"))
    history.add(generateItemWithResult("(1+2)(4-2)"))
    history.add(generateItemWithResult("2x(1-9)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult("1.2x5(4)"))
    history.add(generateItemWithResult(longText))

    // with error
    history.add(generateItemWithError("4+5()-44", "Syntax error"))

    history.add(generateItemWithResult("4+(5)-44"))
    history.add(generateItemWithResult(".00000003"))
    history.add(generateItemWithResult("55^6"))

    // multiple errors
    history.add(generateItemWithError("400/3..3", "Syntax error"))
    history.add(generateItemWithError("(500-5))-6", "Syntax error"))

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

/**
 * Type a computation in the main TextView, get the result, and generate a history item.
 *
 * @param computeText [String]: the computation to run
 * @param previousResult [String]: result of previous computation, to use in creating the text for the history item.
 * Defaults to empty string.
 * @return [TestHI]: history item with the typed text and generated result
 */
private fun generateItemWithResult(computeText: String, previousResult: String = ""): TestHI {
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

    return TestHI(previousResult + computeText, result)
}

/**
 * Type a computation that generates an error, and return the appropriate history item.
 *
 * @param computeText [String]: the computation to run
 * @param error [String]: the expected error message
 * @return [TestHI]: history item with the specified compute text and error
 */
private fun generateItemWithError(computeText: String, error: String): TestHI {
    clearText()
    typeText(computeText)
    equals()
    return TestHI(computeText, error)
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

    history.checkAllDisplayed(randomness)

    if (randomness.isZero()) {
        history.checkDisplayOrdered()
    } else {
        // one retry in case of small probability where numbers aren't shuffled
        try {
            history.checkDisplayShuffled(randomness)
        } catch (_: Throwable) {
            history.checkDisplayShuffled(randomness)
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
