package xyz.lbres.trickcalculator.utils

import android.text.SpannedString
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

private class UnderlineTextMatcher : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) {
        description?.appendText("checking if text is underlined")
    }

    override fun matchesSafely(view: View): Boolean {
        if (view !is TextView) {
            return false
        }

        val text = view.text as SpannedString

        // nothing to click if textview is empty
        if (text.isEmpty()) {
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }

        // get all url clickable spans in text
        val spans: Array<UnderlineSpan> = text.getSpans(0, text.length, UnderlineSpan::class.java)
        if (spans.size != 1) {
            return false
        }

        val span = spans[0]
        return text.getSpanStart(span) == 0 && text.getSpanEnd(span) == text.length
    }
}

fun isUnderlined(): Matcher<View> = UnderlineTextMatcher()
