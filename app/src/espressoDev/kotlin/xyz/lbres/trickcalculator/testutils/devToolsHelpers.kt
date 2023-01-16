package xyz.lbres.trickcalculator.testutils

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R

/**
 * Open dialog using dev tools button
 */
fun openDevTools() {
    onView(withId(R.id.devToolsButton)).perform(click())
}

/**
 * Close dialog
 */
fun closeDialog() {
    onView(withText("Done")).perform(click())
}

fun openSettingsFromDialog() {
    openDevTools()
    onView(withId(R.id.openSettingsButton)).perform(click())
}

fun doClearHistory() {
    openDevTools()
    onView(withText("Clear history")).perform(click())
    closeDialog()
}
