package xyz.lbres.trickcalculator.helpers.matcher

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class PreviousTextViewMatcher : TypeSafeMatcher<View?>() {
    private var previousText: String? = null

    fun reset() {
        previousText = null
    }

    override fun describeTo(description: Description?) {
        description?.appendText("with previous value: '$previousText'")
    }

    override fun matchesSafely(view: View?): Boolean {
        if (view == null || view !is TextView) {
            return false
        }

        if (previousText == null) {
            previousText = view.text.toString()
            return true
        }

        return view.text.toString() == previousText
    }
}
