package xyz.lbres.trickcalculator.helpers

import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert.assertEquals
import java.lang.AssertionError

/**
 * Check that a view is displayed and has a specific text value
 *
 * @param id [IdRes]: id of view
 * @param text [String]: text that view is expected to have
 */
fun checkVisibleWithText(@IdRes id: Int, text: String) {
    onView(withId(id))
        .check(matches(isDisplayed()))
        .check(matches(withText(text)))
}

/**
 * Assert that the correct number of link clicks have occurred, and that the most recent has the correct url.
 *
 * @param url [String]: the expected url of last link
 * @param expectedLinkClicks [Int]: expected number of times that links have been clicked
 */
fun assertLinkOpened(url: String, expectedLinkClicks: Int) {
    val intents = getIntents().filter { it.action == Intent.ACTION_VIEW }

    if (intents.size != expectedLinkClicks) {
        throw AssertionError("Expected $expectedLinkClicks link clicks, found ${intents.size}")
    }

    val intent = intents.last()
    assertEquals(Intent.ACTION_VIEW, intent.action)
    assertEquals(url, intent.dataString)
}
