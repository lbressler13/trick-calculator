package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

fun testNoApplyDecimals() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // no decimal
    history.add(generateHistoryItem("150", "150"))
    history.add(generateHistoryItem("0", "0"))
    history.add(generateHistoryItem("50-40", "10"))
    history.add(generateHistoryItem("16^3", "4096"))
    history.add(generateHistoryItem("15-3x(2-4)", "21"))
    history.add(generateHistoryItem("15-3x()", "Syntax error"))

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // one decimal
    history.add(generateHistoryItem("15.0", "150"))
    history.add(generateHistoryItem(".001", "1"))
    history.add(generateHistoryItem("12345.67890", "1234567890"))
    history.add(generateHistoryItem("4x.25+20/4-5", "100"))
    history.add(generateHistoryItem("15^.2", "225")) // normally throws error
    history.add(generateHistoryItem("15-3x/.2", "Syntax error"))

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // multiple decimals
    history.add(generateHistoryItem("1.5x0.02/3", "10"))
    history.add(generateHistoryItem("1.5x0.02/3.0", "1"))
    history.add(generateHistoryItem("1.0^(1-2)", "0.1"))
    history.add(generateHistoryItem("1.5-3.0//2", "Syntax error"))

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // decimal error
    history.add(generateHistoryItem("1..5", "Syntax error"))
    history.add(generateHistoryItem("100x.5/12..3", "Syntax error"))

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // decimal in result
    clearText()
    typeText("1/2")
    equals()
    history.add(TestHI("1/2", "0.5"))
    typeText("x1")
    equals()
    history.add(TestHI("0.5x1", "0.5"))
    typeText("x.4")
    equals()
    history.add(TestHI("0.5x.4", "2"))

    clearText()
    typeText("1.2/00.7")
    equals()
    history.add(TestHI("1.2/00.7", "1.71429"))
    typeText("+3")
    equals()
    history.add(TestHI("1.71428571+3", "4.71429"))

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()
    setHistoryRandomness(1)
    openHistoryFragment()
    history.runAllChecks(1)
    closeFragment()
    setHistoryRandomness(2)
    openHistoryFragment()
    history.runAllChecks(2)
    closeFragment()
}

fun testNoApplyParens() {

}

private fun generateHistoryItem(computeHistory: String, result: String): TestHI {
    clearText()
    typeText(computeHistory)
    equals()
    return TestHI(computeHistory, result)
}
