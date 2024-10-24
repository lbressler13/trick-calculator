package xyz.lbres.trickcalculator.testutils.viewactions

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.generic.ext.ifNull

/**
 * [ViewAction] to perform an action on a child view with a given ID
 *
 * @param childViewId [IdRes]: resource ID for view to perform action on
 * @param childAction [ViewAction]: action to perform on child
 */
private class ActionOnChildWithIdViewAction(
    @param:IdRes private val childViewId: Int,
    private val childAction: ViewAction,
) : ViewAction {
    override fun getConstraints(): Matcher<View> = isDisplayed()

    override fun getDescription(): String = "clicking view with $childViewId"

    /**
     * Perform [childAction] on a child view with a given ID
     */
    override fun perform(
        uiController: UiController?,
        view: View,
    ) {
        val targetView =
            view.findViewById<View>(childViewId).ifNull {
                throw PerformException.Builder().withActionDescription(this.toString())
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(IllegalStateException("No view found with id $childViewId"))
                    .build()
            }

        childAction.perform(uiController, targetView)
    }
}

/**
 * [ViewAction] to perform an action on a child view with a given ID
 *
 * @param childViewId [IdRes]: resource ID for view to perform action on
 * @param childAction [ViewAction]: action to perform on child
 */
fun actionOnChildWithId(
    @IdRes childViewId: Int,
    childAction: ViewAction,
): ViewAction {
    return ActionOnChildWithIdViewAction(childViewId, childAction)
}
