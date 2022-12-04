package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.repeatUntil

/**
 * Verifies that all items from the history are displayed and shuffled.
 *
 * @param computeHistory [TestHistory]: list of items in history
 * @param randomness [Int]: history randomness setting
 * @param errorMessage [String]: error to show if check fails. Current history will be appended.
 */
fun checkCorrectData(computeHistory: TestHistory, randomness: Int, errorMessage: String) {
    var shuffled = false
    val historySize = computeHistory.size
    val checker = HistoryChecker(computeHistory)

    // additional repeats for 2 items due to occasional failures
    val repeatCount = ternaryIf(historySize == 2, 10, 5)

    // check that all items are displayed, only needs to happen once
    openHistoryFragment()
    checker.checkDisplayed(randomness)
    closeFragment()

    // check that items are shuffled
    repeatUntil(repeatCount, { shuffled }) {
        openHistoryFragment()
        shuffled = shuffled || checker.checkShuffled(randomness)
        closeFragment()
    }

    if (historySize > 1 && !shuffled) {
        throw AssertionError("$errorMessage. History: $computeHistory")
    }
}
