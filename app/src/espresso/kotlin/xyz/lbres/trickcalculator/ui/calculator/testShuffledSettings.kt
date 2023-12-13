package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import junit.framework.AssertionFailedError
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.isDisplayedWithText
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.typeText

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

private val randomizeSignsSwitch = onView(withId(R.id.randomizeSignsSwitch))
private val shuffleComputationSwitch = onView(withId(R.id.shuffleComputationSwitch))
private val shuffleNumbersSwitch = onView(withId(R.id.shuffleNumbersSwitch))
private val shuffleOperatorsSwitch = onView(withId(R.id.shuffleOperatorsSwitch))

fun testRandomizeSigns() {
    openSettingsFragment()
    shuffleOperatorsSwitch.perform(click())
    randomizeSignsSwitch.perform(click())
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
    shuffleOperatorsSwitch.perform(click())
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
    shuffleOperatorsSwitch.perform(click())
    shuffleNumbersSwitch.perform(click())
    closeFragment()

    var options: Set<Number> = (0..9).toSet()
    checkMainTextMatchesMultiple(resultsOf(options), 10, 10, 100, "0")
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 5, "7")

    clearText()
    options = setOf(0, 1.11, 2.22, 3.33, 4.44, 5.55, 6.66, 7.77, 8.88, 9.99)
    checkMainTextMatchesMultiple(resultsOf(options), 3, 3, 10, "4.44")

    clearText()
    options = generateShuffledNumbers { i, j, k ->
        val number = i * 100 + j * 10 + k
        -number
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "-123")

    clearText()
    options = generateShuffledNumbers { i, j, k ->
        val first = i * 10 + j
        val second = k
        first + second
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "02+9")

    clearText()
    options = generateShuffledNumbers { i, j, k ->
        val first = i
        val second = j * 10 + i
        val third = k
        first - second * third
    }
    checkMainTextMatchesMultiple(resultsOf(options), 4, 5, 10, "2-82x7")

    // divide by zero
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

fun testShuffleComputation() {
    openSettingsFragment()
    shuffleOperatorsSwitch.perform(click())
    shuffleComputationSwitch.perform(click())
    closeFragment()

    typeText("-34")
    equals()
    mainText.check(matches(withText("[-34]")))
    clearText()

    var options = setOf(0.5, 2)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 3, 10, "1/2")

    options = setOf(
        244, -255, -242, 2, -255, 0, 4, 8, 0, 5, 8, 2,
        -5, -4, -8, -1024, -8, -625, -2, 2, -2, 0, 3, 0,
        -4, -4, -4, 0, -3, 0, -4, -0.99609375, 255, -3, -0.992, 124
    )
    checkMainTextMatchesMultiple(resultsOf(options), 5, 5, 20, "1-(4-5)^1")

    // previous result
    options = setOf(1, -1)
    checkMainTextMatchesMultiple(resultsOf(options), 2, 2, 10, listOf("1", "-2"))

    // divide by zero
    var errorThrown = false
    repeatUntil(100, { errorThrown }) {
        clearText()
        typeText("0/1")
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

private fun getSingleOpTransform(op: (Int, Int) -> Number): (Int, Int, Int) -> Number {
    return { i, j, k ->
        val first = i * 10 + j
        val second = i * 10 + k
        op(first, second)
    }
}

fun testMultipleShuffle() {
    // randomize signs

    // operators, numbers
    openSettingsFragment()
    shuffleNumbersSwitch.perform(click())
    closeFragment()

    // TODO handle decimals
    val singleOpOptions: ((Int, Int) -> Number) -> Set<Number> = { op ->
        generateShuffledNumbers { i, j, k ->
            val first = i * 10 + j
            val second = i * 10 + k
            op(first, second)
        }
    }
    var options = singleOpOptions(Int::plus) + singleOpOptions(Int::minus) + singleOpOptions(Int::times) + singleOpOptions(Int::div)
    checkMainTextMatchesMultiple(resultsOf(options), 5, 5, 10, "01+02")

    // operators, computation
    openSettingsFragment()
    shuffleNumbersSwitch.perform(click())
    shuffleComputationSwitch.perform(click())
    closeFragment()

    checkMainTextMatchesMultiple(resultsOf(options), 5, 5, 10, "")

    // numbers, computation
    openSettingsFragment()
    shuffleOperatorsSwitch.perform(click())
    shuffleNumbersSwitch.perform(click())
    closeFragment()

    // operators, numbers, computation
    openSettingsFragment()
    shuffleNumbersSwitch.perform(click())
    closeFragment()

    // errors
}

/**
 * Apply a transformation to all permutations of 3 distinct digits
 *
 * @param transform (Int, Int, Int) -> Number: transformation to apply
 * @return [Set]<Number>: result of applying transformation to all permutations
 */
private fun generateShuffledNumbers(transform: (Int, Int, Int) -> Number): Set<Number> {
    val options: MutableSet<Number> = mutableSetOf()
    for (i in 0..9) {
        for (j in 0..9) {
            for (k in 0..9) {
                if (i != j && i != k && j != k) {
                    options.add(transform(i, j, k))
                }
            }
        }
    }
    return options
}
