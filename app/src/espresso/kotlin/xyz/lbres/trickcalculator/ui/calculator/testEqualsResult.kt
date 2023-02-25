package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.withEmptyString

private val mainText = onView(withId(R.id.mainText))

fun testEqualsSingleNumber() {
    mainText.check(matches(withEmptyString()))
    equals()
    mainText.check(matches(withEmptyString()))

    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))

    clearText()
    typeText("9.876")
    equals()
    mainText.check(matches(withText("[9.876]")))

    clearText()
    typeText("000.05")
    equals()
    mainText.check(matches(withText("[0.05]")))
}

fun testEqualsWithSingleOperator() {
    repeat(10) {
        clearText()
        typeText("12+10")
        equals()
        checkMainTextMatchesAny(setOf("[22]", "[2]", "[120]", "[1.2]"))
    }

    repeat(10) {
        clearText()
        typeText("2^3") // exponent
        equals()
        checkMainTextMatchesAny(setOf("[5]", "[-1]", "[6]", "[0.66666667]", "[8]"))
    }

    repeat(10) {
        clearText()
        typeText("1.5x4") // decimal
        equals()
        checkMainTextMatchesAny(setOf("[5.5]", "[-2.5]", "[6]", "[0.375]"))
    }

    repeat(10) {
        clearText()
        typeText("2x5x4") // several same op
        equals()
        checkMainTextMatchesAny(setOf("[11]", "[-7]", "[40]", "[0.1]"))
    }
}

fun testEqualsWithSeveralOperators() {
    repeat(10) {
        clearText()
        typeText("2+5-4")
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[3]", "[22]", "[3.25]", // + = +
                "[1]", "[-18]", "[0.75]", // + = -
                "[14]", "[6]", "[2.5]", // + = x
                "[4.4]", "[-3.6]", "[1.6]" // + = /
            )
        )
    }

    repeat(10) {
        clearText()
        typeText("5^2-4") // exponent
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[3]", "[13]", "[5.5]", "[21]", // + = +
                "[7]", "[-3]", "[4.5]", "[-11]", // + = -
                "[14]", "[6]", "[2.5]", "[80]", // + = x
                "[6.5]", "[-1.5]", "[10]", "[0.3125]", // + = /
                "[29]", "[21]", "[100]", "[6.25]" // + = ^
            )
        )
    }

    repeat(10) {
        clearText()
        typeText("10-.5x4/2+16")
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[10]", "[-21.5]", "[11.875]", "[-5]", "[-21.875]", "[-5.75]", // + = +
                "[10]", "[41.5]", "[8.125]", "[25]", "[41.875]", "[25.75]", // + = -
                "[8.875]", "[-9]", "[1.125]", "[19]", "[-12.75]", "[15.25]", // + = *
                "[-8]", "[12]", "[48]", "[28]", "[66]", "[94]" // + = /
            )
        )
    }
}

fun testEqualsWithParens() {
    clearText()
    typeText("(000.05)")
    equals()
    mainText.check(matches(withText("[0.05]")))

    repeat(10) {
        clearText()
        typeText("2(5-4)")
        equals()
        checkMainTextMatchesAny(
            setOf(
                "[3]", "[22]", "[3.25]", // + = +
                "[-7]", "[-18]", "[0.75]", // + = -
                "[18]", "[2]", "[2.5]", // + = x
                "[0.22222222]", "[2]", "[0.1]" // + = /
            )
        )
    }
}

fun testEqualsWithPreviouslyComputed() {
    repeat(10) {
        clearText()
        typeText("123")
        equals()
        typeText("+2")
        equals()
        checkMainTextMatchesAny(setOf("[125]", "[121]", "[246]", "[61.5]"))
    }

    repeat(10) {
        clearText()
        typeText("123")
        equals()
        typeText("2") // add times between values
        equals()
        checkMainTextMatchesAny(setOf("[125]", "[121]", "[246]", "[61.5]"))
    }
}
