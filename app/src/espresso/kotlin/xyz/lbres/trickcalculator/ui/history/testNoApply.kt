package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openHistoryFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testNoApplyDecimals() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // no decimal
    history.add(generateHI("150") { "150" })
    history.add(generateHI("0") { "0" })
    history.add(generateHI("50-40") { "10" })
    history.add(generateHI("16^3") { "4096" })
    history.add(generateHI("15-3x(2-4)") { "21" })
    history.add(generateHI("15-3x()") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // one decimal
    history.add(generateHI("15.0") { "150" })
    history.add(generateHI(".001") { "1" })
    history.add(generateHI("12345.67890") { "1234567890" })
    history.add(generateHI("4x.25+20/4-5") { "100" })
    history.add(generateHI("15^.2") { "225" }) // normally throws error
    history.add(generateHI("15-3x/.2") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // multiple decimals
    history.add(generateHI("1.5x0.02/3") { "10" })
    history.add(generateHI("1.5x0.02/3.0") { "1" })
    history.add(generateHI("1.0^(1-2)") { "0.1" })
    history.add(generateHI("1.5-3.0//2") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // decimal error
    history.add(generateHI("1..5") { "Syntax error" })
    history.add(generateHI("100x.5/12..3") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // decimal in result
    history.add(generateHI("1/2") { "0.5" })
    history.add(generateHI("x1", "0.5") { "0.5" })
    history.add(generateHI("x.4", "0.5") { "2" })

    history.add(generateHI("1.2/00.7") { "1.71429" })
    history.add(generateHI("+3", "1.71428571") { "4.71429" })

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
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // no parens
    history.add(generateHI("100") { "100" })
    history.add(generateHI("123.4567") { "123.4567" })
    history.add(generateHI("55-6x3") { "37" })
    history.add(generateHI("0.4^2") { "0.16" })
    history.add(generateHI("15-3x4..0") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // one set of parens
    history.add(generateHI("(1000.3)") { "1000.3" })
    history.add(generateHI("(100-204)") { "-104" })
    history.add(generateHI("3x(1-2)") { "1" })
    history.add(generateHI("(4)3") { "12" })
    history.add(generateHI("0.3x12/(5-3)x1+3") { "0.72" })
    history.add(generateHI("5^(3/2+.5)") { "63" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // multiple sets of parens
    history.add(generateHI("(4)(3)") { "12" })
    history.add(generateHI("(2x3)-5(4-1)") { "-15" })
    history.add(generateHI("(4(2+3))/2") { "9.5" })
    history.add(generateHI("(4/(15-3)x(3-1)+10)+1") { "1.26667" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // parens error
    history.add(generateHI("4x((5+2)") { "Syntax error" })
    history.add(generateHI("()5") { "Syntax error" })
    history.add(generateHI("(4(15-3)(11-1))+3)-1") { "Syntax error" })

    openHistoryFragment()
    history.runAllChecks(0)
    closeFragment()

    // using result
    history.add(generateHI("5-3") { "2" })
    history.add(generateHI("(5-3)", "2") { "7" })

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
