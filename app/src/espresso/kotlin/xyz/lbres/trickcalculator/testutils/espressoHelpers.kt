package xyz.lbres.trickcalculator.testutils

import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import java.lang.AssertionError

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
    assertEquals(url, intent.dataString)
}

/**
 * Wrapper function for creating a [Matcher] for text with an empty string
 */
fun isEmptyString(): Matcher<View> = ViewMatchers.withText("")

/**
 * Wrapper function for creating a [Matcher] for text with a non-empty string
 */
fun isNotEmptyString(): Matcher<View> = Matchers.not(ViewMatchers.withText(""))
