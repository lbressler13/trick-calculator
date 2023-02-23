package xyz.lbres.trickcalculator.testutils.viewactions

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.generic.ext.ifNull

private class ActionOnChildWithIdViewAction(private val childViewId: Int, private val childAction: ViewAction) : ViewAction {
    override fun getConstraints(): Matcher<View> = isDisplayed()

    override fun getDescription(): String = "clicking view with $childViewId"

    override fun perform(uiController: UiController?, view: View) {
        val targetView = view.findViewById<View>(childViewId).ifNull {
            throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(IllegalStateException("No view found with id $childViewId"))
                .build()
        }

        childAction.perform(uiController, targetView)
    }
}

fun actionOnChildWithId(childViewId: Int, action: ViewAction): ViewAction = ActionOnChildWithIdViewAction(childViewId, action)
