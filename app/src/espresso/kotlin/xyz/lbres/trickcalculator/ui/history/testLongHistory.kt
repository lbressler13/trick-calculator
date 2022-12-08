package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

fun testLongHistory() {
    toggleShuffleOperators()

    val computeHistory = createHistory()
    checkValues(computeHistory)
}

/**
 * Run checks on compute history for randomness settings 0, 1, 2
 *
 * @param computeHistory [TestHistory]: history to check
 */
private fun checkValues(computeHistory: TestHistory) {
    val checker = HistoryChecker(computeHistory)

    setHistoryRandomness(0)
    openHistoryFragment()
    checker.runAllChecks(0)
    closeFragment()

    /*
    setHistoryRandomness(1)
    openHistoryFragment()
    // very low probably of unshuffled due to long history
    try {
        checker.runAllChecks(1)
    } catch (_: Throwable) {
        checker.runAllChecks(1)
    }
    closeFragment()

    setHistoryRandomness(2)
    openHistoryFragment()
    // very low probably of unshuffled due to long history
    try {
        checker.runAllChecks(2)
    } catch (_: Throwable) {
        checker.runAllChecks(2)
    }
    closeFragment()
     */
}

/**
 * Create a long compute history by typing many values
 *
 * @return [TestHistory]: generated history
 */
private fun createHistory(): TestHistory {
    val computeHistory = mutableListOf<TestHI>()

    typeText("1+2")
    equals()
    computeHistory.add(TestHI("1+2", "3"))
    typeText("-1/2")
    equals()
    computeHistory.add(TestHI("3-1/2", "2.5"))
    clearText()
    typeText("+")
    equals()
    computeHistory.add(TestHI("+", "Syntax error"))
    clearText()
    typeText("1+2-2^3x1")
    equals()
    computeHistory.add(TestHI("1+2-2^3x1", "-5"))
    clearText()
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(TestHI("(1+2)(4-2)", "6"))
    clearText()
    typeText("(1+2)(4-2)")
    equals()
    computeHistory.add(TestHI("(1+2)(4-2)", "6"))
    clearText()
    typeText("2x(1-9)")
    equals()
    computeHistory.add(TestHI("2x(1-9)", "-16"))
    typeText("/5")
    equals()
    computeHistory.add(TestHI("-16/5", "-3.2"))
    clearText()
    typeText("2^6-7x8")
    equals()
    computeHistory.add(TestHI("2^6-7x8", "8"))
    clearText()
    typeText("1.2x5(4)")
    equals()
    computeHistory.add(TestHI("1.2x5(4)", "24"))
    clearText()
    typeText("1..2x5(4)")
    equals()
    computeHistory.add(TestHI("1..2x5(4)", "Syntax error"))
    clearText()
    typeText("1.2x5(4)")
    equals()
    computeHistory.add(TestHI("1.2x5(4)", "24"))
    clearText()
    typeText("2x(1-9)")
    equals()
    computeHistory.add(TestHI("2x(1-9)", "-16"))
    typeText("/5")
    equals()
    computeHistory.add(TestHI("-16/5", "-3.2"))
    clearText()
    typeText("12345.09876")
    equals()
    computeHistory.add(TestHI("12345.09876", "12345.09876"))
    clearText()
    typeText("00000010.000000")
    equals()
    computeHistory.add(TestHI("00000010.000000", "10"))
    typeText("-11")
    equals()
    computeHistory.add(TestHI("10-11", "-1"))
    clearText()

    return computeHistory
}
