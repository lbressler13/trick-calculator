package xyz.lbres.trickcalculator.ui.attributions

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * Adapted from solution on this StackOverflow post:
 * https://stackoverflow.com/questions/38314077/how-to-click-a-clickablespan-using-espresso
 */
fun clickLinkInText(textToClick: String): ViewAction {
    return object : ViewAction {

        override fun getConstraints(): Matcher<View> = Matchers.instanceOf(TextView::class.java)
        override fun getDescription(): String = "Clicking a ClickableSpan"

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

            // get all clickable spans in text
            val spans: Array<URLClickableSpan> = fullText.getSpans(0, fullText.length, URLClickableSpan::class.java)
            // find span matching text
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
}
