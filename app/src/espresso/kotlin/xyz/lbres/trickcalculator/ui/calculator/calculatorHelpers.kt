package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.clearSavedText
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.countDistinctValues
import xyz.lbres.trickcalculator.testutils.textsaver.TextSaver.Companion.saveText
import xyz.lbres.trickcalculator.testutils.withAnyText

private val mainText = onView(withId(R.id.mainText))

/**
 * Check that the main textview matches several different values when repeating test
 *
 * @param options [Collection]<String>: valid values for main textview
 * @param minMatches [Int]: minimum number of distinct values for main textview
 * @param minIterations [Int]: minimum number of times to run test
 * @param maxIterations [Int]: maximum number of times to run test
 * @param enterText () -> [Unit]: function to enter text into the main textview
 */
fun checkMainTextMatchesMultiple(
    options: Collection<String>,
    minMatches: Int,
    minIterations: Int,
    maxIterations: Int,
    enterText: () -> Unit,
) {
    clearText()
    mainText.perform(clearSavedText())
    var distinctValues = 0
    var i = 0
    while (i < maxIterations && (i < minIterations || distinctValues < minMatches)) {
        enterText()
        mainText.perform(saveText())
        mainText.check(matches(withAnyText(options)))
        distinctValues = countDistinctValues(R.id.mainText)
        clearText()

        i++
    }

    if (distinctValues < minMatches) {
        throw AssertionError(
            "Number of distinct values expected to be at least $minMatches. Actual number: $distinctValues",
        )
    }
}

/**
 * Leave calculator fragment by opening attributions fragment, then return by closing attributions fragment
 */
fun leaveAndReturn() {
    openAttributionsFragment()
    closeFragment()
}

/**
 * Map a set of numbers to strings, in the format of a computation result
 *
 * @param numberOptions [Set]<Number>: numbers to map
 * @return [List]<String>: list where each number has been cast to a string, surrounded by square brackets
 */
fun resultsOf(numberOptions: Set<Number>): List<String> = numberOptions.map { "[$it]" }
