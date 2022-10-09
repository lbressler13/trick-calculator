package xyz.lbres.trickcalculator.ui.attributions

import org.junit.Assert.assertEquals
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.MainActivity
import xyz.lbres.trickcalculator.R
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.*

import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal

import org.hamcrest.Matchers.*
import xyz.lbres.kotlinutils.generic.ext.ifNull
import java.lang.AssertionError


@RunWith(AndroidJUnit4::class)
class AttributionsFragmentTest {
    @Rule
    @JvmField
    val rule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun setupTest() {
        val infoButton = onView(withId(R.id.infoButton))
        infoButton.check(matches(isDisplayed()))
        infoButton.perform(click())
    }

    @After
    fun cleanupTest() {
        // Intents.release()
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
            .perform(scrollTo(), click())

        onView(withText("Calculator")).check(matches(isDisplayed()))
    }

    @Test
    fun expandCollapseAttributions() {
        // TODO
        // with link for author/image
    }

    @Test
    fun flaticonPolicyLinks() {
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        onView(withId(R.id.flaticonPolicyMessage)).perform(clickClickableSpan("Flaticon"))
        assertLinkOpened("https://www.flaticon.com")

        onView(withId(R.id.expandCollapseMessage)).perform(click())

        // click both links after expanding
        onView(withId(R.id.flaticonPolicyMessage)).perform(clickClickableSpan("Flaticon"))
        assertLinkOpened("https://www.flaticon.com")

        onView(withId(R.id.flaticonPolicyMessage)).perform(clickClickableSpan("here"))
        assertLinkOpened("https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE")
    }

    @Test
    fun openSettingsFragment() {
        onView(withId(R.id.title)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))
    }

    private fun assertLinkOpened(url: String) {
        val intents = getIntents()

        val intent = intents.lastOrNull().ifNull {
            throw AssertionError("No intents found")
        }

        assertEquals(Intent.ACTION_VIEW, intent.action)
        assertEquals(url, intent.dataString)
    }
}
