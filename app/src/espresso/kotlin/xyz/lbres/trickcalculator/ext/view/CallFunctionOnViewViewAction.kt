package xyz.lbres.trickcalculator.ext.view

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * [ViewAction] to call a function on a view.
 * This is designed only to test view extension methods, not to interact with the UI when testing functionality of the app.
 * If custom action is needed when testing functionality, it should be defined in a more specific ViewAction.
 *
 * @param function ([View]) -> [Unit]: method to call on a view
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

/**
 * Call View.enable() extension method
 */
fun callEnable(): ViewAction = CallFunctionOnViewViewAction(View::enable)

/**
 * Call View.disable() extension method
 */
fun callDisable(): ViewAction = CallFunctionOnViewViewAction(View::disable)

/**
 * Call View.visible() extension method
 */
fun callVisible(): ViewAction = CallFunctionOnViewViewAction(View::visible)

/**
 * Call View.invisible() extension method
 */
fun callInvisible(): ViewAction = CallFunctionOnViewViewAction(View::invisible)

/**
 * Call View.gone() extension method
 */
fun callGone(): ViewAction = CallFunctionOnViewViewAction(View::gone)
