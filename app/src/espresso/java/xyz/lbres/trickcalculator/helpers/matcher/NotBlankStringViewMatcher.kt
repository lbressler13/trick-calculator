package xyz.lbres.trickcalculator.helpers.matcher

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class NotBlankStringViewMatcher : TypeSafeMatcher<View?>() {
    override fun describeTo(description: Description?) {}

    override fun matchesSafely(view: View?): Boolean {
        if (view == null || view !is TextView) {
            return false
        }

        return !view.text.isNullOrBlank()
    }
}