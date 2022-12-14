package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

private const val errorMessage = "History items should be shuffled in history randomness 1."

fun testRandomness1() {
    setHistoryRandomness(1)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val history = TestHistory()

    // one element
    closeFragment()
    typeText("400/5")
    equals()
    history.add(TestHI("400/5", "80"))
    checkCorrectData(history, 1, errorMessage)

    // several elements
    clearText()
    typeText("15-2.5")
    equals()
    history.add(TestHI("15-2.5", "12.5"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    history.add(TestHI("(3-4)(5+2)", "-7"))

    // previously computed
    typeText("+11")
    equals()
    history.add(TestHI("-7+11", "4"))
    checkCorrectData(history, 1, errorMessage)

    // duplicate element
    clearText()
    typeText("(3-4)(5+2)")
    equals()
    history.add(TestHI("(3-4)(5+2)", "-7"))

    // error
    clearText()
    typeText("+")
    equals()
    history.add(TestHI("+", "Syntax error"))

    clearText()
    typeText("2^0.5")
    equals()
    history.add(TestHI("2^0.5", "Exponents must be whole numbers"))

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    clearText()
    typeText(longText)
    equals()
    history.add(TestHI(longText, longResult))
    checkCorrectData(history, 1, errorMessage)
}

fun testRandomness1Reshuffled() {
    setHistoryRandomness(1)
    toggleShuffleOperators()

    val history = TestHistory()

    typeText("400/5")
    equals()
    history.add(TestHI("400/5", "80"))

    clearText()
    typeText("15-2.5")
    equals()
    history.add(TestHI("15-2.5", "12.5"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    history.add(TestHI("(3-4)(5+2)", "-7"))

    typeText("+11")
    equals()
    history.add(TestHI("-7+11", "4"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    history.add(TestHI("(3-4)(5+2)", "-7"))

    clearText()
    typeText("+")
    equals()
    history.add(TestHI("+", "Syntax error"))

    clearText()
    typeText("2^0.5")
    equals()
    history.add(TestHI("2^0.5", "Exponents must be whole numbers"))

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    clearText()
    typeText(longText)
    equals()
    history.add(TestHI(longText, longResult))

    runSingleReshuffledCheck(history, 1, errorMessage)
    RecyclerViewTextSaver.clearAllSavedValues()
    runSingleReshuffledCheck(history, 1, errorMessage) // re-run with different order of values
}
