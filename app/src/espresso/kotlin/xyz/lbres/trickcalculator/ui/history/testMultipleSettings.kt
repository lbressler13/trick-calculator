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
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

fun testMultipleSettings() {
    val history = TestHistory()

    // no parens + no decimals
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click()) // enable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // disable
    closeFragment()

    history.add(generateHI("100^2") { "10000" })
    history.add(generateHI("10.0^2") { "10000" })
    history.add(generateHI("10.0^.2") { "10000" })
    history.add(generateHI("(15-3)/4x.6") { "10.5" })
    history.add(generateHI("(4/(1.5-3)x(3-.1)+10)+0.01") { "1.26667" })
    history.add(generateHI("(15-3)/(4x).6") { "Syntax error" })
    history.add(generateHI("(15-3)/4x.6.") { "Syntax error" })

    // test with previously computed
    history.add(generateHI("(1.5-2)x4") { "7" })
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

/**
 * Type values that are used for all checks that have only shuffled settings, and add to history
 *
 * @param history [TestHistory]: history to add to
 */
private fun typeShuffledValues(history: TestHistory, shuffleComputation: Boolean) {
    val expBase = ternaryIf(shuffleComputation, "23456", "23456.0987654^7")

    history.add(generateHI("12345") { getMainTextResult() })
    history.add(generateHI("700x35-61") { getMainTextResult() })
    history.add(generateHI("$expBase^7") { getMainTextResult() })
    history.add(generateHI("01+23-45x56/78+90-12x34/56") { getMainTextResult() })
    history.add(generateHI("(14/(1.5-03)x(03-.1)+10)+0.01") { getMainTextResult() })
    history.add(generateHI("700x35-61/") { "Syntax error" })
}

/**
 * Type values that are used for all checks that have a combination of settings types, and add to history
 *
 * @param history [TestHistory]: history to add to
 */
private fun typeCombinationValues(history: TestHistory) {
    history.add(generateHI("700x35-61") { getMainTextResult() })
    history.add(generateHI("01+23-45x56/78+90-12x34/56") { getMainTextResult() })
    history.add(generateHI("(14/(1.5-03)x(03-.1)+10)+0.01") { getMainTextResult() })
    history.add(generateHI("700x35-61.") { "Syntax error" })
}

/**
 * Get the text from the main TextView.
 * Explicitly for using randomized results to generate history items.
 * For other uses, use a [Matcher] or TextSaver
 *
 * @return [String]
 */
private fun getMainTextResult(): String {
    var text = ""

    val viewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = withId(R.id.mainText)
        override fun getDescription(): String = "retrieving main text"

        override fun perform(uiController: UiController?, view: View?) {
            text = (view as TextView).text?.toString() ?: ""
        }
    }

    onView(withId(R.id.mainText)).perform(viewAction)

    if (text.startsWith('[')) {
        return text.substring(1, text.lastIndex) // strip [] around result
    }
    return text
}
