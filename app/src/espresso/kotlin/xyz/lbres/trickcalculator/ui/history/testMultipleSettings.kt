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

fun testMultipleNoApply() {
    val history = TestHistory()

    // no parens + no decimals
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click()) // enable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click()) // disable
    closeFragment()

    history.add(generateTestItem("100^2") { "10000" })
    history.add(generateTestItem("10.0^.2") { "10000" })
    history.add(generateTestItem("(15-3)/4x.6") { "10.5" })
    history.add(generateTestItem("(4/(1.5-3)x(3-.1)+10)+0.01") { "1.26667" })
    history.add(generateTestItem("(15-3)/(4x).6") { "Syntax error" }) // parens error
    history.add(generateTestItem("(15-3)/4x.6.") { "Syntax error" }) // decimal error

    // test with previously computed
    history.add(generateTestItem("(1.5-2)x4") { "7" })
    history.add(generateTestItem("/.21", "7") { "0.33333" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
}

fun testMultipleShuffle() {
    val history = TestHistory()

    // shuffle ops + shuffle nums
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // enable
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
    checkRandomness(history, 1)
}

fun testMultipleSettingsTypes() {
    val history = TestHistory()

    // shuffle ops, shuffle nums, shuffle comp, no apply decimals, no apply parens
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // enable
    onView(withId(R.id.shuffleComputationSwitch)).perform(click()) // enable
    onView(withId(R.id.applyParensSwitch)).perform(click()) // disable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // disable
    closeFragment()

    typeCombinationValues(history)

    // shuffle ops, shuffle nums, no apply parens
    openSettingsFragment()
    onView(withId(R.id.shuffleComputationSwitch)).perform(click()) // disable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // enable
    closeFragment()

    typeCombinationValues(history)

    // shuffle ops, no apply decimals, no apply parens
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click()) // disable
    onView(withId(R.id.applyDecimalsSwitch)).perform(click()) // disable
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
 * @param shuffleComputation [Boolean]: value of shuffled computation setting
 */
private fun typeShuffledValues(history: TestHistory, shuffleComputation: Boolean) {
    val expBase = ternaryIf(shuffleComputation, "23456", "23456.0987^7")

    history.add(generateTestItem("700x35-61") { getMainTextResult() })
    history.add(generateTestItem("$expBase^7") { getMainTextResult() })
    history.add(generateTestItem("14/(1.5-03)x(04.9)+10") { getMainTextResult() })
    history.add(generateTestItem("700x35-61/") { "Syntax error" })
}

/**
 * Type values that are used for all checks that have a combination of settings types, and add to history
 *
 * @param history [TestHistory]: history to add to
 */
private fun typeCombinationValues(history: TestHistory) {
    history.add(generateTestItem("700x35-61") { getMainTextResult() })
    history.add(generateTestItem("14/(1.5-03)x(04.9)+10") { getMainTextResult() })
    history.add(generateTestItem("700x35-61.") { "Syntax error" })
}

/**
 * Get the text from the main TextView.
 * Explicitly for using randomized results to generate history items. For other uses, use a [Matcher] or TextSaver.
 * Not stored as a public util function to avoid misuse out of convenience.
 *
 * @return [String] text in the main TextView, without the brackets surrounding a result, if applicable
 */
private fun getMainTextResult(): String {
    var text = ""

    val getText = object : ViewAction {
        override fun getConstraints(): Matcher<View> = withId(R.id.mainText)
        override fun getDescription(): String = "retrieving main text"

        override fun perform(uiController: UiController?, view: View?) {
            text = (view as TextView).text?.toString() ?: ""
        }
    }

    onView(withId(R.id.mainText)).perform(getText)

    if (text.startsWith('[')) {
        return text.substring(1, text.lastIndex) // strip [] around result
    }
    return text
}
