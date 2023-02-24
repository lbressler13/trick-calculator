package xyz.lbres.trickcalculator.testutils.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * [Matcher] for if a view is shown on the screen. Can match even if isDisplayed would not
 */
private class IsShownViewMatcher : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("match if view is shown")
    }

    /**
     * Return if given view is shown on the screen
     *
     * @param view [View]: view to match
     * @return [Boolean]: `true` if the view is shown, `false` otherwise
     */
    override fun matchesSafely(view: View): Boolean {
        return view.isShown
    }
}

fun isShown(): Matcher<View> = IsShownViewMatcher()
