package xyz.lbres.trickcalculator.testutils.viewactions

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * [ViewAction] to take an action on a view within a single ViewHolder in a RecyclerView
 *
 * Adapted from similar ViewAction created by GitHub user dannyroa in this file:
 * https://github.com/dannyroa/espresso-samples/tree/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview
 *
 * @param position [Int]: position of ViewHolder in the RecyclerView
 * @param viewId [IdRes]: view ID for the view to take an action on
 * @param viewAction [ViewAction]: action to take on the view (i.e. click(), scrollTo())
 */
class ActionOnItemViewAtPositionViewAction(
    private val position: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    override fun getConstraints(): Matcher<View> =
        allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun getDescription(): String =
        "actionOnItemAtPosition performing ViewAction: ${viewAction.description} on item at position $position"

    /**
     * Take an action on the specified view in the ViewHolder
     *
     * @param uiController [UiController]
     * @param view [View]: RecyclerView containing ViewHolder
     */
    override fun perform(uiController: UiController, view: View) {
        view as RecyclerView

        ScrollToPositionViewAction(position).perform(uiController, view)
        uiController.loopMainThreadUntilIdle()

        val vhPosition = position % view.childCount
        val targetView: View? = view.getChildAt(vhPosition).findViewById(viewId)
        if (targetView == null) {
            throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(IllegalStateException("No view with id $viewId found at position $position"))
                .build()
        } else {
            viewAction.perform(uiController, targetView)
        }
    }
}
