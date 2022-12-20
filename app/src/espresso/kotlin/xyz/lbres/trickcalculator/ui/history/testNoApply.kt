package xyz.lbres.trickcalculator.ui.history

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.toggleShuffleOperators

fun testNoApplyDecimals() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyDecimalsSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // no decimal
    history.add(generateTestItem("150") { "150" })
    history.add(generateTestItem("0") { "0" })
    history.add(generateTestItem("50-40") { "10" })
    history.add(generateTestItem("16^3") { "4096" })
    history.add(generateTestItem("15-3x(2-4)") { "21" })
    history.add(generateTestItem("15-3x()") { "Syntax error" })

    checkRandomness(history, 0)

    // one decimal
    history.add(generateTestItem("15.0") { "150" })
    history.add(generateTestItem(".001") { "1" })
    history.add(generateTestItem("12345.67890") { "1234567890" })
    history.add(generateTestItem("4x.25+20/4-5") { "100" })
    history.add(generateTestItem("15^.2") { "225" }) // normally throws error
    history.add(generateTestItem("15-3x/.2") { "Syntax error" })

    checkRandomness(history, 0)

    // multiple decimals
    history.add(generateTestItem("1.5x0.02/3") { "10" })
    history.add(generateTestItem("1.5x0.02/3.0") { "1" })
    history.add(generateTestItem("1.0^(1-2)") { "0.1" })
    history.add(generateTestItem("1.5-3.0//2") { "Syntax error" })

    checkRandomness(history, 0)

    // decimal error
    history.add(generateTestItem("1..5") { "Syntax error" })
    history.add(generateTestItem("100x.5/12..3") { "Syntax error" })

    checkRandomness(history, 0)

    // decimal in result
    history.add(generateTestItem("1/2") { "0.5" })
    history.add(generateTestItem("x1", "0.5") { "0.5" })
    history.add(generateTestItem("x.4", "0.5") { "2" })

    history.add(generateTestItem("1.2/00.7") { "1.71429" })
    history.add(generateTestItem("+3", "1.71428571") { "4.71429" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

fun testNoApplyParens() {
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // no parens
    history.add(generateTestItem("100") { "100" })
    history.add(generateTestItem("123.4567") { "123.4567" })
    history.add(generateTestItem("55-6x3") { "37" })
    history.add(generateTestItem("0.4^2") { "0.16" })
    history.add(generateTestItem("15-3x4..0") { "Syntax error" })

    checkRandomness(history, 0)

    // one set of parens
    history.add(generateTestItem("(1000.3)") { "1000.3" })
    history.add(generateTestItem("(100-204)") { "-104" })
    history.add(generateTestItem("3x(1-2)") { "1" })
    history.add(generateTestItem("(4)3") { "12" })
    history.add(generateTestItem("0.3x12/(5-3)x1+3") { "0.72" })
    history.add(generateTestItem("5^(3/2+.5)") { "63" })

    checkRandomness(history, 0)

    // multiple sets of parens
    history.add(generateTestItem("(4)(3)") { "12" })
    history.add(generateTestItem("(2x3)-5(4-1)") { "-15" })
    history.add(generateTestItem("(4(2+3))/2") { "9.5" })
    history.add(generateTestItem("(4/(15-3)x(3-1)+10)+1") { "1.26667" })

    checkRandomness(history, 0)

    // parens error
    history.add(generateTestItem("4x((5+2)") { "Syntax error" })
    history.add(generateTestItem("()5") { "Syntax error" })
    history.add(generateTestItem("(4(15-3)(11-1))+3)-1") { "Syntax error" })

    checkRandomness(history, 0)

    // using result
    history.add(generateTestItem("5-3") { "2" })
    history.add(generateTestItem("(5-3)", "2") { "7" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}
