package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

private const val recyclerId = R.id.itemsRecycler
private const val errorMessage = "History items should be shuffled in history randomness 1."

fun testRandomness1() {
    setHistoryRandomness(1)
    toggleShuffleOperators()
    openHistoryFragment()

    // no history
    onView(withText("No history")).check(matches(isDisplayed()))

    val computeHistory: MutableList<TestHI> = mutableListOf()

    // one element
    closeFragment()
    typeText("400/5")
    equals()
    computeHistory.add(TestHI("400/5", "80"))
    checkCorrectData(computeHistory, 1, errorMessage)

    // several elements
    clearText()
    typeText("15-2.5")
    equals()
    computeHistory.add(TestHI("15-2.5", "12.5"))
    checkCorrectData(computeHistory, 1, errorMessage)

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(TestHI("(3-4)(5+2)", "-7"))
    checkCorrectData(computeHistory, 1, errorMessage)

    // previously computed
    typeText("+11")
    equals()
    computeHistory.add(TestHI("-7+11", "4"))
    checkCorrectData(computeHistory, 1, errorMessage)

    // duplicate element
    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(TestHI("(3-4)(5+2)", "-7"))
    checkCorrectData(computeHistory, 1, errorMessage)

    // error
    clearText()
    typeText("+")
    equals()
    computeHistory.add(TestHI("+", "Syntax error"))
    checkCorrectData(computeHistory, 1, errorMessage)

    clearText()
    typeText("2^0.5")
    equals()
    computeHistory.add(TestHI("2^0.5", "Exponents must be whole numbers"))
    checkCorrectData(computeHistory, 1, errorMessage)
}
