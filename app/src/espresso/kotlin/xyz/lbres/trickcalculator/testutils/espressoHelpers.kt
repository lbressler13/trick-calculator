package xyz.lbres.trickcalculator.testutils

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import xyz.lbres.trickcalculator.BaseActivity
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

/**
 * [Matcher] that checks if the text of a TextView matches any of the given options
 *
 * @param options [Collection]<String>: text values to match
 */
fun withAnyText(options: Collection<String>): Matcher<View> {
    val matchers = options.map { withText(it) }.toMutableList()
    return anyOf(matchers)
}

/**
 * [Matcher] that checks if a TextView is displayed and has the given text
 *
 * @param text [String]: expected text
 */
fun isDisplayedWithText(text: String): Matcher<View> = allOf(isDisplayed(), withText(text))

/**
 * Get the current activity context
 *
 * @param activityRule [ActivityScenarioRule]: test rule with the activity
 * @return [Context] context object
 */
fun getContextEspresso(activityRule: ActivityScenarioRule<BaseActivity>): Context {
    var context: Context? = null
    activityRule.scenario.onActivity {
        context = it.supportFragmentManager.fragments[0].requireContext()
    }

    return context!!
}
