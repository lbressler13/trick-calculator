package xyz.lbres.trickcalculator.devtools

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import xyz.lbres.trickcalculator.R

fun openDialog() {
    onView(withId(R.id.devToolsButton)).perform(click())
}