package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.trickcalculator.testutils.textsaver.RecyclerViewTextSaver
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testRandomness1() {
    setHistoryRandomness(1)
    toggleShuffleOperators()

    // no history
    checkNoHistoryMessageDisplayed()

    val history = TestHistory()

    // one element
    history.add(generateTestItem("400/5") { "80" })
    checkRandomness(history, 1)

    // several elements
    history.add(generateTestItem("15-2.5") { "12.5" })
    history.add(generateTestItem("(3+4)(5+2)") { "49" })

    // previously computed
    history.add(generateTestItem("+11", "49") { "60" })
    checkRandomness(history, 1)

    // duplicate element
    history.add(generateTestItem("(3+4)(5+2)") { "49" })

    // error
    history.add(generateTestItem("+") { "Syntax error" })
    history.add(generateTestItem("2^0.5") { "Exponents must be whole numbers" })

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateTestItem(longText) { longResult })
    checkRandomness(history, 1)
}

fun testRandomness1Reshuffled() {
    setHistoryRandomness(1)
    toggleShuffleOperators()

    val history = TestHistory()

    history.add(generateTestItem("400/5") { "80" })
    history.add(generateTestItem("15-2.5") { "12.5" })
    history.add(generateTestItem("(3+4)(5+2)") { "49" })
    history.add(generateTestItem("+11", "49") { "60" })
    history.add(generateTestItem("(3+4)(5+2)") { "49" })
    history.add(generateTestItem("+") { "Syntax error" })
    history.add(generateTestItem("2^0.5") { "Exponents must be whole numbers" })

    val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)^3"
    val longResult = "-388245970060605516137019767887509499553681240225702923929715864051.57828"
    history.add(generateTestItem(longText) { longResult })

    runSingleReshuffledCheck(history, 1)
    RecyclerViewTextSaver.clearAllSavedValues()
    runSingleReshuffledCheck(history, 1) // re-run with different order of values
}
