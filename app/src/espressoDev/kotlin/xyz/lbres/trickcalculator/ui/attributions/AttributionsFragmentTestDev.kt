package xyz.lbres.trickcalculator.ui.attributions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import xyz.lbres.trickcalculator.BaseActivity
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.doRefreshUI
import xyz.lbres.trickcalculator.testutils.hideDevToolsButton
import xyz.lbres.trickcalculator.testutils.matchers.withViewHolder
import xyz.lbres.trickcalculator.testutils.openAttributionsFragment
import xyz.lbres.trickcalculator.testutils.rules.RetryRule
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions

@RunWith(AndroidJUnit4::class)
class AttributionsFragmentTestDev {

    private val recyclerId = R.id.attributionsRecycler

    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(BaseActivity::class.java)

    @Rule
    @JvmField
    val retryRule = RetryRule()

    @Before
    fun setupTest() {
        openAttributionsFragment()
    }

    @Test
    fun refreshUI() {
        val authorTitles = authorAttributions.map { "Icon made by ${it.name} from www.flaticon.com" }

        // initial view
        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Expand")))
        authorTitles.indices.forEach {
            val withAuthorTitle = hasDescendant(withText(authorTitles[it]))
            onView(withViewHolder(recyclerId, it)).check(matches(allOf(isDisplayed(), withAuthorTitle)))
        }

        // refresh with all collapsed
        doRefreshUI()

        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Expand")))
        authorTitles.indices.forEach {
            val withAuthorTitle = hasDescendant(withText(authorTitles[it]))
            onView(withViewHolder(recyclerId, it)).check(matches(allOf(isDisplayed(), withAuthorTitle)))
        }

        // expand some
        hideDevToolsButton(0)
        onView(withId(R.id.expandCollapseMessage)).perform(click())
        expandCollapseAttribution(0)
        expandCollapseAttribution(3)
        expandCollapseAttribution(4)
        Thread.sleep(5000) // wait for dev tools button to be visible before clicking refresh button

        // refresh with some expanded
        doRefreshUI()

        onView(withId(R.id.expandCollapseMessage)).check(matches(withText("Collapse")))
        checkImagesDisplayed(listOf(0, 3, 4))
        checkImagesNotPresented(listOf(1, 2))
    }
}
