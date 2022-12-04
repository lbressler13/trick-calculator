package xyz.lbres.trickcalculator.testutils.textsaver

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
 * Class to check if two TextView ViewInteractions contain the same text values.
 * Includes a [SaveTextViewAction] to save the text from the initial TextView,
 * and a [PreviousTextViewMatcher] to match the text from a different TextView against the saved value.
 * Can also be use to validate that text in a single TextView has not changed over time.
 */
class TextSaver {
    /**
     * [ViewAction] to save text from a TextView in a variable that can be accessed by the [PreviousTextViewMatcher]
     */
    private class SaveTextViewAction : ViewAction {
        override fun getConstraints(): Matcher<View> = allOf(
            isAssignableFrom(TextView::class.java),
            isDisplayed()
        )

        override fun getDescription(): String = "saving text for view"

        /**
         * Update [savedTextMapping] with text from TextView
         *
         * @param uiController [UiController]
         * @param view [View]: TextView to read text from
         */
        override fun perform(uiController: UiController?, view: View?) {
            if (view != null) {
                val text = (view as TextView).text?.toString()
                if (text != null) {
                    savedTextMapping[view.id] = text
                }
            }
        }
    }

    /**
     * [TypeSafeMatcher] to match text with the value that was saved by the [SaveTextViewAction]
     */
    private class PreviousTextViewMatcher : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("matching saved test for view")
        }

        override fun matchesSafely(view: View): Boolean {
            if (view !is TextView) {
                return false
            }

            val savedText: String? = savedTextMapping[view.id]

            return savedText != null && view.text?.toString() == savedText
        }
    }

    /**
     * [ViewAction] to clear the saved text for a view
     */
    private class ClearSavedTextViewAction : ViewAction {
        override fun getConstraints(): Matcher<View> = allOf(
            isAssignableFrom(TextView::class.java),
            isDisplayed()
        )

        override fun getDescription(): String = "clearing saved text for view"

        override fun perform(uiController: UiController?, view: View?) {
            if (view?.id != null) {
                savedTextMapping.remove(view.id)
            }
        }
    }

    companion object {
        /**
         * Mapping of view IDs to saved string values
         */
        private var savedTextMapping: MutableMap<Int, String> = mutableMapOf()

        /**
         * Clear saved text for a view
         */
        fun clearSavedText(): ViewAction = ClearSavedTextViewAction()

        /**
         * Save text for a view by mapping the viewId to its text
         */
        fun saveText(): ViewAction = SaveTextViewAction()

        /**
         * Check if the text in a view matches the saved value
         */
        fun withSavedText(): Matcher<View> = PreviousTextViewMatcher()

        /**
         * Clear all saved values in the text saver
         */
        fun clearAllSavedValues() {
            savedTextMapping.clear()
        }
    }
}
