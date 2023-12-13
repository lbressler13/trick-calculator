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
private val useLastHistoryButton = onView(withId(R.id.useLastHistoryButton))

fun testHistoryRandomness() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.historyButton0)).perform(click())
    closeFragment()

    // 0
    typeText("12+34")
    equals()
    useLastHistoryButton.perform(click())
    mainText.check(matches(withText("12+34")))

    // 1
    openSettingsFragment()
    onView(withId(R.id.historyButton1)).perform(click())
    closeFragment()

    clearText()
    typeText("12+34")
    equals()
    useLastHistoryButton.perform(click())
    mainText.check(matches(withText("12+34")))

    // 2
    openSettingsFragment()
    onView(withId(R.id.historyButton2)).perform(click())
    closeFragment()

    clearText()
    typeText("12+34")
    equals()
    useLastHistoryButton.perform(click())
    mainText.check(matches(withText("12+34")))

    // 3
    openSettingsFragment()
    onView(withId(R.id.historyButton3)).perform(click())
    closeFragment()

    clearText()
    typeText("12+34")
    equals()
    useLastHistoryButton.perform(click())
    mainText.check(matches(withText("12+34")))
}
