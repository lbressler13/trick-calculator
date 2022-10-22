package xyz.lbres.trickcalculator.devtools

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.helpers.isNotPresented

private val spinner = onView(withId(R.id.devToolsTimeSpinner))

fun testHideDevToolsOptions() {
    openDialog()

    spinner.check(matches(withSpinnerText("5000ms")))
        .perform(click())

    itemAtPosition(0).check(matches(allOf(isDisplayed(), withText("5000ms"))))
    itemAtPosition(1).check(matches(allOf(isDisplayed(), withText("10000ms"))))
    itemAtPosition(2).check(matches(allOf(isDisplayed(), withText("30000ms"))))
    itemAtPosition(3).check(matches(allOf(isDisplayed(), withText("60000ms"))))

    var performException = false
    try {
        itemAtPosition(4).check(matches(isDisplayed()))
    } catch (e: PerformException) {
        performException = true
    }

    if (!performException) {
        throw PerformException.Builder()
            .withCause(IllegalStateException("Dev tools spinner has too many options"))
            .build()
    }
}

fun testInteractWithHideDevToolsOptions() {
    openDialog()

    spinner.perform(click())
    itemAtPosition(1).perform(click())
    spinner.check(matches(withSpinnerText("10000ms")))

    spinner.perform(click())
    itemAtPosition(0).perform(click())
    spinner.check(matches(withSpinnerText("5000ms")))

    spinner.perform(click())
    itemAtPosition(2).perform(click())
    spinner.check(matches(withSpinnerText("30000ms")))

    spinner.perform(click())
    itemAtPosition(3).perform(click())
    spinner.check(matches(withSpinnerText("60000ms")))

    // close and re-open dialog
    onView(withText("Done")).perform(click())
    openDialog()
    spinner.check(matches(withSpinnerText("60000ms")))
}

fun testHideDevTools() {

}

private fun itemAtPosition(position: Int): DataInteraction {
    return onData(`is`(instanceOf(String::class.java)))
        .inRoot(isPlatformPopup())
        .atPosition(position)
}
