package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import junit.framework.AssertionFailedError
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.testutils.withAnyText
import xyz.lbres.trickcalculator.testutils.withEmptyString

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

fun testClearOnError() {
    openSettingsFragment()
    onView(withId(R.id.clearOnErrorSwitch)).perform(click())
    closeFragment()

    runSingleClearErrorTest("+", "Error: Syntax error")
    runSingleClearErrorTest("0.", "Error: Syntax error")

    typeText("0.")
    errorText.check(isNotPresented())
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // previously computed
    typeText("3")
    equals()
    typeText(".")
    errorText.check(isNotPresented())
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText("Error: Syntax error")))

    // valid text not cleared
    typeText("3/4")
    equals()
    val options = setOf(7, -1, 12, 0.75)
    mainText.check(matches(withAnyText(resultsOf(options))))

    // divide by zero
    toggleShuffleOperators()
    clearText()
    runSingleClearErrorTest("1/0", "Error: Divide by zero")
}

fun testRandomizeSigns() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.randomizeSignsSwitch))
        .perform(click())
        .check(matches(isChecked()))
    closeFragment()

    var options: Set<Number> = setOf(5, -5)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, "5")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, "-5")

    options = setOf(3, -1, -3, 1)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, "1+2")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, "-1-2")

    options = setOf(0.6, -0.6)
    var text = "-.75/1.25"
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, text)

    options = setOf(0.25, 1.75, -0.25, -1.75)
    text = "-1+3/4"
    checkMainTextMatchesMultiple(resultsOf(options), 2, 5, 10, text)

    options = setOf(6, 18, -6, -18)
    val texts = listOf("3", "(4+2)")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, texts)
}

fun testUnshuffleOperators() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    closeFragment()

    repeat(5) {
        clearText()
        typeText("-.2+5^2/(8-4x3)")
        equals()
        mainText.check(matches(withText("[-6.45]")))
    }

    clearText()
    typeText("1+2")
    equals()
    typeText("6")
    equals()
    mainText.check(matches(withText("[18]")))

    repeat(5) {
        clearText()
        typeText("1/0")
        equals()
        errorText.check(matches(isDisplayedWithText("Error: Divide by zero")))
    }
}

fun testShuffleNumbers() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    var options: MutableSet<Number> = (0..9).toMutableSet()
    checkMainTextMatchesMultiple(resultsOf(options), 3, 3, 10, "0")
    checkMainTextMatchesMultiple(resultsOf(options), 3, 3, 10, "7")

    clearText()
    options = mutableSetOf(0, 1.11, 2.22, 3.33, 4.44, 5.55, 6.66, 7.77, 8.88, 9.99)
    checkMainTextMatchesMultiple(resultsOf(options), 3, 3, 10, "4.44")

    clearText()
    options = mutableSetOf()
    for (i in 0..9) {
        for (j in 0..9) {
            for (k in 0..9) {
                if (i != j && i != k && j != k) {
                    val number = i * 100 + j * 10 + k
                    options.add(-number)
                }
            }
        }
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "-123")

    clearText()
    options = mutableSetOf()
    for (i in 0..9) {
        for (j in 0..9) {
            for (k in 0..9) {
                if (i != j && i != k && j != k) {
                    val first = i * 10 + j
                    val second = k
                    options.add(first + second)
                }
            }
        }
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "02+9")

    clearText()
    options = mutableSetOf()
    for (i in 0..9) {
        for (j in 0..9) {
            for (k in 0..9) {
                if (i != j && i != k && j != k) {
                    val first = i
                    val second = j * 10 + i
                    val third = k
                    options.add(first - second * third)
                }
            }
        }
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "2-82x7")

    var errorThrown = false
    repeatUntil(100, { errorThrown }) {
        clearText()
        typeText("1/2")
        equals()
        try {
            errorText.check(matches(isDisplayedWithText("Error: Divide by zero")))
            errorThrown = true
        } catch (_: AssertionFailedError) {}
    }

    if (!errorThrown) {
        throw AssertionError("Divide by zero error expected to be thrown")
    }
}

private fun runSingleClearErrorTest(text: String, error: String) {
    typeText(text)
    mainText.check(matches(withText(text)))
    errorText.check(matches(not(isDisplayed())))
    equals()
    mainText.check(matches(withEmptyString()))
    errorText.check(matches(isDisplayedWithText(error)))
}

private fun generateShuffledNumbers(transform: (Int, Int, Int) -> Number): Set<Number> {
    val numbers: MutableSet<Number> = mutableSetOf()
    for (i in 0..9) {
        for (j in 0..9) {
            for (k in 0..9) {
                if (i != j && i != k && j != k) {
                    numbers.add(transform(i, j, k))
                }
            }
        }
    }

    return numbers
}
