package xyz.lbres.trickcalculator.helpers.viewaction

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * [ViewAction] to scroll to a specified position in a [RecyclerView].
 *
 * Adapted from similar ViewAction created by GitHub user dannyroa in this file:
 * https://github.com/dannyroa/espresso-samples/blob/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview/TestUtils.java#L80
 *
 * @param position [Int]: index to scroll to
 */
class ScrollToPositionViewAction(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(
        ViewMatchers.isAssignableFrom(RecyclerView::class.java),
        ViewMatchers.isDisplayed()
    )
    override fun getDescription(): String = "scroll RecyclerView to position: $position"

    /**
     * Scroll to position
     *
     * @param uiController [UiController]
     * @param view [View]: RecyclerView to scroll
     */
    override fun perform(uiController: UiController, view: View) {
        view as RecyclerView
        view.scrollToPosition(position)
    }
}
