package xyz.lbres.trickcalculator.testutils.textsaver

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

/**
 * Class to check if the current text in a TextView matches the previous text.
 * Includes a [SaveTextViewAction] to save the current text
 * and a [PreviousTextViewMatcher] to match against the previous text
 */
class TextSaver {
    /**
     * [ViewAction] to save text from a TextView in a variable that can be accessed by the [PreviousTextViewMatcher]
     */
    private class SaveTextViewAction : ViewAction {
        override fun getConstraints(): Matcher<View> = allOf(
            isAssignableFrom(TextView::class.java),
            isDisplayed(),
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
                val savedValues = savedTextMapping[view.id]

                if (text != null) {
                    if (savedValues == null) {
                        savedTextMapping[view.id] = mutableListOf(text)
                    } else {
                        savedValues.add(text)
                    }
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

            val savedText: String? = savedTextMapping[view.id]?.last()

            return savedText != null && view.text?.toString() == savedText
        }
    }

    /**
     * [ViewAction] to clear the saved text for a view
     */
    private class ClearSavedTextViewAction : ViewAction {
        override fun getConstraints(): Matcher<View> = allOf(
            isAssignableFrom(TextView::class.java),
            isDisplayed(),
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
        private var savedTextMapping: MutableMap<Int, MutableList<String>> = mutableMapOf()

        /**
         * [ViewAction] to clear saved text for a view
         */
        fun clearSavedText(): ViewAction = ClearSavedTextViewAction()

        /**
         * [ViewAction] to save text for a view by mapping the viewId to its text
         */
        fun saveText(): ViewAction = SaveTextViewAction()

        /**
         * [Matcher] to check if the text in a view matches the saved value
         */
        fun withSavedText(): Matcher<View> = PreviousTextViewMatcher()

        /**
         * Count the number of distinct values that have been saved for a view with a given ID.
         * Does not include values saved before most recent clear.
         *
         * @param viewResId [Int]: resource ID of view to retrieve values for
         * @return [Int]: number of distinct values saved for the given view with the given ID
         */
        fun countDistinctValues(@IdRes viewResId: Int): Int {
            return savedTextMapping[viewResId]?.distinct()?.size ?: 0
        }

        /**
         * Clear all saved values in the TextSaver
         */
        fun clearAllSavedValues() {
            savedTextMapping.clear()
        }
    }
}
