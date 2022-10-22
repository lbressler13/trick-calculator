package xyz.lbres.trickcalculator.devtools

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R

private val spinner = onView(withId(R.id.devToolsTimeSpinner))
private val hideDevToolsButton = onView(withId(R.id.hideDevToolsButton))
private val devToolsButton = onView(withId(R.id.devToolsButton))

fun testCorrectHideDevToolsOptions() {
    openDialog()

    spinner.check(matches(withSpinnerText("5000ms"))).perform(click())

    spinnerItemAt(0).check(matches(allOf(isDisplayed(), withText("5000ms"))))
    spinnerItemAt(1).check(matches(allOf(isDisplayed(), withText("10000ms"))))
    spinnerItemAt(2).check(matches(allOf(isDisplayed(), withText("30000ms"))))
    spinnerItemAt(3).check(matches(allOf(isDisplayed(), withText("60000ms"))))

    var performException = false
    try {
        spinnerItemAt(4).check(matches(isDisplayed()))
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
    spinnerItemAt(1).perform(click())
    spinner.check(matches(withSpinnerText("10000ms")))

    spinner.perform(click())
    spinnerItemAt(0).perform(click())
    spinner.check(matches(withSpinnerText("5000ms")))

    spinner.perform(click())
    spinnerItemAt(2).perform(click())
    spinner.check(matches(withSpinnerText("30000ms")))

    spinner.perform(click())
    spinnerItemAt(3).perform(click())
    spinner.check(matches(withSpinnerText("60000ms")))

    // close and re-open dialog
    closeDialog()
    openDialog()
    spinner.check(matches(withSpinnerText("60000ms")))
}

fun testHideDevTools() {
    devToolsButton.check(matches(isDisplayed()))
    openDialog()

    // 5 seconds
    spinner.check(matches(withSpinnerText("5000ms")))
    hideDevToolsButton.perform(click())
    checkDevToolsHidden(5000)

    openDialog()

    // 10 seconds
    spinner.perform(click())
    spinnerItemAt(1).perform(click())
    spinner.check(matches(withSpinnerText("10000ms")))
    hideDevToolsButton.perform(click())
    checkDevToolsHidden(10000)

    openDialog()

    // 30 seconds
    spinner.perform(click())
    spinnerItemAt(2).perform(click())
    spinner.check(matches(withSpinnerText("30000ms")))
    hideDevToolsButton.perform(click())
    checkDevToolsHidden(30000)

    openDialog()

    // 60 seconds
    spinner.perform(click())
    spinnerItemAt(3).perform(click())
    spinner.check(matches(withSpinnerText("60000ms")))
    hideDevToolsButton.perform(click())
    checkDevToolsHidden(60000)
}

/**
 * Check that the dev tools button is hidden and remains hidden for a certain amount of time.
 * One second buffer in either direction to account for time needed to check if views are visible.
 *
 * @param hideTime [Long]: the expected time for the button to be hidden, in ms
 */
private fun checkDevToolsHidden(hideTime: Long) {
    onView(withText("Developer Tools")).check(doesNotExist())
    devToolsButton.check(matches(not(isDisplayed())))
    Thread.sleep(hideTime - 1000)
    devToolsButton.check(matches(not(isDisplayed())))
    Thread.sleep(2000)
    devToolsButton.check(matches(isDisplayed()))
}

/**
 * Get the item at a specific position in the spinner
 *
 * @param position [Int]
 * @return [DataInteraction]: the item located at [position]
 */
private fun spinnerItemAt(position: Int): DataInteraction {
    return onData(`is`(instanceOf(String::class.java)))
        .inRoot(isPlatformPopup())
        .atPosition(position)
}
