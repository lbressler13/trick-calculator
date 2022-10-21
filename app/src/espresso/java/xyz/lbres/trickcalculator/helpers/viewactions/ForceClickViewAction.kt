package xyz.lbres.trickcalculator.helpers.viewactions

import android.view.View
import androidx.core.view.isVisible
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import org.hamcrest.Matcher

/**
 * [ViewAction] to click a view if it is visible and clickable, regardless of percent visible on screen.
 * Allows click to occur even if isDisplayed() check would fail
 */
class ForceClickViewAction : ViewAction {
    override fun getConstraints(): Matcher<View> = isClickable()
    override fun getDescription(): String = "force-clicking a view"

    /**
     * Click on view if visible
     *
     * @param uiController [UiController]
     * @param view [View]: view to click
     */
    override fun perform(uiController: UiController, view: View) {
        if (view.isVisible) {
            view.callOnClick()
        }
    }
}
