package xyz.lbres.trickcalculator.ui.attributions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Matchers.allOf
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.PerformException
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

// code adapted from this sample: https://github.com/dannyroa/espresso-samples/tree/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview

fun <VH : RecyclerView.ViewHolder?> actionOnItemViewAtPosition(
    position: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemViewAtPositionViewAction<RecyclerView.ViewHolder?>(
        position,
        viewId,
        viewAction
    )
}

fun <VH : RecyclerView.ViewHolder?> actionOnNestedItemViewAtPosition(
    recyclerPosition: Int,
    nestedViewPosition: Int,
    @IdRes nestedRecyclerId: Int,
    @IdRes viewId: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnNestedItemViewAtPositionViewAction<RecyclerView.ViewHolder?>(
        recyclerPosition,
        nestedViewPosition,
        nestedRecyclerId,
        viewId,
        viewAction
    )
}

private class ActionOnItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder?>(
    private val position: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    override fun getDescription(): String =
        "actionOnItemAtPosition performing ViewAction: ${viewAction.description} on item at position $position"

    override fun perform(uiController: UiController, view: View) {
        view as RecyclerView

        ScrollToPositionViewAction(position).perform(uiController, view)
        uiController.loopMainThreadUntilIdle()

        val targetView: View? = view.getChildAt(position).findViewById(viewId)
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

private class ScrollToPositionViewAction(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    override fun getDescription(): String = "scroll RecyclerView to position: $position"

    override fun perform(uiController: UiController?, view: View) {
        view as RecyclerView
        view.scrollToPosition(position)
    }
}

private class ActionOnNestedItemViewAtPositionViewAction<VH : RecyclerView.ViewHolder?>(
    private val recyclerPosition: Int,
    private val nestedViewPosition: Int,
    @param:IdRes private val nestedRecyclerId: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    // override fun getConstraints(): Matcher<View> = allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    override fun getConstraints(): Matcher<View> = isDisplayed()
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
                .withCause(IllegalStateException(
                    "No view with id $viewId found at VH with $nestedViewPosition in nested RecyclerView with id $nestedRecyclerId at position $recyclerPosition"
                ))
                .build()
        } else {
            viewAction.perform(uiController, targetView)
        }
    }
}


fun withRecyclerView(id: Int) = RecyclerViewMatcher(id)
