package xyz.lbres.trickcalculator.ui.devtools

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import xyz.lbres.trickcalculator.R

/**
 * Open dialog using dev tools button
 */
fun openDialog() {
    onView(withId(R.id.devToolsButton)).perform(click())
}

/**
 * Close dialog
 */
fun closeDialog() {
    onView(withText("Done")).perform(click())
}
