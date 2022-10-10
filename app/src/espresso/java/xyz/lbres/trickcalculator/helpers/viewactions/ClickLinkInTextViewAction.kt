package xyz.lbres.trickcalculator.helpers.viewactions

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import xyz.lbres.trickcalculator.ui.attributions.URLClickableSpan

class ClickLinkInTextViewAction(private val textToClick: String) : ViewAction {
    override fun getConstraints(): Matcher<View> = Matchers.instanceOf(TextView::class.java)
    override fun getDescription(): String = "Clicking a URLClickableSpan"

    override fun perform(uiController: UiController, view: View) {
        view as TextView
        // val textview = view as TextView
        val fullText = view.text as SpannableString

        // nothing to click if textview is empty
        if (fullText.isEmpty()) {
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }

        // get all url clickable spans in text
        val spans: Array<URLClickableSpan> = fullText.getSpans(0, fullText.length, URLClickableSpan::class.java)
        // find matching span
        val span = spans.firstOrNull {
            val start = fullText.getSpanStart(it)
            val end = fullText.getSpanEnd(it)
            val spanText = fullText.substring(start, end)
            textToClick == spanText
        }

        if (span != null) {
            span.onClick(view)
        } else {
            // text not present in textview
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }
    }
}
