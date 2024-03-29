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

/**
 * Open settings fragment through dev tools dialog
 */
fun openSettingsFromDialog() {
    openDevTools()
    onView(withId(R.id.openSettingsButton)).perform(click())
}

/**
 * Open dev tools dialog, clear history, and close dev tools dialog
 */
fun doClearHistory() {
    openDevTools()
    onView(withId(R.id.clearHistoryButton)).perform(click())
    closeDialog()
}

/**
 * Open dev tools dialog, refresh UI, and close dev tools dialog
 */
fun doRefreshUI() {
    openDevTools()
    onView(withId(R.id.refreshUIButton)).perform(click())
    closeDialog()
}
