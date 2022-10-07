package xyz.lbres.trickcalculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

fun checkVisibleWithText(id: Int, text: String) {
    onView(withId(id))
        .check(matches(isDisplayed()))
        .check(matches(withText(text)))
}
