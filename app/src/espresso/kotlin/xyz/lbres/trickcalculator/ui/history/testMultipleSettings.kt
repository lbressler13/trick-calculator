package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.general.ternaryIf
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

fun testMultipleSettings() {
    val history = TestHistory()

    // no parens + no decimals
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click()) // enable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // disable
    closeFragment()

    history.add(generateItemWithKnownResult("100^2", "10000"))
    history.add(generateItemWithKnownResult("10.0^2", "10000"))
    history.add(generateItemWithKnownResult("10.0^.2", "10000"))
    history.add(generateItemWithKnownResult("(15-3)/4x.6", "10.5"))
    history.add(generateItemWithKnownResult("(4/(1.5-3)x(3-.1)+10)+0.01", "1.26667"))
    history.add(generateItemWithKnownResult("(15-3)/(4x).6", "Syntax error"))
    history.add(generateItemWithKnownResult("(15-3)/4x.6.", "Syntax error"))

    history.add(generateItemWithKnownResult("(1.5-2)x4", "7"))
    typeText("/.21")
    equals()
    history.add(TestHI("7/.21", "0.33333"))
    typeText("(1.5-02)x04")
    clearText()

    checkRandomness(history, 0)

    // shuffle ops + shuffle nums
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // enable
    onView(withId(R.id.applyParensSwitch)).perform(click()) // disable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // disable
    closeFragment()

    typeShuffledValues(history, shuffleComputation = false)

    // shuffle ops + shuffle comp
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // disable
    onView(withId(R.id.shuffleComputationSwitch)).perform(click()) // enable
    closeFragment()

    typeShuffledValues(history, shuffleComputation = true)

    // shuffle nums + shuffle comp
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // disable
    closeFragment()

    typeShuffledValues(history, shuffleComputation = true)

    // shuffle ops, nums, comp
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // enable
    closeFragment()

    typeShuffledValues(history, shuffleComputation = true)

    checkRandomness(history, 0)

    // combinations
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click()) // enable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    closeFragment()

    typeCombinationValues(history)

    openSettingsFragment()
    onView(withId(R.id.shuffleComputationSwitch)).perform(click()) // disable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // disable
    closeFragment()

    typeCombinationValues(history)

    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // enable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    closeFragment()

    typeCombinationValues(history)

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

private fun typeShuffledValues(history: TestHistory, shuffleComputation: Boolean) {
    val expBase = ternaryIf(shuffleComputation, "23456", "23456.0987654^7")

    history.add(generateItemWithShuffledResult("12345"))
    history.add(generateItemWithShuffledResult("700x35-61"))
    history.add(generateItemWithShuffledResult("$expBase^7"))
    history.add(generateItemWithShuffledResult("01+23-45x56/78+90-12x34/56"))
    history.add(generateItemWithShuffledResult("(14/(1.5-03)x(03-.1)+10)+0.01"))
    history.add(generateItemWithKnownResult("700x35-61/", "Syntax error"))
}

private fun typeCombinationValues(history: TestHistory) {
    history.add(generateItemWithShuffledResult("700x35-61"))
    history.add(generateItemWithShuffledResult("01+23-45x56/78+90-12x34/56"))
    history.add(generateItemWithShuffledResult("(14/(1.5-03)x(03-.1)+10)+0.01"))
    history.add(generateItemWithKnownResult("700x35-61.", "Syntax error"))
}

/**
 * Type a computation that generates an error or a non-shuffled result, and return the appropriate history item.
 *
 * @param computeText [String]: the computation to run
 * @param resultError [String]: the expected result or error
 * @return [TestHI]: history item with the specified compute text and result/error string
 */
private fun generateItemWithKnownResult(computeText: String, resultError: String): TestHI {
    clearText()
    typeText(computeText)
    equals()
    return TestHI(computeText, resultError)
}

/**
 * Type a computation in the main TextView, get the result with shuffled values, and generate a history item.
 *
 * @param computeText [String]: the computation to run
 * @param previousResult [String]: result of previous computation, to use in creating the text for the history item.
 * Defaults to empty string.
 * @return [TestHI]: history item with the typed text and generated result
 */
private fun generateItemWithShuffledResult(computeText: String, previousResult: String = ""): TestHI {
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
