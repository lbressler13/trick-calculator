package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

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

    history.add(generateHI("400/5") { "80" })
    checkCorrectData(history, 1, errorMessage)

    // several elements
    history.add(generateHI("15-2.5") { "12.5" })
    history.add(generateHI("(3+4)(5+2)") { "49" })

    // previously computed
    history.add(generateHI("+11", "49") { "60" })
    checkCorrectData(history, 1, errorMessage)

    // duplicate element
    history.add(generateHI("(3+4)(5+2)") { "49" })

    // error
    history.add(generateHI("+") { "Syntax error" })
    history.add(generateHI("2^0.5") { "Exponents must be whole numbers" })

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateHI(longText) { longResult })
    checkCorrectData(history, 1, errorMessage)
}

fun testRandomness1Reshuffled() {
    setHistoryRandomness(1)
    toggleShuffleOperators()

    val history = TestHistory()

    history.add(generateHI("400/5") { "80" })
    history.add(generateHI("15-2.5") { "12.5" })
    history.add(generateHI("(3+4)(5+2)") { "49" })
    history.add(generateHI("+11", "49") { "60" })
    history.add(generateHI("(3+4)(5+2)") { "49" })
    history.add(generateHI("+") { "Syntax error" })
    history.add(generateHI("2^0.5") { "Exponents must be whole numbers" })

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateHI(longText) { longResult })

    runSingleReshuffledCheck(history, 1, errorMessage)
    RecyclerViewTextSaver.clearAllSavedValues()
    runSingleReshuffledCheck(history, 1, errorMessage) // re-run with different order of values
}
