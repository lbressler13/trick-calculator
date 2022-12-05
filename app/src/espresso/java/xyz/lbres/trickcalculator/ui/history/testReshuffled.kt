package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matchers.not
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver.Companion.withSavedTextAtPosition
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators
import xyz.lbres.trickcalculator.testutils.viewactions.scrollToPosition
import xyz.lbres.trickcalculator.ui.calculator.clearText
import xyz.lbres.trickcalculator.ui.calculator.equals
import xyz.lbres.trickcalculator.ui.calculator.typeText

fun testRandomness1Reshuffled() {
    val errorMessage = "History items should be shuffled in history randomness 1."
    setHistoryRandomness(1)
    toggleShuffleOperators()

    val computeHistory = createHistory()

    runSingleReshuffledTest(computeHistory, 1, errorMessage)
    RecyclerViewTextSaver.clearAllSavedValues()
    runSingleReshuffledTest(computeHistory, 1, errorMessage) // re-run with different order of values
}

fun testRandomness2Reshuffled() {
    val errorMessage = "History items and pairs should be shuffled in history randomness 2."
    setHistoryRandomness(2)
    toggleShuffleOperators()

    val computeHistory = createHistory()

    runSingleReshuffledTest(computeHistory, 2, errorMessage)
    RecyclerViewTextSaver.clearAllSavedValues()
    runSingleReshuffledTest(computeHistory, 2, errorMessage) // re-run with different order of values
}

/**
 * Create a long compute history by typing values
 *
 * @return [TestHistory]: generated history
 */
private fun createHistory(): TestHistory {
    val computeHistory: MutableList<TestHI> = mutableListOf()

    typeText("400/5")
    equals()
    computeHistory.add(TestHI("400/5", "80"))

    clearText()
    typeText("15-2.5")
    equals()
    computeHistory.add(TestHI("15-2.5", "12.5"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(TestHI("(3-4)(5+2)", "-7"))

    typeText("+11")
    equals()
    computeHistory.add(TestHI("-7+11", "4"))

    clearText()
    typeText("(3-4)(5+2)")
    equals()
    computeHistory.add(TestHI("(3-4)(5+2)", "-7"))

    clearText()
    typeText("+")
    equals()
    computeHistory.add(TestHI("+", "Syntax error"))

    clearText()
    typeText("2^0.5")
    equals()
    computeHistory.add(TestHI("2^0.5", "Exponents must be whole numbers"))

    return computeHistory
}

/**
 * Check that the order changes when opening and closing the fragment.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to throw if basic checks fail
 */
private fun runSingleReshuffledTest(computeHistory: TestHistory, randomness: Int, errorMessage: String) {
    val recyclerId = R.id.itemsRecycler
    checkCorrectData(computeHistory, randomness, errorMessage)
    val historySize = computeHistory.size
    openHistoryFragment()

    // save all current values
    for (position in 0 until historySize) {
        onView(withId(recyclerId)).perform(scrollToPosition(position))
        onView(withViewHolder(recyclerId, position))
            .perform(RecyclerViewTextSaver.saveTextAtPosition(position, R.id.computeText))
    }

    closeFragment()

    // additional repeats for 2 items due to occasional failures
    val repeats = ternaryIf(computeHistory.size == 2, 10, 5)
    var shuffled = false

    repeatUntil(repeats, { shuffled }) {
        openHistoryFragment()

        for (position in 0 until historySize) {
            onView(withId(recyclerId)).perform(scrollToPosition(position))

            try {
                onView(withViewHolder(recyclerId, position))
                    .check(matches(not(withSavedTextAtPosition(position, R.id.computeText))))
                shuffled = true
            } catch (_: Throwable) {}
        }

        closeFragment()
    }

    if (!shuffled) {
        throw AssertionError("Items not re-shuffled for history randomness 1. History: $computeHistory")
    }
}
