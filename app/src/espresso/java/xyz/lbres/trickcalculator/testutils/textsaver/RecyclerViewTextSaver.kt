package xyz.lbres.trickcalculator.testutils.textsaver

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Class to check if two TextView ViewInteractions contain the same text values, at a specific position in a RecyclerView.
 * Includes a [SaveTextViewAction] to save the text from the initial TextView,
 * and a [PreviousTextViewMatcher] to match the text from a different TextView against the saved value.
 * Can also be use to validate that text in a single TextView has not changed over time.
 */
class RecyclerViewTextSaver {
    /**
     * [ViewAction] to save text from a TextView in a variable that can be accessed by the [PreviousTextViewMatcher]
     *
     * @param position [Int]: position of ViewHolder in RecyclerView
     * @param viewId [IdRes]: view ID for the view to save text for
     */
    private class SaveTextViewAction(val position: Int, @IdRes val viewId: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> = isDisplayed()

        override fun getDescription(): String = "saving text for view with ID $viewId at position $position"

        /**
         * Update [savedTextMapping] with text from TextView
         *
         * @param uiController [UiController]
         * @param viewHolder [View]: ViewHolder at specified position
         */
        override fun perform(uiController: UiController?, viewHolder: View) {
            val textview = viewHolder.findViewById<TextView>(viewId)
            val text = textview?.text?.toString()

            if (text != null) {
                val key = Pair(position, viewId)
                savedTextMapping[key] = text
            }
        }
    }

    /**
     * [TypeSafeMatcher] to match text with the value that was saved by the [SaveTextViewAction]
     *
     * @param position [Int]: position of ViewHolder in RecyclerView
     * @param viewId [IdRes]: view ID for the view to match text for
     */
    private class PreviousTextViewMatcher(val position: Int, @IdRes val viewId: Int) : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("matching saved text for view with ID $viewId at position $position")
        }

        override fun matchesSafely(viewHolder: View): Boolean {
            val textview = viewHolder.findViewById<TextView>(viewId)

            if (textview !is TextView) {
                return false
            }

            val key = Pair(position, viewId)
            val savedText: String? = savedTextMapping[key]

            return savedText != null && textview.text?.toString() == savedText
        }
    }

    /**
     * [ViewAction] to clear the saved text for a view
     *
     * @param position [Int]: position of ViewHolder in RecyclerView
     * @param viewId [IdRes]: view ID for the view to clear text for
     */
    private class ClearSavedTextViewAction(val position: Int, @IdRes val viewId: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> = isDisplayed()

        override fun getDescription(): String =
            "clearing saved text at position $position for view with id $viewId"

        override fun perform(uiController: UiController?, viewHolder: View) {
            val key = Pair(position, viewId)
            savedTextMapping.remove(key)
        }
    }

    companion object {
        /**
         * Mapping of position and view ID to saved string values.
         * First value in key is position, and second is view ID.
         */
        private var savedTextMapping: MutableMap<Pair<Int, Int>, String> = mutableMapOf()

        /**
         * Clear saved text for a view for a given position
         */
        fun clearSavedTextAtPosition(position: Int, @IdRes viewId: Int): ViewAction = ClearSavedTextViewAction(position, viewId)

        /**
         * Save text for a view by mapping the viewId to its text at a given position
         */
        fun saveTextAtPosition(position: Int, @IdRes viewId: Int): ViewAction = SaveTextViewAction(position, viewId)

        /**
         * Check if the text in a view matches the saved value at a given position
         */
        fun withSavedTextAtPosition(position: Int, @IdRes viewId: Int): Matcher<View> = PreviousTextViewMatcher(position, viewId)

        /**
         * Clear all saved values in the text saver
         */
        fun clearAllSavedValues() {
            savedTextMapping.clear()
        }
    }
}
