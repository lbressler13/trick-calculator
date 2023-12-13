package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.openSettingsFragment
import xyz.lbres.trickcalculator.testutils.typeText

private val mainText = onView(withId(R.id.mainText))
private val errorText = onView(withId(R.id.errorText))

private val shuffleOperatorsSwitch = onView(withId(R.id.shuffleOperatorsSwitch))

fun testMultipleSettingsTypes() {
    openSettingsFragment()
    shuffleOperatorsSwitch.perform(click())
    closeFragment()

    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))
}
