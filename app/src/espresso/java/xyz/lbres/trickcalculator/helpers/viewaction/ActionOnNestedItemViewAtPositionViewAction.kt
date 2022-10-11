package xyz.lbres.trickcalculator.helpers.viewaction

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
 * [ViewAction] similar to [ActionOnItemViewAtPositionViewAction], using nested RecyclerViews.
 * The positions in the outer RecyclerView and nested RecyclerView are both passed as parameters,
 * and the action is taken on the view in the nested RecyclerView
 *
 * @param recyclerPosition [Int]: position of first ViewHolder in outer RecyclerView
 * @param nestedViewPosition [Int]: position of ViewHolder in nested RecyclerView
 * @param nestedRecyclerId [IdRes]: view ID of the nested RecyclerView
 * @param viewId [IdRes]: view ID of the view to take action on in the nested RecyclerView ViewHolder
 * @param viewAction [ViewAction]: action to take on the view (i.e. click(), scrollTo())
 */
class ActionOnNestedItemViewAtPositionViewAction(
    private val recyclerPosition: Int,
    private val nestedViewPosition: Int,
    @param:IdRes private val nestedRecyclerId: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    override fun getDescription(): String =
        "actionOnNestedItemViewAtPosition performing ViewAction: ${viewAction.description} on item at position $nestedViewPosition in RecyclerView with position $recyclerPosition"

    /**
     * Take an action on the specified view in the nested ViewHolder.
     *
     * @param uiController [UiController]
     * @param view [View]: outer RecyclerView
     */
    override fun perform(uiController: UiController, view: View) {
        view as RecyclerView
        ScrollToPositionViewAction(recyclerPosition).perform(uiController, view)
        uiController.loopMainThreadUntilIdle()

        val nestedRecycler = view.getChildAt(recyclerPosition).findViewById<RecyclerView>(nestedRecyclerId)
        ScrollToPositionViewAction(nestedViewPosition).perform(uiController, nestedRecycler)
        uiController.loopMainThreadUntilIdle()

        val targetView: View? = nestedRecycler.getChildAt(nestedViewPosition).findViewById(viewId)
        if (targetView == null) {
            throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(
                    IllegalStateException(
                        "No view with id $viewId found at VH with $nestedViewPosition in nested RecyclerView with id $nestedRecyclerId at position $recyclerPosition"
                    )
                )
                .build()
        } else {
            viewAction.perform(uiController, targetView)
        }
    }
}
