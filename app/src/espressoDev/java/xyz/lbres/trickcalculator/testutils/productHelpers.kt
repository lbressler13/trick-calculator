package xyz.lbres.trickcalculator.testutils

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R

/**
 * Hide the dev tools button for 60 seconds to avoid interfering with tests when expanding/collapsing attributions.
 */
fun hideDevToolsButton() {
    onView(withId(R.id.devToolsButton)).perform(click())
    onView(withId(R.id.devToolsTimeSpinner)).perform(click())
    onData(`is`(instanceOf(String::class.java)))
        .inRoot(isPlatformPopup())
        .atPosition(3)
        .perform(click())

    onView(withId(R.id.devToolsTimeSpinner)).check(matches(withSpinnerText("60000ms")))
    onView(withId(R.id.hideDevToolsButton)).perform(click())

    onView(withText("Developer Tools")).check(doesNotExist())
    onView(withId(R.id.devToolsButton)).check(matches(not(isDisplayed())))
}
