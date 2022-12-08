package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import xyz.lbres.kotlinutils.int.ext.isZero
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

fun testShuffleOperators() {
    val computeHistory = mutableListOf<TestHI>()

    addSingleItem("3", computeHistory)
    addSingleItem("-1/2", computeHistory, previousResult = "3")
    addSingleItem("0.123456", computeHistory)

    var checker = HistoryChecker(computeHistory)
    checkRandomness(checker, 0)

    addSingleItem("1+2-2/3x1", computeHistory)
    addSingleItem("(1+2)(4-2)", computeHistory)
    addSingleItem("2x(1-9)", computeHistory)
    addSingleItem("1.2x5(4)", computeHistory)
    addSingleItem("1.2x5(4)", computeHistory)

    // with error
    clearText()
    typeText("4+5()-44")
    equals()
    computeHistory.add(TestHI("4+5()-44", "Syntax error"))

    addSingleItem("4+(5)-44", computeHistory)
    addSingleItem(".00000003", computeHistory)
    addSingleItem("55^6", computeHistory)
    // TODO long value
    // addSingleItem("123456789/12.898989898989+(98765x432100)-555555555x13131313131313", computeHistory)

    // multiple errors
    clearText()
    typeText("400/3..3")
    equals()
    computeHistory.add(TestHI("400/3..3", "Syntax error"))

    clearText()
    typeText("(500-5))-6")
    equals()
    computeHistory.add(TestHI("(500-5))-6", "Syntax error"))

    checker = HistoryChecker(computeHistory)
    checkRandomness(checker, 0)
    checkRandomness(checker, 1)
    checkRandomness(checker, 2)
}

fun testShuffledNumbers() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())

}

fun testShuffleComputation() {

}

private fun addSingleItem(computeText: String, computeHistory: MutableList<TestHI>, previousResult: String = "") {
    val maxDecimalLength = 5

    if (previousResult == "") {
        clearText()
    }
    typeText(computeText)
    equals()

    var result = getMainText()
    result = result.substring(1, result.lastIndex) // trim [] around result

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
