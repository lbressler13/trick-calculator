package xyz.lbres.trickcalculator.helpers.viewassertion

import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.util.HumanReadables

/**
 * [ViewAssertion] to assert that view is not presented on screen, due to not existing or not being visible.
 *
 * Adapted from responses to this StackOverflow post:
 * https://stackoverflow.com/questions/41297524/espresso-check-view-either-doesnotexist-or-not-isdisplayed
 */
class NotPresentedViewAssertion : ViewAssertion {
    /**
     * Assert that a view is not in the view hierarchy or is not visible
     */
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (view != null && view.isShown) {
            val viewText = HumanReadables.describe(view)
            throw AssertionError("View is present in hierarchy and visible: $viewText")
        }
    }
}
