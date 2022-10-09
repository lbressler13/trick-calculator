package xyz.lbres.trickcalculator.ui.attributions

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.getIntents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import java.lang.AssertionError

@RunWith(AndroidJUnit4::class)
class AttributionsFragmentTest {
    private var expectedLinkClicks = 0

    @Rule
    @JvmField
    val rule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun setupTest() {
        val infoButton = onView(withId(R.id.infoButton))
        infoButton.check(matches(isDisplayed()))
        infoButton.perform(click())

        expectedLinkClicks = 0
    }

    @Test
    fun loadActionBarWithTitle() {
        val expectedTitle = "Image Attributions"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun flaticonMessage() {
        val flaticonShort =
            "All icons are taken from Flaticon. Expand for details of their attribution policies."
        val flaticonLong =
            "All icons are taken from Flaticon, which allows free use of icons for personal and commercial purposes with attribution. In accordance with their policies, attributions are provided below.\n\nSee here for more information about their policies."

        // initial view
        onView(withId(R.id.flaticonPolicyMessage)).check(matches(withText(flaticonShort)))
        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Expand")))
        onView(withText(flaticonLong)).check(doesNotExist())
        onView(withText("Collapse")).check(doesNotExist())

        // expand
        onView(withId(R.id.expandCollapseMessage)).perform(click())

        onView(withId(R.id.flaticonPolicyMessage)).check(matches(withText(flaticonLong)))
        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Collapse")))
        onView(withText(flaticonShort)).check(doesNotExist())
        onView(withText("Expand")).check(doesNotExist())

        // collapse again
        onView(withId(R.id.expandCollapseMessage)).perform(click())

        onView(withId(R.id.flaticonPolicyMessage)).check(matches(withText(flaticonShort)))
        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Expand")))
        onView(withText(flaticonLong)).check(doesNotExist())
        onView(withText("Collapse")).check(doesNotExist())
    }

    @Test
    fun closeButton() {
        onData(withId(R.id.closeButton))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText("Calculator")).check(matches(isDisplayed()))
    }

    @Test
    fun expandCollapseAttributions() = testExpandCollapseAttributions()

    @Test
    fun flaticonPolicyLinks() {
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        // click link while collapsed
        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("Flaticon"))
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com")

        onView(withId(R.id.expandCollapseMessage)).perform(click())

        // click both links after expanding
        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("Flaticon"))
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com")

        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("here"))
        expectedLinkClicks++
        assertLinkOpened("https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE")
    }

    @Test
    fun attributionsLinks() = testAttributionLinks()

    @Test
    fun openSettingsFragment() {
        onView(withId(R.id.title)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))
    }

    private fun assertLinkOpened(url: String) {
        val intents = getIntents().filter { it.action == Intent.ACTION_VIEW }

        if (intents.size != expectedLinkClicks) {
            throw AssertionError("Expected $expectedLinkClicks link clicks, found ${intents.size}")
        }

        val intent = intents.last()
        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(url, intent.dataString)
    }
}
