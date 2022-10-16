package xyz.lbres.trickcalculator.helpers

import android.view.View
import android.widget.TextView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/**
 * Class designed to check if two TextView ViewInteractions contain the same text values.
 * Includes a [SaveTextViewAction] to save the text from the initial TextView,
 * and a [PreviousTextViewMatcher] to match the text from a different TextView against the saved value.
 * Can also be use to validate that text in a single TextView has not changed over time.
 */
class TextSaver {
    private var savedText: String? = null

    /**
     * [ViewAction] to save text from a TextView in a variable that can be accessed by the [PreviousTextViewMatcher]
     */
    inner class SaveTextViewAction : ViewAction {
        override fun getConstraints(): Matcher<View> = allOf(
            isAssignableFrom(TextView::class.java),
            isDisplayed()
        )

        override fun getDescription(): String = "saving text for view"

        /**
         * Update [savedText] with text from TextView
         *
         * @param uiController [UiController]
         * @param view [View]: TextView to read text from
         */
        override fun perform(uiController: UiController?, view: View?) {
            if (view != null) {
                savedText = (view as TextView).text?.toString()
            }
        }
    }

    /**
     * [TypeSafeMatcher] to match text with the value that was saved by the [SaveTextViewAction]
     */
    inner class PreviousTextViewMatcher : TypeSafeMatcher<View?>() {
        override fun describeTo(description: Description?) {
            description?.appendText("matching for text $savedText")
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view == null || view !is TextView || savedText == null) {
                return false
            }

            return view.text?.toString() == savedText
        }
    }
}
