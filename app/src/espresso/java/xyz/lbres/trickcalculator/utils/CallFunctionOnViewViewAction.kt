package xyz.lbres.trickcalculator.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [ViewAction] to call a function on a view.
 * This is designed for testing functions whose goal is to interact with the UI, not for manipulating the UI in all tests.
 * If custom action is needed when testing functionality, it should be defined in a more specific ViewAction.
 *
 * @param function ([View]) -> [Unit]: function to call on a view
 */
private class CallFunctionOnViewViewAction(private val function: (View) -> Unit) : ViewAction {
    /**
     * Constraints that always return true
     */
    override fun getConstraints(): Matcher<View> {
        return object : BaseMatcher<View>() {
            override fun describeTo(description: Description?) {}
            override fun matches(item: Any?): Boolean = true
        }
    }

    override fun getDescription(): String = "calling a function on a view"

    /**
     * Call the function on a view
     *
     * @param view [View]?: view to call function on
     */
    override fun perform(uiController: UiController?, view: View?) {
        if (view != null) {
            function(view)
        }
    }
}

fun callEnable(): ViewAction = CallFunctionOnViewViewAction { it.enable() }
fun callDisable(): ViewAction = CallFunctionOnViewViewAction { it.disable() }

fun callVisible(): ViewAction = CallFunctionOnViewViewAction { it.visible() }
fun callInvisible(): ViewAction = CallFunctionOnViewViewAction { it.invisible() }
fun callGone(): ViewAction = CallFunctionOnViewViewAction { it.gone() }
