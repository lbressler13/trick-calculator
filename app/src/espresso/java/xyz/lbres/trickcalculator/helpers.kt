package xyz.lbres.trickcalculator

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Assert
import java.lang.AssertionError

/**
 * Check that a view is displayed and has a specific text value
 *
 * @param id [Int]: id of view
 * @param text [String]: text that view is expected to have
 */
fun checkVisibleWithText(id: Int, text: String) {
    onView(withId(id))
        .check(matches(isDisplayed()))
        .check(matches(withText(text)))
}

fun assertLinkOpened(url: String, expectedLinkClicks: Int) {
    val intents = Intents.getIntents().filter { it.action == Intent.ACTION_VIEW }

    if (intents.size != expectedLinkClicks) {
        throw AssertionError("Expected $expectedLinkClicks link clicks, found ${intents.size}")
    }

    val intent = intents.last()
    Assert.assertEquals(Intent.ACTION_VIEW, intent.action)
    Assert.assertEquals(url, intent.dataString)
}
