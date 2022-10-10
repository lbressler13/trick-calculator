package xyz.lbres.trickcalculator.recyclerview

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import org.hamcrest.Matchers

// code adapted from this sample: https://github.com/dannyroa/espresso-samples/tree/master/RecyclerView/app/src/androidTest/java/com/dannyroa/espresso_samples/recyclerview
class ActionOnItemViewAtPositionViewAction(
    private val position: Int,
    @param:IdRes private val viewId: Int,
    private val viewAction: ViewAction
) : ViewAction {
    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        ViewMatchers.isAssignableFrom(RecyclerView::class.java),
        ViewMatchers.isDisplayed()
    )
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
