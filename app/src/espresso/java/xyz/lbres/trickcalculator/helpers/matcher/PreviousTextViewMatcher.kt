package xyz.lbres.trickcalculator.helpers.matcher

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class PreviousTextViewMatcher : TypeSafeMatcher<View?>() {
    private var previousText: String? = null

    override fun describeTo(description: Description?) {
        description?.appendText("with previous value: '$previousText'")
    }

    override fun matchesSafely(view: View?): Boolean {
        return when {
            view == null || view !is TextView -> false
            previousText != null && view.text == previousText  -> true
            previousText == null -> {
                previousText = view.text?.toString()
                true
            }
            else -> false
        }
    }
}
