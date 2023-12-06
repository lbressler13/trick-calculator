package xyz.lbres.trickcalculator.testutils

import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
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
 * [Matcher] that checks if text of TextView is an empty string
 */
fun withEmptyString(): Matcher<View> = withText("")

/**
 * [Matcher] that checks if text of TextView is a non-empty string
 */
fun withNonEmptyString(): Matcher<View> = not(withText(""))

fun withAnyText(options: Iterable<String>): Matcher<View> {
    val matchers = options.map { withText(it) }.toMutableList()
    return anyOf(matchers)
}
