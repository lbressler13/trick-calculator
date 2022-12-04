package xyz.lbres.trickcalculator.testutils.textsaver

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

class RecyclerViewTextSaver {
    /**
     * [ViewAction] to save text from a TextView in a variable that can be accessed by the [PreviousTextViewMatcher]
     */
    private class SaveTextViewAction(val position: Int, @IdRes val viewId: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

        override fun getDescription(): String = "saving text for view"

        /**
         * Update [savedTextMapping] with text from TextView
         *
         * @param uiController [UiController]
         * @param view [View]: TextView to read text from
         */
        override fun perform(uiController: UiController?, viewHolder: View) {
            val textview = viewHolder.findViewById<TextView>(viewId)

            if (textview != null) {
                val text = textview.text?.toString()
                if (text != null) {
                    val key = Pair(position, viewId)
                    savedTextMapping[key] = text
                }
            }
        }
    }

    /**
     * [TypeSafeMatcher] to match text with the value that was saved by the [SaveTextViewAction]
     */
    private class PreviousTextViewMatcher(val position: Int, @IdRes val viewId: Int) : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("matching saved test for view")
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
     */
    private class ClearSavedTextViewAction(val position: Int, @IdRes val viewId: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> = Matchers.allOf(
            ViewMatchers.isAssignableFrom(View::class.java),
            ViewMatchers.isDisplayed()
        )

        override fun getDescription(): String =
            "clearing saved text at position $position for view with id $viewId"

        override fun perform(uiController: UiController?, viewHolder: View) {
            val key = Pair(position, viewId)
            savedTextMapping.remove(key)
        }
    }

    companion object {
        // first is position, second is view ID
        private var savedTextMapping: MutableMap<Pair<Int, Int>, String> = mutableMapOf()

        fun clearSavedTextAtPosition(position: Int, @IdRes viewId: Int): ViewAction = ClearSavedTextViewAction(position, viewId)

        fun saveTextAtPosition(position: Int, @IdRes viewId: Int): ViewAction = SaveTextViewAction(position, viewId)

        fun withSavedTextAtPosition(position: Int, @IdRes viewId: Int): Matcher<View> = PreviousTextViewMatcher(position, viewId)
    }
}
