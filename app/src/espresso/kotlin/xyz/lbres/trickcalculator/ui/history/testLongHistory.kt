package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testLongHistory() {
    toggleShuffleOperators()

    val history = TestHistory()

    history.add(generateTestItem("1+2") { "3" })
    history.add(generateTestItem("-1/2", "3") { "2.5" })
    history.add(generateTestItem("+") { "Syntax error" })
    history.add(generateTestItem("1+2-2^3x1") { "-5" })
    history.add(generateTestItem("(1+2)(4-2)") { "6" })
    history.add(generateTestItem("(1+2)(4-2)") { "6" })
    history.add(generateTestItem("2x(1-9)") { "-16" })
    history.add(generateTestItem("/5", "-16") { "-3.2" })
    history.add(generateTestItem("2^6-7x8") { "8" })
    history.add(generateTestItem("1.2x5(4)") { "24" })
    history.add(generateTestItem("1..2x5(4)") { "Syntax error" })
    history.add(generateTestItem("1.2x5(4)") { "24" })
    history.add(generateTestItem("2x(1-9)") { "-16" })
    history.add(generateTestItem("/5", "-16") { "-3.2" })
    history.add(generateTestItem("12345.09876") { "12345.09876" })
    history.add(generateTestItem("000000010.000000000000") { "10" })
    history.add(generateTestItem("-11", "10") { "-1" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}
