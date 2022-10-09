package xyz.lbres.trickcalculator.recyclerview

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

class ActionOnNestedItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder?>(
    private val recyclerPosition: Int,
    private val nestedViewPosition: Int,
    @param:IdRes private val nestedRecyclerId: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    override fun getDescription(): String =
        "actionOnNestedItemViewAtPosition performing ViewAction: ${viewAction.description} on item at position $nestedViewPosition in recycler with position $recyclerPosition"

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
