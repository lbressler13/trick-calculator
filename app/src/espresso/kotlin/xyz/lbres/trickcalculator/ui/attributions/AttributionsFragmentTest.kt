package xyz.lbres.trickcalculator.ui.attributions

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.ProductFlavor
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.assertLinkOpened
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.testutils.viewactions.clickLinkInText
import xyz.lbres.trickcalculator.testutils.viewactions.forceClick
import xyz.lbres.trickcalculator.testutils.viewassertions.isNotPresented
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

@RunWith(AndroidJUnit4::class)
class AttributionsFragmentTest {
    private val intent = Intent(ApplicationProvider.getApplicationContext(), BaseActivity::class.java)

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule<BaseActivity>(intent)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Before
    fun setupTest() {
        // setup intents
        Intents.init()
        intending(not(isInternal())).respondWith(ActivityResult(Activity.RESULT_OK, null))

        openAttributionsFragment()

        // hide dev tools to avoid interference with expanding/collapsing attributions
        if (ProductFlavor.devMode) {
            hideDevToolsButton()
        }
    }

    @After
    fun cleanupTest() {
        Intents.release()
    }

    @Test
    fun loadActionBarWithTitle() {
        val expectedTitle = "Image Attributions"
        onView(withId(R.id.title)).check(matches(withText(expectedTitle)))
    }

    @Test
    fun flaticonMessage() {
        val flaticonShort = "All icons are taken from Flaticon. Expand for details of their attribution policies."
        val flaticonLong = "All icons are taken from Flaticon, which allows free use of icons for personal and commercial purposes with attribution. In accordance with their policies, attributions are provided below.\n\nSee here for more information about their policies."

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
        onView(withId(R.id.closeButton)).perform(forceClick())
        onView(withText("Calculator")).check(matches(isDisplayed()))
    }

    @Test
    fun expandCollapseAttributions() = testExpandCollapseAttributions()

    @Test
    fun expandFlaticonMessageAndAttributions() {
        onView(withId(R.id.expandCollapseMessage)).perform(click())
        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Collapse")))

        expandCollapseAttribution(0)
        checkImagesDisplayed(listOf(0))
        checkImagesNotPresented(listOf(1, 2, 3, 4))

        expandCollapseAttribution(1)
        checkImagesDisplayed(listOf(0, 1))
        checkImagesNotPresented(listOf(2, 3, 4))

        expandCollapseAttribution(2)
        checkImagesDisplayed(listOf(0, 1, 2))
        checkImagesNotPresented(listOf(3, 4))

        expandCollapseAttribution(3)
        checkImagesDisplayed(listOf(0, 1, 2, 3))
        checkImagesNotPresented(listOf(4))

        expandCollapseAttribution(4)
        checkImagesDisplayed(listOf(0, 1, 2, 3, 4))
    }

    @Test
    fun flaticonPolicyLinks() {
        var expectedLinkClicks = 0

        // click link while collapsed
        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("Flaticon"))
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

        onView(withId(R.id.expandCollapseMessage)).perform(click())

        // click both links after expanding
        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("Flaticon"))
        expectedLinkClicks++
        assertLinkOpened("https://www.flaticon.com", expectedLinkClicks)

        onView(withId(R.id.flaticonPolicyMessage)).perform(clickLinkInText("here"))
        expectedLinkClicks++
        assertLinkOpened("https://support.flaticon.com/s/article/Attribution-How-when-and-where-FI?language=en_US&Id=ka03V0000004Q5lQAE", expectedLinkClicks)
    }

    @Test
    fun dataNotPersistedOnClose() {
        val recycler = onView(withId(R.id.attributionsRecycler))

        // expand attributions
        for (pair in authorAttributions.withIndex()) {
            val index = pair.index
            val author = pair.value

            expandCollapseAttribution(index)
            recycler.perform(scrollToAuthorPosition(index))
            val image = author.images[0]
            onView(withText(image.url)).check(matches(isDisplayed()))
        }

        // expand flaticon message
        onView(withText("Expand")).check(matches(isDisplayed()))
        onView(withText("Collapse")).check(doesNotExist())
        onView(withText("Expand")).perform(click())
        onView(withText("Expand")).check(doesNotExist())
        onView(withText("Collapse")).check(matches(isDisplayed()))

        // close and reopen fragment
        onView(withId(R.id.closeButton)).perform(forceClick())
        onView(withId(R.id.infoButton)).perform(click())

        // check image attributions no longer displayed
        for (pair in authorAttributions.withIndex()) {
            val index = pair.index
            val author = pair.value

            recycler.perform(scrollToAuthorPosition(index))
            val image = author.images[0]
            onView(withText(image.url)).check(isNotPresented())
        }

        // check flaticon message collapsed
        onView(withText("Expand")).check(matches(isDisplayed()))
        onView(withText("Collapse")).check(doesNotExist())
    }

    @Test
    fun attributionsLinks() = testAttributionLinks()

    @Test
    fun openSettingsFragment() {
        onView(withId(R.id.title)).perform(click())
        onView(withText("Settings")).check(matches(isDisplayed()))
    }
}
