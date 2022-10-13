package xyz.lbres.trickcalculator.helpers.viewaction

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import xyz.lbres.trickcalculator.ui.attributions.URLClickableSpan
import java.lang.Exception

/**
 * Click the first [URLClickableSpan] in a TextView that matches the specified textToClick.
 *
 * Adapted from solution on this StackOverflow post:
 * https://stackoverflow.com/questions/38314077/how-to-click-a-clickablespan-using-espresso
 *
 * @param textToClick [String]: text of span
 */
class ClickLinkInTextViewAction(private val textToClick: String) : ViewAction {
    override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(TextView::class.java)
    override fun getDescription(): String = "clicking a URLClickableSpan with text $textToClick"

    /**
     * Click the first [URLClickableSpan] with specified text
     *
     * @param uiController [UiController]
     * @param view [View]
     */
    override fun perform(uiController: UiController, view: View) {
        view as TextView
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
            throw Exception(Pair(textToClick, fullText).toString())
            // text not present in textview
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }
    }
}
