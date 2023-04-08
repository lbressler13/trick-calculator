package xyz.lbres.trickcalculator.ui.calculator

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

    // no decimal
    typeAndCheckResult("1", "[1]")
    typeAndCheckResult("1+2", "[3]")

    // one decimal
    typeAndCheckResult("0.1", "[1]")
    typeAndCheckResult("1.00", "[100]")
    typeAndCheckResult("1.2+1", "[13]")

    // multiple decimal
    typeAndCheckResult("1.0-.22", "[-12]")
    typeAndCheckResult("(.5-1)x.3", "[12]")

    // decimal in previous result
    typeAndCheckResult("1/2", "[0.5]")
    typeAndCheckResult("+3", "[3.5]", clearPrevious = false)

    // throws decimal errors
    typeAndCheckSyntaxError("1..1")
    typeAndCheckSyntaxError(".")
}

fun testNoApplyParens() {
    // TODO shorter tests
    toggleShuffleOperators()
    openSettingsFragment()
    onView(withId(R.id.applyParensSwitch)).perform(click())
    closeFragment()

    // no parens
    typeAndCheckResult("1", "[1]")
    typeAndCheckResult("5-7", "[-2]")

    // one set of parens
    typeAndCheckResult("(5.5)", "[5.5]")
    typeAndCheckResult("(10-15)", "[-5]")
    typeAndCheckResult("(4-1)3", "[1]")
    typeAndCheckResult("5^(3/2+.5)", "[63]")
    typeAndCheckResult(".3x12/(5-3)+3", "[0.72]")

    // multiple sets of parens
    typeAndCheckResult("((5.5))", "[5.5]")
    typeAndCheckResult("(4)(3)", "[12]")
    typeAndCheckResult("(2x3)-(10/2.0)", "[1]")
    typeAndCheckResult("(4/(15-3)x(3-1)+10)+1", "[1.26666667]")

    // using previous result
    typeAndCheckResult("4", "[4]")
    typeAndCheckResult("5-2", "[18]", clearPrevious = false)

    typeAndCheckResult("4", "[4]")
    typeAndCheckResult("(5-2)-2", "[16]", clearPrevious = false)

    // throws parens errors
    typeAndCheckSyntaxError("()")
    typeAndCheckSyntaxError("3+()")
    typeAndCheckSyntaxError("4x((5)")
    typeAndCheckSyntaxError("(4(15-3)(11-1))+3)-1")
}
