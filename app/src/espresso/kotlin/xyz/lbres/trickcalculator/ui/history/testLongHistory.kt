package xyz.lbres.trickcalculator.ui.history

import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testLongHistory() {
    toggleShuffleOperators()

    val history = TestHistory()

    history.add(generateHI("1+2") { "3" })
    history.add(generateHI("-1/2", "3") { "2.5" })
    history.add(generateHI("+") { "Syntax error" })
    history.add(generateHI("1+2-2^3x1") { "-5" })
    history.add(generateHI("(1+2)(4-2)") { "6" })
    history.add(generateHI("(1+2)(4-2)") { "6" })
    history.add(generateHI("2x(1-9)") { "-16" })
    history.add(generateHI("/5", "-16") { "-3.2" })
    history.add(generateHI("2^6-7x8") { "8" })
    history.add(generateHI("1.2x5(4)") { "24" })
    history.add(generateHI("1..2x5(4)") { "Syntax error" })
    history.add(generateHI("1.2x5(4)") { "24" })
    history.add(generateHI("2x(1-9)") { "-16" })
    history.add(generateHI("/5", "-16") { "-3.2" })
    history.add(generateHI("12345.09876") { "12345.09876" })
    history.add(generateHI("000000010.000000000000") { "10" })
    history.add(generateHI("-11", "10") { "-1" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}
